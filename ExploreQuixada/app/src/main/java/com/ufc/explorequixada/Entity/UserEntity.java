package com.ufc.explorequixada.Entity;

import java.util.ArrayList;
import java.util.UUID;

public class UserEntity {

    private String id;
    private String username;
    private String email;
    private String password;

    private String profileImageUrl;

    private ArrayList<PostEntity> posts;
    public UserEntity(String username, String email, String password, String profileImageUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public UserEntity(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public ArrayList<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostEntity> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + username + '\'' +
                '}';
    }
}
