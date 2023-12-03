package com.ufc.explorequixada.Controller;

import android.view.View;
import android.widget.Toast;

import com.ufc.explorequixada.Activity.RegisterActivity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.Repository.PostDAO;

import java.util.List;

public class PostController {
    private PostDAO postDAO;
    private FeedFragment feedFragment;
    public PostController(FeedFragment feedFragment) {
        feedFragment = feedFragment;
        postDAO = new PostDAO();
    }

    public void newPost(PostEntity post){
        postDAO.newPost(post, isSuccess -> {
            if(isSuccess){
            }else{
                
            }
        });
    }

}

