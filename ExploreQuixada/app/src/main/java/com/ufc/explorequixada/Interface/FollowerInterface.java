package com.ufc.explorequixada.Interface;

import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Repository.FollowerDAO;

public interface FollowerInterface {

    void newFollower(FollowerEntity follower, FollowerDAO.OnFollowerCreatedListener listener);

    void getMyFollowers(String username,final FollowerDAO.OnFollowersLoadedListener listener);

    void unfollow(String followerId, final FollowerDAO.OnFollowerDeletedListener listener);

    void getFollowingCountByUserName(String username, final FollowerDAO.OnFollowersCountLoadedListener listener);

    void getFollowersCountByUserName(String username, final FollowerDAO.OnFollowersCountLoadedListener listener);
}
