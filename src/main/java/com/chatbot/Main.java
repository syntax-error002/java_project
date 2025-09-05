package com.chatbot;

import com.chatbot.ui.LoginFrame;
import com.chatbot.database.DatabaseManager;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set the FlatLaf dark theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize database
        DatabaseManager.getInstance().initializeDatabase();

        // Start the application
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}