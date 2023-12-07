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
import com.ufc.explorequixada.Adapter.UserAdapter;
import com.ufc.explorequixada.Controller.UserController;
import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends Fragment {
    FirebaseFirestore reference;
    private ArrayList<FriendEntity> userFriends;
    private ArrayList<UserEntity> users;
    private UserEntity user;

    private FriendDAO friendDAO;
    private UserDAO userDAO;
    private Button btnAddFriend;
    private Button btnSearch;
    private EditText searchInput;

    private RecyclerView searchResultList;
    private UserAdapter userAdapter;
    private CurrentUserViewModel currentUser;
    private UserController userController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

        user = currentUser.getUser();
        friendDAO = new FriendDAO();
        userDAO = new UserDAO();

        userController = new UserController(this);
        userFriends = new ArrayList<FriendEntity>();
        userAdapter = new UserAdapter(getContext(), userFriends, user);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        reference = FirebaseFirestore.getInstance();

        searchInput = view.findViewById(R.id.searchBar);
        searchResultList = view.findViewById(R.id.friendFindedResult);

        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));

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
        if(user.getFriends() != null) {
            for(UserEntity friend : user.getFriends()) {
                String friendName = friend.getUsername();
                userDAO.orderByUsername(friendName, new UserDAO.onFriendFindedListener() {
                    @Override
                    public void onFriendFinded(FriendEntity friend) {
                        if(friend != null) {
                            userFriends.clear();
                            userFriends.add(friend);
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        } else {
            userFriends.clear();
            userAdapter.notifyDataSetChanged();
        }
    }

    private void SearchFriends(String searchResult) {
        friendDAO.findFriends(searchResult, new FriendDAO.OnFriendFindedListener() {
            @Override
            public void onFriendFinded(List<FriendEntity> friends) {
                if(friends != null && !friends.isEmpty()) {
                    userFriends.clear();
                    userFriends.addAll(friends);
                    userAdapter.notifyDataSetChanged();
                } else {
                    userFriends.clear();
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