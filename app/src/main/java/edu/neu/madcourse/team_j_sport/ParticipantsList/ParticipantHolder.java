package edu.neu.madcourse.team_j_sport.ParticipantsList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.R;

public class ParticipantHolder extends RecyclerView.ViewHolder {

    public ImageView ivAvatar;
    public TextView tvUsername;
    public TextView tvEmail;

    public ParticipantHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        ivAvatar = itemView.findViewById(R.id.iv_participant_avatar);
        tvUsername = itemView.findViewById(R.id.tv_participant_username);
        tvEmail = itemView.findViewById(R.id.tv_participant_email);

    }

}
