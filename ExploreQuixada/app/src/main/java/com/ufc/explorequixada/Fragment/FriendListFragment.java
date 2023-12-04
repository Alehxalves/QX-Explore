package com.ufc.explorequixada.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Activity.RegisterActivity;
import com.ufc.explorequixada.Adapter.PostAdapter;
import com.ufc.explorequixada.Adapter.UserAdapter;
import com.ufc.explorequixada.Entity.AdapterView;
import com.ufc.explorequixada.Entity.AdapterView;
import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends Fragment {
    FirebaseFirestore reference;
    private ArrayList<UserEntity> friends;
    private UserEntity user;

    private FriendDAO friendDAO;
    private Button btnAddFriend;
    private Button btnSearch;
    private EditText searchInput;

    private RecyclerView searchResultList;
    private UserAdapter userAdapter;
    private CurrentUserViewModel currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

        user = new UserEntity();
        friendDAO = new FriendDAO();

        userAdapter = new UserAdapter(getContext(), friends, user);
        friends = new ArrayList<UserEntity>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        searchInput = view.findViewById(R.id.searchBar);
        searchResultList = view.findViewById(R.id.friend_search_list);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.hasFixedSize();

        reference = FirebaseFirestore.getInstance();
        searchResultList.setAdapter(userAdapter);
        getFriends();


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
                addFriend(new fragment_friend_add());
            }
        });

        return view;
    }

    private void getFriends() {
        friendDAO.getAllFriends(new FriendDAO.OnFriendFindedListener() {
            @Override
            public void onFriendFinded(List<FriendEntity> friends) {
                if(friends != null && !friends.isEmpty()) {
                    friends.clear();
                    friends.addAll(friends);
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void SearchFriends(String searchResult) {
        friendDAO.getAllFriends(new FriendDAO.OnFriendFindedListener() {
            @Override
            public void onFriendFinded(List<FriendEntity> friends) {
                if(friends != null && !friends.isEmpty() && friends.contains(searchResult)) {
                    friends.clear();
                    friends.addAll(friends);
                    userAdapter.notifyDataSetChanged();
                } else {
                    friends.clear();
                    Toast.makeText(getContext(), "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void addFriend(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}