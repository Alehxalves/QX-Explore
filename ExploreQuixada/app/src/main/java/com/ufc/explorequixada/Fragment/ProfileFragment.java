package com.ufc.explorequixada.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ufc.explorequixada.Activity.LoginActivity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;
import com.ufc.explorequixada.Utils.UserViewModel;

import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    CurrentUserViewModel currentUser;

    UserEntity user;
    UserDAO userDAO;
    UserViewModel userViewModel;
    FirebaseUser loggedUser;
    //private DatabaseReference userRef;
    FirebaseFirestore userRef;
    FirebaseAuth mAuth;
    Button btnLogout;
    Button btnEditProfile;
    ImageView profileImage;
    TextView username, postCount, friendCount;
    ProgressBar progressBar;

    PostDAO postDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

        user = currentUser.getUser();
        loggedUser = userViewModel.getUser();

        userDAO = new UserDAO();
        postDAO = new PostDAO();

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.setProfileImage);
        username = view.findViewById(R.id.username);
        postCount = view.findViewById(R.id.postCount);
        friendCount = view.findViewById(R.id.friendCount);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        progressBar = view.findViewById(R.id.progressBar);
        setDetails();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(new SettingsFragment());
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    private void editProfile(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void setDetails() {
        progressBar.setVisibility(View.VISIBLE);
        profileImage.setImageResource(R.drawable.user_profile);
        getTotalPosts();
        int totalFriends = getTotalFriends();
        friendCount.setText("Amigos: " + totalFriends);
        username.setText(user.getUsername());
        progressBar.setVisibility(View.GONE);
    }

    private void getTotalPosts() {
        postDAO.getPostCountByUserName(user.getUsername(), count -> {
            postCount.setText("Posts: " + count);
        });
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