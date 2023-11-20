package com.ufc.explorequixada.Interface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Repository.UserDAO;

public interface PostInterface{

    void newPost(PostEntity post, final PostDAO.OnPostCreatedListener listener);

}
