package edu.neu.madcourse.team_j_sport.PostList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import edu.neu.madcourse.team_j_sport.EventList.LocationUtils;
import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

public class PostDetailActivity extends AppCompatActivity {

    public static final String USER_ID_KEY = "user id";

    private String postKey;

    private TextView tvUsername, tvTitle, tvContent, tvDate;
    private EditText etComment;
    private Button btnReply;

    FirebaseAuth fAuth;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    Button btnDelete;

    String firstname, lastname;
    String commentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // get the postKey
        Intent intent = getIntent();
        postKey = intent.getStringExtra(PostHolder.POST_KEY);

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        myRef.child("Users").child(userId).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.getKey().equals("firstname")){
                        firstname = userSnapshot.getValue() + "";
                    }
                    if(userSnapshot.getKey().equals("lastname")){
                        lastname = userSnapshot.getValue() + "";
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });

        initView();
        submitComment(userId, postKey);
    }

    private void initView() {
        btnDelete = findViewById(R.id.btn_post_delete);
        btnDelete.setOnClickListener(view -> {
            myRef.child("Posts").child(postKey).removeValue();
            finish();
        });

        tvUsername = findViewById(R.id.username);
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.content);
        tvDate = findViewById(R.id.date);

        myRef = myRef.child("Posts").child(postKey);
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.getKey().equals("username")){
                        tvUsername.setText(userSnapshot.getValue() + "");
                    }
                    if(userSnapshot.getKey().equals("title")){
                        tvTitle.setText(userSnapshot.getValue() + "");
                    }
                    if(userSnapshot.getKey().equals("content")){
                        tvContent.setText(userSnapshot.getValue() + "");
                    }
                    if(userSnapshot.getKey().equals("date")){
                        tvDate.setText(userSnapshot.getValue() + "");
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });

    }

    private void submitComment(String userIdd, String postKeyy){
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("Posts").child(postKeyy).child("comments")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        int size = (int) dataSnapshot.getChildrenCount();
                        size++;
                        etComment = findViewById(R.id.comment);
                        btnReply = findViewById(R.id.reply);
                        int finalSize = size;
                        btnReply.setOnClickListener(view -> {

                            Comment comment = new Comment();
                            comment.setUid(userIdd);
                            comment.setComment(etComment.getText().toString());
                            comment.setUsername(firstname + " " + lastname);
                            comment.setCommentTime(commentTime);

                            myRef.child("Posts")
                                    .child(postKeyy)
                                    .child("comments")
                                    .child(finalSize + "")
                                    .setValue(comment);
                            etComment.setText("");
                        });

                    }
                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError){

                    }
                });


    }


}