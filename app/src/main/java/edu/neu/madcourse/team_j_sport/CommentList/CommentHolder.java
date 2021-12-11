package edu.neu.madcourse.team_j_sport.CommentList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class CommentHolder extends RecyclerView.ViewHolder {

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
                if(userId.equals(snapshot.getValue())){
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnDelete.setOnClickListener(
                view -> {
                    // delete the event
                    mRef.child("Posts")
                            .child(postKey)
                            .child("comments")
                            .child(commentKey)
                            .removeValue();
                });
    }


    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

}
