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

        // Set modern UI Look & Feel safely
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName()) || "Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
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
