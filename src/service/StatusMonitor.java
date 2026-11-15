package service;

import javax.swing.SwingUtilities;

// Multithreading demonstration: extending the Thread class
public class StatusMonitor extends Thread {

    private volatile boolean running = true;
    private final RescueManager rescueManager;
    private StatusUpdateListener listener;

    // Interface for thread callback to UI
    public interface StatusUpdateListener {
        void onStatusUpdate(String statusMessage);
    }

    public StatusMonitor(RescueManager rescueManager) {
        this.rescueManager = rescueManager;
        // Mark thread as daemon so JVM can shut down cleanly
        setDaemon(true);
    }

    public void setStatusUpdateListener(StatusUpdateListener listener) {
        this.listener = listener;
    }

    public void stopMonitoring() {
        this.running = false;
        this.interrupt();
    }

    // Demonstrating thread run() method and sleep()
    @Override
    public void run() {
        int cycle = 1;
        while (running) {
            try {
                // Thread sleep for 4 seconds
                Thread.sleep(4000);

                if (rescueManager != null) {
                    // Automatically trigger offline sync when DB connection is restored
                    if (DatabaseConnection.isDatabaseAvailable() && OfflineSyncService.getPendingCount() > 0) {
                        OfflineSyncService.syncPendingToDatabase();
                    }

                    int pendingCount = rescueManager.getPendingVictimsCount();
                    int availableTeams = rescueManager.getAvailableTeamsCount();
                    int totalShelters = rescueManager.getAllShelters().size();
                    int pendingSync = OfflineSyncService.getPendingCount();
                    boolean online = DatabaseConnection.isDatabaseAvailable();

                    String dbStatus = online
                            ? (pendingSync > 0 ? "🟢 Online (Syncing " + pendingSync + "...)" : "🟢 Online (Synced)")
                            : ("🟠 Offline Mode (" + pendingSync + " pending sync)");

                    final String message = String.format(
                            "● Live Monitor [%s]: Pending Victims: %d | Available Teams: %d | Total Shelters: %d",
                            dbStatus, pendingCount, availableTeams, totalShelters);

                    // Notify UI safely on Swing EDT
                    if (listener != null) {
                        SwingUtilities.invokeLater(() -> listener.onStatusUpdate(message));
                    }
                }
            } catch (InterruptedException e) {
                // Thread was interrupted to stop
                break;
            } catch (Exception e) {
                System.err.println("[StatusMonitor Thread Exception] " + e.getMessage());
            }
        }
    }
}
