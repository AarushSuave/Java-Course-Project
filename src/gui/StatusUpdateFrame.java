package gui;

import model.Victim;
import service.RescueManager;

import java.awt.BorderLayout;
import java.awt.Color;
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

// Frame for updating the rescue lifecycle status of registered victims
public class StatusUpdateFrame extends JFrame {

    private final RescueManager rescueManager;
    private final DashboardFrame parentDashboard;

    private JComboBox<String> victimCombo;
    private JComboBox<String> statusCombo;
    private JLabel currentStatusLabel;

    public StatusUpdateFrame(DashboardFrame parentDashboard) {
        this.rescueManager = RescueManager.getInstance();
        this.parentDashboard = parentDashboard;

        setTitle("RescueNet - Victim Status Pipeline");
        setSize(580, 420);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        populateVictims();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header
        mainPanel.add(UITheme.createHeaderPanel(
                "Victim Status Update",
                "Progress victim through the disaster response lifecycle"
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
        victimCombo.addActionListener(e -> updateStatusPreview());
        formPanel.add(victimCombo, gbc);

        // Current Status Preview
        gbc.gridx = 1; gbc.gridy = 1;
        currentStatusLabel = new JLabel("Current Rescue Status: -");
        currentStatusLabel.setFont(UITheme.FONT_SMALL);
        currentStatusLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(currentStatusLabel, gbc);

        // New Status Selector
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel sLbl = new JLabel("New Status:");
        sLbl.setFont(UITheme.FONT_BOLD);
        formPanel.add(sLbl, gbc);

        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{
                "Pending",
                "Assigned",
                "Rescued",
                "Shelter Reached",
                "Completed"
        });
        statusCombo.setFont(UITheme.FONT_REGULAR);
        formPanel.add(statusCombo, gbc);

        // Update Button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);

        JPanel btnGroup = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);

        javax.swing.JButton updateBtn = UITheme.createPrimaryButton("Update Status");
        updateBtn.addActionListener(e -> performStatusUpdate());

        btnGroup.add(updateBtn);
        formPanel.add(btnGroup, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void populateVictims() {
        victimCombo.removeAllItems();
        List<Victim> victims = rescueManager.getAllVictims();
        for (Victim v : victims) {
            victimCombo.addItem(v.getVictimId() + " - " + v.getName() + " [" + v.getRescueStatus() + "]");
        }
        updateStatusPreview();
    }

    private int getSelectedVictimId() {
        String selected = (String) victimCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return -1;
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private void updateStatusPreview() {
        int id = getSelectedVictimId();
        if (id != -1) {
            Victim v = rescueManager.searchVictim(id);
            if (v != null) {
                currentStatusLabel.setText("<html><b>Current Rescue Status:</b> " + v.getRescueStatus() 
                        + " | <b>Medical:</b> " + v.getMedicalStatus() + "</html>");
                statusCombo.setSelectedItem(v.getRescueStatus());
            }
        }
    }

    private void performStatusUpdate() {
        int victimId = getSelectedVictimId();
        String newStatus = (String) statusCombo.getSelectedItem();

        if (victimId == -1 || newStatus == null) {
            JOptionPane.showMessageDialog(this, "Please select a victim.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = rescueManager.updateVictimStatus(victimId, newStatus);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Victim #" + victimId + " status updated to: '" + newStatus + "' successfully!",
                    "Status Updated", JOptionPane.INFORMATION_MESSAGE);

            populateVictims();
            if (parentDashboard != null) parentDashboard.refreshDashboardData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update victim status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
