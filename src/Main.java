import gui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Application Entry Point for RescueNet
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       RescueNet: Disaster Response System        ");
        System.out.println("   Second Year Engineering Java OOP Mini Project  ");
        System.out.println("==================================================");

        // Set System Look & Feel for crisp native UI components
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Default look and feel is used
        }

        // Launch GUI safely on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
