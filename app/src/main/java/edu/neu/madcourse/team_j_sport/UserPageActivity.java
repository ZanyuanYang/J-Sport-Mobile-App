package edu.neu.madcourse.team_j_sport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class UserPageActivity extends AppCompatActivity {

    public static final String GET_USER_KEY = "get user";

    private User user;
    private TextView get_username_tv;
    private Button receive;
    private Button sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        get_username_tv = findViewById(R.id.get_username);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String value = sharedPreferences.getString(GET_USER_KEY,"");
        get_username_tv.setText(value);

        // Return ID by username
        long getIdFromFB = getIdFromFirebase("yang");
        System.out.println("getIdFromFB: " + getIdFromFB);

//        Intent preIntent = getIntent();
        //if the user already has a ticket in firebase, then stop jump to the SubmitTicket page.
        //1.check if the userId has already in student_collection
//        user = (User)preIntent.getSerializableExtra(UserLoginActivity.GET_USER_KEY);
//        Long userId = user.getId();
//        String username = user.getUsername();
//        get_username_tv.setText(username);
//        System.out.println("once we entered homepage, the userId: "+ userId);
//        System.out.println("username: "+ username);


        receive = findViewById(R.id.btn_receive);
        receive.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, MessageActivity.class);
            intent.putExtra("inbox", "receive");
            startActivity(intent);
        });

        sent = findViewById(R.id.btn_sent);
        sent.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, MessageActivity.class);
            intent.putExtra("inbox", "sent");
            startActivity(intent);
        });
    }


    // get Id From Firebase
    public long getIdFromFirebase(String username){
        long[] id = {0};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
//                System.out.println("dataSnapshot: " + dataSnapshot.getValue());
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.child("username").getValue().equals(username)){
                        System.out.println("dataSnapshot: " + userSnapshot.child("id").getValue());
                        id[0] = (long) userSnapshot.child("id").getValue();
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });
        return id[0];
    }
}


