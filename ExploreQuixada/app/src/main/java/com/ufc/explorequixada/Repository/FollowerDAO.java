package com.ufc.explorequixada.Repository;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.CommentInterface;
import com.ufc.explorequixada.Interface.FollowerInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FollowerDAO implements FollowerInterface {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference followersCollection = db.collection("followers");

    public void newFollower(FollowerEntity follower, OnFollowerCreatedListener listener) {
        TimeZone brazilTimeZone = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar calendar = Calendar.getInstance(brazilTimeZone);
        Date currentTime = calendar.getTime();

        follower.setDate(currentTime);

        DocumentReference newFollower = followersCollection.document();
        follower.setId(newFollower.getId());
        followersCollection
                .document(follower.getId()).set(follower)
                .addOnSuccessListener(documentReference -> {
                    if (listener != null) {
                        listener.onFollowerCreated(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowerCreated(false);
                    }
                });
    }

    public void getMyFollowers(String username,final FollowerDAO.OnFollowersLoadedListener listener) {
        followersCollection.whereEqualTo("followingUsername", username)
                .orderBy("followerUsername", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if (listener != null) {
                                listener.onFollowersLoaded(Collections.emptyList());
                            }
                            return;
                        }

                        List<FollowerEntity> followers = new ArrayList<>();
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                followers.add(dc.getDocument().toObject(FollowerEntity.class));
                            }
                        }

                        if (listener != null) {
                            listener.onFollowersLoaded(followers);
                        }
                    }
                });
    }

    public void getFollowing(String username,final FollowerDAO.OnFollowersLoadedListener listener) {
        followersCollection.whereEqualTo("followerUsername", username)
                .orderBy("followingUsername", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if (listener != null) {
                                listener.onFollowersLoaded(Collections.emptyList());
                            }
                            return;
                        }

                        List<FollowerEntity> followers = new ArrayList<>();
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                followers.add(dc.getDocument().toObject(FollowerEntity.class));
                            }
                        }

                        if (listener != null) {
                            listener.onFollowersLoaded(followers);
                        }
                    }
                });
    }

    public void findFollowingByUsername(String usernamePattern, final FollowerDAO.OnFollowersLoadedListener listener) {
        String endPattern = usernamePattern + "\uf8ff";

        followersCollection
                .orderBy("followingUsername")
                .startAt(usernamePattern)
                .endAt(endPattern)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FollowerEntity> users = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FollowerEntity user = documentSnapshot.toObject(FollowerEntity.class);
                        users.add(user);
                    }
                    if (listener != null) {
                        listener.onFollowersLoaded(users);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowersLoaded(Collections.emptyList());
                    }
                });
    }

    public void findFollowerByUsername(String usernamePattern, final FollowerDAO.OnFollowersLoadedListener listener) {
        String endPattern = usernamePattern + "\uf8ff";

        followersCollection
                .orderBy("followerUsername")
                .startAt(usernamePattern)
                .endAt(endPattern)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FollowerEntity> users = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FollowerEntity user = documentSnapshot.toObject(FollowerEntity.class);
                        users.add(user);
                    }
                    if (listener != null) {
                        listener.onFollowersLoaded(users);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowersLoaded(Collections.emptyList());
                    }
                });
    }


    public void unfollow(String followerId, final FollowerDAO.OnFollowerDeletedListener listener) {
        followersCollection.document(followerId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onFollowerDeleted(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowerDeleted(false);
                    }
                });
    }

    public void getFollowingCountByUserName(String username, final FollowerDAO.OnFollowersCountLoadedListener listener) {
        followersCollection
                .whereEqualTo("followerUsername", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (listener != null) {
                        listener.onFollowersCountLoaded(queryDocumentSnapshots.size());
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowersCountLoaded(0);
                    }
                });
    }

    public void getFollowersCountByUserName(String username, final FollowerDAO.OnFollowersCountLoadedListener listener) {
        followersCollection
                .whereEqualTo("followingUsername", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (listener != null) {
                        listener.onFollowersCountLoaded(queryDocumentSnapshots.size());
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFollowersCountLoaded(0);
                    }
                });
    }
    public interface OnFollowerCreatedListener {
        void onFollowerCreated(boolean isSuccess);
    }

    public interface OnFollowerDeletedListener {
        void onFollowerDeleted(boolean isSuccess);
    }

    public interface OnFollowersLoadedListener {
        void onFollowersLoaded(List<FollowerEntity> followers);
    }

    public interface OnFollowersCountLoadedListener {
        void onFollowersCountLoaded(int FollowersCount);
    }
}

