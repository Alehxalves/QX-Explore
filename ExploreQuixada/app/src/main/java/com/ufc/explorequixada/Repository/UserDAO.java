package com.ufc.explorequixada.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.UserInterface;

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

    public void getAllUsers(final OnUsersLoadedListener listener) {
        usersCollection.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value, com.google.firebase.firestore.FirebaseFirestoreException error) {
                if(error != null){
                    if (listener != null) {
                        listener.onUsersLoaded(null);
                    }
                    return;
                }

                List<UserEntity> users = value.toObjects(UserEntity.class);
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        users.add(dc.getDocument().toObject(UserEntity.class));
                    }
                }

                if (listener != null) {
                    listener.onUsersLoaded(users);
                }
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
