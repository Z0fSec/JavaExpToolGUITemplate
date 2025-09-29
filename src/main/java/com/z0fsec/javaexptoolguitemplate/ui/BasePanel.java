package com.z0fsec.javaexptoolguitemplate.ui;

import com.z0fsec.javaexptoolguitemplate.util.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.function.Consumer;

public abstract class BasePanel extends JPanel {
    protected Consumer<String> logConsumer;
    protected JTextArea logArea;

    public BasePanel(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer;
        setLayout(new BorderLayout());
    }

    protected JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));

        // 设置日志区域的首选大小，避免过大
        logArea.setPreferredSize(new Dimension(600, 120));
        logArea.setRows(5); // 设置显示5行

        panel.setBorder(BorderFactory.createTitledBorder("操作日志"));
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        return panel;
    }

    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            Date date = new Date();
            logArea.append("["+ TimeUtils.timestampToDate(date.getTime()/1000) + "] "+ message + "\n");
        });
    }
}