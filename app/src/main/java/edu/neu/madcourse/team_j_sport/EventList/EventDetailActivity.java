package edu.neu.madcourse.team_j_sport.EventList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.ParticipantsList.ParticipantsListActivity;
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
    private Button btnParticipants;

    private SharedPreferences sp;
    private DatabaseReference mDatabase;

    public static final String TAG = "EventDetailActivity";
    public static final String PARTICIPANTS = "participants";
    public static final String JOIN = "JOIN";
    public static final String QUIT = "QUIT";
    public static final String FULL = "FULL";
    public static final String DELETE = "DELETE";
    public static final String EXPIRED = "EXPIRED";
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

            String joinText = btnJoin.getText().toString();
            switch (joinText) {
                case QUIT:
                    //Remove the user from the event
                    mDatabase.child("Events")
                            .child(eventKey)
                            .child(PARTICIPANTS)
                            .child(token)
                            .removeValue();
                    break;
                case JOIN:
                    String firstName = sp.getString(MainActivity.FIRST_NAME_KEY, "");
                    String lastName = sp.getString(MainActivity.LAST_NAME_KEY, "");

                    HashMap<String, String> map = new HashMap<>();
                    map.put(MainActivity.EMAIL_KEY, sp.getString(MainActivity.EMAIL_KEY, ""));
                    map.put("username", firstName + " " + lastName);

                    // Add the user into the event's participants list
                    mDatabase.child("Events")
                            .child(eventKey)
                            .child(PARTICIPANTS)
                            .child(token)
                            .setValue(map);
                    break;
                case DELETE:
                    // delete the event
                    mDatabase.child("Events")
                            .child(eventKey)
                            .removeValue();
                    finish();
                    break;
            }
        });

        btnParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParticipantsListActivity.class);
                intent.putExtra(EventHolder.EVENT_KEY, eventKey);
                startActivity(intent);
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
                if(hashMap != null){
                    tvEventName.setText(ifNull(hashMap.get("title")));
                    tvEventSummary.setText(ifNull(hashMap.get("summary")));
                    tvEventDescription.setText(ifNull(hashMap.get("description")));
                    tvEventLocation.setText(ifNull(hashMap.get("location")));
                    tvEventTime.setText(ifNull(hashMap.get("time")));
                    tvEventContact.setText(ifNull(hashMap.get("contact")));

                    String currentLatitude = sp.getString(MainActivity.USER_LATITUDE_KEY, "");
                    String currentLongitude = sp.getString(MainActivity.USER_LONGITUDE_KEY, "");
                    String eventLatitude = ifNull(hashMap.get("latitude"));
                    String eventLongitude = ifNull(hashMap.get("longitude"));
                    tvEventDistance.setText(LocationUtils.getDistanceTo(currentLatitude, currentLongitude, eventLatitude, eventLongitude));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child("limitPerson")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tvEventLimitPerson.setText(snapshot.getValue() == null ? "" : snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // deal with the limit Person
        myRef.child(PARTICIPANTS).addValueEventListener(new ValueEventListener() {
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
                if(!snapshot.exists()){
                    btnJoin.setText(EXPIRED);
                } else if (snapshot.hasChild(token)) {
                    btnJoin.setText(QUIT);
                } else if (snapshot.getChildrenCount() >= limitPerson && !unlimited) {
                    btnJoin.setText(FULL);
                } else if(!DELETE.equals(btnJoin.getText().toString())){
                    btnJoin.setText(JOIN);
                }
                // display the number of current participants
                tvEventLimitPerson.setText(snapshot.getChildrenCount() + "/" + limitPersonText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(token.equals(snapshot.getValue())){
                    btnJoin.setText(DELETE);
                    btnJoin.setBackgroundResource(R.drawable.border_radius_danger);

                    btnParticipants.setVisibility(View.VISIBLE);
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
        tvEventDistance = findViewById(R.id.tv_event_detail_location_distance);
        tvEventLimitPerson = findViewById(R.id.tv_event_detail_limit_person_detail);
        tvEventTime = findViewById(R.id.tv_event_detail_time_detail);
        tvEventContact = findViewById(R.id.tv_event_detail_contact_detail);
        btnParticipants = findViewById(R.id.btn_participants_list);
    }

    private String ifNull(Object str){
        if(str == null){
            return "";
        } else {
            return str.toString();
        }
    }
}