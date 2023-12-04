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
	private ArrayList<UserEntity> friends;
	private int postCount;
	private int friendCount;
    public UserEntity(String username, String email, String password, String profileImageUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
		this.friends = new ArrayList<UserEntity>();
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

	public ArrayList<UserEntity> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<UserEntity> friends) {
		this.friends = friends;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	@Override
    public String toString() {
        return "User{" +
                ", name='" + username + '\'' +
                '}';
    }
}
