package com.chatbot.ui;

import com.chatbot.database.DatabaseManager;
import com.chatbot.model.User;
import com.chatbot.ui.components.ModernButton;
import com.chatbot.ui.components.ModernTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private ModernTextField usernameField;
    private JPasswordField passwordField;
    private ModernButton loginButton;
    private ModernButton registerButton;
    
    public LoginFrame() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        usernameField = new ModernTextField("Username");
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        loginButton = new ModernButton("Login", new Color(0, 123, 255));
        registerButton = new ModernButton("Register", new Color(108, 117, 125));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(33, 37, 41));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("AI Chatbot", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(173, 181, 189));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subtitleLabel, gbc);
        
        // Username field
        gbc.gridy = 2; gbc.gridwidth = 2; gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridy = 4; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
        
        // Enter key support
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void setupFrame() {
        setTitle("AI Chatbot - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set icon (you can add an icon file to resources)
        try {
            setIconImage(Toolkit.getDefaultToolkit().createImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }
        
        User user = DatabaseManager.getInstance().authenticateUser(username, password);
        if (user != null) {
            // Login successful
            dispose();
            SwingUtilities.invokeLater(() -> {
                new MainFrame(user).setVisible(true);
            });
        } else {
            showError("Invalid username or password.");
        }
    }
    
    private void handleRegister() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            new RegisterFrame().setVisible(true);
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}