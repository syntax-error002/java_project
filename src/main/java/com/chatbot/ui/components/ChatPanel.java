package com.chatbot.ui.components;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(33, 37, 41));
        
        // Style scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Add welcome message
        addMessage("Hello! I'm your AI assistant. How can I help you today?", false);
    }
    
    public void addMessage(String message, boolean isUser) {
        SwingUtilities.invokeLater(() -> {
            JPanel messageContainer = createMessageBubble(message, isUser);
            messagesPanel.add(messageContainer);
            messagesPanel.add(Box.createVerticalStrut(10));
            
            // Auto-scroll to bottom
            messagesPanel.revalidate();
            messagesPanel.repaint();
            
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }
    
    private JPanel createMessageBubble(String message, boolean isUser) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, container.getPreferredSize().height));
        
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        
        // Create message text area
        JTextArea textArea = new JTextArea(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Create timestamp
        JLabel timeLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        if (isUser) {
            // User message (right side, blue)
            bubble.setBackground(new Color(0, 123, 255));
            textArea.setForeground(Color.WHITE);
            timeLabel.setForeground(new Color(200, 200, 200));
            
            container.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
            container.add(bubble, BorderLayout.CENTER);
        } else {
            // AI message (left side, gray)
            bubble.setBackground(new Color(52, 58, 64));
            textArea.setForeground(Color.WHITE);
            timeLabel.setForeground(new Color(173, 181, 189));
            
            container.add(bubble, BorderLayout.WEST);
            container.add(Box.createHorizontalStrut(100), BorderLayout.EAST);
        }
        
        bubble.add(textArea, BorderLayout.CENTER);
        bubble.add(timeLabel, BorderLayout.SOUTH);
        
        // Make bubble rounded
        bubble.setOpaque(false);
        bubble.setBorder(new RoundedBorder(bubble.getBackground(), 16));
        
        return container;
    }
    
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int radius;
        
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(x, y, width, height, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(12, 16, 12, 16);
        }
    }
}