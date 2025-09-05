package com.chatbot.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private Color originalColor;
    private Color hoverColor;
    
    public ModernButton(String text, Color backgroundColor) {
        super(text);
        this.originalColor = backgroundColor;
        this.hoverColor = backgroundColor.brighter();
        
        setupButton();
        setupHoverEffect();
    }
    
    private void setupButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(originalColor);
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);
    }
    
    private void setupHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(hoverColor);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(originalColor);
                }
            }
        });
    }
    
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.originalColor = bg;
        this.hoverColor = bg.brighter();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        // Paint text
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), textX, textY);
        
        g2.dispose();
    }
}