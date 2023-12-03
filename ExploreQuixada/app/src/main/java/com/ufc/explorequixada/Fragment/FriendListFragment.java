package com.ufc.explorequixada.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ufc.explorequixada.Activity.RegisterActivity;
import com.ufc.explorequixada.Entity.AdapterView;
import com.ufc.explorequixada.Entity.AdapterView;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends Fragment {
    DatabaseReference reference;
    AdapterView adapter;
    List<UserEntity> users;

    private Button btnAddFriend;
    private Button btnSearch;
    private EditText searchInput;

    private RecyclerView searchResultList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        searchInput = view.findViewById(R.id.searchBar);
        searchResultList = view.findViewById(R.id.friend_search_list);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.hasFixedSize();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        users = new ArrayList<>();
        adapter = new AdapterView(getContext(), users);
        searchResultList.setAdapter(adapter);

        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(searchInput.getText().toString().equals("")) {
                    users.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserEntity user = dataSnapshot.getValue(UserEntity.class);
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchResult = searchInput.getText().toString();

                SearchFriends(searchResult);
            }
        });

        btnAddFriend = view.findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addFriend(new fragment_friend_add());
            }
        });

        /*searchResultList = (RecyclerView) view.findViewById(R.id.friend_search_list);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.setAdapter(new AdapterView(getContext(), new ArrayList<UserEntity>()));

        searchInput = view.findViewById(R.id.searchBar);*/

        return view;
    }

    private void SearchFriends(String searchResult) {

        for(UserEntity user : users) {
            if(user.getUsername().equals(searchResult)) {
                users.clear();
                users.add(user);
                adapter.notifyDataSetChanged();
            }
        }



    }


    public void addFriend(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}