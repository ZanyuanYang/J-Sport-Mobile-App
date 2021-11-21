package edu.neu.madcourse.team_j_sport.EventList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import edu.neu.madcourse.team_j_sport.R;

public class EventDetailActivity extends AppCompatActivity {

    TextView tvEventDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        tvEventDetail = findViewById(R.id.tv_event_detail);
        tvEventDetail.setText("eventKey: " + getIntent().getStringExtra("eventKey"));

    }
}