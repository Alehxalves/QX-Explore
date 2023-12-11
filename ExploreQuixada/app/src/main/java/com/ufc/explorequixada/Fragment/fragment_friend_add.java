package com.ufc.explorequixada.Fragment;

import android.os.Bundle;

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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Adapter.PostAdapter;
import com.ufc.explorequixada.Adapter.UserAdapter;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FollowerDAO;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class fragment_friend_add extends Fragment {

	CurrentUserViewModel currentUser;
	UserEntity user;
	FollowerDAO followerDAO;
	UserDAO userDAO;
	EditText editTextUserName;
	Button btnFindFriend, btnReturn;
	RecyclerView recycleViewFriends;
	ArrayList<UserEntity> users;
	UserAdapter userAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

		user = currentUser.getUser();
		userDAO = new UserDAO();
		followerDAO = new FollowerDAO();

		users = new ArrayList<UserEntity>();
		userAdapter = new UserAdapter(fragment_friend_add.this.getContext(),users, user);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		btnFindFriend = view.findViewById(R.id.btnFindFriend);
		btnReturn = view.findViewById(R.id.btnReturn);
		editTextUserName = view.findViewById(R.id.userName);
		recycleViewFriends = view.findViewById(R.id.SearchFriendResult);
		recycleViewFriends.setHasFixedSize(true);
		recycleViewFriends.setLayoutManager(new LinearLayoutManager(this.getContext()));

		recycleViewFriends.setAdapter(userAdapter);
		btnFindFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
			}
		});
		btnReturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				voltar(new FollowingFragment());
			}
		});
		return view;
	}
	public void search() {
		String username = editTextUserName.getText().toString();
		userDAO.findUsersByUsername(username, new UserDAO.OnUsersLoadedListener() {
			@Override
			public void onUsersLoaded(List<UserEntity> loadedUsers) {
				users.clear();
				users.addAll(loadedUsers);
				userAdapter.notifyDataSetChanged();
			}
		});
		editTextUserName.setText(null);
	}

	public void voltar(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}

}