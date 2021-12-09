package edu.neu.madcourse.team_j_sport.about_me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.Post.AddPost;
import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostAdapter;
import edu.neu.madcourse.team_j_sport.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyPostsActivity extends AppCompatActivity {


    private final ArrayList<ItemPost> itemPosts = new ArrayList<>();

    SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        token = sharedPreferences.getString(MainActivity.USER_ID_KEY, "");

        initView();
        initFloatingBtn();
        initPostList();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        TextView tvEventList = findViewById(R.id.tv_post_list);
        tvEventList.setText("My Posts");
    }

    private void initFloatingBtn() {
        FloatingActionButton fab = findViewById(R.id.Post_FAB);
        fab.setOnClickListener(
                view -> {
                    Intent intent = new Intent(getApplicationContext(), AddPost.class);
                    startActivity(intent);
                });
    }

    private void initPostList() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;

        myRef = database.getReference().child("Posts");

        myRef
                .orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                assert hashMap != null;

                                String uid = Objects.requireNonNull(hashMap.get("uid")).toString();

                                if(token.equals(uid)){
                                    itemPosts.add(
                                            new ItemPost(
                                                    "",
                                                    Objects.requireNonNull(hashMap.get("title")).toString(),
                                                    Objects.requireNonNull(hashMap.get("content")).toString(),
                                                    snapshot.getKey()));
                                    createRecyclerView();
                                }

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
        RecyclerView recyclerView = findViewById(R.id.rv_post_list);
        recyclerView.setHasFixedSize(true);

        PostAdapter postAdapter = new PostAdapter(itemPosts, getApplicationContext());

        EditText etSearch = findViewById(R.id.et_post_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s){
                filter(s.toString(), postAdapter);
            }
        });

        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void filter(String text, PostAdapter postAdapter){
        ArrayList<ItemPost> filteredList = new ArrayList<>();
        for(ItemPost item : itemPosts){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        postAdapter.filterList(filteredList);
    }


}