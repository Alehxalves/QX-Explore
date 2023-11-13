package com.ufc.explorequixada.Controller;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Repository.UserRepository;
import com.ufc.explorequixada.Entity.UserEntity;
import androidx.annotation.NonNull; // Import necessário para NonNull

import com.google.android.gms.tasks.Task;


import java.util.Objects;

public class UserController {
    private UserRepository userRepository;

    public UserController() {
        userRepository = new UserRepository("users");
    }

    public void save(UserEntity user) {
        userRepository.createDocument(user);
    }

    public void getUserByEmail(String email) {

    }

    public void checkIfUsernameExists(String username, OnCheckUsernameListener listener) {
        userRepository.findByUsername(username, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserEntity user = documentSnapshot.toObject(UserEntity.class);
                if (user != null) {
                    listener.onUsernameExists(true);
                } else {
                    listener.onUsernameExists(false);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUsernameExists(false);
            }
        });
    }
    public interface OnCheckUsernameListener {
        void onUsernameExists(boolean usernameExists);
    }
}
