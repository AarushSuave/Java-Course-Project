package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// Final Class demonstrating: final class keyword, final variables, final methods, and File Handling
public final class Report implements Reportable {

    // Final constants
    public static final String APP_NAME = "RescueNet - Disaster Response Management System";
    public static final String APP_VERSION = "1.0.0";

    private final String timestamp;
    private final String reportContent;

    // Parameterized constructor
    public Report(String reportContent) {
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.reportContent = reportContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReportContent() {
        return reportContent;
    }

    // Implementing interface method
    @Override
    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================================\n");
        sb.append("                ").append(APP_NAME).append("\n");
        sb.append("                 SYSTEM STATUS REPORT\n");
        sb.append("=====================================================\n");
        sb.append("Generated On : ").append(timestamp).append("\n");
        sb.append("System Build : v").append(APP_VERSION).append("\n");
        sb.append("-----------------------------------------------------\n\n");
        sb.append(reportContent).append("\n");
        sb.append("=====================================================\n");
        sb.append("                 END OF OFFICIAL REPORT\n");
        sb.append("=====================================================\n");
        return sb.toString();
    }

    // Final method demonstrating that this method cannot be overridden
    @Override
    public final boolean saveToFile(String filePath) {
        File file = new File(filePath);
        // Ensure parent directories exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        PrintWriter writer = null;
        try {
            // Demonstrating File Handling with FileWriter & PrintWriter
            writer = new PrintWriter(new FileWriter(file, false));
            writer.print(generateSummary());
            System.out.println("[File Handling] Report successfully written to: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("[File Handling Error] Failed to write report: " + e.getMessage());
            return false;
        } finally {
            // Demonstrating finally block for closing resources
            if (writer != null) {
                writer.close();
            }
        }
    }
}
