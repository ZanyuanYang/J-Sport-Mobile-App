package edu.neu.madcourse.team_j_sport.EventList;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.neu.madcourse.team_j_sport.R;

public class EventDetailActivity extends AppCompatActivity {

    TextView tvEventName;
    TextView tvEventSummary;
    TextView tvEventDescription;
    Button btnJoin;
    TextView tvEventLocation;
    TextView tvEventLimitPerson;
    TextView tvEventTime;
    TextView tvEventContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initView();
        setText();

        btnJoin.setOnClickListener(view -> {
            //TODO: Add the user into the event's participants list
        });
    }

    private void setText() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;
        myRef = database.getReference().child("Events").child(String.valueOf(getIntent().getStringExtra(EventHolder.EVENT_KEY)));
        myRef.child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventName.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("summary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventSummary.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventDescription.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventLocation.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("limitPerson").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventLimitPerson.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventTime.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("contact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEventContact.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        tvEventName = findViewById(R.id.tv_event_detail_name);
        tvEventSummary = findViewById(R.id.tv_event_detail_summary_detail);
        tvEventDescription = findViewById(R.id.tv_event_detail_description_detail);
        btnJoin = findViewById(R.id.btn_join);
        tvEventLocation = findViewById(R.id.tv_event_detail_location_detail);
        tvEventLimitPerson = findViewById(R.id.tv_event_detail_limit_person_detail);
        tvEventTime = findViewById(R.id.tv_event_detail_time_detail);
        tvEventContact = findViewById(R.id.tv_event_detail_contact_detail);

    }
}