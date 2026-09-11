package gui;

import model.Shelter;
import service.RescueManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
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

// Frame for Shelter Management: Monitoring Bed Capacity and Allocations
public class ShelterFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JTextField idField;
    private JTextField nameField;
    private JTextField locationField;
    private JTextField capacityField;
    private JTextField occupiedField;
    private DefaultTableModel tableModel;

    public ShelterFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Shelter Management");
        setSize(850, 520);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadShelterTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Relief Shelter Management", "Manage emergency shelter camps, capacities, and live bed occupancy"), BorderLayout.NORTH);

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

        JLabel formTitle = new JLabel("Register Shelter");
        formTitle.setFont(UITheme.FONT_HEADER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // Shelter ID
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Shelter ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setText(String.valueOf(rescueManager.getAllShelters().size() + 1));
        formPanel.add(idField, gbc);

        // Name
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Shelter Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(10);
        formPanel.add(nameField, gbc);

        // Location
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(10);
        formPanel.add(locationField, gbc);

        // Capacity
        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("Capacity (Beds):"), gbc);
        gbc.gridx = 1;
        capacityField = new JTextField(10);
        capacityField.setText("20");
        formPanel.add(capacityField, gbc);

        // Occupied
        gbc.gridy = 5; gbc.gridx = 0;
        formPanel.add(new JLabel("Current Occupancy:"), gbc);
        gbc.gridx = 1;
        occupiedField = new JTextField(10);
        occupiedField.setText("0");
        formPanel.add(occupiedField, gbc);

        // Add Button
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 4, 6, 4);
        javax.swing.JButton addBtn = UITheme.createPrimaryButton("Register Shelter");
        addBtn.addActionListener(e -> addShelter());
        formPanel.add(addBtn, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);

        // Right Table Panel
        String[] columns = {"ID", "Shelter Name", "Location", "Capacity", "Occupied", "Available Beds"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(UITheme.FONT_REGULAR);
        table.getTableHeader().setFont(UITheme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addShelter() {
        try {
            String idStr = idField.getText().trim();
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String capStr = capacityField.getText().trim();
            String occStr = occupiedField.getText().trim();

            if (idStr.isEmpty() || name.isEmpty() || location.isEmpty() || capStr.isEmpty() || occStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            int capacity = Integer.parseInt(capStr);
            int occupied = Integer.parseInt(occStr);

            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (occupied < 0 || occupied > capacity) {
                JOptionPane.showMessageDialog(this, "Occupancy must be between 0 and capacity.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (rescueManager.getShelterById(id) != null) {
                JOptionPane.showMessageDialog(this, "Shelter ID already exists! Please use a unique ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Shelter s = new Shelter(id, name, location, capacity, occupied);
            rescueManager.addShelter(s);

            loadShelterTable();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();

            JOptionPane.showMessageDialog(this, "Shelter registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            locationField.setText("");
            idField.setText(String.valueOf(id + 1));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Capacity, and Occupancy must be numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadShelterTable() {
        tableModel.setRowCount(0);
        List<Shelter> list = rescueManager.getAllShelters();
        for (Shelter s : list) {
            tableModel.addRow(new Object[]{
                    s.getShelterId(),
                    s.getName(),
                    s.getLocation(),
                    s.getCapacity(),
                    s.getOccupied(),
                    s.getAvailableCapacity()
            });
        }
    }
}
