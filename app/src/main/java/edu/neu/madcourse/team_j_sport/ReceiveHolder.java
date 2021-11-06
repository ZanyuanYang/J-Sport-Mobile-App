package edu.neu.madcourse.team_j_sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceiveHolder extends RecyclerView.ViewHolder {

    public ImageView imgSticker;
    public TextView tvSender;
    public TextView tvDate;


    public ReceiveHolder(@NonNull View itemView) {
        super(itemView);
        this.imgSticker = itemView.findViewById(R.id.img_sticker);
        this.tvSender = itemView.findViewById(R.id.tv_sender);
        this.tvDate = itemView.findViewById(R.id.tv_date);
    }
}
