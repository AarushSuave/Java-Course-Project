package gui;

import model.RescueTeam;
import model.Victim;
import service.RescueManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Frame for Assigning Rescue Teams to Victims needing assistance
public class RescueAssignmentFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JComboBox<String> victimCombo;
    private JComboBox<String> teamCombo;
    private JLabel victimInfoLabel;
    private JLabel teamInfoLabel;

    public RescueAssignmentFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Rescue Mission Assignment");
        setSize(650, 480);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        populateDropdowns();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Rescue Mission Assignment", "Deploy available rescue units with specialized vehicles to pending victims"), BorderLayout.NORTH);

        // Center Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BG);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Victim Selector
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel vLbl = new JLabel("Select Victim:");
        vLbl.setFont(UITheme.FONT_BOLD);
        formPanel.add(vLbl, gbc);

        gbc.gridx = 1;
        victimCombo = new JComboBox<>();
        victimCombo.setFont(UITheme.FONT_REGULAR);
        victimCombo.addActionListener(e -> updateVictimDetails());
        formPanel.add(victimCombo, gbc);

        // Victim Details Preview
        gbc.gridx = 1; gbc.gridy = 1;
        victimInfoLabel = new JLabel("Status: - | Location: -");
        victimInfoLabel.setFont(UITheme.FONT_SMALL);
        victimInfoLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(victimInfoLabel, gbc);

        // Team Selector
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel tLbl = new JLabel("Select Rescue Team:");
        tLbl.setFont(UITheme.FONT_BOLD);
        formPanel.add(tLbl, gbc);

        gbc.gridx = 1;
        teamCombo = new JComboBox<>();
        teamCombo.setFont(UITheme.FONT_REGULAR);
        teamCombo.addActionListener(e -> updateTeamDetails());
        formPanel.add(teamCombo, gbc);

        // Team Details Preview
        gbc.gridx = 1; gbc.gridy = 3;
        teamInfoLabel = new JLabel("Availability: - | Vehicle: -");
        teamInfoLabel.setFont(UITheme.FONT_SMALL);
        teamInfoLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(teamInfoLabel, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);

        JPanel btnGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);

        javax.swing.JButton refreshBtn = UITheme.createSecondaryButton("Refresh Lists");
        refreshBtn.addActionListener(e -> populateDropdowns());

        javax.swing.JButton assignBtn = UITheme.createPrimaryButton("Deploy & Assign Team");
        assignBtn.addActionListener(e -> assignTeam());

        btnGroup.add(refreshBtn);
        btnGroup.add(assignBtn);
        formPanel.add(btnGroup, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void populateDropdowns() {
        victimCombo.removeAllItems();
        List<Victim> victims = rescueManager.getAllVictims();
        for (Victim v : victims) {
            victimCombo.addItem(v.getVictimId() + " - " + v.getName() + " (" + v.getRescueStatus() + ")");
        }

        teamCombo.removeAllItems();
        List<RescueTeam> teams = rescueManager.getAllTeams();
        for (RescueTeam t : teams) {
            teamCombo.addItem(t.getTeamId() + " - " + t.getTeamName() + (t.isAvailable() ? " [Available]" : " [Busy]"));
        }

        updateVictimDetails();
        updateTeamDetails();
    }

    private int getSelectedVictimId() {
        String selected = (String) victimCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return -1;
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private int getSelectedTeamId() {
        String selected = (String) teamCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return -1;
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private void updateVictimDetails() {
        int id = getSelectedVictimId();
        if (id != -1) {
            Victim v = rescueManager.searchVictim(id);
            if (v != null) {
                victimInfoLabel.setText("<html><b>Location:</b> " + v.getLocation() + " | <b>Medical:</b> " + v.getMedicalStatus() 
                        + " | <b>Status:</b> " + v.getRescueStatus() + "</html>");
            }
        }
    }

    private void updateTeamDetails() {
        int id = getSelectedTeamId();
        if (id != -1) {
            RescueTeam t = rescueManager.getTeamById(id);
            if (t != null) {
                String vType = (t.getAssignedVehicle() != null) ? t.getAssignedVehicle().getVehicleType() : "None";
                teamInfoLabel.setText("<html><b>Status:</b> " + (t.isAvailable() ? "<font color='green'>Available</font>" : "<font color='red'>Busy</font>") 
                        + " | <b>Vehicle:</b> " + vType + "</html>");
            }
        }
    }

    private void assignTeam() {
        int victimId = getSelectedVictimId();
        int teamId = getSelectedTeamId();

        if (victimId == -1 || teamId == -1) {
            JOptionPane.showMessageDialog(this, "Please select both a Victim and a Rescue Team.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        RescueTeam team = rescueManager.getTeamById(teamId);
        if (team == null || !team.isAvailable()) {
            JOptionPane.showMessageDialog(this, "The selected Rescue Team is currently BUSY on another mission.", "Team Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = rescueManager.assignRescueTeamToVictim(victimId, teamId);
        if (success) {
            // Polymorphic dispatch demonstration
            team.executeRescue();

            JOptionPane.showMessageDialog(this,
                    "Team '" + team.getTeamName() + "' successfully assigned to Victim #" + victimId + "!\n"
                    + "Team status set to BUSY. Victim status set to ASSIGNED.",
                    "Mission Dispatched", JOptionPane.INFORMATION_MESSAGE);

            populateDropdowns();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign rescue mission.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
