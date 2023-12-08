package com.ufc.explorequixada.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.CommentDAO;
import com.ufc.explorequixada.Repository.PostDAO;
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
        return new MyViewHolder(view,this.comments, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        CommentEntity comment = this.comments.get(position);
        holder.setCurrentComment(comment);

        if(user != null && comment.getUsername().equals(this.user.getUsername())) {
            holder.username.setText("Eu");
            holder.btnDeleteComment.setVisibility(View.VISIBLE);
        }else {
            holder.username.setText(comment.getUsername());
            holder.btnDeleteComment.setVisibility(View.GONE);
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
        CommentDAO commentDAO;
        CommentEntity currentComment;
        TextView username, date, commentContent;
        FloatingActionButton btnDeleteComment;

        CommentAdapter adapter;
        ArrayList<CommentEntity> comments;
        public MyViewHolder(@NonNull View itemView, ArrayList<CommentEntity> comments, CommentAdapter adapter) {
            super(itemView);
            this.comments = comments;
            this.adapter = adapter;
            commentDAO = new CommentDAO();
            currentComment = getCurrentComment();
            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.date);
            btnDeleteComment = itemView.findViewById(R.id.btnDeleteComment);
            commentContent = itemView.findViewById(R.id.commentContent);

            btnDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirmação");
            builder.setMessage("Tem certeza de que deseja deletar este comentário?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteComment();
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

        public void deleteComment() {
            commentDAO.deleteCommentById(currentComment.getId(), new CommentDAO.OnCommentDeletedListener() {
                @Override
                public void onCommentDeleted(boolean isSuccess) {
                    if(isSuccess) {
                        Toast.makeText(itemView.getContext(), "Comentário deletado.", Toast.LENGTH_SHORT).show();
                        comments.remove(currentComment);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(itemView.getContext(), "Erro ao deletar comentário.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void setCurrentComment(CommentEntity comment) {
            currentComment = comment;
        }
        public CommentEntity getCurrentComment() {
            return currentComment;
        }
    }
}
