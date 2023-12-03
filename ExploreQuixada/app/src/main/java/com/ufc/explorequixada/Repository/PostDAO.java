package com.ufc.explorequixada.Repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.PostInterface;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class PostDAO implements PostInterface {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference postsCollection = db.collection("posts");

    @Override
    public void newPost(PostEntity post, final OnPostCreatedListener listener) {
        TimeZone brazilTimeZone = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar calendar = Calendar.getInstance(brazilTimeZone);
        Date currentTime = calendar.getTime();

        post.setDate(currentTime);

        DocumentReference newPost = postsCollection.document();
        post.setId(newPost.getId());
        postsCollection
                .document(post.getId()).set(post)
                .addOnSuccessListener(documentReference -> {
                    if (listener != null) {
                        listener.onPostCreated(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onPostCreated(false);
                    }
                });

    }

    public void deletePostById(String postId, final OnPostDeletedListener listener) {
        postsCollection.document(postId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onPostDeleted(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onPostDeleted(false);
                    }
                });
    }

    public void getAllPosts(final OnPostsLoadedListener listener) {
        postsCollection.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if (listener != null) {
                        listener.onPostsLoaded(Collections.emptyList());
                    }
                    return;
                }

                List<PostEntity> posts = new ArrayList<>();
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        posts.add(dc.getDocument().toObject(PostEntity.class));
                    }
                }

                if (listener != null) {
                    listener.onPostsLoaded(posts);
                }
            }
        });
    }
    public interface OnPostCreatedListener {
        void onPostCreated(boolean isSuccess);
    }

    public interface OnPostDeletedListener {
        void onPostDeleted(boolean isSuccess);
    }

    public interface OnPostsLoadedListener {
        void onPostsLoaded(List<PostEntity> posts);
    }

}
