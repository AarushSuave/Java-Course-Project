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
    private JButton toggleDbBtn;

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
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(820, 85));
        statsPanel.setPreferredSize(new Dimension(820, 85));

        statDisasters = new JLabel("0", JLabel.CENTER);
        statVictims = new JLabel("0", JLabel.CENTER);
        statTeams = new JLabel("0", JLabel.CENTER);
        statShelters = new JLabel("0", JLabel.CENTER);

        statsPanel.add(createStatCard("Active Disasters", statDisasters, new Color(198, 40, 40)));
        statsPanel.add(createStatCard("Registered Victims", statVictims, new Color(211, 84, 0)));
        statsPanel.add(createStatCard("Available Teams", statTeams, new Color(34, 139, 74)));
        statsPanel.add(createStatCard("Available Beds", statShelters, new Color(21, 101, 192)));

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
        liveStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        liveStatusLabel.setForeground(new Color(20, 30, 45));

        JPanel rightBtnGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        rightBtnGroup.setOpaque(false);

        // Simulation Toggle Button for Live Demo with Examiners
        toggleDbBtn = UITheme.createStyledButton(
                service.DatabaseConnection.isSimulatedOffline() ? "🔌 Reconnect DB" : "⚡ Simulate DB Failure",
                service.DatabaseConnection.isSimulatedOffline() ? new Color(34, 139, 74) : new Color(205, 35, 35),
                Color.WHITE,
                service.DatabaseConnection.isSimulatedOffline() ? new Color(42, 165, 88) : new Color(230, 50, 50)
        );
        toggleDbBtn.setToolTipText("Toggle MySQL offline/online simulation to test offline queue & sync live");
        toggleDbBtn.addActionListener(e -> {
            boolean currentlyOffline = service.DatabaseConnection.isSimulatedOffline();
            service.DatabaseConnection.setSimulatedOffline(!currentlyOffline);
            boolean nowOffline = service.DatabaseConnection.isSimulatedOffline();

            toggleDbBtn.setText(nowOffline ? "🔌 Reconnect DB" : "⚡ Simulate DB Failure");
            toggleDbBtn.setUI(new UITheme.FlatButtonUI(
                    nowOffline ? new Color(34, 139, 74) : new Color(205, 35, 35),
                    Color.WHITE,
                    nowOffline ? new Color(42, 165, 88) : new Color(230, 50, 50)
            ));

            refreshDashboardData();

            if (nowOffline) {
                JOptionPane.showMessageDialog(this,
                        "Simulated DB Failure ACTIVE!\n\n"
                                + "- MySQL connection is now blocked by the app.\n"
                                + "- All additions & updates will be queued into 'offline_sync_queue.txt'.\n"
                                + "- Check the bottom status bar: 'Offline Mode'.",
                        "Offline Simulation On", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "MySQL Reconnected!\n\n"
                                + "- Live connection restored.\n"
                                + "- Background monitor will auto-sync pending records, or click 'Sync Now'.",
                        "Database Online", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton syncBtn = UITheme.createPrimaryButton("Sync Now");
        syncBtn.setToolTipText("Immediately push queued offline actions into MySQL");
        syncBtn.addActionListener(e -> {
            int pending = service.OfflineSyncService.getPendingCount();
            if (pending == 0) {
                JOptionPane.showMessageDialog(this, "No pending offline actions to sync. Database is up to date!", "Sync Status", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!service.DatabaseConnection.isDatabaseAvailable()) {
                JOptionPane.showMessageDialog(this, "Cannot sync: MySQL database server is currently unreachable.\nRecords remain safely stored in the offline queue file.", "Database Offline", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean ok = service.OfflineSyncService.syncPendingToDatabase();
            if (ok) {
                refreshDashboardData();
                JOptionPane.showMessageDialog(this, "Successfully synchronized all pending operations into MySQL!", "Sync Completed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Sync attempted but some items could not be processed. Remaining items kept in queue.", "Partial Sync", JOptionPane.WARNING_MESSAGE);
            }
        });

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

        rightBtnGroup.add(toggleDbBtn);
        rightBtnGroup.add(syncBtn);
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
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 220, 232), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel titleLbl = new JLabel(title, JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(new Color(90, 105, 125));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(accentColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JButton createModuleButton(String title, String subtitle) {
        JButton btn = new JButton("<html><center><b style='color:#0F1D33; font-size:15px;'>" + title + "</b><br/><span style='color:#3E4B5E; font-size:12px; font-weight:600;'>" + subtitle + "</span></center></html>");
        btn.setFont(UITheme.FONT_REGULAR);
        btn.setBackground(new Color(245, 248, 252));
        btn.setForeground(new Color(15, 29, 51));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(185, 200, 220), 1, true),
                new EmptyBorder(14, 12, 14, 12)
        ));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Interactive hover feedback
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new java.awt.Color(220, 235, 255));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(20, 60, 125), 2, true),
                        new EmptyBorder(13, 11, 13, 11)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(245, 248, 252));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(185, 200, 220), 1, true),
                        new EmptyBorder(14, 12, 14, 12)
                ));
            }
        });

        return btn;
    }

    public void refreshDashboardData() {
        statDisasters.setText(String.valueOf(rescueManager.getAllDisasters().size()));
        statVictims.setText(String.valueOf(rescueManager.getAllVictims().size()));
        statTeams.setText(String.valueOf(rescueManager.getAvailableTeamsCount()) + "/" + rescueManager.getAllTeams().size());
        statShelters.setText(String.valueOf(rescueManager.getTotalShelterAvailableSpaces()));

        if (toggleDbBtn != null) {
            boolean isOffline = service.DatabaseConnection.isSimulatedOffline();
            toggleDbBtn.setText(isOffline ? "🔌 Reconnect DB" : "⚡ Simulate DB Failure");
            toggleDbBtn.setUI(new UITheme.FlatButtonUI(
                    isOffline ? new Color(34, 139, 74) : new Color(205, 35, 35),
                    Color.WHITE,
                    isOffline ? new Color(42, 165, 88) : new Color(230, 50, 50)
            ));
        }
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
