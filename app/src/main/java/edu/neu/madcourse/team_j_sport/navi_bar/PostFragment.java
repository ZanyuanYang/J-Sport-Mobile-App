package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import edu.neu.madcourse.team_j_sport.Post.AddPost;
import edu.neu.madcourse.team_j_sport.Post.Posts;
import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostAdapter;
import edu.neu.madcourse.team_j_sport.R;

public class PostFragment extends Fragment {
    public static final String TAG = "PostFragment";
    private final ArrayList<ItemPost> itemPosts = new ArrayList<>();

    View view;
    PostAdapter postAdapter;


    SharedPreferences sharedPreferences;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        initFloatingBtn();
        initPostList();
        return view;
    }

    private void initFloatingBtn() {
        FloatingActionButton fab = view.findViewById(R.id.Post_FAB);
        fab.setOnClickListener(
                view -> {
                    Intent intent = new Intent(getActivity(), AddPost.class);
                    startActivity(intent);
                });
    }

    private void initPostList() {
        Log.d(TAG, "init List");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;
        myRef = database.getReference().child("Posts");
        myRef.orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Log.d(TAG, "childEvent - add");

                                String key = snapshot.getKey();
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                if (hashMap == null) throw new AssertionError();

                                if (!itemPosts.contains(key)) {
                                    Log.d(TAG, "onChildAdded - add Post");
                                    itemPosts.add(
                                            new ItemPost(
                                                    Objects.requireNonNull(hashMap.get("uid")).toString(),
                                                    Objects.requireNonNull(hashMap.get("title")).toString(),
                                                    Objects.requireNonNull(hashMap.get("content")).toString(),
                                                    snapshot.getKey()));
                                    createRecyclerView();
                                }
                            }

                            @Override
                            public void onChildChanged(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Log.d(TAG, "childEvent - change");
                                postAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                Log.d(TAG, "childEvent - remove");
                                String key = snapshot.getKey();
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                Log.d(TAG, key + " " + Objects.requireNonNull(hashMap.get("title")).toString());

                                removeItemById(key);
                                postAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildMoved(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Log.d(TAG, "childEvent - move");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d(TAG, "childEvent - error");
                            }
                        });
    }

    private void removeItemById(String id) {
        int target = -1;
        for (int i = 0; i < itemPosts.size(); i++) {
            if (itemPosts.get(i).getPostId().equals(id))
                target = i;
        }
        if (target != -1) {
            itemPosts.remove(target);
        }
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.rv_post_list);
        recyclerView.setHasFixedSize(true);

        postAdapter = new PostAdapter(itemPosts, getActivity().getApplicationContext());

        EditText etSearch = view.findViewById(R.id.et_post_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), postAdapter);
            }
        });

        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void filter(String text, PostAdapter postAdapter) {
        ArrayList<ItemPost> filteredList = new ArrayList<>();
        for (ItemPost item : itemPosts) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        postAdapter.filterList(filteredList);
    }

    public void onResume() {
        super.onResume();
        // Restore the search box and remove focus
        EditText et = view.findViewById(R.id.et_post_search);
        et.setText("");
        et.clearFocus();
        // Clear the current recyclerView and fetch the latest events from database
        initPostList();
    }

}