package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.about_me.ChangePasswordActivity;
import edu.neu.madcourse.team_j_sport.about_me.EditProfileActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyEventsActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyPostsActivity;
import edu.neu.madcourse.team_j_sport.about_me.avatar.GalleryActivity;


public class MeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MeFragment";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";

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

        String name = sharedPreferences.getString(FIRST_NAME_KEY, "fName")
                + " "
                + sharedPreferences.getString(LAST_NAME_KEY, "lName");
        String email = sharedPreferences.getString(EMAIL_KEY, "email");

        ((TextView)(view.findViewById(R.id.tv_me_name))).setText(name);
        ((TextView)(view.findViewById(R.id.tv_me_email))).setText(email);

        view.findViewById(R.id.iv_edit_profile).setOnClickListener(this::onClick);
        view.findViewById(R.id.iv_log_out).setOnClickListener(this::onClick);

        view.findViewById(R.id.iv_avatar).setOnClickListener(this::onClick);

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
                sharedPreferences.edit().putBoolean("isUserLogin",false).apply();
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_avatar:
                // FIXME: Only using gallery feature for avatars
                Log.d(TAG, "Avatar is clicked");
                intent = new Intent(getActivity(), GalleryActivity.class);

//                intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setType("image/*");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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