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

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    private final ArrayList<ItemMessage> itemMessages;
    private final Context mContext;

    public MessageAdapter(ArrayList<ItemMessage> itemMessages, Context mContext) {
        this.itemMessages = itemMessages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        ItemMessage itemMessage = itemMessages.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(itemMessage.getImageName());
        Glide.with(mContext /* context */)
                .load(storageReference)
                .into(holder.imgSticker);
        holder.tvUser.setText(itemMessage.getUserName());
        holder.tvDate.setText(itemMessage.getDate());
    }

    @Override
    public int getItemCount() {
        return itemMessages.size();
    }
}
