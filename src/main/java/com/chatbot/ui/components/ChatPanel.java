package com.chatbot.ui.components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatPanel extends JPanel {
    private JPanel messagesPanel;
    private JScrollPane scrollPane;

    public ChatPanel() {
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(33, 37, 41));

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(33, 37, 41));
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(33, 37, 41));
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);

        addMessage("Hello! I'm your AI assistant. How can I help you today?", false);
    }

    public void addMessage(String message, boolean isUser) {
        SwingUtilities.invokeLater(() -> {
            JPanel messageRow = createMessageBubble(message, isUser);
            messagesPanel.add(messageRow);
            messagesPanel.add(Box.createVerticalStrut(15));

            messagesPanel.revalidate();
            messagesPanel.repaint();

            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }

    private String convertMarkdownToHtml(String markdown) {
        // CSS for styling the HTML content
        String css = "<style>" +
                "body { width: 350px; font-family: Segoe UI; font-size: 12pt; color: white; margin: 0; padding: 5px; }" +
                "h1, h2, h3 { margin: 10px 0 5px 0; padding: 0; font-weight: bold; }" +
                "h1 { font-size: 18pt; }" +
                "h2 { font-size: 16pt; }" +
                "h3 { font-size: 14pt; }" +
                "p { margin: 5px 0; }" +
                "ul { margin: 5px 0 5px 20px; padding: 0; }" +
                "li { margin: 2px 0; }" +
                "pre { background-color: #2b2b2b; border: 1px solid #444; border-radius: 5px; padding: 10px; margin: 10px 0; font-family: Consolas, monaco, monospace; font-size: 11pt; }" +
                "code { font-family: Consolas, monaco, monospace; }" +
                "</style>";

        // First, handle multi-line code blocks to preserve their content
        Pattern codeBlockPattern = Pattern.compile("```(.*?\\n)(.+?)```", Pattern.DOTALL);
        Matcher codeBlockMatcher = codeBlockPattern.matcher(markdown);
        StringBuffer sb = new StringBuffer();
        while (codeBlockMatcher.find()) {
            String code = codeBlockMatcher.group(2).trim();
            // Escape special HTML characters inside the code block
            String escapedCode = code.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
            codeBlockMatcher.appendReplacement(sb, "<pre><code>" + escapedCode + "</code></pre>");
        }
        codeBlockMatcher.appendTail(sb);
        String html = sb.toString();

        // Process remaining markdown line-by-line
        String[] lines = html.split("\\n");
        StringBuilder result = new StringBuilder();
        boolean inList = false;

        for (String line : lines) {
            if (line.trim().startsWith("###")) {
                result.append("<h3>").append(line.substring(3).trim()).append("</h3>");
            } else if (line.trim().startsWith("##")) {
                result.append("<h2>").append(line.substring(2).trim()).append("</h2>");
            } else if (line.trim().startsWith("#")) {
                result.append("<h1>").append(line.substring(1).trim()).append("</h1>");
            } else if (line.trim().startsWith("*")) {
                if (!inList) {
                    result.append("<ul>");
                    inList = true;
                }
                result.append("<li>").append(line.substring(1).trim()).append("</li>");
            } else {
                if (inList) {
                    result.append("</ul>");
                    inList = false;
                }
                if (!line.trim().isEmpty() && !line.startsWith("<")) {
                    result.append("<p>").append(line.trim()).append("</p>");
                }
                 else {
                    result.append(line);
                 }
            }
        }

        if (inList) {
            result.append("</ul>");
        }
        
        // Final processing for bold
        String finalHtml = result.toString().replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");

        return "<html><head>" + css + "</head><body>" + finalHtml + "</body></html>";
    }


    private JPanel createMessageBubble(String message, boolean isUser) {
        // The content of the bubble
        String labelText = isUser ? "<html><body style='width: 350px; font-family: Segoe UI; font-size: 12pt; color: white;'>" + message.replace("\n", "<br>") + "</body></html>" : convertMarkdownToHtml(message);
        JLabel messageLabel = new JLabel(labelText);
        messageLabel.setVerticalAlignment(SwingConstants.TOP);

        // The bubble panel, which holds the text
        JPanel bubble = new JPanel(new BorderLayout(0, 4));
        bubble.setOpaque(false);
        bubble.add(messageLabel, BorderLayout.CENTER);
        
        // Timestamp
        JLabel timeLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(new Color(200, 200, 200, 180));
        timeLabel.setHorizontalAlignment(isUser ? SwingConstants.RIGHT : SwingConstants.LEFT);
        bubble.add(timeLabel, BorderLayout.SOUTH);

        // Set the bubble's appearance
        Color startColor, endColor;
        if (isUser) {
            startColor = new Color(0, 123, 255);
            endColor = new Color(0, 80, 220);
        } else {
            startColor = new Color(60, 65, 75);
            endColor = new Color(48, 52, 60);
        }
        bubble.setBorder(new ModernBubbleBorder(startColor, endColor, 20, 5, isUser));

        // The row panel, which aligns the bubble left or right
        JPanel row = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.add(bubble);

        return row;
    }

    private static class ModernBubbleBorder extends AbstractBorder {
        private Color startColor, endColor;
        private int radius, shadowSize;
        private boolean isUser;

        public ModernBubbleBorder(Color start, Color end, int r, int ss, boolean isUser) {
            this.startColor = start;
            this.endColor = end;
            this.radius = r;
            this.shadowSize = ss;
            this.isUser = isUser;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int bubbleX = isUser ? x : x + shadowSize;
            int bubbleWidth = width - shadowSize;
            
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(bubbleX, y + shadowSize, bubbleWidth, height - shadowSize, radius, radius);

            GradientPaint gp = new GradientPaint(bubbleX, y, startColor, bubbleX, y + height, endColor);
            g2.setPaint(gp);
            g2.fillRoundRect(bubbleX, y, bubbleWidth, height - shadowSize, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int padding = 12;
            int shadowPad = shadowSize + 5;
            return new Insets(padding, isUser ? padding : shadowPad, padding, isUser ? shadowPad : padding);
        }
    }
}
