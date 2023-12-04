package com.ufc.explorequixada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
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
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
		UserEntity user = this.users.get(position);
		holder.userDAO = new UserDAO();
		holder.userDAO.findByUsername(user.getUsername(), userEntity -> {
			if(userEntity != null) {
				holder.username.setText(userEntity.getUsername());
			}
		});
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder{
		UserDAO userDAO;
		TextView username;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			username = itemView.findViewById(R.id.username);
		}
	}
}
