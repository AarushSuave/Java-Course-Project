package gui;

import service.Report;
import service.RescueManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Frame for Generating and Exporting Reports (Demonstrating Interfaces and File I/O)
public class ReportFrame extends JFrame {

    private final RescueManager rescueManager;
    private JTextArea reportTextArea;
    private Report currentReport;

    public ReportFrame() {
        this.rescueManager = RescueManager.getInstance();

        setTitle("RescueNet - System Reports & File Export");
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        generateLiveReport();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel(
                "Disaster Relief Operations Report",
                "Generate consolidated analytics and export official text logs to disk"
        ), BorderLayout.NORTH);

        // Center Text Area
        reportTextArea = new JTextArea();
        reportTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportTextArea.setEditable(false);
        reportTextArea.setBackground(UITheme.CARD_BG);
        reportTextArea.setForeground(new Color(30, 30, 30));
        reportTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh Report");
        refreshBtn.addActionListener(e -> generateLiveReport());

        JButton saveBtn = UITheme.createPrimaryButton("Save Report to 'reports/report.txt'");
        saveBtn.addActionListener(e -> saveReportToFile());

        btnPanel.add(refreshBtn);
        btnPanel.add(saveBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void generateLiveReport() {
        int totalDisasters = rescueManager.getAllDisasters().size();
        int totalVictims = rescueManager.getAllVictims().size();
        int rescuedVictims = rescueManager.getRescuedVictimsCount();
        int pendingVictims = rescueManager.getPendingVictimsCount();
        int totalTeams = rescueManager.getAllTeams().size();
        int availableTeams = rescueManager.getAvailableTeamsCount();
        int totalShelters = rescueManager.getAllShelters().size();
        int availableBeds = rescueManager.getTotalShelterAvailableSpaces();

        StringBuilder content = new StringBuilder();
        content.append("1. DISASTER OVERVIEW\n");
        content.append("   - Active Disaster Events    : ").append(totalDisasters).append("\n\n");

        content.append("2. VICTIM RESPONSE PIPELINE\n");
        content.append("   - Total Registered Victims  : ").append(totalVictims).append("\n");
        content.append("   - Rescued / In Shelters     : ").append(rescuedVictims).append("\n");
        content.append("   - Pending Rescue Assistance : ").append(pendingVictims).append("\n\n");

        content.append("3. EMERGENCY RELIEF UNITS\n");
        content.append("   - Total Rescue Teams        : ").append(totalTeams).append("\n");
        content.append("   - Teams Ready for Mission   : ").append(availableTeams).append("\n\n");

        content.append("4. SHELTER OCCUPANCY & CAPACITY\n");
        content.append("   - Total Relief Shelters     : ").append(totalShelters).append("\n");
        content.append("   - Total Available Beds      : ").append(availableBeds).append("\n");

        // Instantiating final class Report implementing Reportable interface
        currentReport = new Report(content.toString());
        reportTextArea.setText(currentReport.generateSummary());
    }

    private void saveReportToFile() {
        if (currentReport == null) {
            generateLiveReport();
        }

        String outputPath = "reports/report.txt";
        // Demonstrating File I/O through Report class
        boolean success = currentReport.saveToFile(outputPath);

        if (success) {
            File file = new File(outputPath);
            JOptionPane.showMessageDialog(this,
                    "Report successfully exported to:\n" + file.getAbsolutePath(),
                    "File Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to export report to file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
