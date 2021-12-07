package edu.neu.madcourse.team_j_sport.user_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.HomepageActivity;
import edu.neu.madcourse.team_j_sport.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    public static final String USERS_TABLE_KEY = "users";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    public static final String GET_USER_KEY = "get user";

    private EditText firstname_et, lastname_et, email_et, password_et;
    private Button register_btn;

    // Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // check auto login
    SharedPreferences sp;

    private NewUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("login", MODE_PRIVATE);
        if(sp.getBoolean("isUserLogin",true)){
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
        }

        firstname_et = findViewById(R.id.emailEditText);
        lastname_et = findViewById(R.id.lastname);
        email_et = findViewById(R.id.email);
        password_et = findViewById(R.id.password);

        register_btn = findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstname_et.getText().toString();
                String lastname = lastname_et.getText().toString();
                String email = email_et.getText().toString();
                String password = password_et.getText().toString();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference().child("Users");

                if(TextUtils.isEmpty(firstname)){
                    firstname_et.setText("Please enter first name");
                }else if(TextUtils.isEmpty(lastname)){
                    lastname_et.setText("Please enter last name");
                }
                else if(TextUtils.isEmpty(email)){
                    email_et.setText("Please enter email");
                }
                else if(TextUtils.isEmpty(password)){
                    password_et.setText("Please enter password");
                }else{
                    //TODO add to firebase
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //get user id just generated
                                        String userId = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection(USERS_TABLE_KEY).document(userId);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put(FIRST_NAME_KEY, firstname);
                                        user.put(LAST_NAME_KEY, lastname);
                                        user.put(EMAIL_KEY, email);
                                        user.put(USER_ID_KEY,userId);

                                        myRef.addValueEventListener(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                                //add the user to database
                                                myRef.child(userId).setValue(user);
                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NotNull DatabaseError databaseError){

                                            }
                                        });

                                        newUser = new NewUser();
                                        newUser.setFirstname(firstname);
                                        newUser.setLastname(lastname);
                                        newUser.setEmail(email);
                                        newUser.setId(userId);
                                        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
//                                        intent.putExtra(GET_USER_KEY,newUser);

                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(FIRST_NAME_KEY, firstname);
                                        editor.putString(LAST_NAME_KEY, lastname);
                                        editor.putString(USER_ID_KEY, userId);
                                        editor.putString(EMAIL_KEY, email);
                                        editor.apply();

                                        startActivity(intent);
                                        sp.edit().putBoolean("isUserLogin",true).apply();

                                    } else {
                                        Log.e("tag", task.getException().toString());
                                        Toast.makeText(getApplicationContext(), "Failing", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
            }
        });

    }
}