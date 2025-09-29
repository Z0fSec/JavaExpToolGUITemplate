package com.z0fsec.javaexptoolguitemplate.ui;

import com.z0fsec.javaexptoolguitemplate.model.VulnData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.function.Consumer;

public class MainPanel extends BasePanel {
    private JTextField targetUrlField;
    private JComboBox<VulnData> vulnComboBox;

    // TabbedPane和三个功能面板
    private JTabbedPane tabbedPane;
    private TargetInfoPanel targetInfoPanel;
    private CommandPanel commandPanel;
    private MemoryShellPanel memoryShellPanel;

    private List<VulnData> vulnList;

    public MainPanel(Consumer<String> logConsumer) {
        super(logConsumer);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 漏洞探测面板
        mainPanel.add(createVulnPanel(), BorderLayout.NORTH);

        // 创建TabbedPane
        tabbedPane = new JTabbedPane();

        // 初始化三个功能面板
        targetInfoPanel = new TargetInfoPanel();
        commandPanel = new CommandPanel();
        memoryShellPanel = new MemoryShellPanel();

        // 添加tab页
        tabbedPane.addTab("目标环境信息", targetInfoPanel);
        tabbedPane.addTab("命令操作", commandPanel);
        tabbedPane.addTab("内存马", memoryShellPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // 创建底部面板
        add(createBottomPanel(), BorderLayout.SOUTH);
        loadVulnData();
    }

    /**
     * 创建底部面板，包含日志组件
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createLogPanel(), BorderLayout.CENTER);
        return bottomPanel;
    }

    private JPanel createVulnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        vulnComboBox = new JComboBox<>();
        vulnComboBox.setRenderer(new VulnComboBoxRenderer());
        vulnComboBox.addItemListener(this::onVulnSelected);

        targetUrlField = new JTextField(30);

        int row = 0;

        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("选择漏洞:"), c);
        c.gridx = 1; c.weightx = 1.0;
        panel.add(vulnComboBox, c);

        c.gridx = 2; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel("目标地址:"), c);
        c.gridx = 3; c.weightx = 1.0;
        panel.add(targetUrlField, c);

        JButton detectBtn = new JButton("漏洞检测");
        detectBtn.addActionListener(e -> detectVulnerability());
        c.gridx = 4; c.weightx = 0;
        panel.add(detectBtn, c);

        JButton clearLogBtn = new JButton("清除日志");
        clearLogBtn.addActionListener(e -> clearLog());
        c.gridx = 5; c.weightx = 0;
        panel.add(clearLogBtn, c);

        panel.setBorder(BorderFactory.createTitledBorder("漏洞探测"));
        return panel;
    }

    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton btnLoadData = new JButton("加载环境信息");
        btnLoadData.addActionListener(e -> loadTargetInfo());

        JButton btnTestConnection = new JButton("测试连接");
        btnTestConnection.addActionListener(e -> testConnection());

        JButton btnClear = new JButton("清空输入");
        btnClear.addActionListener(e -> clearInputFields());

        panel.add(btnLoadData);
        panel.add(btnTestConnection);
        panel.add(btnClear);

        return panel;
    }

    protected JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));

        // 设置日志区域的首选大小
        logArea.setPreferredSize(new Dimension(600, 120));
        logArea.setRows(5);

        panel.setBorder(BorderFactory.createTitledBorder("操作日志"));
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        return panel;
    }

    private void loadVulnData() {
        // 这里可以加载预定义的漏洞列表
        // vulnList = vulnService.loadAllVulns();
        // vulnComboBox.removeAllItems();
        // vulnComboBox.addItem(null); // 添加空选项
        // for (VulnData vuln : vulnList) {
        //     vulnComboBox.addItem(vuln);
        // }
        // appendLog("已加载 " + vulnList.size() + " 个漏洞配置");
    }

    private void onVulnSelected(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            VulnData selectedData = (VulnData) e.getItem();
            if (selectedData != null) {
                targetUrlField.setText(selectedData.getTargetUrl());
                appendLog("已选择漏洞: " + selectedData.getVulnName() +
                        (selectedData.getRemark() != null ? " (" + selectedData.getRemark() + ")" : ""));
            }
        }
    }

    private void detectVulnerability() {
        String targetUrl = targetUrlField.getText().trim();
        if (targetUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目标地址", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendLog("开始检测漏洞: " + targetUrl);
        // 这里实现漏洞检测逻辑
    }

    private void clearLog() {
        logArea.setText("");
    }

    private void clearInputFields() {
        targetUrlField.setText("");
        vulnComboBox.setSelectedIndex(0);

        // 清空三个功能面板的数据
        targetInfoPanel.clearData();
        commandPanel.clearData();
        memoryShellPanel.clearData();
    }

    private void testConnection() {
        String targetUrl = targetUrlField.getText().trim();
        if (targetUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目标地址", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 这里实现连接测试逻辑
            appendLog("连接测试成功 ✅");
            JOptionPane.showMessageDialog(this, "连接测试成功", "连接测试", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            appendLog("连接测试异常: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "连接测试异常: " + e.getMessage(), "连接测试", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTargetInfo() {
        String targetUrl = targetUrlField.getText().trim();
        if (targetUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目标地址", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendLog("开始加载目标环境信息...");

        try {
            // 加载目标环境信息
            targetInfoPanel.loadData(targetUrl);
            appendLog("目标环境信息加载完成 ✅");
        } catch (Exception e) {
            appendLog("加载环境信息失败: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载环境信息失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 漏洞下拉框渲染器
    private static class VulnComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof VulnData) {
                VulnData data = (VulnData) value;
                String text = data.getVulnName();
                if (data.getRemark() != null && !data.getRemark().isEmpty()) {
                    text += " - " + data.getRemark();
                }
                if (data.getVulnType() != null && !data.getVulnType().isEmpty()) {
                    text += " [" + data.getVulnType() + "]";
                }
                setText(text);
            } else if (value == null) {
                setText("-- 选择漏洞 --");
            }

            return this;
        }
    }

    // 目标环境信息面板
    private class TargetInfoPanel extends JPanel {
        private JTable infoTable;
        private DefaultTableModel tableModel;

        public TargetInfoPanel() {
            initUI();
        }

        private void initUI() {
            setLayout(new BorderLayout());

            // 创建表格模型
            String[] columnNames = {"信息类型", "详细信息"};
            tableModel = new DefaultTableModel(columnNames, 0);
            infoTable = new JTable(tableModel);

            // 设置表格属性
            infoTable.setRowHeight(25);
            infoTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            infoTable.getColumnModel().getColumn(1).setPreferredWidth(400);

            add(new JScrollPane(infoTable), BorderLayout.CENTER);
        }

        public void loadData(String targetUrl) {
            // 清空现有数据
            tableModel.setRowCount(0);

            // 模拟加载目标环境信息
            // 这里应该实现实际的目标信息获取逻辑
            tableModel.addRow(new Object[]{"目标URL", targetUrl});
            tableModel.addRow(new Object[]{"服务器类型", "Tomcat 9.0"});
            tableModel.addRow(new Object[]{"Java版本", "1.8.0_291"});
            tableModel.addRow(new Object[]{"操作系统", "Linux"});
            tableModel.addRow(new Object[]{"Web应用", "Spring Boot Application"});
            tableModel.addRow(new Object[]{"框架信息", "Spring Framework 5.3.8"});
        }

        public void clearData() {
            tableModel.setRowCount(0);
        }
    }

    // 命令操作面板
    private class CommandPanel extends JPanel {
        private JTextField commandField;
        private JTextArea resultArea;
        private JButton executeBtn, clearBtn;

        public CommandPanel() {
            initUI();
        }

        private void initUI() {
            setLayout(new BorderLayout());

            // 命令输入面板
            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("命令执行"));

            commandField = new JTextField();
            executeBtn = new JButton("执行命令");
            clearBtn = new JButton("清空结果");

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(executeBtn);
            buttonPanel.add(clearBtn);

            inputPanel.add(commandField, BorderLayout.CENTER);
            inputPanel.add(buttonPanel, BorderLayout.EAST);

            // 结果展示区域
            resultArea = new JTextArea();
            resultArea.setEditable(false);
            resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            JScrollPane resultScrollPane = new JScrollPane(resultArea);
            resultScrollPane.setBorder(BorderFactory.createTitledBorder("执行结果"));

            add(inputPanel, BorderLayout.NORTH);
            add(resultScrollPane, BorderLayout.CENTER);

            // 添加事件监听
            executeBtn.addActionListener(this::onExecuteCommand);
            clearBtn.addActionListener(e -> resultArea.setText(""));
        }

        private void onExecuteCommand(ActionEvent e) {
            String command = commandField.getText().trim();
            if (command.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要执行的命令", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            appendLog("执行命令: " + command);

            // 这里实现命令执行逻辑
            try {
                // 模拟命令执行结果
                String result = "命令执行结果:\n> " + command + "\n";
                result += "root@target:/# 命令执行成功\n";
                result += "当前目录文件列表:\n";
                result += "drwxr-xr-x  2 root root 4096 Jan 10 10:00 bin\n";
                result += "drwxr-xr-x  3 root root 4096 Jan 10 10:00 etc\n";
                result += "drwxr-xr-x  2 root root 4096 Jan 10 10:00 webapps\n";

                resultArea.setText(result);
                appendLog("命令执行完成");
            } catch (Exception ex) {
                resultArea.setText("命令执行失败: " + ex.getMessage());
                appendLog("命令执行失败: " + ex.getMessage());
            }
        }

        public void loadData() {
            // 可以加载命令执行历史等
        }

        public void clearData() {
            commandField.setText("");
            resultArea.setText("");
        }
    }

    // 内存马操作面板
    private class MemoryShellPanel extends JPanel {
        private JComboBox<String> injectionTypeCombo;
        private JTextField injectorClassField;
        private JTextField shellPathField;
        private JTextField passwordField;
        private JTextArea customCodeArea;
        private JButton injectBtn, uninstallBtn, listBtn;

        public MemoryShellPanel() {
            initUI();
        }

        private void initUI() {
            setLayout(new BorderLayout());

            // 配置面板
            JPanel configPanel = new JPanel(new GridBagLayout());
            configPanel.setBorder(BorderFactory.createTitledBorder("内存马配置"));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            // 注入类型
            c.gridx = 0; c.gridy = 0; c.weightx = 0;
            configPanel.add(new JLabel("注入类型:"), c);
            injectionTypeCombo = new JComboBox<>(new String[]{
                    "Filter内存马", "Servlet内存马", "Interceptor内存马",
                    "Controller内存马", "WebSocket内存马", "自定义"
            });
            c.gridx = 1; c.weightx = 1.0;
            configPanel.add(injectionTypeCombo, c);

            // 注入器类名
            c.gridx = 0; c.gridy = 1; c.weightx = 0;
            configPanel.add(new JLabel("注入器类名:"), c);
            injectorClassField = new JTextField();
            injectorClassField.setText("com.example.MemoryShellInjector");
            c.gridx = 1; c.weightx = 1.0;
            configPanel.add(injectorClassField, c);

            // Shell路径
            c.gridx = 0; c.gridy = 2; c.weightx = 0;
            configPanel.add(new JLabel("Shell路径:"), c);
            shellPathField = new JTextField();
            shellPathField.setText("/shell");
            c.gridx = 1; c.weightx = 1.0;
            configPanel.add(shellPathField, c);

            // 连接密码
            c.gridx = 0; c.gridy = 3; c.weightx = 0;
            configPanel.add(new JLabel("连接密码:"), c);
            passwordField = new JTextField();
            passwordField.setText("pass123");
            c.gridx = 1; c.weightx = 1.0;
            configPanel.add(passwordField, c);

            // 自定义代码区域
            c.gridx = 0; c.gridy = 4; c.weightx = 0;
            configPanel.add(new JLabel("自定义代码:"), c);
            customCodeArea = new JTextArea(8, 30);
            customCodeArea.setText("// 在这里输入自定义的内存马代码\n// 例如：\n// @WebServlet(name = \"shell\", urlPatterns = \"/shell\")\n// public class ShellServlet extends HttpServlet { ... }");
            customCodeArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            JScrollPane codeScrollPane = new JScrollPane(customCodeArea);
            c.gridx = 0; c.gridy = 5; c.gridwidth = 2; c.weightx = 1.0; c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            configPanel.add(codeScrollPane, c);

            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            injectBtn = new JButton("注入内存马");
            uninstallBtn = new JButton("卸载内存马");
            listBtn = new JButton("列出内存马");

            injectBtn.addActionListener(this::onInjectShell);
            uninstallBtn.addActionListener(this::onUninstallShell);
            listBtn.addActionListener(this::onListShells);

            buttonPanel.add(injectBtn);
            buttonPanel.add(uninstallBtn);
            buttonPanel.add(listBtn);

            add(configPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void onInjectShell(ActionEvent e) {
            String injectionType = (String) injectionTypeCombo.getSelectedItem();
            String injectorClass = injectorClassField.getText().trim();
            String shellPath = shellPathField.getText().trim();
            String password = passwordField.getText().trim();

            if (injectorClass.isEmpty() || shellPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整的配置信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            appendLog("开始注入内存马 - 类型: " + injectionType + ", 路径: " + shellPath);

            // 这里实现内存马注入逻辑
            try {
                // 模拟注入过程
                Thread.sleep(1000);
                appendLog("内存马注入成功 ✅");
                appendLog("访问地址: " + targetUrlField.getText() + shellPath);
                appendLog("连接密码: " + password);
                JOptionPane.showMessageDialog(this, "内存马注入成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                appendLog("内存马注入失败: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "注入失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void onUninstallShell(ActionEvent e) {
            String shellPath = shellPathField.getText().trim();
            if (shellPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要卸载的内存马路径", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要卸载内存马 '" + shellPath + "' 吗？", "确认卸载",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                appendLog("开始卸载内存马: " + shellPath);
                // 这里实现内存马卸载逻辑
                appendLog("内存马卸载成功");
            }
        }

        private void onListShells(ActionEvent e) {
            appendLog("列出当前内存马...");
            // 这里实现列出已注入内存马的逻辑
            appendLog("1. Filter内存马 - 路径: /shell1");
            appendLog("2. Servlet内存马 - 路径: /shell2");
        }

        public void loadData() {
            // 可以加载内存马配置历史等
        }

        public void clearData() {
            injectionTypeCombo.setSelectedIndex(0);
            injectorClassField.setText("com.example.MemoryShellInjector");
            shellPathField.setText("/shell");
            passwordField.setText("pass123");
            customCodeArea.setText("// 在这里输入自定义的内存马代码");
        }
    }
}