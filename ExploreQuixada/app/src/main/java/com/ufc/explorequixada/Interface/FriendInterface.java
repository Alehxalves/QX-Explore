package com.ufc.explorequixada.Interface;

import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Repository.FriendDAO;

public interface FriendInterface {
	void addFriend(UserEntity user, final FriendDAO.OnFriendAddedListener listener);
}
