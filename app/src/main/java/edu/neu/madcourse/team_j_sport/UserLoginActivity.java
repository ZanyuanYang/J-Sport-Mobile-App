package edu.neu.madcourse.team_j_sport;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class UserLoginActivity extends AppCompatActivity {
    private Button login_btn;
    private EditText username_et;

    private User newUser;

    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login_btn = findViewById(R.id.login);
        username_et = findViewById(R.id.username);

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");

        // To get the current ID number
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });


        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();

                if(TextUtils.isEmpty(username)){
                    username_et.setError("Please enter username");
                }else{
                    System.out.println("max_id:" + maxid);
                    newUser = new User();
                    newUser.setId(maxid+1);
                    newUser.setUsername(username);
                    myRef.child(String.valueOf(maxid+1)).setValue(newUser);

                }
            }
        });
    }

}
