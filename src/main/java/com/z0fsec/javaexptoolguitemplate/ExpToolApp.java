package com.z0fsec.javaexptoolguitemplate;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.z0fsec.javaexptoolguitemplate.ui.MainPanel;
import com.z0fsec.javaexptoolguitemplate.util.MenuUtil;
import com.z0fsec.javaexptoolguitemplate.util.Z0fSecConstants;

import javax.swing.*;
import java.awt.*;

public class ExpToolApp {

    private JFrame frame;
    private MainPanel mainPanel;

    public ExpToolApp() {
        initializeLookAndFeel();
        createAndShowGUI();
    }

    public static void main(String[] args) {
        // 设置系统属性以改善跨平台体验
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", Z0fSecConstants.NAME);

        // 在EDT中启动GUI
        SwingUtilities.invokeLater(() -> {
            try {
                new ExpToolApp();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "启动应用程序时发生错误:\n" + e.getMessage(),
                        "启动错误",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());

            // 设置全局字体
            Font font = new Font("Microsoft YaHei UI", Font.PLAIN, 13);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("TextArea.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("TabbedPane.font", font);
            UIManager.put("CheckBox.font", font);
            UIManager.put("ComboBox.font", font);
            UIManager.put("Table.font", font);
            UIManager.put("List.font", font);
            UIManager.put("Menu.font", font);
            UIManager.put("MenuItem.font", font);

            // 设置一些FlatLaf的特定属性以获得更好的视觉效果
            UIManager.put("Component.arrowType", "chevron");
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Button.arc", 8);
            UIManager.put("TextComponent.arc", 5);

        } catch (Exception e) {
            System.err.println("Failed to initialize LaF: " + e.getMessage());
            // 回退到系统默认外观
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame(Z0fSecConstants.NAME + " " + Z0fSecConstants.VERSION +
                " - By " + Z0fSecConstants.AUTHOR + " " + Z0fSecConstants.DESCRIPTION);

        // 设置窗口图标
        setWindowIcon();

        // 创建菜单栏
        frame.setJMenuBar(MenuUtil.createMenuBar(frame));

        // 设置窗口属性
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750); // 稍微增大窗口以适应新布局
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null); // 居中显示

        // 设置窗口关闭时的确认对话框
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });

        // 创建主面板 - 不再需要传递日志消费者，因为MainPanel现在使用自己的彩色日志系统
        mainPanel = new MainPanel(System.out::println);

        // 添加主面板到窗口
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // 显示窗口
        frame.setVisible(true);

        // 触发面板就绪回调
        SwingUtilities.invokeLater(() -> mainPanel.onPanelReady());
    }

    private void setWindowIcon() {
        try {
            // 尝试设置窗口图标（如果有的话）
            // ImageIcon icon = new ImageIcon(getClass().getResource("/icons/app-icon.png"));
            // frame.setIconImage(icon.getImage());

            // 设置任务栏图标（Windows）
//            Taskbar taskbar = Taskbar.getTaskbar();
            try {
                // 如果找到图标文件，设置任务栏图标
                // taskbar.setIconImage(icon.getImage());
            } catch (UnsupportedOperationException e) {
                System.out.println("The os does not support: 'taskbar.setIconImage'");
            } catch (SecurityException e) {
                System.out.println("There was a security exception for: 'taskbar.setIconImage'");
            }
        } catch (Exception e) {
            // 忽略图标设置异常
            System.err.println("Could not set window icon: " + e.getMessage());
        }
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                frame,
                "确定要退出 " + Z0fSecConstants.NAME + " 吗？",
                "确认退出",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // 保存配置或执行清理操作（如果有的话）
            saveApplicationState();
            frame.dispose();
            System.exit(0);
        }
    }

    private void saveApplicationState() {
        // 这里可以添加保存应用程序状态的逻辑
        // 例如：保存窗口大小、位置、最近使用的目标地址等
        try {
            // 示例：保存窗口尺寸和位置
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node(this.getClass().getName());
            prefs.putInt("window_width", frame.getWidth());
            prefs.putInt("window_height", frame.getHeight());
            prefs.putInt("window_x", frame.getX());
            prefs.putInt("window_y", frame.getY());

            System.out.println("应用程序状态已保存");
        } catch (Exception e) {
            System.err.println("保存应用程序状态失败: " + e.getMessage());
        }
    }

    private void loadApplicationState() {
        // 这里可以添加加载应用程序状态的逻辑
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node(this.getClass().getName());
            int width = prefs.getInt("window_width", 1000);
            int height = prefs.getInt("window_height", 750);
            int x = prefs.getInt("window_x", -1);
            int y = prefs.getInt("window_y", -1);

            if (x != -1 && y != -1) {
                frame.setLocation(x, y);
            }
            frame.setSize(width, height);
        } catch (Exception e) {
            System.err.println("加载应用程序状态失败: " + e.getMessage());
        }
    }
}