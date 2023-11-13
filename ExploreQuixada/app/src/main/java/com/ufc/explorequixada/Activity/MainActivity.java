package com.ufc.explorequixada.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.Fragment.HomeFragment;
import com.ufc.explorequixada.Fragment.ProfileFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Utils.UserViewModel;
import com.ufc.explorequixada.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    UserViewModel userViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUser(user);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.feed) {
                replaceFragment(new FeedFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
