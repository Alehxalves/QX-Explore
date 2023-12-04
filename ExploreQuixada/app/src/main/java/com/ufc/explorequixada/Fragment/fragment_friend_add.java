package com.ufc.explorequixada.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

	private Button btnFriendAdded;
	private Button btnReturnFriendList;
	private TextInputEditText txtFriendAdded;

	private FirebaseFirestore reference;

	private ArrayList<UserEntity> friends;
	private UserEntity user;
	private CurrentUserViewModel currentUser;
	private FriendDAO friendDAO;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentUser = new ViewModelProvider(requireActivity()).get(CurrentUserViewModel.class);

		user = new UserEntity();
		friendDAO = new FriendDAO();

		friends = new ArrayList<UserEntity>();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		reference = FirebaseFirestore.getInstance();

		btnReturnFriendList = view.findViewById(R.id.btnReturn);
		btnFriendAdded = view.findViewById(R.id.btnConfirmarAddFriend);
		txtFriendAdded = view.findViewById(R.id.userName);

		btnFriendAdded.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				friendAdd(txtFriendAdded.getText().toString());
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

	private void friendAdd(String toString) {
		String friend = toString;

		UserEntity friendUser = referenceName(friend);

		friendDAO.addFriend(friendUser, new FriendDAO.OnFriendAddedListener() {
			@Override
			public void onFriendAdded(boolean isAdded) {
				if(isAdded) {
					Toast.makeText(getContext(), "Amigo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
					returnFriend(new FriendListFragment());
				} else {
					Toast.makeText(getContext(), "Erro ao adicionar amigo!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private UserEntity referenceName(String friend) {
		reference.collection("users").whereEqualTo("username", friend).get().addOnSuccessListener(queryDocumentSnapshots -> {
			if(!queryDocumentSnapshots.isEmpty()) {
				for(UserEntity documentSnapshot : queryDocumentSnapshots.toObjects(UserEntity.class)) {
					FriendEntity friendEntity = new FriendEntity();
					friendEntity.setName(documentSnapshot.getUsername());
					friendEntity.setId(documentSnapshot.getId());
					return;
				}
			}
		});
		return null;
	}

	public void returnFriend(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}

}