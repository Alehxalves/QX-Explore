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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Activity.LoginActivity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.UserViewModel;

public class ProfileFragment extends Fragment {
    private UserDAO userDAO;
    private UserViewModel userViewModel;
    private FirebaseUser loggedUser;
    private Button btnLogout;
    private ImageView profileImage;
    private TextView username, postCount, friendCount;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        loggedUser = userViewModel.getUser();
        userDAO = new UserDAO();

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
        progressBar = view.findViewById(R.id.progressBar);
        setDetails();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    public void setDetails() {
        profileImage.setImageResource(R.drawable.user_profile);
        int totalPosts = getTotalPosts();
        postCount.setText("Posts: " + totalPosts);
        int totalFriends = getTotalFriends();
        friendCount.setText("Amigos: " + totalFriends);
        progressBar.setVisibility(View.VISIBLE);
        userDAO.findByEmail(loggedUser.getEmail(), new UserDAO.OnUserFindedListener() {
            @Override
            public void onUserFinded(UserEntity user) {
                if (user != null) {
                    username.setText(user.getUsername());
                    progressBar.setVisibility(View.GONE);
                } else {

                    Toast.makeText(getActivity(), "Ocorreu um erro inesperado.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
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