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
                    int pendingCount = rescueManager.getPendingVictimsCount();
                    int availableTeams = rescueManager.getAvailableTeamsCount();
                    int totalShelters = rescueManager.getAllShelters().size();

                    final String message = String.format(
                            "● Live Monitor [Cycle %d]: Pending Victims: %d | Available Teams: %d | Total Shelters: %d",
                            cycle++, pendingCount, availableTeams, totalShelters);

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
