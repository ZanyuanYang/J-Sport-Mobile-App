package edu.neu.madcourse.team_j_sport;

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

public class MessageActivity extends AppCompatActivity {

    private final ArrayList<ItemMessage> itemMessages = new ArrayList<>();

    SharedPreferences sharedPreferences;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

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

        initialItemData(savedInstanceState);
    }

    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        int size = itemMessages == null ? 0 : itemMessages.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        for(int i = 0; i < size; i++){
            outState.putString(KEY_OF_INSTANCE + i + "0", itemMessages.get(i).getImageName());
            outState.putString(KEY_OF_INSTANCE + i + "1", itemMessages.get(i).getUserName());
            outState.putString(KEY_OF_INSTANCE + i + "2", itemMessages.get(i).getDate());
        }

        super.onSaveInstanceState(outState);
    }

    private void initialItemData(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)){
            if(itemMessages == null || itemMessages.size() == 0){
                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                for(int i = 0; i < size; i++){
                    String imageUrl = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String sender = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String date = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    assert itemMessages != null;
                    itemMessages.add(new ItemMessage(imageUrl, sender, date));
                }
            }
        }
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