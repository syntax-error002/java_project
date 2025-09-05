package com.chatbot.ui;

import com.chatbot.model.User;
import com.chatbot.service.GeminiService;
import com.chatbot.service.VoiceService;
import com.chatbot.database.DatabaseManager;
import com.chatbot.ui.components.ChatPanel;
import com.chatbot.ui.components.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private User currentUser;
    private GeminiService geminiService;
    private VoiceService voiceService;
    private ChatPanel chatPanel;
    private JTextField messageField;
    private ModernButton sendButton;
    private ModernButton voiceButton;
    private ModernButton historyButton;
    private ModernButton settingsButton;
    private ModernButton logoutButton;
    
    public MainFrame(User user) {
        this.currentUser = user;
        this.voiceService = new VoiceService();
        
        // Initialize Gemini service with API key
        // In a real application, you would get this from configuration or environment
        String apiKey = System.getProperty("GEMINI_API_KEY", "your-api-key-here");
        this.geminiService = new GeminiService(apiKey);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        chatPanel = new ChatPanel();
        
        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        messageField.setBackground(new Color(52, 58, 64));
        messageField.setForeground(Color.WHITE);
        messageField.setCaretColor(Color.WHITE);
        
        sendButton = new ModernButton("Send", new Color(0, 123, 255));
        voiceButton = new ModernButton("🎤", new Color(220, 53, 69));
        voiceButton.setPreferredSize(new Dimension(50, 40));
        
        historyButton = new ModernButton("History", new Color(108, 117, 125));
        settingsButton = new ModernButton("Settings", new Color(108, 117, 125));
        logoutButton = new ModernButton("Logout", new Color(220, 53, 69));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with user info and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 37, 41));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);
        controlPanel.add(historyButton);
        controlPanel.add(settingsButton);
        controlPanel.add(logoutButton);
        
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.EAST);
        
        // Chat area
        add(topPanel, BorderLayout.NORTH);
        add(chatPanel, BorderLayout.CENTER);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(new Color(33, 37, 41));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel messagePanel = new JPanel(new BorderLayout(10, 0));
        messagePanel.setOpaque(false);
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(voiceButton, BorderLayout.EAST);
        
        inputPanel.add(messagePanel, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        
        voiceButton.addActionListener(e -> toggleVoiceInput());
        
        historyButton.addActionListener(e -> showChatHistory());
        settingsButton.addActionListener(e -> showSettings());
        logoutButton.addActionListener(e -> handleLogout());
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (voiceService.isRecording()) {
                    voiceService.stopRecording();
                }
                System.exit(0);
            }
        });
    }
    
    private void setupFrame() {
        setTitle("AI Chatbot - " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) return;
        
        // Add user message to chat
        chatPanel.addMessage(message, true);
        messageField.setText("");
        
        // Disable send button while processing
        sendButton.setEnabled(false);
        sendButton.setText("Sending...");
        
        // Process message in background
        SwingUtilities.invokeLater(() -> {
            String response = geminiService.generateResponse(message);
            
            // Add AI response to chat
            chatPanel.addMessage(response, false);
            
            // Save to database
            DatabaseManager.getInstance().saveChatMessage(currentUser.getId(), message, response);
            
            // Re-enable send button
            sendButton.setEnabled(true);
            sendButton.setText("Send");
            
            // Speak response if voice is enabled
            DatabaseManager.UserSettings settings = DatabaseManager.getInstance().getUserSettings(currentUser.getId());
            if (settings.isVoiceEnabled()) {
                voiceService.speakText(response, settings.getVoiceSpeed());
            }
        });
    }
    
    private void toggleVoiceInput() {
        if (voiceService.isRecording()) {
            voiceService.stopRecording();
            voiceButton.setText("🎤");
            voiceButton.setBackground(new Color(220, 53, 69));
        } else {
            voiceButton.setText("⏹");
            voiceButton.setBackground(new Color(40, 167, 69));
            
            voiceService.startRecording().thenAccept(text -> {
                SwingUtilities.invokeLater(() -> {
                    messageField.setText(text);
                    voiceButton.setText("🎤");
                    voiceButton.setBackground(new Color(220, 53, 69));
                });
            });
        }
    }
    
    private void showChatHistory() {
        new ChatHistoryDialog(this, currentUser).setVisible(true);
    }
    
    private void showSettings() {
        new SettingsDialog(this, currentUser).setVisible(true);
    }
    
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            if (voiceService.isRecording()) {
                voiceService.stopRecording();
            }
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
}