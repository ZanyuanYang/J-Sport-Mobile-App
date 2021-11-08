package edu.neu.madcourse.team_j_sport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.FCM.FCMServer;

public class UserPageActivity extends AppCompatActivity {
    public static final String TAG = "UserPageActivity";
    public static final String GET_USER_KEY = "get user";
    public static final String TOPIC_USER_ID = "userId-";

    private Button receive;
    private Button sent;

    private User user;
    private TextView get_username_tv;

    private Long userId;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);


        get_username_tv = findViewById(R.id.get_username);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String value = sharedPreferences.getString(GET_USER_KEY,"");

        // For FCM subscription and receiving
        userId = sharedPreferences.getLong(UserLoginActivity.GET_USER_ID,1L);
        Log.d(TAG, "SP: " + userId);

        subscribeToUserIdTopic(TOPIC_USER_ID + userId);

        FCMServer.testSend((long) 0);

        get_username_tv.setText(value);

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

    /**
     * Subscribe this User to the notification topic they belong to
     * @param s the Topic to subscribe
     */
    private void subscribeToUserIdTopic(String s) {
        if (topic != null && topic.length() > 0) {
            unsubscribe();
        }

        topic = s;
        Log.d(TAG, "Subscribing to new topic: " + topic);

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }

                        Log.d(TAG, msg);
                    }
                });
    }

    private void unsubscribe() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_unsubscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_unsubscribe_failed);
                        }
                        Log.d(TAG, msg);
                    }
                });
    }

}
