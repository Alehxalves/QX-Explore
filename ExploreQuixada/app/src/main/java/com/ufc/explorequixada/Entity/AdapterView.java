package com.ufc.explorequixada.Entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Utils.ViewHolder;

import java.util.List;

public class AdapterView extends RecyclerView.Adapter<AdapterView.MyViewHolder>{

	Context context;
	List<UserEntity> users;

	public AdapterView(Context context, List<UserEntity> users) {
		this.context = context;
		this.users = users;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

		UserEntity user = users.get(position);
		holder.username.setText(user.getUsername());

	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		TextView username;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			username = itemView.findViewById(R.id.username);
		}
	}

}
