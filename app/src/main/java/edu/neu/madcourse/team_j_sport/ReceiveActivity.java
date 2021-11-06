package edu.neu.madcourse.team_j_sport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReceiveActivity extends AppCompatActivity {

    private final ArrayList<ItemReceive> itemReceives = new ArrayList<>();

    private RecyclerView recyclerView;
    private ReceiveAdapter receiveAdapter;
    private RecyclerView.LayoutManager layoutManager;

    SharedPreferences sharedPreferences;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        Long userId = sharedPreferences.getLong(UserLoginActivity.GET_USER_ID,1L);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child(String.valueOf(userId)).child("Messages");
        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HashMap hashMap = (HashMap) snapshot.getValue();
                itemReceives.add(new ItemReceive(hashMap.get("imageUrl").toString(), hashMap.get("sender").toString(), hashMap.get("date").toString()));
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

        int size = itemReceives == null ? 0 : itemReceives.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        for(int i = 0; i < size; i++){
            outState.putString(KEY_OF_INSTANCE + i + "0", itemReceives.get(i).getImageUrl());
            outState.putString(KEY_OF_INSTANCE + i + "1", itemReceives.get(i).getSender());
            outState.putString(KEY_OF_INSTANCE + i + "2", itemReceives.get(i).getDate());
        }

        super.onSaveInstanceState(outState);
    }

    private void initialItemData(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)){
            if(itemReceives == null || itemReceives.size() == 0){
                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                for(int i = 0; i < size; i++){
                    String imageUrl = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String sender = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String date = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    itemReceives.add(new ItemReceive(imageUrl, sender, date));
                }
            }
        }
    }

    private void createRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rv_receive);
        recyclerView.setHasFixedSize(true);

        receiveAdapter = new ReceiveAdapter(itemReceives);

        recyclerView.setAdapter(receiveAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}