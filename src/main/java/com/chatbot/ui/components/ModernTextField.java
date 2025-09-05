package com.chatbot.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ModernTextField extends JTextField {
    private String placeholder;
    private boolean showingPlaceholder;
    
    public ModernTextField(String placeholder) {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        
        setupTextField();
        setupPlaceholder();
    }
    
    private void setupTextField() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        setBackground(new Color(52, 58, 64));
        setCaretColor(Color.WHITE);
    }
    
    private void setupPlaceholder() {
        setText(placeholder);
        setForeground(new Color(173, 181, 189));
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.WHITE);
                    showingPlaceholder = false;
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(new Color(173, 181, 189));
                    showingPlaceholder = true;
                }
            }
        });
    }
    
    @Override
    public String getText() {
        return showingPlaceholder ? "" : super.getText();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        super.paintComponent(g);
        g2.dispose();
    }
}