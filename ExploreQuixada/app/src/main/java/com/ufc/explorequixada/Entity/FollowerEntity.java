package com.ufc.explorequixada.Entity;

import java.util.Date;

public class FollowerEntity {
	private String id;
	private String followerUsername;
	private String followingUsername;
	private Date date;

	public FollowerEntity() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFollowerUsername() {
		return followerUsername;
	}

	public void setFollowerUsername(String followerUsername) {
		this.followerUsername = followerUsername;
	}

	public String getFollowingUsername() {
		return followingUsername;
	}

	public void setFollowingUsername(String followingUsername) {
		this.followingUsername = followingUsername;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
