package com.ufc.explorequixada.Repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.UserInterface;

public class UserDAO implements UserInterface{

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersCollection = db.collection("users");

    @Override
    public void createUser(UserEntity user, final OnUserCreatedListener listener) {
        usersCollection
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    if (listener != null) {
                        listener.onUserCreated(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onUserCreated(false);
                    }
                });
    }

    @Override
    public boolean updateUser(UserEntity user) {
        return false;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    public interface OnUserCreatedListener {
        void onUserCreated(boolean isSuccess);
    }
}
