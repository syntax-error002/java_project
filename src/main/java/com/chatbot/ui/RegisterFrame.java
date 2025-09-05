package com.chatbot.ui;

import com.chatbot.database.DatabaseManager;
import com.chatbot.ui.components.ModernButton;
import com.chatbot.ui.components.ModernTextField;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private ModernTextField usernameField;
    private ModernTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private ModernButton registerButton;
    private ModernButton backButton;
    
    public RegisterFrame() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        usernameField = new ModernTextField("Username");
        emailField = new ModernTextField("Email (optional)");
        
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        
        // Style password fields
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        passwordField.setFont(fieldFont);
        confirmPasswordField.setFont(fieldFont);
        
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        registerButton = new ModernButton("Create Account", new Color(40, 167, 69));
        backButton = new ModernButton("Back to Login", new Color(108, 117, 125));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(33, 37, 41));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Join the AI conversation", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(173, 181, 189));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subtitleLabel, gbc);
        
        // Form fields
        gbc.insets = new Insets(10, 0, 10, 0);
        
        gbc.gridy = 2;
        mainPanel.add(usernameField, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(emailField, gbc);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4; gbc.insets = new Insets(20, 0, 5, 0);
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(passwordField, gbc);
        
        // Confirm password label
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 6; gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(confirmLabel, gbc);
        
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(confirmPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        gbc.gridy = 8; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> handleBack());
        
        getRootPane().setDefaultButton(registerButton);
    }
    
    private void setupFrame() {
        setTitle("AI Chatbot - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required.");
            return;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters long.");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        
        // Attempt to create user
        boolean success = DatabaseManager.getInstance().createUser(username, password, email.isEmpty() ? null : email);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully! You can now log in.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            handleBack();
        } else {
            showError("Username already exists. Please choose a different username.");
        }
    }
    
    private void handleBack() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}