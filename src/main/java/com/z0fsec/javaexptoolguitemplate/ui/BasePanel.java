package com.z0fsec.javaexptoolguitemplate.ui;

import com.z0fsec.javaexptoolguitemplate.util.TimeUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BasePanel extends JPanel {
    protected Consumer<String> logConsumer;
    protected JTextPane logArea;
    protected StyledDocument logDocument;
    protected StyleContext logStyleContext;
    protected Map<String, Style> logStyles;

    // 日志级别颜色定义
    private static final Color COLOR_INFO = new Color(0, 100, 0); // 深绿色
    private static final Color COLOR_SUCCESS = new Color(0, 128, 0); // 绿色
    private static final Color COLOR_WARNING = new Color(255, 140, 0); // 橙色
    private static final Color COLOR_ERROR = new Color(220, 20, 60); // 红色
    private static final Color COLOR_DEBUG = new Color(70, 130, 180); // 钢蓝色
    private static final Color COLOR_DEFAULT = new Color(0, 0, 0); // 黑色

    // 日志级别样式名称
    private static final String STYLE_INFO = "info";
    private static final String STYLE_SUCCESS = "success";
    private static final String STYLE_WARNING = "warning";
    private static final String STYLE_ERROR = "error";
    private static final String STYLE_DEBUG = "debug";
    private static final String STYLE_DEFAULT = "default";

    // ASCII 表情符号
    private static final String ASCII_INFO = "[i]";
    private static final String ASCII_SUCCESS = "[+]";
    private static final String ASCII_WARNING = "[!]";
    private static final String ASCII_ERROR = "[-]";
    private static final String ASCII_DEBUG = "[*]";

    public BasePanel(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer;
        setLayout(new BorderLayout());
        initializeLogStyles();
    }

    /**
     * 初始化日志样式
     */
    private void initializeLogStyles() {
        logStyleContext = new StyleContext();
        logStyles = new HashMap<>();

        // 创建各种日志级别的样式
        createLogStyle(STYLE_DEFAULT, COLOR_DEFAULT, false);
        createLogStyle(STYLE_INFO, COLOR_INFO, false);
        createLogStyle(STYLE_SUCCESS, COLOR_SUCCESS, false);
        createLogStyle(STYLE_WARNING, COLOR_WARNING, false);
        createLogStyle(STYLE_ERROR, COLOR_ERROR, false);
        createLogStyle(STYLE_DEBUG, COLOR_DEBUG, true); // 调试信息使用斜体
    }

    /**
     * 创建日志样式
     */
    private void createLogStyle(String styleName, Color color, boolean italic) {
        Style style = logStyleContext.addStyle(styleName, null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setFontFamily(style, "Microsoft YaHei");
        if (italic) {
            StyleConstants.setItalic(style, true);
        }
        logStyles.put(styleName, style);
    }

    /**
     * 创建带滚动条的日志面板
     */
    protected JPanel createLogPanelWithScroll() {
        JPanel panel = new JPanel(new BorderLayout());

        // 使用JTextPane替代JTextArea以支持彩色文本
        logArea = new JTextPane();
        logDocument = logArea.getStyledDocument();
        logArea.setEditable(false);

        // 设置支持中文的字体
        setOptimalFont(logArea);

        // 设置日志区域的背景色
//        logArea.setBackground(new Color(248, 248, 248)); // 浅灰色背景

        // 创建带滚动条的面板
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.setBorder(BorderFactory.createTitledBorder("操作日志"));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 设置最优字体以支持中文
     */
    private void setOptimalFont(JTextPane textPane) {
        // 尝试使用支持中文的字体
        String[] preferredFonts = {
                "Microsoft YaHei UI",     // Windows 中文UI字体
                "PingFang SC",           // macOS 苹方
                "Noto Sans CJK SC",      // Google Noto字体
                "Source Han Sans SC",    // 思源黑体
                "SimHei",                // 黑体
                "SimSun",                // 宋体
                "DejaVu Sans",           // 跨平台字体
        };

        Font font = null;
        for (String fontName : preferredFonts) {
            font = new Font(fontName, Font.PLAIN, 12);
            if (font.getFamily().equals(fontName)) {
                break;
            }
        }

        if (font != null) {
            textPane.setFont(font);
        } else {
            // 回退到默认字体
            textPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        }
    }

    /**
     * 添加日志（默认样式）
     */
    public void appendLog(String message) {
        appendLog(message, STYLE_DEFAULT);
    }

    /**
     * 添加信息日志（绿色）
     */
    public void appendInfo(String message) {
        appendLog(ASCII_INFO + " " + message, STYLE_INFO);
    }

    /**
     * 添加成功日志（深绿色）
     */
    public void appendSuccess(String message) {
        appendLog(ASCII_SUCCESS + " " + message, STYLE_SUCCESS);
    }

    /**
     * 添加警告日志（橙色）
     */
    public void appendWarning(String message) {
        appendLog(ASCII_WARNING + " " + message, STYLE_WARNING);
    }

    /**
     * 添加错误日志（红色）
     */
    public void appendError(String message) {
        appendLog(ASCII_ERROR + " " + message, STYLE_ERROR);
    }

    /**
     * 添加调试日志（蓝色斜体）
     */
    public void appendDebug(String message) {
        appendLog(ASCII_DEBUG + " " + message, STYLE_DEBUG);
    }

    /**
     * 核心日志添加方法
     */
    public void appendLog(String message, String styleName) {
        SwingUtilities.invokeLater(() -> {
            try {
                Date date = new Date();
                String timestamp = TimeUtils.timestampToDate(date.getTime() / 1000);

                if (logDocument != null) {
                    // 时间戳使用默认样式
                    Style timestampStyle = logStyles.get(STYLE_DEFAULT);
                    logDocument.insertString(logDocument.getLength(), "[" + timestamp + "] ", timestampStyle);

                    // 消息使用指定样式
                    Style messageStyle = logStyles.getOrDefault(styleName, logStyles.get(STYLE_DEFAULT));
                    logDocument.insertString(logDocument.getLength(), message + "\n", messageStyle);

                    // 自动滚动到最后
                    logArea.setCaretPosition(logDocument.getLength());
                }
                // 如果日志行数过多，自动清理（保留最近500行）
                autoCleanLog();

            } catch (BadLocationException e) {
                System.err.println("日志添加失败: " + e.getMessage());
            }
        });
    }

    /**
     * 自动清理日志，防止内存占用过多
     */
    private void autoCleanLog() {
        try {
            int maxLines = 500;
            int lineCount = logDocument.getDefaultRootElement().getElementCount();

            if (lineCount > maxLines) {
                int removeCount = lineCount - maxLines;
                Element root = logDocument.getDefaultRootElement();
                int endOffset = root.getElement(removeCount - 1).getEndOffset();
                logDocument.remove(0, endOffset);

                // 添加清理提示
                appendWarning("日志已自动清理，移除了 " + removeCount + " 行旧日志");
            }
        } catch (Exception e) {
            // 忽略清理过程中的异常
        }
    }

    /**
     * 清空日志
     */
    public void clearLog() {
        SwingUtilities.invokeLater(() -> {
            try {
                logDocument.remove(0, logDocument.getLength());
            } catch (BadLocationException e) {
                System.err.println("清空日志失败: " + e.getMessage());
            }
        });
    }

    /**
     * 获取日志内容
     */
    public String getLogContent() {
        try {
            return logDocument.getText(0, logDocument.getLength());
        } catch (BadLocationException e) {
            return "";
        }
    }

    /**
     * 导出日志到文件
     */
    public void exportLog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出日志");
        fileChooser.setSelectedFile(new java.io.File("log_" +
                TimeUtils.timestampToDate(System.currentTimeMillis() / 1000).replace(":", "-") + ".txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.nio.file.Files.write(file.toPath(), getLogContent().getBytes());
                appendSuccess("日志已导出到: " + file.getAbsolutePath());
            } catch (Exception e) {
                appendError("导出日志失败: " + e.getMessage());
            }
        }
    }
}