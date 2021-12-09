package edu.neu.madcourse.team_j_sport.PostList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.team_j_sport.R;

public class PostDetailActivity extends AppCompatActivity {

    private String postKey;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // get the postKey
        Intent intent = getIntent();
        postKey = intent.getStringExtra(PostHolder.POST_KEY);

        initView();
    }

    private void initView() {
        btnDelete = findViewById(R.id.btn_post_delete);
        btnDelete.setOnClickListener(view -> {
            myRef.child("Posts").child(postKey).removeValue();
            finish();
        });
    }
}