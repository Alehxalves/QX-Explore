package com.ufc.explorequixada.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    UserEntity user;
    ArrayList<CommentEntity> comments;

    public CommentAdapter(Context context, ArrayList<CommentEntity> comments, UserEntity user){
        this.context = context;
        this.comments = comments;
        this.user = user;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.comment_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        CommentEntity comment = this.comments.get(position);

        if(comment.getUsername().equals(this.user.getUsername())) {
            holder.username.setText("Eu");
        }else {
            holder.username.setText(comment.getUsername());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yy HH:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        String formattedDate = dateFormat.format(comment.getDate());
        holder.date.setText(formattedDate);
        holder.commentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView username, date, commentContent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.date);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
