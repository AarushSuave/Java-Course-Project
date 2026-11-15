package service;

import model.Victim;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * OfflineSyncService manages:
 * 1. Persistent offline queue (offline_sync_queue.txt) for operations done while MySQL is disconnected.
 * 2. Replay/synchronization of offline operations into the database once connectivity is restored.
 * 3. Offline CSV/Batch export and import for physical dispatch / field coordination.
 */
public class OfflineSyncService {

    private static final String QUEUE_FILE = "offline_sync_queue.txt";

    // Action types
    public static final String ACTION_VICTIM_UPSERT = "VICTIM_UPSERT";
    public static final String ACTION_STATUS_UPDATE = "STATUS_UPDATE";
    public static final String ACTION_TEAM_ASSIGN = "TEAM_ASSIGN";
    public static final String ACTION_SHELTER_ALLOC = "SHELTER_ALLOC";

    /**
     * Appends an action line to the offline queue file.
     * Thread-safe across UI actions and background threads.
     */
    public static synchronized void queueAction(String actionType, String payload) {
        try (FileWriter fw = new FileWriter(QUEUE_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(actionType + "|" + payload);
        } catch (IOException e) {
            System.err.println("[OfflineSyncService] Error writing to queue: " + e.getMessage());
        }
    }

    /**
     * Returns the count of pending operations in the offline queue file.
     */
    public static synchronized int getPendingCount() {
        File file = new File(QUEUE_FILE);
        if (!file.exists() || file.length() == 0) return 0;

        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            return 0;
        }
        return count;
    }

    /**
     * Replays all queued actions into MySQL database using transactions.
     * Returns true if all queued items were successfully synced and the queue cleared.
     */
    public static synchronized boolean syncPendingToDatabase() {
        File file = new File(QUEUE_FILE);
        if (!file.exists() || file.length() == 0) {
            return true;
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return false; // Still offline
        }

        List<String> remainingLines = new ArrayList<>();
        boolean originalAutoCommit = true;

        try {
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // Transactional sync

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    boolean ok = processQueueLine(conn, line);
                    if (!ok) {
                        remainingLines.add(line);
                    }
                }
            }

            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                // Ignore rollback failure
            }
            System.err.println("[OfflineSyncService] Sync transaction error: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                // Ignore
            }
        }

        // Rewrite queue file with remaining/unprocessed lines (empty if all succeeded)
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (String rem : remainingLines) {
                pw.println(rem);
            }
        } catch (IOException e) {
            System.err.println("[OfflineSyncService] Failed to update queue file: " + e.getMessage());
        }

        return remainingLines.isEmpty();
    }

    /**
     * Executes a single queued operation against the DB.
     */
    private static boolean processQueueLine(Connection conn, String line) {
        String[] parts = line.split("\\|", 2);
        if (parts.length < 2) return true; // Malformed, skip

        String action = parts[0];
        String data = parts[1];

        try {
            switch (action) {
                case ACTION_VICTIM_UPSERT: {
                    // ID|Name|Age|Phone|Location|MedicalStatus|RescueStatus|AssignedTeamId|AllocatedShelterId
                    String[] f = data.split("\\|", -1);
                    if (f.length >= 9) {
                        String sql = "INSERT INTO victims (victim_id, name, age, phone, location, medical_status, rescue_status, assigned_team_id, allocated_shelter_id) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                                + "ON DUPLICATE KEY UPDATE name=VALUES(name), age=VALUES(age), phone=VALUES(phone), location=VALUES(location), "
                                + "medical_status=VALUES(medical_status), rescue_status=VALUES(rescue_status), assigned_team_id=VALUES(assigned_team_id), allocated_shelter_id=VALUES(allocated_shelter_id)";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, Integer.parseInt(f[0]));
                            ps.setString(2, f[1]);
                            ps.setInt(3, Integer.parseInt(f[2]));
                            ps.setString(4, f[3]);
                            ps.setString(5, f[4]);
                            ps.setString(6, f[5]);
                            ps.setString(7, f[6]);
                            ps.setInt(8, Integer.parseInt(f[7]));
                            ps.setInt(9, Integer.parseInt(f[8]));
                            ps.executeUpdate();
                        }
                    }
                    return true;
                }
                case ACTION_STATUS_UPDATE: {
                    // victimId|newStatus
                    String[] f = data.split("\\|", -1);
                    if (f.length >= 2) {
                        String sql = "UPDATE victims SET rescue_status=? WHERE victim_id=?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, f[1]);
                            ps.setInt(2, Integer.parseInt(f[0]));
                            ps.executeUpdate();
                        }
                    }
                    return true;
                }
                case ACTION_TEAM_ASSIGN: {
                    // victimId|teamId
                    String[] f = data.split("\\|", -1);
                    if (f.length >= 2) {
                        String sql = "UPDATE victims SET assigned_team_id=?, rescue_status='Assigned' WHERE victim_id=?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, Integer.parseInt(f[1]));
                            ps.setInt(2, Integer.parseInt(f[0]));
                            ps.executeUpdate();
                        }
                    }
                    return true;
                }
                case ACTION_SHELTER_ALLOC: {
                    // victimId|shelterId
                    String[] f = data.split("\\|", -1);
                    if (f.length >= 2) {
                        String sql = "UPDATE victims SET allocated_shelter_id=?, rescue_status='Shelter Reached' WHERE victim_id=?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, Integer.parseInt(f[1]));
                            ps.setInt(2, Integer.parseInt(f[0]));
                            ps.executeUpdate();
                        }
                    }
                    return true;
                }
                default:
                    return true;
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("[OfflineSyncService] Error processing line: " + line + " - " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // EMERGENCY OFFLINE BATCH EXPORT & IMPORT (CSV / Mesh Hand-off)
    // =========================================================================

    /**
     * Exports all victims to a standardized CSV file for pen-drive/field dispatch.
     */
    public static boolean exportVictimsToCSV(List<Victim> victims, File targetFile) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(targetFile))) {
            // CSV Header
            pw.println("VictimID,Name,Age,Phone,Location,MedicalStatus,RescueStatus,AssignedTeamID,AllocatedShelterID");
            for (Victim v : victims) {
                pw.printf("%d,\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%d,%d%n",
                        v.getVictimId(),
                        v.getName().replace("\"", "\"\""),
                        v.getAge(),
                        v.getPhone(),
                        v.getLocation().replace("\"", "\"\""),
                        v.getMedicalStatus(),
                        v.getRescueStatus(),
                        v.getAssignedTeamId(),
                        v.getAllocatedShelterId());
            }
            return true;
        } catch (IOException e) {
            System.err.println("[OfflineSyncService] Export CSV error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Imports victims from an external CSV file (e.g. delivered via USB drive from another field team),
     * merges them into the system, and queues/saves them to the DB.
     */
    public static int importVictimsFromCSV(File sourceFile, RescueManager manager) {
        if (!sourceFile.exists()) return 0;

        int importedCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line = br.readLine(); // Read header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Simple CSV line parser handling quotes
                List<String> tokens = parseCsvLine(line);
                if (tokens.size() >= 7) {
                    try {
                        int id = Integer.parseInt(tokens.get(0).trim());
                        String name = tokens.get(1).trim();
                        int age = Integer.parseInt(tokens.get(2).trim());
                        String phone = tokens.get(3).trim();
                        String location = tokens.get(4).trim();
                        String medStatus = tokens.get(5).trim();
                        String rescueStatus = tokens.get(6).trim();

                        int teamId = tokens.size() > 7 ? Integer.parseInt(tokens.get(7).trim()) : 0;
                        int shelterId = tokens.size() > 8 ? Integer.parseInt(tokens.get(8).trim()) : 0;

                        Victim existing = manager.searchVictim(id);
                        if (existing == null) {
                            Victim newVictim = new Victim(id, name, age, phone, location, medStatus, rescueStatus, teamId, shelterId);
                            manager.addVictim(newVictim);
                            importedCount++;
                        } else {
                            // Update existing record
                            existing.setMedicalStatus(medStatus);
                            existing.setRescueStatus(rescueStatus);
                            existing.setAssignedTeamId(teamId);
                            existing.setAllocatedShelterId(shelterId);
                            manager.updateVictim(existing);
                            importedCount++;
                        }
                    } catch (Exception ex) {
                        System.err.println("[OfflineSyncService] Skip invalid CSV line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[OfflineSyncService] Import CSV error: " + e.getMessage());
        }
        return importedCount;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                list.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());
        return list;
    }
}
