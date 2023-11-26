package com.ufc.explorequixada.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.Fragment.HomeFragment;
import com.ufc.explorequixada.Fragment.ProfileFragment;
import com.ufc.explorequixada.Fragment.FriendListFragment;
import com.ufc.explorequixada.Fragment.SettingsFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.UserViewModel;
import com.ufc.explorequixada.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    UserDAO userDAO;
    FirebaseAuth auth;
    UserViewModel userViewModel;
    ActivityMainBinding binding;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDAO = new UserDAO();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        auth = FirebaseAuth.getInstance();
        userRef = (FirebaseDatabase.getInstance().getReference().child("Users"));
        FirebaseUser loggedUer = auth.getCurrentUser();

        if(loggedUer == null) {
            LoginStart();
        } else {
            CheckUserExist();
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUser(loggedUer);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.feed) {
                replaceFragment(new FeedFragment());
            } else if (itemId == R.id.friendList) {
                replaceFragment(new FriendListFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void CheckUserExist() {
        final String user_id = auth.getCurrentUser().getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)) {
                    sendToSetup();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    private void sendToSetup() {
        replaceFragment(new SettingsFragment());
    }

    private void LoginStart() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}