package com.ufc.explorequixada.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ufc.explorequixada.Activity.RegisterActivity;
import com.ufc.explorequixada.R;

public class fragment_friend_add extends Fragment {

	private Button btnAddFriend;
	private Button btnReturnFriendList;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		btnReturnFriendList = view.findViewById(R.id.btnReturn);

		btnReturnFriendList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnFriend(new FriendListFragment());
			}
		});

		return view;
	}

	public void returnFriend(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}

}