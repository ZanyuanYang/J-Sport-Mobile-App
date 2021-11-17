package edu.neu.madcourse.team_j_sport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class EventListActivity extends AppCompatActivity {

    public static final String TAG = "EventListActivity";
    public static final String USERS_TABLE_KEY = "users";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    public static final String GET_USER_KEY = "get user";

    TextView textView_tv;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        textView_tv = findViewById(R.id.test);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String firstname = sharedPreferences.getString(FIRST_NAME_KEY,"");
        String lastname = sharedPreferences.getString(LAST_NAME_KEY,"");
        String email = sharedPreferences.getString(EMAIL_KEY,"");
        String userId = sharedPreferences.getString(USER_ID_KEY,"");

        Log.d(TAG, "firstname: " + firstname);
        Log.d(TAG, "lastname: " + lastname);
        Log.d(TAG, "email: " + email);
        Log.d(TAG, "userId: " + userId);



        textView_tv.setText("firstname: " + firstname + "\n"
                + "lastname: " + lastname + "\n"
                + "email:" + email + "\n"
                + "userid: " + userId);
    }
}