package com.chatbot.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private int id;
    private int userId;
    private String message;
    private String response;
    private LocalDateTime timestamp;
    
    public ChatMessage(int id, int userId, String message, String response, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.response = response;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}