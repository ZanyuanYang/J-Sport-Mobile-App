package edu.neu.madcourse.team_j_sport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String USERS_TABLE_KEY = "users";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    public static final String GET_USER_KEY = "get user";

    private Button login_page;

    private EditText email_et, password_et;
    private Button login_btn;

    private TextView register_btn, forgot_password_btn;

    private String email, password;

    // Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    NewUser user;

    // check auto login
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = database.getReference().child("Users");

        sp = getSharedPreferences("login", MODE_PRIVATE);
        if(sp.getBoolean("logged",false)){
            Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
            startActivity(intent);
        }

        email_et = findViewById(R.id.email);
        password_et = findViewById(R.id.password);
        login_btn = findViewById(R.id.submit_reset);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_et.getText().toString();
                password = password_et.getText().toString();

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = fAuth.getCurrentUser().getUid();

                                    userTable.addValueEventListener(new ValueEventListener(){
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                                if(userSnapshot.child("email").getValue().equals(email)){
                                                    Log.d(TAG, "FIRSTNAME: " + userSnapshot.child("firstname").getValue());
                                                    Intent intent = new Intent(getApplicationContext(), EventListActivity.class);

                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putString(FIRST_NAME_KEY, userSnapshot.child("firstname").getValue().toString());
                                                    editor.putString(LAST_NAME_KEY, userSnapshot.child("lastname").getValue().toString());
                                                    editor.putString(USER_ID_KEY, userId);
                                                    editor.putString(EMAIL_KEY, email);
                                                    editor.apply();

                                                    startActivity(intent);
                                                    sp.edit().putBoolean("logged",true).apply();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NotNull DatabaseError databaseError){

                                        }
                                    });

                                }
                                else {
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        // User does not exist
                                        Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        // User authentication failed
                                        Toast.makeText(getApplicationContext(), "Email or password incorrect.", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Unexpected error occurred.", Toast.LENGTH_SHORT).show();
                                        Log.e("E/LoginActivity", e.getMessage());
                                    }
                                }
                            }
                        }
                );

            }
        });


        register_btn = findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgot_password_btn = findViewById(R.id.forgot_password);
        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });



        getMyToken();


        login_page = findViewById(R.id.login_page);
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });


        // Write a message to the database
        DatabaseReference myRef = database.getReference("message");

//        myRef.setValue("Hello, World123!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "FIREBASE TEST: ";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public String getMyToken() {
        final String TAG = "FIREBASE getMyToken";
        String res = "";
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        return res;
    }
}