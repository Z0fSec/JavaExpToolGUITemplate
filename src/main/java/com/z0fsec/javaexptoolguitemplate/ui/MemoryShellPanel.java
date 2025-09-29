package com.z0fsec.javaexptoolguitemplate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class MemoryShellPanel extends BasePanel {
    private JComboBox<String> injectionTypeCombo;
    private JTextField injectorClassField;
    private JTextField shellPathField;
    private JTextField passwordField;
    private JTextArea customCodeArea;
    private JButton injectBtn, uninstallBtn, listBtn, clearConfigBtn;

    public MemoryShellPanel(Consumer<String> logConsumer) {
        super(logConsumer);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建主内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 配置面板
        JPanel configPanel = createConfigPanel();

        // 按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 添加组件到内容面板
        contentPanel.add(configPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建分割面板，上半部分显示配置，下半部分显示日志
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contentPanel, createLogPanelWithScroll());
        splitPane.setResizeWeight(0.6); // 60%空间给配置，40%给日志
        splitPane.setOneTouchExpandable(true); // 添加快速展开/折叠按钮

        add(splitPane, BorderLayout.CENTER);
        
        // 初始化完成日志
        appendInfo("内存马管理面板已就绪");
    }

    /**
     * 创建配置面板
     */
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("内存马配置"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        // 注入类型
        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        panel.add(new JLabel("注入类型:"), c);
        injectionTypeCombo = new JComboBox<>(new String[]{
                "Filter内存马", "Servlet内存马", "Interceptor内存马",
                "Controller内存马", "WebSocket内存马", "自定义"
        });
        injectionTypeCombo.addActionListener(e -> onInjectionTypeChanged());
        c.gridx = 1; c.weightx = 1.0;
        panel.add(injectionTypeCombo, c);

        // 注入器类名
        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        panel.add(new JLabel("注入器类名:"), c);
        injectorClassField = new JTextField();
        injectorClassField.setText("com.example.MemoryShellInjector");
        c.gridx = 1; c.weightx = 1.0;
        panel.add(injectorClassField, c);

        // Shell路径
        c.gridx = 0; c.gridy = 2; c.weightx = 0;
        panel.add(new JLabel("Shell路径:"), c);
        shellPathField = new JTextField();
        shellPathField.setText("/shell");
        c.gridx = 1; c.weightx = 1.0;
        panel.add(shellPathField, c);

        // 连接密码
        c.gridx = 0; c.gridy = 3; c.weightx = 0;
        panel.add(new JLabel("连接密码:"), c);
        passwordField = new JTextField();
        passwordField.setText("pass123");
        c.gridx = 1; c.weightx = 1.0;
        panel.add(passwordField, c);

        // 自定义代码区域
        c.gridx = 0; c.gridy = 4; c.weightx = 0;
        panel.add(new JLabel("自定义代码:"), c);
        customCodeArea = new JTextArea(8, 30);
        customCodeArea.setText(getDefaultCustomCode());
        customCodeArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        customCodeArea.setBackground(new Color(250, 250, 250));
        JScrollPane codeScrollPane = new JScrollPane(customCodeArea);
        codeScrollPane.setPreferredSize(new Dimension(500, 150));
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2; c.weightx = 1.0; c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(codeScrollPane, c);

        return panel;
    }

    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        injectBtn = new JButton("注入内存马");
        uninstallBtn = new JButton("卸载内存马");
        listBtn = new JButton("列出内存马");
        clearConfigBtn = new JButton("清空配置");

        injectBtn.addActionListener(this::onInjectShell);
        uninstallBtn.addActionListener(this::onUninstallShell);
        listBtn.addActionListener(this::onListShells);
        clearConfigBtn.addActionListener(e -> clearData());

        panel.add(injectBtn);
        panel.add(uninstallBtn);
        panel.add(listBtn);
        panel.add(clearConfigBtn);

        return panel;
    }

    /**
     * 获取默认的自定义代码
     */
    private String getDefaultCustomCode() {
        return "// 在这里输入自定义的内存马代码\n" +
                "// 例如：\n" +
                "// @WebServlet(name = \"shell\", urlPatterns = \"/shell\")\n" +
                "// public class ShellServlet extends HttpServlet {\n" +
                "//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) {\n" +
                "//         // 内存马逻辑\n" +
                "//     }\n" +
                "// }";
    }

    /**
     * 注入类型改变事件
     */
    private void onInjectionTypeChanged() {
        String selectedType = (String) injectionTypeCombo.getSelectedItem();
        appendDebug("注入类型已更改为: " + selectedType);
        
        // 根据选择的类型更新默认配置
        updateDefaultConfig(selectedType);
    }

    /**
     * 根据注入类型更新默认配置
     */
    private void updateDefaultConfig(String injectionType) {
        switch (injectionType) {
            case "Filter内存马":
                shellPathField.setText("/filterShell");
                injectorClassField.setText("com.example.FilterShellInjector");
                break;
            case "Servlet内存马":
                shellPathField.setText("/servletShell");
                injectorClassField.setText("com.example.ServletShellInjector");
                break;
            case "Interceptor内存马":
                shellPathField.setText("/interceptorShell");
                injectorClassField.setText("com.example.InterceptorShellInjector");
                break;
            case "Controller内存马":
                shellPathField.setText("/controllerShell");
                injectorClassField.setText("com.example.ControllerShellInjector");
                break;
            case "WebSocket内存马":
                shellPathField.setText("/websocketShell");
                injectorClassField.setText("com.example.WebSocketShellInjector");
                break;
            case "自定义":
                shellPathField.setText("/customShell");
                injectorClassField.setText("com.example.CustomShellInjector");
                break;
        }
    }

    private void onInjectShell(ActionEvent e) {
        String injectionType = (String) injectionTypeCombo.getSelectedItem();
        String injectorClass = injectorClassField.getText().trim();
        String shellPath = shellPathField.getText().trim();
        String password = passwordField.getText().trim();
        String customCode = customCodeArea.getText().trim();

        if (injectorClass.isEmpty() || shellPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整的配置信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendInfo("开始注入内存马...");
        appendDebug("注入类型: " + injectionType);
        appendDebug("注入器类: " + injectorClass);
        appendDebug("Shell路径: " + shellPath);
        appendDebug("连接密码: " + password);

        // 模拟注入过程
        new Thread(() -> {
            try {
                appendInfo("正在编译自定义代码...");
                Thread.sleep(800);
                
                appendInfo("正在创建类加载器...");
                Thread.sleep(600);
                
                appendInfo("正在注入内存马...");
                Thread.sleep(1000);
                
                SwingUtilities.invokeLater(() -> {
                    appendSuccess("🎯 内存马注入成功！");
                    appendInfo("📍 访问地址: " + shellPath);
                    appendInfo("🔑 连接密码: " + password);
                    appendInfo("💡 注入器类: " + injectorClass);
                    appendInfo("⏰ 注入时间: " + java.time.LocalTime.now());
                    
                    JOptionPane.showMessageDialog(this, 
                        "内存马注入成功！\n" +
                        "访问地址: " + shellPath + "\n" +
                        "连接密码: " + password, 
                        "✅ 注入成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    appendError("内存马注入过程被中断: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, 
                        "注入过程被中断: " + ex.getMessage(), 
                        "❌ 注入失败", 
                        JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    appendError("内存马注入失败: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, 
                        "注入失败: " + ex.getMessage(), 
                        "❌ 注入失败", 
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void onUninstallShell(ActionEvent e) {
        String shellPath = shellPathField.getText().trim();
        if (shellPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要卸载的内存马路径", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要卸载内存马 '" + shellPath + "' 吗？\n此操作不可逆！", 
                "⚠️ 确认卸载",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            appendWarning("开始卸载内存马: " + shellPath);
            
            // 模拟卸载过程
            new Thread(() -> {
                try {
                    Thread.sleep(800);
                    SwingUtilities.invokeLater(() -> {
                        appendSuccess("内存马卸载成功: " + shellPath);
                        JOptionPane.showMessageDialog(this, 
                            "内存马卸载成功！", 
                            "✅ 卸载成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (InterruptedException ex) {
                    SwingUtilities.invokeLater(() -> {
                        appendError("内存马卸载过程被中断: " + ex.getMessage());
                    });
                }
            }).start();
        } else {
            appendInfo("用户取消了卸载操作");
        }
    }

    private void onListShells(ActionEvent e) {
        appendInfo("正在扫描已注入的内存马...");
        
        // 模拟扫描过程
        new Thread(() -> {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    appendSuccess("📋 当前已注入的内存马列表:");
                    appendInfo("1. Filter内存马 - 路径: /filterShell - 状态: 活跃");
                    appendInfo("2. Servlet内存马 - 路径: /servletShell - 状态: 活跃");
                    appendInfo("3. Interceptor内存马 - 路径: /api/shell - 状态: 休眠");
                    appendInfo("4. Controller内存马 - 路径: /controller/cmd - 状态: 活跃");
                    appendInfo("共发现 4 个内存马");
                });
            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    appendError("扫描过程被中断: " + ex.getMessage());
                });
            }
        }).start();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        // 可以加载内存马配置历史等
        appendDebug("正在加载内存马配置数据...");
        
        // 模拟加载配置
        injectionTypeCombo.setSelectedIndex(0);
        injectorClassField.setText("com.example.FilterShellInjector");
        shellPathField.setText("/filterShell");
        passwordField.setText("pass123");
        customCodeArea.setText(getDefaultCustomCode());
        
        appendSuccess("内存马配置数据加载完成");
    }

    /**
     * 清空数据
     */
    public void clearData() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要清空所有配置吗？", 
                "确认清空",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            injectionTypeCombo.setSelectedIndex(0);
            injectorClassField.setText("");
            shellPathField.setText("");
            passwordField.setText("");
            customCodeArea.setText(getDefaultCustomCode());
            
            appendInfo("🧹 内存马配置已清空");
        }
    }

    /**
     * 导出配置
     */
    public void exportConfig() {
        StringBuilder config = new StringBuilder();
        config.append("内存马配置导出\n");
        config.append("导出时间: ").append(java.time.LocalDateTime.now()).append("\n\n");
        config.append("注入类型: ").append(injectionTypeCombo.getSelectedItem()).append("\n");
        config.append("注入器类名: ").append(injectorClassField.getText()).append("\n");
        config.append("Shell路径: ").append(shellPathField.getText()).append("\n");
        config.append("连接密码: ").append(passwordField.getText()).append("\n\n");
        config.append("自定义代码:\n").append(customCodeArea.getText());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出内存马配置");
        fileChooser.setSelectedFile(new java.io.File("memory_shell_config_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.nio.file.Files.write(file.toPath(), config.toString().getBytes());
                appendSuccess("内存马配置已导出到: " + file.getAbsolutePath());
            } catch (Exception e) {
                appendError("导出配置失败: " + e.getMessage());
            }
        }
    }
}