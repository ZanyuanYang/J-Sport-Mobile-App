package edu.neu.madcourse.team_j_sport.ParticipantsList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.EventList.EventHolder;
import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostAdapter;
import edu.neu.madcourse.team_j_sport.R;

public class ParticipantsListActivity extends AppCompatActivity {

    private final ArrayList<ItemParticipant> participants = new ArrayList<>();
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);

        Intent intent = getIntent();
        eventKey = intent.getStringExtra(EventHolder.EVENT_KEY);

        initParticipants();
    }

    private void initParticipants() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;

        myRef = database.getReference().child("Events").child(eventKey).child("participants");

        myRef
                .orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                assert hashMap != null;
                                participants.add(
                                        new ItemParticipant(
                                                snapshot.getKey(),
                                                Objects.requireNonNull(hashMap.get("username")).toString(),
                                                Objects.requireNonNull(hashMap.get("email")).toString()
                                                ));
                                createRecyclerView();
                            }

                            @Override
                            public void onChildChanged(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                            @Override
                            public void onChildMoved(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_participant_list);
        recyclerView.setHasFixedSize(true);

        ParticipantAdapter participantAdapter = new ParticipantAdapter(participants, getApplicationContext());


        recyclerView.setAdapter(participantAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}