package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Helper class for consistent UI styling across all Swing frames
public class UITheme {

    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(24, 43, 73);      // Deep Navy
    public static final Color ACCENT_COLOR = new Color(230, 81, 0);       // Alert Orange
    public static final Color SUCCESS_COLOR = new Color(46, 125, 50);     // Forest Green
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);// Clean Off-white/light gray
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(33, 33, 33);
    public static final Color TEXT_MUTED = new Color(117, 117, 117);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    // Button Styling Helper
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR, Color.WHITE);
    }

    public static JButton createAccentButton(String text) {
        return createStyledButton(text, ACCENT_COLOR, Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, new Color(224, 224, 224), TEXT_DARK);
    }

    // Header Panel Helper
    public static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel header = new JPanel();
        header.setLayout(new java.awt.BorderLayout(5, 5));
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(FONT_SMALL);
        subLabel.setForeground(new Color(200, 220, 240));

        header.add(titleLabel, java.awt.BorderLayout.NORTH);
        header.add(subLabel, java.awt.BorderLayout.SOUTH);

        return header;
    }
}
