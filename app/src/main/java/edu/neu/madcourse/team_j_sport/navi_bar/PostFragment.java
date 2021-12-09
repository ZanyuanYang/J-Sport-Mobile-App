package edu.neu.madcourse.team_j_sport.navi_bar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.neu.madcourse.team_j_sport.EventList.AddEvent;
import edu.neu.madcourse.team_j_sport.Post.AddPost;
import edu.neu.madcourse.team_j_sport.R;

public class PostFragment extends Fragment {
    View view;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        initFloatingBtn();
        // Inflate the layout for this fragment
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
}