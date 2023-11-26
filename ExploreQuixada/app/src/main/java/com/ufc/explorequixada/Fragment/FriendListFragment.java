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

import com.ufc.explorequixada.Activity.RegisterActivity;
import com.ufc.explorequixada.Entity.AdapterView;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends Fragment {

    private Button btnAddFriend;
    private Button btnSearch;
    private SearchView searchView;
    private EditText searchInput;

    private RecyclerView searchResultList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = searchInput.getText().toString();

            }
        });

        btnAddFriend = view.findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(new fragment_friend_add());
            }
        });

        /*searchResultList = (RecyclerView) view.findViewById(R.id.friend_search_list);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.setAdapter(new AdapterView(getContext(), new ArrayList<UserEntity>()));

        searchInput = view.findViewById(R.id.searchBar);*/

        return view;
    }

    public void addFriend(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}