package edu.neu.madcourse.team_j_sport.navi_bar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.team_j_sport.EventList.AddEvent;
import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.UserLoginActivity;
import edu.neu.madcourse.team_j_sport.UserPageActivity;
import edu.neu.madcourse.team_j_sport.about_me.ChangePasswordActivity;
import edu.neu.madcourse.team_j_sport.about_me.EditProfileActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyEventsActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyPostsActivity;

import static android.content.Context.MODE_PRIVATE;


public class MeFragment extends Fragment implements View.OnClickListener {

    View view;
    SharedPreferences sharedPreferences;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_me, container, false);
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);

        view.findViewById(R.id.iv_edit_profile).setOnClickListener(this::onClick);
        view.findViewById(R.id.iv_log_out).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_my_posts).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_my_events).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_change_password).setOnClickListener(this::onClick);
        view.findViewById(R.id.iv_arrow_1).setOnClickListener(this::onClick);
        view.findViewById(R.id.iv_arrow_2).setOnClickListener(this::onClick);
        view.findViewById(R.id.iv_arrow_3).setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_edit_profile:
                intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_log_out:
                // TODO: some action to log out
                sharedPreferences.edit().putBoolean("isUserLogin",false).apply();
                intent = new Intent(getActivity(), UserLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_my_posts:
            case R.id.iv_arrow_1:
                intent = new Intent(getActivity(), MyPostsActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_my_events:
            case R.id.iv_arrow_2:
                intent = new Intent(getActivity(), MyEventsActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_change_password:
            case R.id.iv_arrow_3:
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}