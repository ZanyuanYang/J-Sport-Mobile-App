package edu.neu.madcourse.team_j_sport.EventList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.R;

public class EventHolder extends RecyclerView.ViewHolder {

    public TextView tvEventTitle;
    public TextView tvEventTime;
    public TextView tvEventSummary;
    public TextView tvEventOrganizer;
    public TextView tvEventLocation;
    private String eventKey;

    public EventHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        tvEventTitle = itemView.findViewById(R.id.tv_event_title);
        tvEventTime = itemView.findViewById(R.id.tv_event_time);
        tvEventSummary = itemView.findViewById(R.id.tv_event_summary);
        tvEventOrganizer = itemView.findViewById(R.id.tv_event_organizer);
        tvEventLocation = itemView.findViewById(R.id.tv_event_location);

    itemView.setOnClickListener(
            view -> {
              System.out.println("????" + getLayoutPosition());
              Intent intent = new Intent(mContext, EventDetailActivity.class);
              intent.putExtra("eventKey", eventKey);
              mContext.startActivity(intent);
            });
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
