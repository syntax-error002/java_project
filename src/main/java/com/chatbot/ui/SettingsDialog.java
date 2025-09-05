package com.chatbot.ui;

import com.chatbot.database.DatabaseManager;
import com.chatbot.model.User;
import com.chatbot.ui.components.ModernButton;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private User currentUser;
    private JCheckBox voiceEnabledCheckbox;
    private JSlider voiceSpeedSlider;
    private JLabel speedValueLabel;
    
    public SettingsDialog(Frame parent, User user) {
        super(parent, "Settings", true);
        this.currentUser = user;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupDialog();
        loadCurrentSettings();
    }
    
    private void initializeComponents() {
        voiceEnabledCheckbox = new JCheckBox("Enable voice responses");
        voiceEnabledCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        voiceEnabledCheckbox.setForeground(Color.WHITE);
        voiceEnabledCheckbox.setOpaque(false);
        
        voiceSpeedSlider = new JSlider(50, 200, 100);
        voiceSpeedSlider.setMajorTickSpacing(50);
        voiceSpeedSlider.setMinorTickSpacing(25);
        voiceSpeedSlider.setPaintTicks(true);
        voiceSpeedSlider.setPaintLabels(true);
        voiceSpeedSlider.setBackground(new Color(52, 58, 64));
        voiceSpeedSlider.setForeground(Color.WHITE);
        
        speedValueLabel = new JLabel("1.0x");
        speedValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        speedValueLabel.setForeground(Color.WHITE);
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Voice settings section
        JLabel voiceLabel = new JLabel("Voice Settings");
        voiceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        voiceLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        settingsPanel.add(voiceLabel, gbc);
        
        // Voice enabled checkbox
        gbc.gridy = 1; gbc.gridwidth = 2;
        settingsPanel.add(voiceEnabledCheckbox, gbc);
        
        // Voice speed label
        JLabel speedLabel = new JLabel("Voice Speed:");
        speedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        speedLabel.setForeground(Color.WHITE);
        gbc.gridy = 2; gbc.gridwidth = 1;
        settingsPanel.add(speedLabel, gbc);
        
        gbc.gridx = 1;
        settingsPanel.add(speedValueLabel, gbc);
        
        // Voice speed slider
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        settingsPanel.add(voiceSpeedSlider, gbc);
        
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
    
    private void setupEventHandlers() {
        voiceSpeedSlider.addChangeListener(e -> {
            double speed = voiceSpeedSlider.getValue() / 100.0;
            speedValueLabel.setText(String.format("%.1fx", speed));
        });
        
        voiceEnabledCheckbox.addActionListener(e -> {
            boolean enabled = voiceEnabledCheckbox.isSelected();
            voiceSpeedSlider.setEnabled(enabled);
            speedValueLabel.setEnabled(enabled);
        });
    }
    
    private void setupDialog() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    private void loadCurrentSettings() {
        DatabaseManager.UserSettings settings = DatabaseManager.getInstance().getUserSettings(currentUser.getId());
        
        voiceEnabledCheckbox.setSelected(settings.isVoiceEnabled());
        voiceSpeedSlider.setValue((int) (settings.getVoiceSpeed() * 100));
        speedValueLabel.setText(String.format("%.1fx", settings.getVoiceSpeed()));
        
        voiceSpeedSlider.setEnabled(settings.isVoiceEnabled());
        speedValueLabel.setEnabled(settings.isVoiceEnabled());
    }
    
    private void saveSettings() {
        boolean voiceEnabled = voiceEnabledCheckbox.isSelected();
        double voiceSpeed = voiceSpeedSlider.getValue() / 100.0;
        
        DatabaseManager.getInstance().updateUserSettings(currentUser.getId(), voiceEnabled, voiceSpeed);
        
        JOptionPane.showMessageDialog(this, 
            "Settings saved successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
}