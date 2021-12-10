package edu.neu.madcourse.team_j_sport.PostList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import edu.neu.madcourse.team_j_sport.EventList.ItemEvent;
import edu.neu.madcourse.team_j_sport.R;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private ArrayList<ItemPost> postList;
    private final Context mContext;

    public PostAdapter(ArrayList<ItemPost> postList, Context mContext) {
        this.postList = postList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        ItemPost currentPost = postList.get(position);

        String content = currentPost.getContent();
        if(content.length() >= 32){
            content = content.substring(0,32) + "...";
        }

        //TODO: init the avatar
        holder.ivAvatar.setImageResource(R.drawable.avatar_default);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("avatars/" + currentPost.getAvatar() +".jpg");
        Glide.with(mContext /* context */)
                .load(storageReference)
                .into(holder.ivAvatar);

        holder.tvPostTitle.setText(currentPost.getTitle());
        holder.tvPostContent.setText(content);

        holder.setPostKey(currentPost.getPostId());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void filterList(ArrayList<ItemPost> filteredList){
        postList = filteredList;
        notifyDataSetChanged();
    }
}
