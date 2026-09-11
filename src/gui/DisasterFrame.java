package gui;

import model.Disaster;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

// Frame for Disaster Management: Adding and Viewing Disasters
public class DisasterFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JTextField idField;
    private JComboBox<String> typeCombo;
    private JTextField locationField;
    private JComboBox<String> severityCombo;
    private DefaultTableModel tableModel;

    public DisasterFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Disaster Management");
        setSize(750, 520);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadDisasterTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Disaster Management", "Register and track active disaster occurrences"), BorderLayout.NORTH);

        // Center Split: Left (Add Form) & Right (Table)
        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setOpaque(false);

        // Left Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BG);
        formPanel.setBorder(new javax.swing.border.CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(280, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel formTitle = new JLabel("Register Disaster");
        formTitle.setFont(UITheme.FONT_HEADER);
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;
        // Disaster ID
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Disaster ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setText(String.valueOf(100 + rescueManager.getAllDisasters().size() + 1));
        formPanel.add(idField, gbc);

        // Disaster Type
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"Flood", "Earthquake", "Fire", "Cyclone", "Landslide", "Tsunami", "Industrial Hazard"});
        formPanel.add(typeCombo, gbc);

        // Location
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(10);
        formPanel.add(locationField, gbc);

        // Severity
        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("Severity:"), gbc);
        gbc.gridx = 1;
        severityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        formPanel.add(severityCombo, gbc);

        // Add Button
        gbc.gridy = 5; gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 4, 6, 4);
        javax.swing.JButton addBtn = UITheme.createPrimaryButton("Add Disaster");
        addBtn.addActionListener(e -> addDisaster());
        formPanel.add(addBtn, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);

        // Right Table Panel
        String[] columns = {"ID", "Type", "Location", "Severity"};
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

    private void addDisaster() {
        try {
            String idStr = idField.getText().trim();
            String location = locationField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String severity = (String) severityCombo.getSelectedItem();

            if (idStr.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            if (rescueManager.getDisasterById(id) != null) {
                JOptionPane.showMessageDialog(this, "Disaster ID already exists! Choose a unique ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Disaster d = new Disaster(id, type, location, severity);
            rescueManager.addDisaster(d);

            loadDisasterTable();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();

            JOptionPane.showMessageDialog(this, "Disaster registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            locationField.setText("");
            idField.setText(String.valueOf(id + 1));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Disaster ID must be a valid integer number.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDisasterTable() {
        tableModel.setRowCount(0);
        List<Disaster> list = rescueManager.getAllDisasters();
        for (Disaster d : list) {
            tableModel.addRow(new Object[]{
                    d.getDisasterId(),
                    d.getType(),
                    d.getLocation(),
                    d.getSeverity()
            });
        }
    }
}
