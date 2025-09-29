package com.z0fsec.javaexptoolguitemplate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class CommandPanel extends BasePanel {
    private JTextField commandField;
    private JTextArea resultArea;
    private JButton executeBtn, clearResultBtn;

    public CommandPanel(Consumer<String> logConsumer) {
        super(logConsumer);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建主内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 命令输入面板
        JPanel inputPanel = createInputPanel();

        // 结果展示区域
        JPanel resultPanel = createResultPanel();

        // 添加组件到内容面板
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(resultPanel, BorderLayout.CENTER);

//        // 创建分割面板，上半部分显示命令执行，下半部分显示日志
//        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contentPanel, createLogPanelWithScroll());
//        splitPane.setResizeWeight(0.5); // 50%空间给命令执行，50%给日志
//        splitPane.setOneTouchExpandable(true); // 添加快速展开/折叠按钮

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * 创建命令输入面板
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("命令执行"));

        commandField = new JTextField();
        commandField.addActionListener(this::onExecuteCommand); // 支持回车执行

        executeBtn = new JButton("执行命令");
        executeBtn.addActionListener(this::onExecuteCommand);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(executeBtn);

        panel.add(commandField, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * 创建结果展示面板
     */
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("执行结果"));
        resultScrollPane.setPreferredSize(new Dimension(600, 200));

        clearResultBtn = new JButton("清空结果");
        clearResultBtn.addActionListener(e -> resultArea.setText(""));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearResultBtn);

        panel.add(resultScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void onExecuteCommand(ActionEvent e) {
        String command = commandField.getText().trim();
        if (command.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要执行的命令", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 记录到面板日志
        appendInfo("执行命令: " + command);

        // 在结果区域显示执行过程
        resultArea.setText("执行命令: " + command + "\n\n");

        // 模拟命令执行过程
        new Thread(() -> {
            try {
                // 模拟执行延迟
                Thread.sleep(1000);
                
                SwingUtilities.invokeLater(() -> {
                    String result = resultArea.getText();
                    result += "命令执行结果:\n";
                    result += "> " + command + "\n";
                    result += "root@target:/# 命令执行成功\n\n";
                    result += "当前目录文件列表:\n";
                    result += "drwxr-xr-x  2 root root 4096 Jan 10 10:00 bin\n";
                    result += "drwxr-xr-x  3 root root 4096 Jan 10 10:00 etc\n";
                    result += "drwxr-xr-x  2 root root 4096 Jan 10 10:00 webapps\n";
                    result += "-rw-r--r--  1 root root 1024 Jan 10 10:00 readme.txt\n";
                    result += "drwxr-xr-x  4 root root 4096 Jan 10 10:00 logs\n\n";
                    result += "执行完成时间: " + java.time.LocalTime.now() + "\n";
                    
                    resultArea.setText(result);
                    appendSuccess("命令执行完成: " + command);
                });
            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    resultArea.setText("命令执行被中断: " + ex.getMessage());
                    appendError("命令执行被中断: " + ex.getMessage());
                });
            }
        }).start();
    }

    public void loadData() {
        // 可以加载命令执行历史等
        appendInfo("命令面板已就绪");
    }

    public void clearData() {
        commandField.setText("");
        resultArea.setText("");
        appendInfo("命令面板数据已清空");
    }
}