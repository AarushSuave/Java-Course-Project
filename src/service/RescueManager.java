package service;

import exception.ShelterFullException;
import model.Ambulance;
import model.Disaster;
import model.RescueOfficer;
import model.RescueTeam;
import model.RescueVan;
import model.Shelter;
import model.Victim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Central Manager demonstrating Collections (ArrayList, HashMap, Iterator, for-each), Method Overloading, and JDBC Sync
public class RescueManager {

    // Singleton instance
    private static RescueManager instance;

    // Collections Framework Demonstrations
    private final List<Disaster> disasters;
    private final List<Victim> victims;
    private final List<RescueTeam> teams;
    private final List<Shelter> shelters;
    private final List<RescueOfficer> officers;

    // HashMap for fast Key-Value lookup by Victim ID
    private final Map<Integer, Victim> victimMap;

    // Private constructor for Singleton Pattern
    private RescueManager() {
        this.disasters = new ArrayList<>();
        this.victims = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.shelters = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.victimMap = new HashMap<>();

        // Initialize database if available
        DatabaseConnection.initializeDatabaseTables();

        // Load data from database or seed initial memory data
        loadFromDatabaseOrSeed();
    }

    // Static method to get Singleton instance
    public static synchronized RescueManager getInstance() {
        if (instance == null) {
            instance = new RescueManager();
        }
        return instance;
    }

    // ==========================================
    // SEED INITIAL DATA
    // ==========================================
    private void seedSampleData() {
        // Disasters
        addDisaster(new Disaster(101, "Flood", "River Valley Zone B", "High"));
        addDisaster(new Disaster(102, "Earthquake", "North District Sector 4", "Critical"));
        addDisaster(new Disaster(103, "Urban Fire", "Industrial Area Gate 2", "Medium"));

        // Shelters
        addShelter(new Shelter(1, "City Central Community Hall", "Downtown", 10, 4));
        addShelter(new Shelter(2, "St. Mary School Relief Camp", "North Hill", 8, 8)); // Full shelter to test exception
        addShelter(new Shelter(3, "Sports Complex Shelter", "East Wing", 15, 2));

        // Rescue Teams with Polymorphic Vehicles
        addRescueTeam(new RescueTeam(1, "Alpha Rapid Response", 6, true, new Ambulance("AMB-101", "Force Traveller ALS", 3, true)));
        addRescueTeam(new RescueTeam(2, "Bravo Evacuation Squad", 8, true, new RescueVan("VAN-202", "Tata Winger Rescue", 10, true)));
        addRescueTeam(new RescueTeam(3, "Delta Marine Unit", 5, false, new RescueVan("VAN-303", "Heavy Duty Flood Van", 8, true)));

        // Rescue Officers
        addOfficer(new RescueOfficer(501, "Capt. Vikram Rao", 38, "9876543210", "Downtown HQ", "Team Lead", 1));
        addOfficer(new RescueOfficer(502, "Dr. Priya Sharma", 32, "9876543211", "North Station", "Senior Paramedic", 1));
        addOfficer(new RescueOfficer(503, "Rahul Verma", 29, "9876543212", "East Post", "Rescue Specialist", 2));

        // Victims
        addVictim(new Victim(1001, "Aarav Patel", 28, "9123456780", "River Valley Zone B", "Minor Injury", "Pending"));
        addVictim(new Victim(1002, "Sunita Devi", 54, "9123456781", "North District Sector 4", "Critical", "Assigned", 1, 0));
        addVictim(new Victim(1003, "Rohan Sen", 19, "9123456782", "Industrial Area Gate 2", "Normal", "Rescued", 2, 0));
        addVictim(new Victim(1004, "Meera Nair", 42, "9123456783", "Downtown", "Normal", "Shelter Reached", 0, 1));
    }

    // ==========================================
    // DISASTER MANAGEMENT
    // ==========================================
    public void addDisaster(Disaster d) {
        disasters.add(d);
        saveDisasterToDb(d);
    }

    public List<Disaster> getAllDisasters() {
        return new ArrayList<>(disasters);
    }

    public Disaster getDisasterById(int id) {
        for (Disaster d : disasters) {
            if (d.getDisasterId() == id) {
                return d;
            }
        }
        return null;
    }

    // ==========================================
    // VICTIM MANAGEMENT & METHOD OVERLOADING
    // ==========================================
    public void addVictim(Victim v) {
        victims.add(v);
        victimMap.put(v.getVictimId(), v);
        saveVictimToDb(v);
    }

    // Method Overloading Demonstration: Adding victim by individual parameters
    public void addVictim(int id, String name, int age, String phone, String location, String medStatus, String rescueStatus) {
        Victim v = new Victim(id, name, age, phone, location, medStatus, rescueStatus);
        addVictim(v);
    }

    public List<Victim> getAllVictims() {
        return new ArrayList<>(victims);
    }

    // Method Overloading Demonstration: Search by ID (int)
    public Victim searchVictim(int victimId) {
        return victimMap.get(victimId);
    }

    // Method Overloading Demonstration: Search by Name (String)
    public Victim searchVictim(String name) {
        if (name == null) return null;
        for (Victim v : victims) {
            if (v.getName().equalsIgnoreCase(name.trim())) {
                return v;
            }
        }
        return null;
    }

    public boolean updateVictimStatus(int victimId, String newStatus) {
        Victim v = searchVictim(victimId);
        if (v != null) {
            v.setRescueStatus(newStatus);
            saveVictimToDb(v);
            return true;
        }
        return false;
    }

    // Demonstrating Iterator usage for safe removal or counting
    public int getPendingVictimsCount() {
        int count = 0;
        Iterator<Victim> it = victims.iterator();
        while (it.hasNext()) {
            Victim v = it.next();
            if ("Pending".equalsIgnoreCase(v.getRescueStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getRescuedVictimsCount() {
        int count = 0;
        for (Victim v : victims) {
            if ("Rescued".equalsIgnoreCase(v.getRescueStatus()) 
                    || "Shelter Reached".equalsIgnoreCase(v.getRescueStatus()) 
                    || "Completed".equalsIgnoreCase(v.getRescueStatus())) {
                count++;
            }
        }
        return count;
    }

    // ==========================================
    // RESCUE TEAM MANAGEMENT
    // ==========================================
    public void addRescueTeam(RescueTeam team) {
        teams.add(team);
        saveTeamToDb(team);
    }

    public List<RescueTeam> getAllTeams() {
        return new ArrayList<>(teams);
    }

    public RescueTeam getTeamById(int teamId) {
        for (RescueTeam t : teams) {
            if (t.getTeamId() == teamId) {
                return t;
            }
        }
        return null;
    }

    public int getAvailableTeamsCount() {
        int count = 0;
        for (RescueTeam t : teams) {
            if (t.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // ==========================================
    // SHELTER MANAGEMENT & EXCEPTION HANDLING
    // ==========================================
    public void addShelter(Shelter s) {
        shelters.add(s);
        saveShelterToDb(s);
    }

    public List<Shelter> getAllShelters() {
        return new ArrayList<>(shelters);
    }

    public Shelter getShelterById(int shelterId) {
        for (Shelter s : shelters) {
            if (s.getShelterId() == shelterId) {
                return s;
            }
        }
        return null;
    }

    public int getTotalShelterAvailableSpaces() {
        int total = 0;
        for (Shelter s : shelters) {
            total += s.getAvailableCapacity();
        }
        return total;
    }

    // ==========================================
    // RESCUE OFFICERS
    // ==========================================
    public void addOfficer(RescueOfficer officer) {
        officers.add(officer);
    }

    public List<RescueOfficer> getAllOfficers() {
        return new ArrayList<>(officers);
    }

    // ==========================================
    // RESCUE ASSIGNMENT LOGIC
    // ==========================================
    public boolean assignRescueTeamToVictim(int victimId, int teamId) {
        Victim victim = searchVictim(victimId);
        RescueTeam team = getTeamById(teamId);

        if (victim == null || team == null) {
            return false;
        }

        if (!team.isAvailable()) {
            return false;
        }

        // Assign team and update victim
        team.assignToMission();
        victim.setAssignedTeamId(teamId);
        victim.setRescueStatus("Assigned");

        saveTeamToDb(team);
        saveVictimToDb(victim);
        return true;
    }

    // ==========================================
    // SHELTER ALLOCATION & CUSTOM EXCEPTION
    // ==========================================
    public boolean allocateVictimToShelter(int victimId, int shelterId) throws ShelterFullException {
        Victim victim = searchVictim(victimId);
        Shelter shelter = getShelterById(shelterId);

        if (victim == null || shelter == null) {
            return false;
        }

        // Throws ShelterFullException if shelter is full
        shelter.allocateVictim();

        victim.setAllocatedShelterId(shelterId);
        victim.setRescueStatus("Shelter Reached");

        saveShelterToDb(shelter);
        saveVictimToDb(victim);
        return true;
    }

    // ==========================================
    // JDBC PERSISTENCE HELPERS
    // ==========================================
    private void saveDisasterToDb(Disaster d) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String sql = "INSERT INTO disasters (disaster_id, type, location, severity) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE type=VALUES(type), location=VALUES(location), severity=VALUES(severity)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getDisasterId());
            ps.setString(2, d.getType());
            ps.setString(3, d.getLocation());
            ps.setString(4, d.getSeverity());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Silently fallback if error
        }
    }

    private void saveVictimToDb(Victim v) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String sql = "INSERT INTO victims (victim_id, name, age, phone, location, medical_status, rescue_status, assigned_team_id, allocated_shelter_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE name=VALUES(name), age=VALUES(age), phone=VALUES(phone), location=VALUES(location), "
                + "medical_status=VALUES(medical_status), rescue_status=VALUES(rescue_status), "
                + "assigned_team_id=VALUES(assigned_team_id), allocated_shelter_id=VALUES(allocated_shelter_id)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getVictimId());
            ps.setString(2, v.getName());
            ps.setInt(3, v.getAge());
            ps.setString(4, v.getPhone());
            ps.setString(5, v.getLocation());
            ps.setString(6, v.getMedicalStatus());
            ps.setString(7, v.getRescueStatus());
            ps.setInt(8, v.getAssignedTeamId());
            ps.setInt(9, v.getAllocatedShelterId());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Silently fallback if error
        }
    }

    private void saveTeamToDb(RescueTeam t) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String sql = "INSERT INTO teams (team_id, team_name, member_count, is_available, vehicle_type) VALUES (?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE team_name=VALUES(team_name), member_count=VALUES(member_count), "
                + "is_available=VALUES(is_available), vehicle_type=VALUES(vehicle_type)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getTeamId());
            ps.setString(2, t.getTeamName());
            ps.setInt(3, t.getMemberCount());
            ps.setBoolean(4, t.isAvailable());
            ps.setString(5, t.getAssignedVehicle() != null ? t.getAssignedVehicle().getVehicleType() : "None");
            ps.executeUpdate();
        } catch (SQLException e) {
            // Silently fallback if error
        }
    }

    private void saveShelterToDb(Shelter s) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String sql = "INSERT INTO shelters (shelter_id, name, location, capacity, occupied) VALUES (?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE name=VALUES(name), location=VALUES(location), capacity=VALUES(capacity), occupied=VALUES(occupied)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getShelterId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getLocation());
            ps.setInt(4, s.getCapacity());
            ps.setInt(5, s.getOccupied());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Silently fallback if error
        }
    }

    private void loadFromDatabaseOrSeed() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            seedSampleData();
            return;
        }

        try {
            // Check if victims table has data
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM victims");
            if (rs.next() && rs.getInt(1) > 0) {
                // Load Disasters
                ResultSet rsDisasters = conn.createStatement().executeQuery("SELECT * FROM disasters");
                while (rsDisasters.next()) {
                    disasters.add(new Disaster(
                            rsDisasters.getInt("disaster_id"),
                            rsDisasters.getString("type"),
                            rsDisasters.getString("location"),
                            rsDisasters.getString("severity")
                    ));
                }

                // Load Shelters
                ResultSet rsShelters = conn.createStatement().executeQuery("SELECT * FROM shelters");
                while (rsShelters.next()) {
                    shelters.add(new Shelter(
                            rsShelters.getInt("shelter_id"),
                            rsShelters.getString("name"),
                            rsShelters.getString("location"),
                            rsShelters.getInt("capacity"),
                            rsShelters.getInt("occupied")
                    ));
                }

                // Load Teams
                ResultSet rsTeams = conn.createStatement().executeQuery("SELECT * FROM teams");
                while (rsTeams.next()) {
                    String vType = rsTeams.getString("vehicle_type");
                    model.Vehicle veh = (vType != null && vType.contains("Ambulance")) ? new Ambulance() : new RescueVan();
                    teams.add(new RescueTeam(
                            rsTeams.getInt("team_id"),
                            rsTeams.getString("team_name"),
                            rsTeams.getInt("member_count"),
                            rsTeams.getBoolean("is_available"),
                            veh
                    ));
                }

                // Load Victims
                ResultSet rsVictims = conn.createStatement().executeQuery("SELECT * FROM victims");
                while (rsVictims.next()) {
                    Victim v = new Victim(
                            rsVictims.getInt("victim_id"),
                            rsVictims.getString("name"),
                            rsVictims.getInt("age"),
                            rsVictims.getString("phone"),
                            rsVictims.getString("location"),
                            rsVictims.getString("medical_status"),
                            rsVictims.getString("rescue_status"),
                            rsVictims.getInt("assigned_team_id"),
                            rsVictims.getInt("allocated_shelter_id")
                    );
                    victims.add(v);
                    victimMap.put(v.getVictimId(), v);
                }
            } else {
                // Seed initial data and write to DB
                seedSampleData();
            }
        } catch (SQLException e) {
            seedSampleData();
        }
    }
}
