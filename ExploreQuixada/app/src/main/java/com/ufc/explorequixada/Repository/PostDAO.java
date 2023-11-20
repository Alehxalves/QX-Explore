package com.ufc.explorequixada.Repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Interface.PostInterface;


public class PostDAO implements PostInterface {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference postsCollection = db.collection("posts");

    @Override
    public void newPost(PostEntity post, final OnPostCreatedListener listener) {
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

    public interface OnPostCreatedListener {
        void onPostCreated(boolean isSuccess);
    }

}
