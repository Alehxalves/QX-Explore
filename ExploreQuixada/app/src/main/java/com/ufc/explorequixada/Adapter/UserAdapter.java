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
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FriendDAO;
import com.ufc.explorequixada.Repository.UserDAO;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

	UserEntity user;
	Context context;
	ArrayList<FriendEntity> friends;

	public UserAdapter(Context context, ArrayList<FriendEntity> friends, UserEntity user) {
		this.context = context;
		this.friends = friends;
		this.user = user;
	}

	@NonNull
	@Override
	public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(this.context).inflate(R.layout.user_layout, parent, false);
		return new MyViewHolder(view, this.user);
	}

	@Override
	public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
		FriendEntity currentFriend = this.friends.get(position);
		holder.setCurrentFriend(currentFriend);
		if(currentFriend != null) {
			holder.username.setText(currentFriend.getName());
			holder.btnAddFriend.setVisibility(View.GONE);
			holder.btnDeleteFriend.setVisibility(View.VISIBLE);
		} else {
			holder.username.setText(currentFriend.getName());
			holder.btnAddFriend.setVisibility(View.VISIBLE);
			holder.btnDeleteFriend.setVisibility(View.GONE);
		}

		holder.getFriends();

		holder.userDAO = new UserDAO();
		holder.userDAO.findByUsername(user.getUsername(), userEntity -> {
			if(userEntity != null) {
				holder.username.setText(userEntity.getUsername());
			}
		});
	}

	@Override
	public int getItemCount() {
		return friends.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder{
		UserDAO userDAO;
		FriendDAO friendDAO;
		TextView username, email;
		ImageView profileImage;

		Button btnAddFriend;
		Button btnDeleteFriend;

		ArrayList<FriendEntity> friends;

		UserEntity userCurrent;
		FriendEntity friendCurrent;


		public MyViewHolder(@NonNull View itemView, UserEntity user) {
			super(itemView);

			username = itemView.findViewById(R.id.username);
			email = itemView.findViewById(R.id.email);
			profileImage = itemView.findViewById(R.id.profileImg);
			btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
			btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);

			friends = new ArrayList<FriendEntity>();
			userDAO = new UserDAO();
			friendDAO = new FriendDAO();

			userCurrent = user;
			friendCurrent = getCurrentFriend();

			btnAddFriend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					addFriend(username.getText().toString());
				}
			});

			btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteFriend(username.getText().toString());
				}
			});
		}

		public void getFriends() {
			if(userCurrent.getFriends() != null) {
				for(UserEntity friend : userCurrent.getFriends()) {
					String friendName = friend.getUsername();
					userDAO.orderByUsername(friendName, new UserDAO.onFriendFindedListener() {
						@Override
						public void onFriendFinded(FriendEntity friend) {
							if(friend != null) {
								friends.clear();
								friends.add(friend);
							}
						}
					});
				}
			} else {
				friends.clear();
			}
		}

		public void addFriend(String username) {
			UserEntity friend = new UserEntity();
			friend.setUsername(username);

			friendDAO.addFriend(friend, new FriendDAO.OnFriendAddedListener() {
				@Override
				public void onFriendAdded(boolean success) {
					if(success) {
						Toast.makeText(itemView.getContext(), "Amigo adicionado", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		private void deleteFriend(String toString) {
			String friend = toString;

			friendDAO.removeFriend(friend, new FriendDAO.OnFriendRemovedListener() {
				@Override
				public void onFriendRemoved(boolean isRemoved) {
					if(isRemoved) {
						Toast.makeText(itemView.getContext(), "Amigo removido", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		public void setCurrentFriend(FriendEntity friend) {
			friendCurrent = friend;
		}
		private FriendEntity getCurrentFriend() {
			return friendCurrent;
		}
	}
}
