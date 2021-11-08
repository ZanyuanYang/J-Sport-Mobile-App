package edu.neu.madcourse.team_j_sport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserPageActivity extends AppCompatActivity {

    public static final String GET_USER_KEY = "get user";

    private User user;
    private TextView get_username_tv;
    private Button receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        get_username_tv = findViewById(R.id.get_username);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String value = sharedPreferences.getString(GET_USER_KEY,"");
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
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPageActivity.this, ReceiveActivity.class);
                startActivity(intent);
            }
        });

        Button goSendStikerBtn = findViewById(R.id.Go_Send_Sticker);
        goSendStikerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this, UserSendStickerActivity.class);
                startActivity(intent);
            }
        });


    }
}
