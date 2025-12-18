// TokoAlatPancingApp.java
package com.tokopancing;

import com.tokopancing.gui.MainForm;

public class TokoAlatPancingApp {
    public static void main(String[] args) {
        // Jalankan aplikasi
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });
    }
}