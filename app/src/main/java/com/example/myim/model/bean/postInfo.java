package com.example.myim.model.bean;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;

public class postInfo {
    private int id;
    private ArrayList<String> photos;
    private ArrayList<String> labels;
    private int userId;
    private String title;
    private String context;
    private int likes;
    private int dislikes;
    private String userName;
    private String photo;
    private ArrayList<CommentInfo> comments;

    private Timestamp timestamp;

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public void setUserId(int id) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setComments(ArrayList<CommentInfo> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public ArrayList<CommentInfo> getComments() {
        return comments;
    }
}
