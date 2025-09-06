package com.chatbot.ui.events;

import com.chatbot.tasks.GeminiTask;
import com.chatbot.ui.components.ChatPanel;
import com.chatbot.ui.components.ModernTextField;
import com.chatbot.utils.CommandExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInputListener implements ActionListener {
    private ModernTextField inputField;
    private ChatPanel chatPanel;

    public UserInputListener(ModernTextField inputField, ChatPanel chatPanel) {
        this.inputField = inputField;
        this.chatPanel = chatPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }

        // Add user message to chat panel
        chatPanel.addMessage(userInput, true);
        inputField.setText("");

        // Check for local commands first
        String commandResponse = CommandExecutor.execute(userInput.toLowerCase());

        if (commandResponse != null) {
            // If it was a command, show the execution feedback
            chatPanel.addMessage(commandResponse, false);
        } else {
            // Otherwise, send the query to the Gemini API
            // Show a thinking indicator
            chatPanel.addMessage("<i>Thinking...</i>", false);
            // Execute the API call in a background thread
            GeminiTask task = new GeminiTask(userInput, chatPanel);
            task.execute();
        }
    }
}
