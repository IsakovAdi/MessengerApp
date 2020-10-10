package com.example.messengerapp.models;

public class Chat {
    private String message;
    private String senderId;
    private String receiver;

    public Chat(){}

    public Chat(String message, String senderId, String receiver) {
        this.message = message;
        this.senderId = senderId;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
