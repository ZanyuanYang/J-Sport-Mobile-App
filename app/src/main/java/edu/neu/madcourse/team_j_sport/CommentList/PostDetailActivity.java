package edu.neu.madcourse.team_j_sport.CommentList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostAdapter;
import edu.neu.madcourse.team_j_sport.PostList.PostHolder;
import edu.neu.madcourse.team_j_sport.R;

public class PostDetailActivity extends AppCompatActivity {

    private final ArrayList<ItemComment> comments = new ArrayList<>();

    public static final String USER_ID_KEY = "user id";

    private String postKey;
    private String userId;

    private TextView tvUsername, tvTitle, tvContent, tvDate;
    private EditText etComment;
    private Button btnReply;

    FirebaseAuth fAuth;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

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

        initUsername();
        initView();
        submitComment(userId, postKey);

        initCommentList();
    }

    private void initCommentList() {

        DatabaseReference myRef = database.getReference().child("Posts").child(postKey).child("comments");

        myRef
                .orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                assert hashMap != null;
                                comments.add(
                                        new ItemComment(
                                                Objects.requireNonNull(hashMap.get("uid")).toString(),
                                                Objects.requireNonNull(hashMap.get("username")).toString(),
                                                Objects.requireNonNull(hashMap.get("commentTime")).toString(),
                                                Objects.requireNonNull(hashMap.get("comment")).toString(),
                                                snapshot.getKey()));
                                createRecyclerView();
                            }

                            @Override
                            public void onChildChanged(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                            @Override
                            public void onChildMoved(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_comment_list);
        recyclerView.setHasFixedSize(true);

        CommentAdapter commentAdapter = new CommentAdapter(comments, getApplicationContext(), myRef, postKey);

        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initUsername() {
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
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
    }

    private void initView() {
        btnDelete = findViewById(R.id.btn_post_delete);

        myRef.child("Posts").child(postKey).child("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(userId.equals(snapshot.getValue())){
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnDelete.setOnClickListener(view -> {
            myRef.child("Posts").child(postKey).removeValue();
            finish();
        });

        tvUsername = findViewById(R.id.username);
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.content);
        tvDate = findViewById(R.id.date);

        myRef.child("Posts").child(postKey).addValueEventListener(new ValueEventListener(){
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

        myRef.child("Posts").child(postKeyy).child("comments")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        etComment = findViewById(R.id.comment);
                        btnReply = findViewById(R.id.reply);
                        String commentId = UUID.randomUUID().toString();
                        btnReply.setOnClickListener(view -> {

                            Comment comment = new Comment();
                            comment.setUid(userIdd);
                            comment.setComment(etComment.getText().toString());
                            comment.setUsername(firstname + " " + lastname);
                            comment.setCommentTime(commentTime);

                            myRef.child("Posts")
                                    .child(postKeyy)
                                    .child("comments")
                                    .child(commentId)
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