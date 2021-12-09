package edu.neu.madcourse.team_j_sport.PostList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import edu.neu.madcourse.team_j_sport.R;

public class PostDetailActivity extends AppCompatActivity {

    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initView();


        Intent intent = getIntent();
        String postKey = intent.getStringExtra(PostHolder.POST_KEY);
        btnDelete.setText(postKey);


    }

    private void initView() {
        btnDelete = findViewById(R.id.btn_post_delete);
    }
}