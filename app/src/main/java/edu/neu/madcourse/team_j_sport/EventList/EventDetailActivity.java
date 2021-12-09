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

import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvEventName;
    private TextView tvEventSummary;
    private TextView tvEventDescription;
    private Button btnJoin;
    private TextView tvEventLocation;
    private TextView tvEventDistance;
    private TextView tvEventLimitPerson;
    private TextView tvEventTime;
    private TextView tvEventContact;

    private SharedPreferences sp;
    private DatabaseReference mDatabase;

    public static final String PARTICIPANTS = "participants";
    public static final String JOIN = "JOIN";
    public static final String QUIT = "QUIT";
    public static final String FULL = "FULL";
    public static final String UNLIMITED = "+∞";

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
        myRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap hashMap = (HashMap) snapshot.getValue();
                assert hashMap != null;
                tvEventName.setText(Objects.requireNonNull(hashMap.get("title")).toString());
                tvEventSummary.setText(Objects.requireNonNull(hashMap.get("summary")).toString());
                tvEventDescription.setText(Objects.requireNonNull(hashMap.get("description")).toString());
                tvEventLocation.setText(Objects.requireNonNull(hashMap.get("location")).toString());
                tvEventTime.setText(Objects.requireNonNull(hashMap.get("time")).toString());
                tvEventContact.setText(Objects.requireNonNull(hashMap.get("contact")).toString());

                String currentLatitude = sp.getString(MainActivity.USER_LATITUDE_KEY, "");
                String currentLongitude = sp.getString(MainActivity.USER_LONGITUDE_KEY, "");
                String eventLatitude = Objects.requireNonNull(hashMap.get("latitude")).toString();
                String eventLongitude = Objects.requireNonNull(hashMap.get("longitude")).toString();
                tvEventDistance.setText(LocationUtils.getDistanceTo(currentLatitude, currentLongitude, eventLatitude, eventLongitude));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("Events")
                .child(eventKey)
                .child("limitPerson")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tvEventLimitPerson.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // deal with the limit Person
        mDatabase.child("Events")
                .child(eventKey)
                .child(PARTICIPANTS).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] limitPersonOriginal = tvEventLimitPerson.getText().toString().split("/");
                String limitPersonText;
                if(limitPersonOriginal.length == 1){
                    limitPersonText = limitPersonOriginal[0];
                } else {
                    limitPersonText = limitPersonOriginal[1];
                }
                int limitPerson = 0;
                boolean unlimited = false;
                if("".equals(limitPersonText) || UNLIMITED.equals(limitPersonText)){
                    unlimited = true;
                    limitPersonText = UNLIMITED;
                } else {
                    limitPerson = Integer.parseInt(limitPersonText);
                }
                if (snapshot.hasChild(token)) {
                    btnJoin.setText(QUIT);
                } else if (snapshot.getChildrenCount() >= limitPerson && !unlimited) {
                    btnJoin.setText(FULL);
                } else {
                    btnJoin.setText(JOIN);
                }
                // display the number of current participants
                tvEventLimitPerson.setText(snapshot.getChildrenCount() + "/" + limitPersonText);
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
        tvEventDistance = findViewById(R.id.tv_event_detail_location_distance);
        tvEventLimitPerson = findViewById(R.id.tv_event_detail_limit_person_detail);
        tvEventTime = findViewById(R.id.tv_event_detail_time_detail);
        tvEventContact = findViewById(R.id.tv_event_detail_contact_detail);
    }
}