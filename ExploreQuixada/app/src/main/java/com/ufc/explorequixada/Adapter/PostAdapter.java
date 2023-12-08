package com.ufc.explorequixada.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.auth.User;
import com.ufc.explorequixada.Activity.MainActivity;
import com.ufc.explorequixada.Entity.CommentEntity;
import com.ufc.explorequixada.Entity.PostEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.FeedFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.CommentDAO;
import com.ufc.explorequixada.Repository.PostDAO;
import com.ufc.explorequixada.Utils.CurrentUserViewModel;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    UserEntity user;
    Context context;
    ArrayList<PostEntity> posts;

    public PostAdapter(Context context, ArrayList<PostEntity> posts, UserEntity user) {
        this.context = context;
        this.posts = posts;
        this.user = user;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.post_item, parent, false);
        return new MyViewHolder(view, this.user, this.posts, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostEntity currentPost = this.posts.get(position);
        holder.setCurrentPost(currentPost);
        holder.getComments();

        if(currentPost.getUsername().equals(user.getUsername())) {
            holder.username.setText("Eu");
            holder.btnDeletePost.setVisibility(View.VISIBLE);
        }else {
            holder.btnDeletePost.setVisibility(View.GONE);
            holder.username.setText(currentPost.getUsername());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yy HH:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        String formattedDate = dateFormat.format(currentPost.getDate());
        holder.date.setText(formattedDate);
        holder.content.setText(currentPost.getContent());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        PostDAO postDAO;
        RecyclerView recyclerViewComment;
        CommentAdapter commentAdapter;
        CommentDAO commentDAO;
        Button btnComment;
        FloatingActionButton btnDeletePost;
        ArrayList<CommentEntity> comments;
        TextView username, date, content;
        EditText editTextComment;
        UserEntity currentUser;
        PostEntity currentPost;
        PostAdapter adapter;
        ArrayList<PostEntity> posts;
        public MyViewHolder(@NonNull View itemView, UserEntity user, ArrayList<PostEntity> posts, PostAdapter adapter) {
            super(itemView);
            this.posts = posts;
            this.adapter = adapter;
            postDAO = new PostDAO();
            commentDAO = new CommentDAO();
            comments = new ArrayList<CommentEntity>();
            currentUser = user;
            currentPost = getCurrentPost();

            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            editTextComment = itemView.findViewById(R.id.comment);
            btnComment = itemView.findViewById(R.id.btnComent);
            btnDeletePost = itemView.findViewById(R.id.btnDeletePost);

            recyclerViewComment = itemView.findViewById(R.id.recycleViewComments);
            recyclerViewComment.setHasFixedSize(true);
            recyclerViewComment.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            commentAdapter = new CommentAdapter(itemView.getContext(),comments, currentUser);
            recyclerViewComment.setAdapter(commentAdapter);

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment();
                }
            });

            btnDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });
        }

        public void getComments() {
            commentDAO.getAllCommentsForPost(currentPost.getId(), new CommentDAO.OnCommentsLoadedListener() {
                @Override
                public void onCommentsLoaded(List<CommentEntity> loadedComments) {
                    if (loadedComments != null && !loadedComments.isEmpty()) {
                        comments.clear();
                        comments.addAll(loadedComments);
                        commentAdapter.notifyDataSetChanged();
                    } else {
                    }
                }
            });
        }

        public void comment() {
            CommentEntity comment = new CommentEntity();
            comment.setPostId(currentPost.getId());
            comment.setContent(editTextComment.getText().toString());
            comment.setUsername(currentUser.getUsername());

            commentDAO.newComment(comment, isSuccess -> {
                if(isSuccess){
                    Toast.makeText(itemView.getContext(), "Comentário feito.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(itemView.getContext(), "Ocorreu um erro inesperado.", Toast.LENGTH_SHORT).show();
                }
            });
            editTextComment.setText(null);
            getComments();
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirmação");
            builder.setMessage("Tem certeza de que deseja deletar esta postagem?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletePost();
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

        private void deletePost() {
            postDAO.deletePostById(currentPost.getId(), new PostDAO.OnPostDeletedListener() {
                @Override
                public void onPostDeleted(boolean isSuccess) {
                    if (isSuccess) {
                        Toast.makeText(itemView.getContext(), "Postagem deletada", Toast.LENGTH_SHORT).show();
                        posts.remove(currentPost);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(itemView.getContext(), "Erro ao deletar postagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void setCurrentPost(PostEntity post) {
            currentPost = post;
        }
        public PostEntity getCurrentPost() {
            return currentPost;
        }
    }
}
