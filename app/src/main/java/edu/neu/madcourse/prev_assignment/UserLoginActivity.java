package edu.neu.madcourse.prev_assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.team_j_sport.R;

public class UserLoginActivity extends AppCompatActivity {
    public static final String TAG = "UserLoginActivity";
    public static final String GET_USER_KEY = "get user";
    public static final String GET_USER_ID = "get user id";

    private Button login_btn;
    private EditText username_et;
    private User newUser;
    long maxId = -1;
    long id;

    UtilsFunction utilsFunction;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login_btn = findViewById(R.id.login);
        username_et = findViewById(R.id.et_old_password);

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");

        // To get the current ID number
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    myRef.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            maxId = Long.parseLong(((HashMap)snapshot.getValue()).get("id").toString());
                            Log.d(TAG, "maxId: " + maxId);
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
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });


        sp = getSharedPreferences("login", MODE_PRIVATE);
        if(sp.getBoolean("isUserLogin",true)){
            Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
            startActivity(intent);
        }
        
        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                Long userId = (long) ++maxId;

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference().child("Users");

                if(TextUtils.isEmpty(username)){
                    username_et.setError("Please enter username");
                }else{
                    final boolean[] checkUserExistsOrNot = {false};


                    myRef.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                if(userSnapshot.child("username").getValue().equals(username)){
                                    checkUserExistsOrNot[0] = true;
                                }
                            }

                            if(checkUserExistsOrNot[0] == true){
                                Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(GET_USER_KEY, username);

                                // get user id by username
                                myRef.addValueEventListener(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                            if(userSnapshot.child("username").getValue().equals(username)){
                                                id = (long) userSnapshot.child("id").getValue();
                                                editor.putLong(GET_USER_ID, id);
                                                editor.apply();
                                                startActivity(intent);
                                                sp.edit().putBoolean("isUserLogin",true).apply();
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NotNull DatabaseError databaseError){

                                    }
                                });
                            }else{
                                Log.d(TAG, "userId: " + userId);
                                newUser = new User();

                                newUser.setId(userId);
                                newUser.setUsername(username);
                                myRef.child(String.valueOf(userId)).setValue(newUser);

                                Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(GET_USER_KEY, username);
                                editor.putLong(GET_USER_ID, userId);
                                editor.apply();

                                startActivity(intent);
                                sp.edit().putBoolean("isUserLogin",true).apply();
                            }


                        }
                        @Override
                        public void onCancelled(@NotNull DatabaseError databaseError){

                        }
                    });

                }
            }
        });
    }
}
