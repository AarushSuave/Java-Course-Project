package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Login Frame providing authentication into RescueNet
public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("RescueNet - System Login");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = UITheme.createHeaderPanel("RescueNet Login", "Disaster Response Management System");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Card
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(UITheme.CARD_BG);
        cardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(20, 25, 20, 25),
                new LineBorder(new Color(230, 230, 230), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UITheme.FONT_BOLD);
        cardPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        userField = new JTextField(15);
        userField.setFont(UITheme.FONT_REGULAR);
        userField.setText("admin"); // Default convenience
        cardPanel.add(userField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.FONT_BOLD);
        cardPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passField = new JPasswordField(15);
        passField.setFont(UITheme.FONT_REGULAR);
        passField.setText("admin123"); // Default convenience
        cardPanel.add(passField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);

        JPanel btnPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton resetBtn = UITheme.createSecondaryButton("Clear");
        resetBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
        });

        JButton loginBtn = UITheme.createPrimaryButton("Login");
        loginBtn.addActionListener(e -> performLogin());

        // Press Enter to login
        passField.addActionListener(e -> performLogin());
        userField.addActionListener(e -> performLogin());

        btnPanel.add(resetBtn);
        btnPanel.add(loginBtn);
        cardPanel.add(btnPanel, gbc);

        // Default Credentials hint
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 8, 0, 8);
        JLabel hintLabel = new JLabel("Default: admin / admin123", JLabel.CENTER);
        hintLabel.setFont(UITheme.FONT_SMALL);
        hintLabel.setForeground(UITheme.TEXT_MUTED);
        cardPanel.add(hintLabel, gbc);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void performLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate credentials (admin / admin123)
        if ("admin".equals(username) && "admin123".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome to RescueNet.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            new DashboardFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", 
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }
}
