package com.ufc.explorequixada.Fragment;

import android.os.Bundle;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Adapter.FollowersAdapter;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FollowerDAO;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends Fragment {
    FirebaseFirestore reference;
    FollowerDAO followerDAO;
    ArrayList<FollowerEntity> userFriends;
    UserEntity user;
    FriendDAO friendDAO;
    UserDAO userDAO;
    Button btnAddFriend;
    Button btnSearch;
    EditText searchInput;
    RecyclerView searchResultList;
    FollowersAdapter followersAdapter;
    CurrentUserViewModel currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

        user = currentUser.getUser();
        friendDAO = new FriendDAO();
        userDAO = new UserDAO();
        followerDAO = new FollowerDAO();

        userFriends = new ArrayList<FollowerEntity>();
        followersAdapter = new FollowersAdapter(FriendListFragment.this.getContext(), userFriends, user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        reference = FirebaseFirestore.getInstance();
        btnSearch = view.findViewById(R.id.btnSearch);
        //btnAddFriend = view.findViewById(R.id.btnAddFriend);
        searchInput = view.findViewById(R.id.searchBar);
        searchResultList = view.findViewById(R.id.friendFindedResult);

        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        searchResultList.setAdapter(followersAdapter);
        getFriends();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFriends();
            }
        });

        return view;
    }

    private void getFriends() {
        followerDAO.getMyFollowers(user.getUsername(), new FollowerDAO.OnFollowersLoadedListener() {
            @Override
            public void onFollowersLoaded(List<FollowerEntity> followers) {
                if(followers != null && !followers.isEmpty()) {
                    userFriends.clear();
                    userFriends.addAll(followers);
                    followersAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void SearchFriends() {
        String followerUsername = searchInput.getText().toString();
        if (followerUsername.equals("") || followerUsername.equals(" ") || followerUsername == null) {
            getFriends();
        } else {
            followerDAO.findFollowerByUsername(followerUsername, new FollowerDAO.OnFollowersLoadedListener() {
                @Override
                public void onFollowersLoaded(List<FollowerEntity> followers) {
                    if (followers != null && !followers.isEmpty()) {
                        userFriends.clear();
                        userFriends.addAll(followers);
                        followersAdapter.notifyDataSetChanged();
                    } else {
                        userFriends.clear();
                        followersAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}