package com.ufc.explorequixada.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ufc.explorequixada.Adapter.PostAdapter;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    CurrentUserViewModel currentUser;
    PostDAO postDAO;
    UserEntity user;
    EditText editTextPost;
    Button bntPost;
    RecyclerView recycleViewPosts;
    ArrayList<PostEntity> posts;
    PostAdapter postAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

        user = currentUser.getUser();
        postDAO = new PostDAO();

        posts = new ArrayList<PostEntity>();
        postAdapter = new PostAdapter(FeedFragment.this.getContext(),posts, user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        bntPost = view.findViewById(R.id.bntPost);
        editTextPost = view.findViewById(R.id.postContent);
        recycleViewPosts = view.findViewById(R.id.recycleViewPosts);
        recycleViewPosts.setHasFixedSize(true);
        recycleViewPosts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recycleViewPosts.setAdapter(postAdapter);
        getPosts();
        bntPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        return view;
    }

    public void post() {
        PostEntity post = new PostEntity();

        post.setContent(editTextPost.getText().toString());
        post.setUsername(user.getUsername());

        postDAO.newPost(post, new PostDAO.OnPostCreatedListener() {
            @Override
            public void onPostCreated(boolean isSuccess) {

            }
        });

        editTextPost.setText(null);
        getPosts();
    }

    public void getPosts() {
        postDAO.getAllPosts(new PostDAO.OnPostsLoadedListener() {
            @Override
            public void onPostsLoaded(List<PostEntity> loadedPosts) {
                if (loadedPosts != null && !loadedPosts.isEmpty()) {
                    posts.clear();
                    posts.addAll(loadedPosts);
                    postAdapter.notifyDataSetChanged();
                } else {
                }
            }
        });
    }

}