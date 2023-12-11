package com.ufc.explorequixada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FollowerDAO;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.UserDAO;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

	UserEntity user;
	Context context;
	ArrayList<UserEntity> users;

	public UserAdapter(Context context, ArrayList<UserEntity> users, UserEntity user) {
		this.context = context;
		this.users = users;
		this.user = user;
	}

	@NonNull
	@Override
	public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(this.context).inflate(R.layout.user_layout, parent, false);
		return new MyViewHolder(view, this.users,this.user);
	}

	@Override
	public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
		UserEntity currentFollow = this.users.get(position);
		holder.setCurrentFollow(currentFollow);
		if(currentFollow != null && currentFollow.getUsername().toUpperCase() != user.getUsername().toUpperCase()) {
			holder.username.setText(currentFollow.getUsername());
			holder.email.setText(currentFollow.getEmail());
			holder.btnDeleteFriend.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		if(users != null) {
			return this.users.size();
		}else {
			return 0;
		}
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder{
		UserDAO userDAO;
		FollowerDAO followerDAO;
		TextView username, email;
		ImageView profileImage;
		FloatingActionButton btnAddFriend;
		FloatingActionButton btnDeleteFriend;
		ArrayList<FollowerEntity> friends;
		UserEntity currentUser;
		UserEntity currentFollow;
		ArrayList<UserEntity> users;

		public MyViewHolder(@NonNull View itemView, ArrayList<UserEntity> users, UserEntity user) {
			super(itemView);
			this.users = users;
			username = itemView.findViewById(R.id.username);
			email = itemView.findViewById(R.id.email);
			profileImage = itemView.findViewById(R.id.profileImg);
			btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
			btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);

			friends = new ArrayList<FollowerEntity>();
			userDAO = new UserDAO();
			followerDAO = new FollowerDAO();

			currentUser = user;
			currentFollow = getCurrentFollow();
			btnAddFriend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					addFriend();
				}
			});
		}

		public void addFriend() {
			FollowerEntity followerEntity = new FollowerEntity();
			followerEntity.setFollowerUsername(this.currentUser.getUsername());
			followerEntity.setFollowingUsername(this.currentFollow.getUsername());

			followerDAO.newFollower(followerEntity, new FollowerDAO.OnFollowerCreatedListener() {
				@Override
				public void onFollowerCreated(boolean isSuccess) {
					if(isSuccess) {
						Toast.makeText(itemView.getContext(), "Seguindo " + followerEntity.getFollowingUsername(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(itemView.getContext(), "Erro inesperado.", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		public void setCurrentFollow(UserEntity followerEntity) {
			this.currentFollow = followerEntity;
		}
		private UserEntity getCurrentFollow() {
			return this.currentFollow;
		}
	}
}
