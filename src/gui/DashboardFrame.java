package gui;

import service.RescueManager;
import service.StatusMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Main Dashboard providing access to all RescueNet modules and hosting the background StatusMonitor Thread
public class DashboardFrame extends JFrame implements StatusMonitor.StatusUpdateListener {

    private final RescueManager rescueManager;
    private StatusMonitor statusMonitor;

    private JLabel statDisasters;
    private JLabel statVictims;
    private JLabel statTeams;
    private JLabel statShelters;
    private JLabel liveStatusLabel;

    public DashboardFrame() {
        this.rescueManager = RescueManager.getInstance();

        setTitle("RescueNet - Disaster Response Management System");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        // Start Multithreading Status Monitor
        startStatusMonitor();

        // Cleanly stop thread on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (statusMonitor != null) {
                    statusMonitor.stopMonitoring();
                }
            }
        });
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header Panel
        JPanel headerPanel = UITheme.createHeaderPanel(
                "RescueNet Command Dashboard",
                "Real-time Disaster Response & Resource Management Portal"
        );
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Content Area (Stats Cards + Action Grid)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // 1. Stats Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(820, 80));
        statsPanel.setPreferredSize(new Dimension(820, 80));

        statDisasters = new JLabel("0", JLabel.CENTER);
        statVictims = new JLabel("0", JLabel.CENTER);
        statTeams = new JLabel("0", JLabel.CENTER);
        statShelters = new JLabel("0", JLabel.CENTER);

        statsPanel.add(createStatCard("Active Disasters", statDisasters, new Color(211, 47, 47)));
        statsPanel.add(createStatCard("Registered Victims", statVictims, UITheme.ACCENT_COLOR));
        statsPanel.add(createStatCard("Available Teams", statTeams, UITheme.SUCCESS_COLOR));
        statsPanel.add(createStatCard("Available Beds", statShelters, new Color(25, 118, 210)));

        centerPanel.add(statsPanel);
        centerPanel.add(Box.createVerticalStrut(15));

        // 2. Action Modules Grid (8 Major Modules)
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        gridPanel.setOpaque(false);

        JButton btnDisasters = createModuleButton("1. Disasters", "Register & View Disasters");
        btnDisasters.addActionListener(e -> new DisasterFrame(this).setVisible(true));

        JButton btnVictims = createModuleButton("2. Victims", "Register & Search Victims");
        btnVictims.addActionListener(e -> new VictimFrame(this).setVisible(true));

        JButton btnTeams = createModuleButton("3. Rescue Teams", "Manage Teams & Vehicles");
        btnTeams.addActionListener(e -> new RescueTeamFrame(this).setVisible(true));

        JButton btnShelters = createModuleButton("4. Shelters", "Manage Relief Shelters");
        btnShelters.addActionListener(e -> new ShelterFrame(this).setVisible(true));

        JButton btnAssign = createModuleButton("5. Assign Rescue", "Deploy Teams to Victims");
        btnAssign.addActionListener(e -> new RescueAssignmentFrame(this).setVisible(true));

        JButton btnShelterAlloc = createModuleButton("6. Shelter Allocation", "Allocate Beds to Victims");
        btnShelterAlloc.addActionListener(e -> new ShelterAllocationFrame(this).setVisible(true));

        JButton btnStatus = createModuleButton("7. Update Status", "Track Rescue Progress");
        btnStatus.addActionListener(e -> new StatusUpdateFrame(this).setVisible(true));

        JButton btnReports = createModuleButton("8. Reports", "Generate & Save Reports");
        btnReports.addActionListener(e -> new ReportFrame().setVisible(true));

        gridPanel.add(btnDisasters);
        gridPanel.add(btnVictims);
        gridPanel.add(btnTeams);
        gridPanel.add(btnShelters);
        gridPanel.add(btnAssign);
        gridPanel.add(btnShelterAlloc);
        gridPanel.add(btnStatus);
        gridPanel.add(btnReports);

        centerPanel.add(gridPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Footer (Live Thread Status Bar + Logout Button)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 5));
        bottomPanel.setBackground(UITheme.CARD_BG);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        liveStatusLabel = new JLabel("● Initializing background status monitor thread...");
        liveStatusLabel.setFont(UITheme.FONT_REGULAR);
        liveStatusLabel.setForeground(new Color(46, 125, 50));

        JPanel rightBtnGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        rightBtnGroup.setOpaque(false);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshDashboardData());

        JButton logoutBtn = UITheme.createAccentButton("Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (statusMonitor != null) statusMonitor.stopMonitoring();
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });

        rightBtnGroup.add(refreshBtn);
        rightBtnGroup.add(logoutBtn);

        bottomPanel.add(liveStatusLabel, BorderLayout.WEST);
        bottomPanel.add(rightBtnGroup, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Load initial numbers
        refreshDashboardData();
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel titleLbl = new JLabel(title, JLabel.CENTER);
        titleLbl.setFont(UITheme.FONT_SMALL);
        titleLbl.setForeground(UITheme.TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JButton createModuleButton(String title, String subtitle) {
        JButton btn = new JButton("<html><center><b><font size='4'>" + title + "</font></b><br/><font size='2' color='#666666'>" + subtitle + "</font></center></html>");
        btn.setFont(UITheme.FONT_REGULAR);
        btn.setBackground(UITheme.CARD_BG);
        btn.setForeground(UITheme.TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 215, 225), 1, true),
                new EmptyBorder(12, 8, 12, 8)
        ));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    public void refreshDashboardData() {
        statDisasters.setText(String.valueOf(rescueManager.getAllDisasters().size()));
        statVictims.setText(String.valueOf(rescueManager.getAllVictims().size()));
        statTeams.setText(String.valueOf(rescueManager.getAvailableTeamsCount()) + "/" + rescueManager.getAllTeams().size());
        statShelters.setText(String.valueOf(rescueManager.getTotalShelterAvailableSpaces()));
    }

    private void startStatusMonitor() {
        statusMonitor = new StatusMonitor(rescueManager);
        statusMonitor.setStatusUpdateListener(this);
        statusMonitor.start(); // Demonstrating Thread start()
    }

    // Thread Callback Implementation
    @Override
    public void onStatusUpdate(String statusMessage) {
        liveStatusLabel.setText(statusMessage);
        refreshDashboardData();
    }
}
