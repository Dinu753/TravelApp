package com.dinushi.travelexplorer;

public class PostModel {
    public String title;
    public String description;
    public String imageUri;
    public String location;
    public String userId;

    //  Required empty constructor for Firestore
    public PostModel() {}

    public PostModel(String title, String description, String imageUri, String location, String userId) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.location = location;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }
}



