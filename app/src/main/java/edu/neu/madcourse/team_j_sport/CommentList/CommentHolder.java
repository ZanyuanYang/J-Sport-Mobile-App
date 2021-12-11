package edu.neu.madcourse.team_j_sport.CommentList;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.R;

public class CommentHolder extends RecyclerView.ViewHolder {

    public static final String COMMENT_KEY = "comment key";

    public ImageView ivAvatar;
    public TextView tvUsername;
    public TextView tvTime;
    public TextView tvContent;
    public Button btnDelete;

    private String commentKey;

    public CommentHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        ivAvatar = itemView.findViewById(R.id.iv_comment_avatar);
        tvUsername = itemView.findViewById(R.id.tv_comment_username);
        tvTime = itemView.findViewById(R.id.tv_comment_time);
        tvContent = itemView.findViewById(R.id.tv_comment_content);

//        btnDelete.setOnClickListener(
//                view -> {
//                    //TODO: delete the comment
//                });
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
