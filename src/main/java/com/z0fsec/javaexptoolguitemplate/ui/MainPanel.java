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

        // 初始化三个功能面板，传递日志消费者
        targetInfoPanel = new TargetInfoPanel(this::appendLog);
        commandPanel = new CommandPanel(this::appendLog);
        memoryShellPanel = new MemoryShellPanel(this::appendLog);

        // 添加tab页
        tabbedPane.addTab("目标环境信息", targetInfoPanel);
        tabbedPane.addTab("命令操作", commandPanel);
        tabbedPane.addTab("内存马", memoryShellPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // 创建底部面板
//        add(createBottomPanel(), BorderLayout.SOUTH);
        loadVulnData();
    }

//    /**
//     * 创建底部面板，包含日志组件
//     */
//    private JPanel createBottomPanel() {
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//
//        bottomPanel.add(createLogPanel(), BorderLayout.NORTH);
//
//        return bottomPanel;
//    }

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
        c.gridx = 1; c.weightx = 0;
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


    private void loadVulnData() {
        // 这里可以加载预定义的漏洞列表
        // vulnList = vulnService.loadAllVulns();
        // vulnComboBox.removeAllItems();
        // vulnComboBox.addItem(null); // 添加空选项
        // for (VulnData vuln : vulnList) {
        //     vulnComboBox.addItem(vuln);
        // }
        // appendSuccess("已加载 " + vulnList.size() + " 个漏洞配置");
    }

    private void onVulnSelected(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            VulnData selectedData = (VulnData) e.getItem();
            if (selectedData != null) {
                targetUrlField.setText(selectedData.getTargetUrl());
                appendSuccess("已选择漏洞: " + selectedData.getVulnName() +
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

        appendInfo("🔍 开始检测漏洞: " + targetUrl);

        // 模拟检测过程
        new Thread(() -> {
            try {
                appendDebug("正在扫描目标系统...");
                Thread.sleep(1000);
                appendDebug("分析应用程序框架...");
                Thread.sleep(800);
                appendWarning("发现潜在安全风险");
                Thread.sleep(600);
                appendSuccess("漏洞检测完成");

                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "漏洞检测完成", "完成", JOptionPane.INFORMATION_MESSAGE));
            } catch (InterruptedException ex) {
                appendError("检测过程被中断: " + ex.getMessage());
            }
        }).start();
    }

    private void clearInputFields() {
        targetUrlField.setText("");
        vulnComboBox.setSelectedIndex(0);

        // 清空三个功能面板的数据
        targetInfoPanel.clearData();
        commandPanel.clearData();
        memoryShellPanel.clearData();

        appendInfo("🧹 已清空所有输入字段");
    }

    private void testConnection() {
        String targetUrl = targetUrlField.getText().trim();
        if (targetUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目标地址", "❌ 错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendInfo("正在测试连接到: " + targetUrl);

        // 模拟连接测试
        new Thread(() -> {
            try {
                Thread.sleep(1500); // 模拟网络延迟

                // 随机模拟成功或失败
                if (Math.random() > 0.3) {
                    appendSuccess("连接测试成功");
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "连接测试成功", "✅ 连接测试", JOptionPane.INFORMATION_MESSAGE));
                } else {
                    throw new Exception("连接超时或目标不可达");
                }
            } catch (Exception e) {
                appendError("连接测试异常: " + e.getMessage());
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "连接测试异常: " + e.getMessage(), "❌ 连接测试", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void loadTargetInfo() {
        String targetUrl = targetUrlField.getText().trim();
        if (targetUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目标地址", "❌ 错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendInfo("开始加载目标环境信息...");

        try {
            // 加载目标环境信息
            targetInfoPanel.loadData(targetUrl);
            appendSuccess("目标环境信息加载完成");
        } catch (Exception e) {
            appendError("加载环境信息失败: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载环境信息失败: " + e.getMessage(), "❌ 错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 获取当前目标URL
    public String getTargetUrl() {
        return targetUrlField.getText().trim();
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

    /**
     * 添加一些示例漏洞数据用于演示
     */
    public void loadSampleVulnData() {
        // 示例漏洞数据
        vulnComboBox.removeAllItems();
        vulnComboBox.addItem(null);

        vulnComboBox.addItem(new VulnData("Spring Framework RCE", "CVE-2022-22965", "远程代码执行", "http://localhost:8080"));
        vulnComboBox.addItem(new VulnData("Log4Shell", "CVE-2021-44228", "日志注入RCE", "http://localhost:8080"));
        vulnComboBox.addItem(new VulnData("Fastjson反序列化", "CNVD-2019-22238", "反序列化漏洞", "http://localhost:8080"));
        vulnComboBox.addItem(new VulnData("Shiro RememberMe", "CVE-2016-4437", "反序列化", "http://localhost:8080"));

        appendSuccess("📦 已加载 " + 4 + " 个示例漏洞配置");
    }

    /**
     * 主界面初始化完成后的回调
     */
    public void onPanelReady() {
        appendSuccess("🚀 安全检测工具已就绪");
        appendInfo("💡 提示：请先输入目标地址并选择要检测的漏洞");
        appendInfo("📍 示例：http://192.168.1.100:8080 或 https://example.com");

        // 加载示例数据
        loadSampleVulnData();
    }
}