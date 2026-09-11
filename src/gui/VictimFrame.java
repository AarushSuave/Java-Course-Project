package gui;

import model.Victim;
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

// Frame for Victim Management: Registration, Search (Demonstrating Overloaded Methods), and Status Table
public class VictimFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField locationField;
    private JComboBox<String> medStatusCombo;
    private JComboBox<String> rescueStatusCombo;

    private JTextField searchField;
    private DefaultTableModel tableModel;

    public VictimFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Victim Management");
        setSize(980, 600);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadVictimTable(rescueManager.getAllVictims());
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Victim Registry & Search", "Register disaster-affected individuals and monitor their relief pipeline"), BorderLayout.NORTH);

        // Center split
        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setOpaque(false);

        // Left Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.CARD_BG);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(320, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Register Victim");
        formTitle.setFont(UITheme.FONT_HEADER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // Victim ID
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Victim ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setText(String.valueOf(1000 + rescueManager.getAllVictims().size() + 1));
        formPanel.add(idField, gbc);

        // Name
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(10);
        formPanel.add(nameField, gbc);

        // Age
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(10);
        formPanel.add(ageField, gbc);

        // Phone
        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(10);
        formPanel.add(phoneField, gbc);

        // Location
        gbc.gridy = 5; gbc.gridx = 0;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(10);
        formPanel.add(locationField, gbc);

        // Medical Status
        gbc.gridy = 6; gbc.gridx = 0;
        formPanel.add(new JLabel("Medical Status:"), gbc);
        gbc.gridx = 1;
        medStatusCombo = new JComboBox<>(new String[]{"Normal", "Minor Injury", "Critical", "Deceased"});
        formPanel.add(medStatusCombo, gbc);

        // Rescue Status
        gbc.gridy = 7; gbc.gridx = 0;
        formPanel.add(new JLabel("Rescue Status:"), gbc);
        gbc.gridx = 1;
        rescueStatusCombo = new JComboBox<>(new String[]{"Pending", "Assigned", "Rescued", "Shelter Reached", "Completed"});
        formPanel.add(rescueStatusCombo, gbc);

        // Submit Button
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 4, 4, 4);
        JButton addBtn = UITheme.createPrimaryButton("Register Victim");
        addBtn.addActionListener(e -> registerVictim());
        formPanel.add(addBtn, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);

        // Right Area: Search Bar on Top + Table
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);

        // Search Bar Panel
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchBarPanel.setBackground(UITheme.CARD_BG);
        searchBarPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));

        searchBarPanel.add(new JLabel("Search (by ID or Name):"));
        searchField = new JTextField(18);
        searchBarPanel.add(searchField);

        JButton searchBtn = UITheme.createPrimaryButton("Search");
        searchBtn.addActionListener(e -> performSearch());

        JButton resetSearchBtn = UITheme.createSecondaryButton("Show All");
        resetSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadVictimTable(rescueManager.getAllVictims());
        });

        searchBarPanel.add(searchBtn);
        searchBarPanel.add(resetSearchBtn);

        rightPanel.add(searchBarPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Age", "Phone", "Location", "Medical", "Rescue Status", "Team ID", "Shelter ID"};
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
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void registerVictim() {
        try {
            String idStr = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String phone = phoneField.getText().trim();
            String location = locationField.getText().trim();
            String medStatus = (String) medStatusCombo.getSelectedItem();
            String rescueStatus = (String) rescueStatusCombo.getSelectedItem();

            if (idStr.isEmpty() || name.isEmpty() || ageStr.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all mandatory fields (ID, Name, Age, Location).", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            int age = Integer.parseInt(ageStr);

            if (age < 0 || age > 130) {
                JOptionPane.showMessageDialog(this, "Please enter a realistic age.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (rescueManager.searchVictim(id) != null) {
                JOptionPane.showMessageDialog(this, "Victim ID already exists! Please use a unique ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Creating Victim object (Demonstrating Polymorphism & Object Instantiation)
            Victim v = new Victim(id, name, age, phone.isEmpty() ? "N/A" : phone, location, medStatus, rescueStatus);
            rescueManager.addVictim(v);

            loadVictimTable(rescueManager.getAllVictims());
            if (parentDashboard != null) parentDashboard.refreshDashboardData();

            JOptionPane.showMessageDialog(this, "Victim registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Reset fields
            nameField.setText("");
            ageField.setText("");
            phoneField.setText("");
            locationField.setText("");
            idField.setText(String.valueOf(id + 1));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Demonstrating Method Overloading:
    // Calling rescueManager.searchVictim(int) vs rescueManager.searchVictim(String)
    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadVictimTable(rescueManager.getAllVictims());
            return;
        }

        Victim found = null;
        try {
            // If user entered integer, call overloaded searchVictim(int id)
            int id = Integer.parseInt(query);
            found = rescueManager.searchVictim(id);
        } catch (NumberFormatException e) {
            // Otherwise, call overloaded searchVictim(String name)
            found = rescueManager.searchVictim(query);
        }

        if (found != null) {
            loadVictimTable(List.of(found));
        } else {
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "No victim found matching: " + query, "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadVictimTable(List<Victim> list) {
        tableModel.setRowCount(0);
        for (Victim v : list) {
            tableModel.addRow(new Object[]{
                    v.getVictimId(),
                    v.getName(),
                    v.getAge(),
                    v.getPhone(),
                    v.getLocation(),
                    v.getMedicalStatus(),
                    v.getRescueStatus(),
                    v.getAssignedTeamId() == 0 ? "-" : v.getAssignedTeamId(),
                    v.getAllocatedShelterId() == 0 ? "-" : v.getAllocatedShelterId()
            });
        }
    }
}
