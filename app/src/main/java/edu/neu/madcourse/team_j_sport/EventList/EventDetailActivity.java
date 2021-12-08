package edu.neu.madcourse.team_j_sport.EventList;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvEventName;
    private TextView tvEventSummary;
    private TextView tvEventDescription;
    private Button btnJoin;
    private TextView tvEventLocation;
    private TextView tvEventLimitPerson;
    private TextView tvEventTime;
    private TextView tvEventContact;

    private SharedPreferences sp;
    private DatabaseReference mDatabase;

    public static final String PARTICIPANTS = "participants";
    public static final String JOIN = "JOIN";
    public static final String QUIT = "QUIT";
    public static final String FULL = "FULL";

    private String token;
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        sp = getSharedPreferences("login", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        token = sp.getString(MainActivity.USER_ID_KEY, "");
        eventKey = String.valueOf(getIntent().getStringExtra(EventHolder.EVENT_KEY));

        initView();
        setText();

        btnJoin.setOnClickListener(view -> {

            String Joined = btnJoin.getText().toString();
            if (QUIT.equals(Joined)) {
                //Remove the user from the event
                mDatabase.child("Events")
                        .child(eventKey)
                        .child(PARTICIPANTS)
                        .child(token)
                        .removeValue();
            } else if (JOIN.equals(Joined)){
                String firstName = sp.getString(MainActivity.FIRST_NAME_KEY, "");
                String lastName = sp.getString(MainActivity.LAST_NAME_KEY, "");
                // Add the user into the event's participants list
                mDatabase.child("Events")
                        .child(eventKey)
                        .child(PARTICIPANTS)
                        .child(token)
                        .setValue(firstName + " " + lastName);
            }
        });
    }

    private void setText() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;
        myRef = database.getReference().child("Events").child(eventKey);
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
        mDatabase.child("Events")
                .child(eventKey)
                .child(PARTICIPANTS).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String limitPersonText = tvEventLimitPerson.getText().toString();
                int limitPerson = 0;
                boolean unlimited = false;
                if("".equals(limitPersonText)){
                    unlimited = true;
                } else {
                    limitPerson = Integer.parseInt(tvEventLimitPerson.getText().toString());
                }
                if (snapshot.hasChild(token)) {
                    btnJoin.setText(QUIT);
                } else if (snapshot.getChildrenCount() >= limitPerson && !unlimited) {
                    btnJoin.setText(FULL);
                } else {
                    btnJoin.setText(JOIN);
                }
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