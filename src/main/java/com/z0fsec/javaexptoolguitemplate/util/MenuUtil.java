package com.z0fsec.javaexptoolguitemplate.util;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

public class MenuUtil {
    public static JMenuBar createMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createSettingMenu(frame));
        menuBar.add(createProxyMenu(frame));
        menuBar.add(createAboutMenu(frame));
        return menuBar;
    }

    private static JMenu createSettingMenu(JFrame frame) {
        JMenu settingsMenu = new JMenu("设置");
        settingsMenu.add(MenuUtil.createThemeMenu(frame));
        return settingsMenu;
    }

    private static JMenu createThemeMenu(JFrame frame) {
        JMenu themeMenu = new JMenu("主题");

        String[] themeNames = {"FlatLight", "FlatDark", "FlatDarcula", "FlatIntelliJ", "FlatMacDark", "FlatMacLight"};

        for (String themeName : themeNames) {
            JMenuItem themeItem = createThemeMenuItem(frame, themeName);
            themeMenu.add(themeItem);
        }
        return themeMenu;
    }

    private static JMenuItem createThemeMenuItem(JFrame frame, String themeName) {
        JMenuItem themeItem = new JMenuItem(themeName);
        themeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(themeName, frame);
            }
        });
        return themeItem;
    }

    private static void setLookAndFeel(String themeName, JFrame frame) {
        try {
            switch (themeName) {
                case "FlatLight":
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                case "FlatDark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "FlatIntelliJ":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "FlatDarcula":
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                case "FlatMacLight":
                    UIManager.setLookAndFeel(new FlatMacLightLaf());
                    break;
                case "FlatMacDark":
                    UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    break;
                default:
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                    break;
            }
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static JMenu createAboutMenu(JFrame frame) {
        try {
            JMenu aboutMenu = new JMenu("关于");

            // 作者信息菜单项
            JMenuItem authorItem = new JMenuItem("作者信息");
            authorItem.setToolTipText("查看作者信息");

            // 版本信息菜单项
            JMenuItem versionItem = new JMenuItem("版本信息");
            versionItem.setToolTipText("查看版本详情和更新日志");

            // 点Star支持一下
            JMenuItem startProjectItem = new JMenuItem("点Star支持一下");
            startProjectItem.setToolTipText("");

            // 作者信息弹窗
            authorItem.addActionListener(e -> {
                try {
                    // 创建自定义对话框内容
                    JPanel authorPanel = new JPanel(new BorderLayout(10, 10));
                    authorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    // 作者头像和基本信息面板
                    JPanel infoPanel = new JPanel(new BorderLayout(10, 10));

                    // 头像标签（使用默认图标或加载网络头像）
                    JLabel avatarLabel = new JLabel();
                    try {
                        // 这里可以加载网络头像或使用本地资源
                        ImageIcon originalIcon = new ImageIcon(
                                "/images/avatar.png"
                        );
                        Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        avatarLabel.setIcon(new ImageIcon(scaledImage));
                    } catch (Exception ex) {
                        // 使用默认头像
                        avatarLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                    }
                    avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    // 作者信息文本
                    JTextArea authorInfo = new JTextArea();
                    authorInfo.setText("作者: EatMans\n\n" +
                            "GitHub: https://github.com/eatmans\n" +
                            "\n" +
                            "专注于安全工具开发，热爱开源分享。");
                    authorInfo.setEditable(false);
                    authorInfo.setBackground(authorPanel.getBackground());
                    authorInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    infoPanel.add(avatarLabel, BorderLayout.WEST);
                    infoPanel.add(authorInfo, BorderLayout.CENTER);

                    // 链接面板
                    JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    JButton githubButton = new JButton("访问GitHub");
                    githubButton.addActionListener(ev -> {
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/eatmans"));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "无法打开链接: " + ex.getMessage());
                        }
                    });
                    linkPanel.add(githubButton);

                    authorPanel.add(infoPanel, BorderLayout.CENTER);
                    authorPanel.add(linkPanel, BorderLayout.SOUTH);

                    JOptionPane.showMessageDialog(frame, authorPanel, "作者信息",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "加载作者信息失败: " + ex.getMessage());
                }
            });

            // 版本信息弹窗
            versionItem.addActionListener(e -> {
                try {
                    // 创建版本信息内容
                    JPanel versionPanel = new JPanel(new BorderLayout(10, 10));
                    versionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    // 版本基本信息
                    JPanel basicInfoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                    basicInfoPanel.add(new JLabel("当前版本: " + Z0fSecConstants.VERSION));
                    basicInfoPanel.add(new JLabel("发布日期: 2025-09-29"));

                    // 更新日志区域
                    JTextArea changelogArea = new JTextArea(8, 30);
                    changelogArea.setText("版本更新日志:\n\n" +
                            "v0.1 (2025-09-29)\n" +
                            "• 添加主题功能\n"
                    );
                    changelogArea.setEditable(false);
                    changelogArea.setCaretPosition(0);
                    JScrollPane scrollPane = new JScrollPane(changelogArea);
                    scrollPane.setBorder(BorderFactory.createTitledBorder("更新日志"));

                    versionPanel.add(basicInfoPanel, BorderLayout.NORTH);
                    versionPanel.add(scrollPane, BorderLayout.CENTER);

                    JOptionPane.showMessageDialog(frame, versionPanel, "版本信息",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "加载版本信息失败: " + ex.getMessage());
                }
            });

            startProjectItem.addActionListener(e -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI oURL = new URI("https://github.com/z0fsec/JavaExpToolGUITemplate");
                    desktop.browse(oURL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // 添加菜单项
            aboutMenu.add(authorItem);
            aboutMenu.add(versionItem);
            aboutMenu.add(startProjectItem);

            return aboutMenu;

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "创建关于菜单失败: " + ex.getMessage());
        }
        return null;
    }

    //创建代理菜单
    private static JMenu createProxyMenu(JFrame frame) {
        JMenu proxysMenu = new JMenu("代理");

        // 全局代理设置菜单项
        JMenuItem globalProxyItem = new JMenuItem("全局代理设置");
        globalProxyItem.setToolTipText("设置全局网络代理");

        // 导入配置菜单项
        JMenuItem importConfigItem = new JMenuItem("导入配置");
        importConfigItem.setToolTipText("从JSON文件导入配置");

        // 导出配置菜单项
        JMenuItem exportConfigItem = new JMenuItem("导出配置");
        exportConfigItem.setToolTipText("将配置导出到JSON文件");

        // 全局代理设置弹窗
        globalProxyItem.addActionListener(e -> showGlobalProxyDialog(frame));


        // 导出配置
        exportConfigItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择导出位置");
            fileChooser.setSelectedFile(new File("service_config_export.json"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON文件", "json"));

            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // 确保文件扩展名正确
                if (!selectedFile.getName().toLowerCase().endsWith(".json")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                }
            }
        });

        proxysMenu.add(globalProxyItem);
        proxysMenu.add(new JSeparator()); // 分隔线
        proxysMenu.add(importConfigItem);
        proxysMenu.add(exportConfigItem);

        return proxysMenu;
    }


    // 辅助方法：设置字段面板中所有组件的启用状态
    private static void setFieldsEnabled(Container container, boolean enabled) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField || comp instanceof JComboBox ||
                    comp instanceof JPasswordField) {
                comp.setEnabled(enabled);
            } else if (comp instanceof Container) {
                setFieldsEnabled((Container) comp, enabled);
            }
        }
    }

    // 显示全局代理设置对话框
    private static void showGlobalProxyDialog(JFrame frame) {
        JPanel proxyPanel = new JPanel(new GridBagLayout());
        proxyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 这里可以添加全局代理设置，与之前类似但应用到所有服务
        JCheckBox enableCheckbox = new JCheckBox("启用全局代理");
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        proxyPanel.add(enableCheckbox, gbc);

        // 代理主机
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        proxyPanel.add(new JLabel("代理主机:"), gbc);

        gbc.gridx = 1;
        JTextField hostField = new JTextField(15);
        proxyPanel.add(hostField, gbc);

        // 代理端口
        gbc.gridy = 2;
        gbc.gridx = 0;
        proxyPanel.add(new JLabel("代理端口:"), gbc);

        gbc.gridx = 1;
        JTextField portField = new JTextField(10);
        proxyPanel.add(portField, gbc);

        // 应用到所有服务复选框
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JCheckBox applyToAllCheckbox = new JCheckBox("应用到所有服务配置");
        proxyPanel.add(applyToAllCheckbox, gbc);

        int result = JOptionPane.showConfirmDialog(frame, proxyPanel, "全局代理设置",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (applyToAllCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(frame, "全局代理设置已应用到所有服务！");
            }

            // 设置系统代理
            if (enableCheckbox.isSelected()) {
                System.setProperty("http.proxyHost", hostField.getText());
                System.setProperty("http.proxyPort", portField.getText());
                System.setProperty("https.proxyHost", hostField.getText());
                System.setProperty("https.proxyPort", portField.getText());
                JOptionPane.showMessageDialog(frame, "系统代理设置已应用！");
            }
        }
    }
}
