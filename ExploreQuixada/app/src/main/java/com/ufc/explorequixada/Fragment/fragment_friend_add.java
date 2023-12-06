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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.explorequixada.Adapter.UserAdapter;
import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class fragment_friend_add extends Fragment {

	private Button searchFriend;
	private Button btnReturnFriendList;
	private EditText txtFriendAdded;

	private FirebaseFirestore reference;

	private ArrayList<UserEntity> friends;
	private UserEntity user;
	private CurrentUserViewModel currentUser;
	private FriendDAO friendDAO;
	private RecyclerView friendFindResult;
	private UserAdapter userAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

		user = new UserEntity();
		friendDAO = new FriendDAO();

		userAdapter = new UserAdapter(getContext(), friends, user);
		friends = new ArrayList<UserEntity>();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		reference = FirebaseFirestore.getInstance();

		btnReturnFriendList = view.findViewById(R.id.btnReturn);
		txtFriendAdded = view.findViewById(R.id.userName);
		searchFriend = view.findViewById(R.id.btnFindFriend);
		friendFindResult = view.findViewById(R.id.SearchFriendResult);

		friendFindResult.setLayoutManager(new LinearLayoutManager(getContext()));
		friendFindResult.hasFixedSize();
		friendFindResult.setAdapter(userAdapter);

		searchFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String friend = txtFriendAdded.getText().toString();
				if(friend.isEmpty()) {
					txtFriendAdded.setError("Campo vazio");
					return;
				} else {
					findFriend(friend);
				}
			}
		});

		btnReturnFriendList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnFriend(new FriendListFragment());
			}
		});

		return view;
	}

	private void findFriend(String friend) {
		friendDAO.findFriends(friend, new FriendDAO.OnFriendFindedListener() {
			@Override
			public void onFriendFinded(List<FriendEntity> friends) {
				if(friends != null && !friends.isEmpty()) {
					friends.clear();
					friends.addAll(friends);
					userAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "Nenhum amigo encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void returnFriend(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}

}