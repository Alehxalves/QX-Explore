package com.ufc.explorequixada.Repository;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.UserInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDAO implements UserInterface{

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersCollection = db.collection("users");

    @Override
    public void createUser(UserEntity user, final OnUserCreatedListener listener) {

        DocumentReference newUser = usersCollection.document();
        user.setId(newUser.getId());
        usersCollection
                .document(user.getId()).set(user)
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
    public UserEntity findByEmail(String email, final OnUserFindedListener listener) {
        usersCollection
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserEntity user = documentSnapshot.toObject(UserEntity.class);
                            if (listener != null) {
                                listener.onUserFinded(user);
                            }
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onUserFinded(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onUserFinded(null);
                    }
                });
        return null;
    }

    public UserEntity findByUsername(String username, final OnUserFindedListener listener) {
        usersCollection
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserEntity user = documentSnapshot.toObject(UserEntity.class);
                            if (listener != null) {
                                listener.onUserFinded(user);
                            }
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onUserFinded(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onUserFinded(null);
                    }
                });
        return null;
    }

    public void findUsersByUsername(String usernamePattern, final OnUsersLoadedListener listener) {
        String endPattern = usernamePattern + "\uf8ff";

        usersCollection
                .orderBy("username")
                .startAt(usernamePattern)
                .endAt(endPattern)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserEntity> users = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        UserEntity user = documentSnapshot.toObject(UserEntity.class);
                        users.add(user);
                    }
                    if (listener != null) {
                        listener.onUsersLoaded(users);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onUsersLoaded(Collections.emptyList());
                    }
                });
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    public interface OnUserCreatedListener {
        void onUserCreated(boolean isSuccess);
    }

    public interface OnUserFindedListener {
        void onUserFinded(UserEntity user);
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<UserEntity> user);
    }
}
