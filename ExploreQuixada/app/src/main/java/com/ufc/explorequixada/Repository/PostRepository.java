package com.ufc.explorequixada.Repository;

import com.google.firebase.firestore.DocumentReference;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Interface.PostInterface;
import com.google.android.gms.tasks.OnCompleteListener;


public class PostRepository extends GenericRepository<PostEntity> implements PostInterface {
    public PostRepository(String collectionName) {
        super(collectionName);
    }

}
