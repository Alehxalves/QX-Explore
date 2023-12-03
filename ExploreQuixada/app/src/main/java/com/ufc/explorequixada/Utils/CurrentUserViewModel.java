package com.ufc.explorequixada.Utils;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Entity.UserEntity;

public class CurrentUserViewModel extends ViewModel {
    private UserEntity user;
    public UserEntity getUser() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }
}
