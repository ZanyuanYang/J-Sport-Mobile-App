package edu.neu.madcourse.team_j_sport.EventList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.team_j_sport.R;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    private final ArrayList<ItemEvent> eventList;
    private final Context mContext;

    public EventAdapter(ArrayList<ItemEvent> eventList, Context mContext) {
        this.eventList = eventList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        ItemEvent currentEvent = eventList.get(position);

        holder.tvEventTitle.setText(currentEvent.getTitle());
        holder.tvEventTime.setText(currentEvent.getTime());
        holder.tvEventSummary.setText(currentEvent.getSummary());
        holder.tvEventOrganizer.setText("Organizer: " + currentEvent.getOrganizer());
        holder.tvEventLocation.setText(currentEvent.getLocation());
        holder.setEventKey(currentEvent.getEventId());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
