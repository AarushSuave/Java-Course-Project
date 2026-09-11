package gui;

import model.Ambulance;
import model.RescueTeam;
import model.RescueVan;
import model.Vehicle;
import service.RescueManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

// Frame for Rescue Team Management: Demonstrating Polymorphism & Vehicle Hierarchy
public class RescueTeamFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JTextField idField;
    private JTextField nameField;
    private JTextField countField;
    private JComboBox<String> vehicleTypeCombo;
    private DefaultTableModel tableModel;
    private JTable table;

    public RescueTeamFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Rescue Team Management");
        setSize(850, 540);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadTeamTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Rescue Team Management", "Deploy and inspect tactical rescue units with specialized vehicles"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setOpaque(false);

        // Left Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BG);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(300, 380));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Add Rescue Team");
        formTitle.setFont(UITheme.FONT_HEADER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // Team ID
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Team ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setText(String.valueOf(rescueManager.getAllTeams().size() + 1));
        formPanel.add(idField, gbc);

        // Team Name
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Team Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(10);
        formPanel.add(nameField, gbc);

        // Member Count
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Members:"), gbc);
        gbc.gridx = 1;
        countField = new JTextField(10);
        countField.setText("6");
        formPanel.add(countField, gbc);

        // Vehicle Type
        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("Vehicle:"), gbc);
        gbc.gridx = 1;
        vehicleTypeCombo = new JComboBox<>(new String[]{"Ambulance (ICU)", "Rescue Van (Evacuation)"});
        formPanel.add(vehicleTypeCombo, gbc);

        // Add Team Button
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 4, 6, 4);
        JButton addBtn = UITheme.createPrimaryButton("Add Team");
        addBtn.addActionListener(e -> addTeam());
        formPanel.add(addBtn, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);

        // Right Table + Action buttons
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);

        String[] columns = {"ID", "Team Name", "Members", "Availability", "Assigned Vehicle"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(UITheme.FONT_REGULAR);
        table.getTableHeader().setFont(UITheme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Action Toolbar below Table
        JPanel actionToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        actionToolbar.setOpaque(false);

        JButton toggleBtn = UITheme.createSecondaryButton("Toggle Availability (Busy/Ready)");
        toggleBtn.addActionListener(e -> toggleTeamStatus());

        JButton opBtn = UITheme.createAccentButton("Run Rescue Operation Test");
        opBtn.addActionListener(e -> testRescueOperation());

        actionToolbar.add(toggleBtn);
        actionToolbar.add(opBtn);
        rightPanel.add(actionToolbar, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addTeam() {
        try {
            String idStr = idField.getText().trim();
            String name = nameField.getText().trim();
            String countStr = countField.getText().trim();
            String vehicleChoice = (String) vehicleTypeCombo.getSelectedItem();

            if (idStr.isEmpty() || name.isEmpty() || countStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            int count = Integer.parseInt(countStr);

            if (rescueManager.getTeamById(id) != null) {
                JOptionPane.showMessageDialog(this, "Team ID already exists! Please use a unique ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Demonstrating Polymorphism: Vehicle base reference holding Ambulance or RescueVan
            Vehicle assignedVehicle;
            if (vehicleChoice != null && vehicleChoice.contains("Ambulance")) {
                assignedVehicle = new Ambulance("AMB-" + id, "Advanced Life Support", 2, true);
            } else {
                assignedVehicle = new RescueVan("VAN-" + id, "Heavy Rescue Carrier", 10, true);
            }

            RescueTeam team = new RescueTeam(id, name, count, true, assignedVehicle);
            rescueManager.addRescueTeam(team);

            loadTeamTable();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();

            JOptionPane.showMessageDialog(this, "Rescue Team added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            idField.setText(String.valueOf(id + 1));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Member Count must be numeric values.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTeamStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a team from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int teamId = (int) tableModel.getValueAt(selectedRow, 0);
        RescueTeam team = rescueManager.getTeamById(teamId);
        if (team != null) {
            team.setAvailable(!team.isAvailable());
            loadTeamTable();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();
            JOptionPane.showMessageDialog(this, "Team availability updated to: " + (team.isAvailable() ? "AVAILABLE" : "BUSY"), 
                    "Status Changed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void testRescueOperation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a team to run the rescue operation test.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int teamId = (int) tableModel.getValueAt(selectedRow, 0);
        RescueTeam team = rescueManager.getTeamById(teamId);
        if (team != null) {
            // Polymorphic dispatch demonstration
            Vehicle v = team.getAssignedVehicle();
            String message = "Team: " + team.getTeamName() + "\nVehicle: " + (v != null ? v.getVehicleType() : "None")
                    + "\n\n[Polymorphism In Action]:";
            if (v instanceof Ambulance) {
                message += "\nAmbulance dispatched! Paramedics treating critical casualties.";
            } else if (v instanceof RescueVan) {
                message += "\nRescue Van deployed! Transporting evacuation equipment.";
            } else {
                message += "\nStandard squad mobilized.";
            }
            JOptionPane.showMessageDialog(this, message, "Rescue Operation Live Test", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadTeamTable() {
        tableModel.setRowCount(0);
        List<RescueTeam> list = rescueManager.getAllTeams();
        for (RescueTeam t : list) {
            tableModel.addRow(new Object[]{
                    t.getTeamId(),
                    t.getTeamName(),
                    t.getMemberCount(),
                    t.isAvailable() ? "Available (Ready)" : "Busy (On Mission)",
                    t.getAssignedVehicle() != null ? t.getAssignedVehicle().getVehicleType() : "None"
            });
        }
    }
}
