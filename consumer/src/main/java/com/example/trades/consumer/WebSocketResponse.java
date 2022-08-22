package com.example.trades.consumer;

public class WebSocketResponse {
    private String content;
    public WebSocketResponse() {
    }
    public WebSocketResponse(String content) {
        this.content = content;
    }
    public String getContent() { 
        return content;
    }
}
