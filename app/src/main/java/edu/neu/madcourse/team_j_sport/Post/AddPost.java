package edu.neu.madcourse.team_j_sport.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

import edu.neu.madcourse.team_j_sport.EventList.Events;
import edu.neu.madcourse.team_j_sport.R;
public class AddPost extends AppCompatActivity {
    private SharedPreferences sp;
    private DatabaseReference mDatabase;
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";

    public static final String USER_ID_KEY = "user id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        setContentView(R.layout.activity_add_post);
        setUpFirebase();
        initView();
    }
    private void initView(){
        Button btn = findViewById(R.id.Post_SubmitButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();

            }
        });
    }
    private void setUpFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    private void addPost(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childCnt = snapshot.getChildrenCount();
                Posts post = getPostInfo();
                String uuid = UUID.randomUUID().toString();
                mDatabase.child("Posts")
                        .child(uuid)
                        .setValue(post);
                Toast.makeText(AddPost.this,"Add Post Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("Posts")
                .addListenerForSingleValueEvent(listener);
    }
    private Posts getPostInfo(){
        EditText contentET = findViewById(R.id.Post_ContentEditText);
        EditText titlET = findViewById(R.id.Post_TitleEditText);
        String title = titlET.getText().toString();
        String content = contentET.getText().toString();
        String userId = sp.getString(USER_ID_KEY, "");
        String userName = sp.getString(FIRST_NAME_KEY,"") + " "+ sp.getString(LAST_NAME_KEY, "");
        String date = getDate();
        Posts post = new Posts(content, date, title, userId, userName);
        return  post;
    }
    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return "" + hour + ":" + min + " " + month + "/" + day;
    }
}