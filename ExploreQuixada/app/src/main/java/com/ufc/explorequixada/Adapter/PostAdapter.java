package com.ufc.explorequixada.Adapter;

import android.content.Context;
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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostEntity currentPost = this.posts.get(position);
        holder.setCurrentUser(this.user);
        holder.setCurrentPost(currentPost);

        if(currentPost.getUsername().equals(user.getUsername())) {
            holder.btnDeletePost.setVisibility(View.VISIBLE);
            holder.username.setText("Eu");
        }else {
            holder.btnDeletePost.setVisibility(View.GONE);
            holder.username.setText(currentPost.getUsername());
        }
        holder.getComments();

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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDAO = new PostDAO();
            commentDAO = new CommentDAO();
            comments = new ArrayList<CommentEntity>();
            currentUser = getCurrentUser();
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
                    deletePost();
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
                    Toast.makeText(itemView.getContext(), "Postagem feita", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(itemView.getContext(), "Ocorreu um erro inesperado.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void deletePost() {
            postDAO.deletePostById(currentPost.getId(), new PostDAO.OnPostDeletedListener() {
                @Override
                public void onPostDeleted(boolean isSuccess) {
                    if(isSuccess) {
                        Toast.makeText(itemView.getContext(), "Postagem deletada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Erro ao deletar postagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        public void setCurrentUser(UserEntity user) {
            currentUser = user;
        }
        public UserEntity getCurrentUser() {
            return currentUser;
        }

        public void setCurrentPost(PostEntity post) {
            currentPost = post;
        }
        public PostEntity getCurrentPost() {
            return currentPost;
        }
    }
}
