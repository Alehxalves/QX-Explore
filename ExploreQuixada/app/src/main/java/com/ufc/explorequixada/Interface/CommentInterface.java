package com.ufc.explorequixada.Interface;

import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Repository.CommentDAO;
import com.ufc.explorequixada.Repository.PostDAO;

public interface CommentInterface {

    void newComment(CommentEntity post, final CommentDAO.OnCommentCreatedListener listener);
    void getAllCommentsForPost(String postId,final CommentDAO.OnCommentsLoadedListener listener);
    void deleteCommentById(String commentId, final CommentDAO.OnCommentDeletedListener listener);


}
