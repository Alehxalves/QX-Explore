package com.ufc.explorequixada.Repository;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Interface.CommentInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CommentDAO implements CommentInterface {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference commentsCollection = db.collection("comments");

    @Override
    public void newComment(CommentEntity comment, OnCommentCreatedListener listener) {
        TimeZone brazilTimeZone = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar calendar = Calendar.getInstance(brazilTimeZone);
        Date currentTime = calendar.getTime();

        comment.setDate(currentTime);

        DocumentReference newPost = commentsCollection.document();
        comment.setId(newPost.getId());
        commentsCollection
                .document(comment.getId()).set(comment)
                .addOnSuccessListener(documentReference -> {
                    if (listener != null) {
                        listener.onCommentCreated(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onCommentCreated(false);
                    }
                });
    }

    public void getAllCommentsForPost(String postId,final CommentDAO.OnCommentsLoadedListener listener) {
        commentsCollection.whereEqualTo("postId", postId)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if (listener != null) {
                                listener.onCommentsLoaded(Collections.emptyList());
                            }
                            return;
                        }

                        List<CommentEntity> comments = new ArrayList<>();
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                comments.add(dc.getDocument().toObject(CommentEntity.class));
                            }
                        }

                        if (listener != null) {
                            listener.onCommentsLoaded(comments);
                        }
                    }
                });
    }

    public interface OnCommentCreatedListener {
        void onCommentCreated(boolean isSuccess);
    }

    public interface OnCommentsLoadedListener {
        void onCommentsLoaded(List<CommentEntity> comments);
    }
}
