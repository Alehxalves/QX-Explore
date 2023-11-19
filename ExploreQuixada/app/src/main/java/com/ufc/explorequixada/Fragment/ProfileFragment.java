package com.ufc.explorequixada.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Activity.LoginActivity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Utils.UserViewModel;

public class ProfileFragment extends Fragment {
    private UserViewModel userViewModel;
    private FirebaseUser user;
    private Button btnLogout;
    private ImageView profileImage;
    private TextView username, postCount, friendCount, title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = userViewModel.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        username = view.findViewById(R.id.username);
        postCount = view.findViewById(R.id.postCount);
        friendCount = view.findViewById(R.id.friendCount);
        btnLogout = view.findViewById(R.id.btnLogout);

        profileImage.setImageResource(R.drawable.user_profile);
        username.setText("Alex Alves");
        int totalPosts = getTotalPosts();
        postCount.setText("Posts: " + totalPosts);
        int totalFriends = getTotalFriends();
        friendCount.setText("Amigos: " + totalFriends);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    private int getTotalPosts() {
        return 10;
    }

    private int getTotalFriends() {
        return 20;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        requireActivity().finish();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }
}