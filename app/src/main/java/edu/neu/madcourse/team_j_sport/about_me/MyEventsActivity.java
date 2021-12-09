package edu.neu.madcourse.team_j_sport.about_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.EventList.AddEvent;
import edu.neu.madcourse.team_j_sport.EventList.EventAdapter;
import edu.neu.madcourse.team_j_sport.EventList.ItemEvent;
import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class MyEventsActivity extends AppCompatActivity {

    private final ArrayList<ItemEvent> itemEvents = new ArrayList<>();

    SharedPreferences sharedPreferences;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        token = sharedPreferences.getString(MainActivity.USER_ID_KEY, "");

        initEventList();
        initFloatingBtn();
    }

    private void initFloatingBtn() {
        FloatingActionButton fab = findViewById(R.id.Event_FAB);
        fab.setOnClickListener(
                view -> {
                    Intent intent = new Intent(getApplicationContext(), AddEvent.class);
                    startActivity(intent);
                });
    }
    private void initEventList() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;

        myRef = database.getReference().child("Events");

        myRef
                .orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                assert hashMap != null;

                                HashMap participants = (HashMap) hashMap.get("participants");

                                if(participants != null && participants.containsKey(token)) {
                                        itemEvents.add(
                                                new ItemEvent(
                                                        Objects.requireNonNull(hashMap.get("title")).toString(),
                                                        Objects.requireNonNull(hashMap.get("time")).toString(),
                                                        Objects.requireNonNull(hashMap.get("summary")).toString(),
                                                        Objects.requireNonNull(hashMap.get("organizer")).toString(),
                                                        Objects.requireNonNull(hashMap.get("location")).toString(),
                                                        snapshot.getKey()));
                                        createRecyclerView();
                                }
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
        RecyclerView recyclerView = findViewById(R.id.rv_event_list);
        recyclerView.setHasFixedSize(true);

        EditText editText = findViewById(R.id.edittext);

        EventAdapter eventAdapter = new EventAdapter(itemEvents, getApplicationContext());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s){
                filter(s.toString(), eventAdapter);
            }
        });


        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void filter(String text, EventAdapter eventAdapter){
        ArrayList<ItemEvent> filteredList = new ArrayList<>();
        for(ItemEvent item : itemEvents){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        eventAdapter.filterList(filteredList);

    }
}