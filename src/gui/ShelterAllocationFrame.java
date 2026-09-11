package gui;

import exception.ShelterFullException;
import model.Shelter;
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

// Frame for Shelter Allocation demonstrating User-Defined Exception Handling (ShelterFullException)
public class ShelterAllocationFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JComboBox<String> victimCombo;
    private JComboBox<String> shelterCombo;
    private JLabel victimInfoLabel;
    private JLabel shelterInfoLabel;

    public ShelterAllocationFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Shelter Bed Allocation");
        setSize(660, 480);
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
        mainPanel.add(UITheme.createHeaderPanel(
                "Shelter Bed Allocation",
                "Assign rescued victims to relief shelters (Demonstrates ShelterFullException)"
        ), BorderLayout.NORTH);

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

        // Victim Details
        gbc.gridx = 1; gbc.gridy = 1;
        victimInfoLabel = new JLabel("Current Status: - | Location: -");
        victimInfoLabel.setFont(UITheme.FONT_SMALL);
        victimInfoLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(victimInfoLabel, gbc);

        // Shelter Selector
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel sLbl = new JLabel("Select Shelter:");
        sLbl.setFont(UITheme.FONT_BOLD);
        formPanel.add(sLbl, gbc);

        gbc.gridx = 1;
        shelterCombo = new JComboBox<>();
        shelterCombo.setFont(UITheme.FONT_REGULAR);
        shelterCombo.addActionListener(e -> updateShelterDetails());
        formPanel.add(shelterCombo, gbc);

        // Shelter Details
        gbc.gridx = 1; gbc.gridy = 3;
        shelterInfoLabel = new JLabel("Capacity: - | Occupied: - | Available Beds: -");
        shelterInfoLabel.setFont(UITheme.FONT_SMALL);
        shelterInfoLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(shelterInfoLabel, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);

        JPanel btnGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);

        javax.swing.JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> populateDropdowns());

        javax.swing.JButton allocBtn = UITheme.createPrimaryButton("Allocate to Shelter");
        allocBtn.addActionListener(e -> performAllocation());

        btnGroup.add(refreshBtn);
        btnGroup.add(allocBtn);
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

        shelterCombo.removeAllItems();
        List<Shelter> shelters = rescueManager.getAllShelters();
        for (Shelter s : shelters) {
            shelterCombo.addItem(s.getShelterId() + " - " + s.getName() + " [" + s.getOccupied() + "/" + s.getCapacity() + " Beds]");
        }

        updateVictimDetails();
        updateShelterDetails();
    }

    private int getSelectedVictimId() {
        String selected = (String) victimCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return -1;
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private int getSelectedShelterId() {
        String selected = (String) shelterCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return -1;
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private void updateVictimDetails() {
        int id = getSelectedVictimId();
        if (id != -1) {
            Victim v = rescueManager.searchVictim(id);
            if (v != null) {
                victimInfoLabel.setText("<html><b>Location:</b> " + v.getLocation() + " | <b>Status:</b> " + v.getRescueStatus() + "</html>");
            }
        }
    }

    private void updateShelterDetails() {
        int id = getSelectedShelterId();
        if (id != -1) {
            Shelter s = rescueManager.getShelterById(id);
            if (s != null) {
                String color = s.isAvailable() ? "green" : "red";
                shelterInfoLabel.setText("<html><b>Location:</b> " + s.getLocation() + " | <b>Occupancy:</b> " + s.getOccupied() + "/" + s.getCapacity() 
                        + " | <b>Available Beds:</b> <font color='" + color + "'><b>" + s.getAvailableCapacity() + "</b></font></html>");
            }
        }
    }

    // Demonstrating User-Defined Exception Handling with try-catch
    private void performAllocation() {
        int victimId = getSelectedVictimId();
        int shelterId = getSelectedShelterId();

        if (victimId == -1 || shelterId == -1) {
            JOptionPane.showMessageDialog(this, "Please select both a Victim and a Shelter.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Attempting allocation which may throw ShelterFullException
            boolean success = rescueManager.allocateVictimToShelter(victimId, shelterId);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Victim #" + victimId + " allocated to Shelter #" + shelterId + " successfully!\n"
                        + "Victim status updated to: 'Shelter Reached'.",
                        "Shelter Allocated", JOptionPane.INFORMATION_MESSAGE);

                populateDropdowns();
                if (parentDashboard != null) parentDashboard.refreshDashboardData();
            }
        } catch (ShelterFullException e) {
            // Catching custom user-defined exception and showing user-friendly message
            JOptionPane.showMessageDialog(this,
                    "[Custom Exception Caught: ShelterFullException]\n\n" + e.getMessage()
                    + "\n\nPlease select another relief shelter with available capacity.",
                    "Allocation Rejected - Shelter Full", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
