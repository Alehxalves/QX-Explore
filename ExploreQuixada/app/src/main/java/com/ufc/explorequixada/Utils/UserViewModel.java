package com.ufc.explorequixada.Utils;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    private FirebaseUser user;
    public FirebaseUser getUser() {
        return user;
    }
    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
