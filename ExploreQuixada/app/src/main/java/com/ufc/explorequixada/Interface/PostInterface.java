package com.ufc.explorequixada.Interface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Repository.UserDAO;

public interface PostInterface{

    void newPost(PostEntity post, final PostDAO.OnPostCreatedListener listener);

    void deletePostById(String postId, final PostDAO.OnPostDeletedListener listener);

    void getAllPosts(final PostDAO.OnPostsLoadedListener listener);

    void getPostCountByUserName(String username, final PostDAO.OnPostCountLoadedListener listener);
}
