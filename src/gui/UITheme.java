package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

// Helper class for consistent modern UI styling across all Swing frames
public class UITheme {

    // High-Contrast Emergency Service Color Palette
    public static final Color PRIMARY_COLOR = new Color(20, 35, 60);       // Sleek Navy Slate
    public static final Color PRIMARY_LIGHT = new Color(30, 50, 85);
    public static final Color ACCENT_COLOR = new Color(225, 75, 20);       // Emergency Orange
    public static final Color ACCENT_HOVER = new Color(245, 95, 35);
    public static final Color SUCCESS_COLOR = new Color(34, 139, 74);      // Modern Green
    public static final Color BACKGROUND_COLOR = new Color(243, 245, 248); // Clean soft background
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(20, 25, 35);           // Deep high-contrast dark text
    public static final Color TEXT_MUTED = new Color(75, 85, 100);
    public static final Color BORDER_COLOR = new Color(190, 200, 215);

    // Modern High-Contrast Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.BOLD, 11);

    // Custom Button UI to bypass OS paint engine overriding colors
    public static class FlatButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final Color bgColor;
        private final Color fgColor;
        private final Color hoverColor;

        public FlatButtonUI(Color bg, Color fg, Color hover) {
            this.bgColor = bg;
            this.fgColor = fg;
            this.hoverColor = hover;
        }

        @Override
        public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
            JButton b = (JButton) c;
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            boolean isHover = b.getModel().isRollover();
            boolean isPressed = b.getModel().isPressed();

            if (!b.isEnabled()) {
                g2.setColor(new Color(220, 225, 232));
            } else if (isPressed) {
                g2.setColor(bgColor.darker());
            } else if (isHover) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(bgColor);
            }

            g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 6, 6);

            // Draw border
            g2.setColor(b.isEnabled() ? bgColor.darker() : new Color(190, 195, 205));
            g2.drawRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, 6, 6);

            // Draw text
            g2.setFont(b.getFont());
            java.awt.FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(b.getText());
            int textHeight = fm.getAscent();
            int x = (b.getWidth() - textWidth) / 2;
            int y = (b.getHeight() + textHeight) / 2 - 2;

            g2.setColor(b.isEnabled() ? fgColor : new Color(130, 140, 155));
            g2.drawString(b.getText(), x, y);
            g2.dispose();
        }
    }

    // High-Contrast Button Factory
    public static JButton createStyledButton(String text, Color bg, Color fg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setUI(new FlatButtonUI(bg, fg, hover));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 24, 34));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, new Color(20, 60, 125), Color.WHITE, new Color(30, 85, 170));
    }

    public static JButton createAccentButton(String text) {
        return createStyledButton(text, new Color(205, 35, 35), Color.WHITE, new Color(230, 50, 50));
    }

    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, new Color(34, 139, 74), Color.WHITE, new Color(42, 165, 88));
    }

    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, new Color(232, 237, 245), new Color(20, 30, 45), new Color(218, 226, 238));
    }

    // Modern Header Panel with Clean Aesthetic
    public static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel header = new JPanel();
        header.setLayout(new java.awt.BorderLayout(5, 4));
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(16, 22, 16, 22));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(FONT_SMALL);
        subLabel.setForeground(new Color(185, 205, 230));

        header.add(titleLabel, java.awt.BorderLayout.NORTH);
        header.add(subLabel, java.awt.BorderLayout.SOUTH);

        return header;
    }

    /**
     * Styles any JTable with:
     * 1. Clean row height and modern font
     * 2. Alternate row striping (#FAFAFC vs White)
     * 3. Distinct colored badges for Critical medical status or Pending rescue
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(FONT_REGULAR);
        table.setShowGrid(true);
        table.setGridColor(new Color(235, 240, 245));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(242, 246, 250));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // Alternating row renderer with status badges
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    if (row % 2 == 1) {
                        c.setBackground(new Color(250, 252, 255));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                // Highlight Critical status rows with subtle urgency
                String strVal = (value != null) ? value.toString() : "";
                if ("Critical".equalsIgnoreCase(strVal)) {
                    c.setForeground(new Color(211, 47, 47));
                    c.setFont(FONT_BOLD);
                } else if ("Rescued".equalsIgnoreCase(strVal) || "Shelter Reached".equalsIgnoreCase(strVal) || "Completed".equalsIgnoreCase(strVal)) {
                    c.setForeground(new Color(46, 125, 50));
                    c.setFont(FONT_BOLD);
                } else if (!isSelected) {
                    c.setForeground(TEXT_DARK);
                    c.setFont(FONT_REGULAR);
                }

                return c;
            }
        });
    }
}

