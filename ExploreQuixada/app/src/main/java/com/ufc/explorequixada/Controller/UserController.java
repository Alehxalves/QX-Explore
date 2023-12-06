package com.ufc.explorequixada.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.Fragment.FriendListFragment;
import com.ufc.explorequixada.Repository.UserDAO;

public class UserController {

    private final UserDAO userDAO;
    private FriendListFragment friendListFragment;

    public UserController(FriendListFragment friendListFragment) {
        userDAO = new UserDAO();
        friendListFragment = friendListFragment;
    }

    public void createUser(UserEntity user) {
        userDAO.createUser(user, isSuccess -> {
            if (isSuccess) {
            } else {
            }
        });
    }

}
