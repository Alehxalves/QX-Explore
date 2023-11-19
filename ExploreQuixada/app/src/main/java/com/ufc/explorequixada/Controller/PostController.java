package com.ufc.explorequixada.Controller;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Repository.PostRepository;
import com.ufc.explorequixada.Entity.UserEntity;
import androidx.annotation.NonNull; // Import necessário para NonNull

import com.google.android.gms.tasks.Task;


import java.util.Objects;

public class PostController {
    private PostRepository postRepository;

    public PostController() {
        postRepository = new PostRepository("posts");
    }

    public void save(PostEntity post) {
        this.postRepository.createDocument(post);
    }


}

