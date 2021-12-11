package edu.neu.madcourse.team_j_sport.CommentList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    public static final String TAG = "CommentAdapter";

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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("avatars/" + currentComment.getAvatar() + ".jpg");
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

    private String getTime(String currentTime) {
        //String commentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date commentTime = simpleDateFormat.parse(currentTime, new ParsePosition(0));

        long between = (new Date().getTime() - commentTime.getTime()) / 1000;

        // s
        if (between < 60) {
            return between + "s ago";
        }

        // min
        between /= 60;
        if (between < 60) {
            return between + "mins ago";
        }

        // hour
        between /= 60;
        if (between < 24) {
            return between + "hours ago";
        }

        //day
        between /= 24;
        return between + "days ago";
    }

    private void removeCommentById(String cKey) {
        Log.d(TAG, "old comments size: " + comments.size());
        List<ItemComment> newComments = new ArrayList<>();
        for (ItemComment ic: comments) {
            if (ic.getCommentId().equals(cKey))
                continue;
            newComments.add(new ItemComment(ic));
        }
        comments = (ArrayList)newComments;

        Log.d(TAG, "new comments size: " + comments.size());
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public static final String CH_TAG = "sub-CommentHolder";
        public static final String COMMENT_KEY = "comment key";

        public ImageView ivAvatar;
        public TextView tvUsername;
        public TextView tvTime;
        public TextView tvContent;
        public Button btnDelete;

        private String postKey;
        private String commentKey;

        public CommentHolder(@NonNull View itemView, Context mContext, DatabaseReference mRef, String postKey) {
            super(itemView);
            this.postKey = postKey;
            ivAvatar = itemView.findViewById(R.id.iv_comment_avatar);
            tvUsername = itemView.findViewById(R.id.tv_comment_username);
            tvTime = itemView.findViewById(R.id.tv_comment_time);
            tvContent = itemView.findViewById(R.id.tv_comment_content);
            btnDelete = itemView.findViewById(R.id.btn_comment_delete);

            mRef.child("Posts").child(postKey).child("uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SharedPreferences sharedPreferences = mContext.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String userId = sharedPreferences.getString(MainActivity.USER_ID_KEY, "");
                    if (userId.equals(snapshot.getValue())) {
                        btnDelete.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });

            btnDelete.setOnClickListener(
                    view -> {
                        Log.d(CH_TAG, "Removing comment-" + commentKey);

                        // Delete the event
                        mRef.child("Posts")
                                .child(postKey)
                                .child("comments")
                                .child(commentKey)
                                .removeValue();

                        // Update the comment list and refresh recyclerView
                        removeCommentById(commentKey);
                        notifyDataSetChanged();
                    });
        }

        public void setCommentKey(String commentKey) {
            this.commentKey = commentKey;
        }
    }
}
