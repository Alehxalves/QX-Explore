package com.ufc.explorequixada.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ufc.explorequixada.Entity.FollowerEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.FollowerDAO;
import com.ufc.explorequixada.Repository.UserDAO;

import java.util.ArrayList;
public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.MyViewHolder>{

    UserEntity user;
    Context context;
    ArrayList<FollowerEntity> followers;

    public FollowingAdapter(Context context, ArrayList<FollowerEntity> followers, UserEntity user) {
        this.context = context;
        this.followers = followers;
        this.user = user;
    }

    @NonNull
    @Override
    public FollowingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.user_layout, parent, false);
        return new MyViewHolder(view, this.followers,this.user, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.MyViewHolder holder, int position) {
        FollowerEntity currentFollow = this.followers.get(position);
        holder.setCurrentFollow(currentFollow);
        if(currentFollow != null) {
            holder.username.setText(currentFollow.getFollowingUsername());
            holder.email.setVisibility(View.GONE);
            holder.btnAddFriend.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(followers != null) {
            return this.followers.size();
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
        FollowerEntity currentFollow;
        FollowingAdapter adapter;

        public MyViewHolder(@NonNull View itemView, ArrayList<FollowerEntity> followers, UserEntity user, FollowingAdapter adapter) {
            super(itemView);
            this.friends = followers;
            this.adapter = adapter;
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            profileImage = itemView.findViewById(R.id.profileImg);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
            btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);

            userDAO = new UserDAO();
            followerDAO = new FollowerDAO();

            currentUser = user;
            currentFollow = getCurrentFollow();

            btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirmação");
            builder.setMessage("Tem certeza de que deseja para de seguir este usuário?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    unfollow();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void unfollow() {
            followerDAO.unfollow(currentFollow.getId(), new FollowerDAO.OnFollowerDeletedListener() {
                @Override
                public void onFollowerDeleted(boolean isSuccess) {
                    if (isSuccess) {
                        Toast.makeText(itemView.getContext(), "Você deixou de seguir " + currentFollow.getFollowingUsername(), Toast.LENGTH_SHORT).show();
                        friends.remove(currentFollow);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(itemView.getContext(), "Erro ao deletar postagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        public void setCurrentFollow(FollowerEntity followerEntity) {
            this.currentFollow = followerEntity;
        }
        private FollowerEntity getCurrentFollow() {
            return this.currentFollow;
        }
    }
}