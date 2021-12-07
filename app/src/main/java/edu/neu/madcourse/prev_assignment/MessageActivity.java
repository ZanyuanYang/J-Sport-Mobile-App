package edu.neu.madcourse.prev_assignment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.R;

public class MessageActivity extends AppCompatActivity {

    private final ArrayList<ItemMessage> itemMessages = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        Long userId = sharedPreferences.getLong(UserLoginActivity.GET_USER_ID,1L);

        // whether it is a sent page or a receive page
        String inbox = getIntent().getStringExtra("inbox");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;

        if("sent".equals(inbox)){
            myRef = database.getReference().child("Users").child(String.valueOf(userId)).child("SentMessages");
        } else {
            myRef = database.getReference().child("Users").child(String.valueOf(userId)).child("ReceivedMessages");
        }

        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HashMap hashMap = (HashMap) snapshot.getValue();
                assert hashMap != null;
                if("sent".equals(inbox)){
                    itemMessages.add(new ItemMessage(Objects.requireNonNull(hashMap.get("imageName")).toString(), Objects.requireNonNull(hashMap.get("receiverName")).toString(), Objects.requireNonNull(hashMap.get("date")).toString()));
                } else {
                    itemMessages.add(new ItemMessage(Objects.requireNonNull(hashMap.get("imageName")).toString(), Objects.requireNonNull(hashMap.get("senderName")).toString(), Objects.requireNonNull(hashMap.get("date")).toString()));
                }
                createRecyclerView();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_message);
        recyclerView.setHasFixedSize(true);

        MessageAdapter messageAdapter = new MessageAdapter(itemMessages, getApplicationContext());

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}