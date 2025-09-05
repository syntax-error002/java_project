package com.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GeminiService {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private String apiKey;
    private ObjectMapper objectMapper;
    
    public GeminiService(String apiKey) {
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
    }
    
    public String generateResponse(String message) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL + "?key=" + apiKey);
            post.setHeader("Content-Type", "application/json");
            
            // Create request body
            String requestBody = String.format("""
                {
                    "contents": [{
                        "parts": [{
                            "text": "%s"
                        }]
                    }]
                }
                """, message.replace("\"", "\\\""));
            
            post.setEntity(new StringEntity(requestBody));
            
            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getStatusLine().getStatusCode() == 200) {
                    return parseResponse(responseBody);
                } else {
                    return "Sorry, I'm having trouble connecting to the AI service. Please try again later.";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Sorry, there was an error processing your request. Please try again.";
        }
    }
    
    private String parseResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.get("candidates");
            
            if (candidates != null && candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.get("content");
                
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && parts.size() > 0) {
                        JsonNode text = parts.get(0).get("text");
                        if (text != null) {
                            return text.asText();
                        }
                    }
                }
            }
            
            return "I'm sorry, I couldn't generate a response. Please try again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response. Please try again.";
        }
    }
}