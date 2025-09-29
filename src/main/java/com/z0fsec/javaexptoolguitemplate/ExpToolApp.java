package com.z0fsec.javaexptoolguitemplate;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.z0fsec.javaexptoolguitemplate.ui.MainPanel;
import com.z0fsec.javaexptoolguitemplate.util.MenuUtil;
import com.z0fsec.javaexptoolguitemplate.util.Z0fSecConstants;

import javax.swing.*;
import java.awt.*;

public class ExpToolApp {

    private JFrame frame;
    private JTabbedPane tabbedPane;

    // 各Tab页的面板实例
    private MainPanel mainPanel;


    public ExpToolApp() {
        frame = new JFrame(Z0fSecConstants.NAME + " " + Z0fSecConstants.VERSION +" By "+ Z0fSecConstants.AUTHOR +" - "+ Z0fSecConstants.DESCRIPTION);
        frame.setJMenuBar(MenuUtil.createMenuBar(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 700);
        frame.setLocationRelativeTo(null);

        mainPanel =new MainPanel(this::appendLog);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("TextArea.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("TabbedPane.font", font);
            UIManager.put("CheckBox.font", font);
            UIManager.put("ComboBox.font", font);
        } catch (Exception e) {
            // 忽略字体设置异常
        }

        SwingUtilities.invokeLater(ExpToolApp::new);
    }
    private void appendLog(String message) {
        int selectedTab = tabbedPane.getSelectedIndex();
        switch (selectedTab) {
            case 0:
                mainPanel.appendLog(message);
                break;
        }
    }

}
