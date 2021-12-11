package edu.neu.madcourse.team_j_sport.CommentList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostHolder;
import edu.neu.madcourse.team_j_sport.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private ArrayList<ItemComment> comments;
    private final Context mContext;
    private final DatabaseReference mRef;
    private final String postKey;

    public CommentAdapter(ArrayList<ItemComment> comments, Context mContext, DatabaseReference mRef, String postKey) {
        this.comments = comments;
        this.mContext = mContext;
        this.mRef = mRef;
        this.postKey = postKey;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view, mContext, mRef, postKey);
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

        // init the time
        holder.tvTime.setText(getTime(currentComment.getTime()));

        holder.tvContent.setText(currentComment.getContent());
        holder.setCommentKey(currentComment.getCommentId());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private String getTime(String currentTime){
        //String commentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date commentTime = simpleDateFormat.parse(currentTime, new ParsePosition(0));
        // s
        long between = (new Date().getTime() - commentTime.getTime())/1000;
        if(between < 60){
            return between + "s ago";
        }
        // min
        between /= 60;
        if(between < 60){
            return between + "mins ago";
        }
        // hour
        between /= 60;
        if(between < 24){
            return between + "hours ago";
        }
        //day
        between /= 24;
        return between + "days ago";
    }
}
