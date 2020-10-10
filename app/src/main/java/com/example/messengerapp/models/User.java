package com.example.messengerapp.models;

public class User {
    private String id;
    private String email;
    private String username;
    private String imageUrl;

    public User(){

    }

    public User(String id, String email, String username, String imageUrl) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
