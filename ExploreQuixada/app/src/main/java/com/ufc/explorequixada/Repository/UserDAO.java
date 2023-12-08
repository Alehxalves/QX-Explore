package com.ufc.explorequixada.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Entity.FriendEntity;
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

    public UserEntity orderByUsername(String username, final onFriendFindedListener listener) {
        usersCollection
                .whereEqualTo("username", username)
                .orderBy("username", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            FriendEntity friend = documentSnapshot.toObject(FriendEntity.class);
                            if (listener != null) {
                                listener.onFriendFinded(friend);
                            }
                            return;
                        }
                    }
                    if (listener != null) {
                        listener.onFriendFinded(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFriendFinded(null);
                    }
                });
        return null;
    }

    public UserEntity getAllUsers(String username, final OnUsersLoadedListener listener) {
        usersCollection.whereEqualTo("username", username)
                .orderBy("username", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (listener != null) {
                                listener.onUsersLoaded(Collections.emptyList());
                            }
                            return;
                        }

                        List<FriendEntity> users = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            FriendEntity user = documentSnapshot.toObject(FriendEntity.class);
                            users.add(user);
                        }

                        if (listener != null) {
                            listener.onUsersLoaded(users);
                        }
                    }
                });
        return null;
    }

    public UserEntity addFriend(UserEntity user, final OnUserFindedListener listener) {
        String name = user.getUsername();
        findByUsername(name, new OnUserFindedListener() {
            @Override
            public void onUserFinded(UserEntity user) {
                if(user == null) {
                    if(listener != null) {
                        listener.onUserFinded(null);
                    }
                    return;
                }

                user.setFriendCount(user.getFriendCount() + 1);

                usersCollection.document(user.getId()).update("friends", user.getFriends());
                usersCollection.document(user.getId()).update("friendCount", user.getFriendCount());
                if(listener != null) {
                    listener.onUserFinded(user);
                }
            }
        });
        return null;
    }

    public UserEntity removeFriend(UserEntity user, final OnUserFindedListener listener) {
        String name = user.getUsername();
        findByUsername(name, new OnUserFindedListener() {
            @Override
            public void onUserFinded(UserEntity user) {
                if(user == null) {
                    if(listener != null) {
                        listener.onUserFinded(null);
                    }
                    return;
                }

                user.setFriendCount(user.getFriendCount() - 1);

                usersCollection.document(user.getId()).update("friends", user.getFriends());
                usersCollection.document(user.getId()).update("friendCount", user.getFriendCount());
                if(listener != null) {
                    listener.onUserFinded(user);
                }
            }
        });
        return null;
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

    public interface onFriendFindedListener {
        void onFriendFinded(FriendEntity friend);
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<FriendEntity> user);
    }
}
