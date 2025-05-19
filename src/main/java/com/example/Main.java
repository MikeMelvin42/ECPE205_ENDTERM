package org.example;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GUIFrame frame = new GUIFrame("i pushed the key");
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Fatal Error: " + e.getMessage(),
                        "Startup Failure",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
