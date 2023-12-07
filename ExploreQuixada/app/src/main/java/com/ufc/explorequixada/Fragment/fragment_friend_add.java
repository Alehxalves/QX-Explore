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
import com.ufc.explorequixada.Controller.UserController;
import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.UserDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class fragment_friend_add extends Fragment {

	private Button btnFindFriend;
	private Button btnReturnFriendList;
	private EditText txtFriendAdded;

	private FirebaseFirestore reference;

	private ArrayList<FriendEntity> userFriends;
	private UserEntity user;
	private CurrentUserViewModel currentUser;
	private FriendDAO friendDAO;
	private UserDAO userDAO;
	private RecyclerView friendFindResult;
	private UserAdapter userAdapter;
	private UserController userController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

		user = currentUser.getUser();
		friendDAO = new FriendDAO();
		userDAO = new UserDAO();

		userController = new UserController(this);
		userFriends = new ArrayList<FriendEntity>();
		userAdapter = new UserAdapter(fragment_friend_add.this.getContext(), userFriends, user);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		reference = FirebaseFirestore.getInstance();

		btnFindFriend = view.findViewById(R.id.btnFindFriend);
		btnReturnFriendList = view.findViewById(R.id.btnReturn);
		txtFriendAdded = view.findViewById(R.id.userName);

		friendFindResult = view.findViewById(R.id.SearchFriendResult);

		friendFindResult.setHasFixedSize(true);
		friendFindResult.setLayoutManager(new LinearLayoutManager(getContext()));

		friendFindResult.setAdapter(userAdapter);

		btnFindFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String friendName = txtFriendAdded.getText().toString();
				if(friendName.isEmpty()) {
					Toast.makeText(getContext(), "Digite o nome do amigo", Toast.LENGTH_SHORT).show();
					return;
				} else {
					findFriend(friendName);
				}
			}

			});

		btnReturnFriendList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				returnFriendList(new FriendListFragment());
			}
		});

		return view;
	}

	private void findFriend(String friendName) {
		userDAO.findByUsername(friendName, new UserDAO.OnUserFindedListener() {
			@Override
			public void onUserFinded(UserEntity user) {
				if(user != null) {
					orderUsers(user);
				} else {
					Toast.makeText(getContext(), "Amigo não encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void orderUsers(UserEntity user) {
		userDAO.orderByUsername(user.getUsername(), new UserDAO.onFriendFindedListener() {
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

	private void returnFriendList(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}
}