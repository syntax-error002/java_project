package com.chatbot.ui;

import com.chatbot.database.DatabaseManager;
import com.chatbot.model.ChatMessage;
import com.chatbot.model.User;
import com.chatbot.ui.components.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatHistoryDialog extends JDialog {
    private User currentUser;
    private JList<ChatMessage> historyList;
    private DefaultListModel<ChatMessage> listModel;
    
    public ChatHistoryDialog(Frame parent, User user) {
        super(parent, "Chat History", true);
        this.currentUser = user;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupDialog();
        loadChatHistory();
    }
    
    private void initializeComponents() {
        listModel = new DefaultListModel<>();
        historyList = new JList<>(listModel);
        historyList.setCellRenderer(new ChatHistoryRenderer());
        historyList.setBackground(new Color(52, 58, 64));
        historyList.setSelectionBackground(new Color(0, 123, 255));
        historyList.setSelectionForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(33, 37, 41));
        
        // Title
        JLabel titleLabel = new JLabel("Chat History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // History list
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(52, 58, 64));
        
        // Close button
        ModernButton closeButton = new ModernButton("Close", new Color(108, 117, 125));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(33, 37, 41));
        buttonPanel.add(closeButton);
        
        closeButton.addActionListener(e -> dispose());
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Double-click to view full conversation
        historyList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ChatMessage selected = historyList.getSelectedValue();
                    if (selected != null) {
                        showFullConversation(selected);
                    }
                }
            }
        });
    }
    
    private void setupDialog() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void loadChatHistory() {
        List<ChatMessage> messages = DatabaseManager.getInstance().getChatHistory(currentUser.getId(), 50);
        for (ChatMessage message : messages) {
            listModel.addElement(message);
        }
    }
    
    private void showFullConversation(ChatMessage message) {
        JDialog conversationDialog = new JDialog(this, "Conversation Details", true);
        conversationDialog.setLayout(new BorderLayout());
        conversationDialog.getContentPane().setBackground(new Color(33, 37, 41));
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(52, 58, 64));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String conversation = String.format(
            "Time: %s\n\nYou: %s\n\nAI: %s",
            message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            message.getMessage(),
            message.getResponse()
        );
        textArea.setText(conversation);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        
        ModernButton closeBtn = new ModernButton("Close", new Color(108, 117, 125));
        closeBtn.addActionListener(e -> conversationDialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(new Color(33, 37, 41));
        btnPanel.add(closeBtn);
        
        conversationDialog.add(scrollPane, BorderLayout.CENTER);
        conversationDialog.add(btnPanel, BorderLayout.SOUTH);
        conversationDialog.setSize(500, 400);
        conversationDialog.setLocationRelativeTo(this);
        conversationDialog.setVisible(true);
    }
    
    private static class ChatHistoryRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof ChatMessage) {
                ChatMessage message = (ChatMessage) value;
                String preview = message.getMessage();
                if (preview.length() > 50) {
                    preview = preview.substring(0, 50) + "...";
                }
                
                String displayText = String.format(
                    "<html><b>%s</b><br><span style='color: #adb5bd;'>%s</span></html>",
                    message.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    preview
                );
                setText(displayText);
            }
            
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            if (!isSelected) {
                setBackground(new Color(52, 58, 64));
                setForeground(Color.WHITE);
            }
            
            return this;
        }
    }
}