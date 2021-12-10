package edu.neu.madcourse.team_j_sport.ParticipantsList;

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

public class ParticipantAdapter extends RecyclerView.Adapter<PostHolder> {

    private final ArrayList<ItemParticipant> participants;
    private final Context mContext;

    public ParticipantAdapter(ArrayList<ItemParticipant> participant, Context mContext) {
        this.participants = participant;
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
        ItemParticipant currentPost = participants.get(position);
        
        holder.ivAvatar.setImageResource(R.drawable.avatar_default);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("avatars/" + currentPost.getUid() +".jpg");
        Glide.with(mContext /* context */)
                .load(storageReference)
                .into(holder.ivAvatar);

        holder.tvPostTitle.setText(currentPost.getUsername());
        holder.tvPostContent.setText(currentPost.getEmail());
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

}
