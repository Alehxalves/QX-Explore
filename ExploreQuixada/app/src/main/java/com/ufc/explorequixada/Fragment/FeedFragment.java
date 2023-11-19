package com.ufc.explorequixada.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Controller.PostController;
import com.ufc.explorequixada.Controller.UserController;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Utils.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    private UserViewModel userViewModel;
    private UserController userController;
    private PostController postController;
    private FirebaseUser user;
    private EditText editTextPost;
    private Button bntPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = userViewModel.getUser();
        userController = new UserController();
        postController = new PostController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        editTextPost = view.findViewById(R.id.editTextPost);
        bntPost = view.findViewById(R.id.bntPost);


        bntPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        return view;
    }

    public void post() {
//        //UserEntity user2 = userController.getUserByEmail(user.getEmail());
//        PostEntity post = new PostEntity();
//
//        post.setTitle("Teste");
//        post.setContent(editTextPost.getText().toString());
//        post.setUserId(user2.getId());
//
//        postController.save(post);
    }
}