package edu.neu.madcourse.team_j_sport.CommentList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostHolder;
import edu.neu.madcourse.team_j_sport.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private ArrayList<ItemComment> comments;
    private final Context mContext;

    public CommentAdapter(ArrayList<ItemComment> comments, Context mContext) {
        this.comments = comments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        ItemComment currentComment = comments.get(position);

        //TODO: init the avatar
        holder.ivAvatar.setImageResource(R.drawable.avatar_default);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("avatars/" + currentComment.getAvatar() +".jpg");
        Glide.with(mContext /* context */)
                .load(storageReference)
                .into(holder.ivAvatar);

        holder.tvUsername.setText(currentComment.getUsername());
        holder.tvTime.setText(currentComment.getTime());
        holder.tvContent.setText(currentComment.getContent());
        holder.setCommentKey(currentComment.getCommentId());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}
