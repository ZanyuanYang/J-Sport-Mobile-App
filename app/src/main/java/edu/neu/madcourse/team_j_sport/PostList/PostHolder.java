package edu.neu.madcourse.team_j_sport.PostList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.CommentList.PostDetailActivity;
import edu.neu.madcourse.team_j_sport.R;

public class PostHolder extends RecyclerView.ViewHolder {

    public static final String POST_KEY = "post key";

    public ImageView ivAvatar;
    public TextView tvPostTitle;
    public TextView tvPostContent;

    private String postKey;

    public PostHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        ivAvatar = itemView.findViewById(R.id.iv_post_avatar);
        tvPostTitle = itemView.findViewById(R.id.tv_post_title);
        tvPostContent = itemView.findViewById(R.id.tv_post_content);

        itemView.setOnClickListener(
                view -> {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(POST_KEY, postKey);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                });
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
