package com.ufc.explorequixada.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.Fragment.FollowingAndFollowers;
import com.ufc.explorequixada.Fragment.HomeFragment;
import com.ufc.explorequixada.Fragment.ProfileFragment;
import com.ufc.explorequixada.Fragment.FriendListFragment;
import com.ufc.explorequixada.Fragment.SettingsFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;
import com.ufc.explorequixada.Utils.UserViewModel;
import com.ufc.explorequixada.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    UserDAO userDAO;
    FirebaseAuth auth;
    UserViewModel userViewModel;
    CurrentUserViewModel currentUserViewModel;
    ActivityMainBinding binding;
    private DatabaseReference userRef;
    UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDAO = new UserDAO();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FeedFragment());

        auth = FirebaseAuth.getInstance();
        userRef = (FirebaseDatabase.getInstance().getReference().child("Users"));
        FirebaseUser loggedUer = auth.getCurrentUser();

        if(loggedUer == null) {
            LoginStart();
        }


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        currentUserViewModel = new ViewModelProvider(this).get(CurrentUserViewModel.class);
        userViewModel.setUser(loggedUer);
        currentUser = new UserEntity();
        getCurrentUser();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.feed) {
                replaceFragment(new FeedFragment());
            } else if (itemId == R.id.friendList) {
                replaceFragment(new FollowingAndFollowers());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    public void getCurrentUser() {
        userDAO.findByEmail(userViewModel.getUser().getEmail(), new UserDAO.OnUserFindedListener() {
            @Override
            public void onUserFinded(UserEntity user) {
                if (user != null) {
                    currentUser.setEmail(user.getEmail());
                    currentUser.setUsername(user.getUsername());
                } else {
                    Toast.makeText(MainActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        currentUserViewModel.setUser(currentUser);
    }

    private void sendToSetup() {
        replaceFragment(new SettingsFragment());
    }

    private void LoginStart() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout() {
        auth.signOut();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}