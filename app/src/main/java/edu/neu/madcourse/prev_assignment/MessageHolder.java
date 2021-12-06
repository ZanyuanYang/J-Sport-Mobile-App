package edu.neu.madcourse.prev_assignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.R;

public class MessageHolder extends RecyclerView.ViewHolder {

    public ImageView imgSticker;
    public TextView tvUser;
    public TextView tvDate;


    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        this.imgSticker = itemView.findViewById(R.id.img_sticker);
        this.tvUser = itemView.findViewById(R.id.tv_user);
        this.tvDate = itemView.findViewById(R.id.tv_date);
    }
}
