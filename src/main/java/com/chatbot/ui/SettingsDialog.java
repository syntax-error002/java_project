package com.chatbot.ui;

import com.chatbot.database.DatabaseManager;
import com.chatbot.model.User;
import com.chatbot.ui.components.ModernButton;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private User currentUser;
    
    public SettingsDialog(Frame parent, User user) {
        super(parent, "Settings", true);
        this.currentUser = user;
        
        initializeComponents();
        setupLayout();
        setupDialog();
        loadCurrentSettings();
    }
    
    private void initializeComponents() {
        // No components to initialize
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(33, 37, 41));
        
        // Title
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Settings panel
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(new Color(33, 37, 41));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(33, 37, 41));
        
        ModernButton saveButton = new ModernButton("Save", new Color(40, 167, 69));
        ModernButton cancelButton = new ModernButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        saveButton.addActionListener(e -> saveSettings());
        cancelButton.addActionListener(e -> dispose());
        
        add(titleLabel, BorderLayout.NORTH);
        add(settingsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    private void loadCurrentSettings() {
        // No settings to load
    }
    
    private void saveSettings() {
        JOptionPane.showMessageDialog(this, 
            "Settings saved successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
}