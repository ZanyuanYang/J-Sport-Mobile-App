package edu.neu.madcourse.team_j_sport;

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

public class ReceiveAdapter extends RecyclerView.Adapter<ReceiveHolder> {

    private final ArrayList<ItemReceive> itemReceives;
    private final Context mContext;

    public ReceiveAdapter(ArrayList<ItemReceive> itemReceives, Context mContext) {
        this.itemReceives = itemReceives;
        this.mContext = mContext;
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(itemReceive.getImageName());
        Glide.with(mContext /* context */)
                .load(storageReference)
                .into(holder.imgSticker);
        holder.tvSender.setText(itemReceive.getSender());
        holder.tvDate.setText(itemReceive.getDate());
    }

    @Override
    public int getItemCount() {
        return itemReceives.size();
    }
}
