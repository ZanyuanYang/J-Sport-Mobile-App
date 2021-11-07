package edu.neu.madcourse.team_j_sport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiveAdapter extends RecyclerView.Adapter<ReceiveHolder> {

    private final ArrayList<ItemReceive> itemReceives;

    public ReceiveAdapter(ArrayList<ItemReceive> itemReceives) {
        this.itemReceives = itemReceives;
    }

    @NonNull
    @Override
    public ReceiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive, parent, false);
        return new ReceiveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveHolder holder, int position) {
        ItemReceive itemReceive = itemReceives.get(position);
        //just for test
        holder.imgSticker.setImageResource(R.drawable.common_full_open_on_phone);
        holder.tvSender.setText(itemReceive.getSender());
        holder.tvDate.setText(itemReceive.getDate());
    }

    @Override
    public int getItemCount() {
        return itemReceives.size();
    }
}
