package com.z0fsec.javaexptoolguitemplate.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class TargetInfoPanel extends BasePanel {
    private JTable infoTable;
    private DefaultTableModel tableModel;

    public TargetInfoPanel(Consumer<String> logConsumer) {
        super(logConsumer);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建主内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 创建表格模型
        String[] columnNames = {"信息类型", "详细信息"};
        tableModel = new DefaultTableModel(columnNames, 0);
        infoTable = new JTable(tableModel);

        // 设置表格属性
        infoTable.setRowHeight(25);
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(400);

        // 创建表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("目标环境信息"));
        tablePanel.add(new JScrollPane(infoTable), BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 添加组件到内容面板
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建分割面板，上半部分显示目标信息，下半部分显示日志
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contentPanel, createLogPanelWithScroll());
        splitPane.setResizeWeight(0.6); // 60%空间给目标信息，40%给日志
        splitPane.setOneTouchExpandable(true); // 添加快速展开/折叠按钮

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton btnLoadData = new JButton("加载环境信息");
        btnLoadData.addActionListener(e -> loadData());

        JButton btnExport = new JButton("导出信息");
        btnExport.addActionListener(e -> exportInfo());

        JButton btnClear = new JButton("清空信息");
        btnClear.addActionListener(e -> clearData());

        panel.add(btnLoadData);
        panel.add(btnExport);
        panel.add(btnClear);

        return panel;
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

        appendSuccess("目标环境信息已加载");
    }

    public void clearData() {
        tableModel.setRowCount(0);
        appendInfo("目标环境信息已清空");
    }

    /**
     * 加载环境信息（供按钮调用）
     */
    private void loadData() {
        // 这里可以添加从外部获取目标URL的逻辑
        // 暂时使用模拟数据
        loadData("http://localhost:8080");
    }

    /**
     * 导出环境信息
     */
    private void exportInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("目标环境信息报告\n");
        sb.append("生成时间: ").append(java.time.LocalDateTime.now()).append("\n\n");
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            sb.append(tableModel.getValueAt(i, 0)).append(": ")
              .append(tableModel.getValueAt(i, 1)).append("\n");
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出环境信息");
        fileChooser.setSelectedFile(new java.io.File("target_info_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.nio.file.Files.write(file.toPath(), sb.toString().getBytes());
                appendSuccess("环境信息已导出到: " + file.getAbsolutePath());
            } catch (Exception e) {
                appendError("导出环境信息失败: " + e.getMessage());
            }
        }
    }
}