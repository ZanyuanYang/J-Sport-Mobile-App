package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.team_j_sport.R;

public class EventFragment extends Fragment {

    public static final String TAG = "EventListActivity";
    public static final String USERS_TABLE_KEY = "users";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    public static final String GET_USER_KEY = "get user";

    TextView tvEvent;

    View view;


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event, container, false);
        initTextView();
        return view;
    }

    private void initTextView() {
        tvEvent = view.findViewById(R.id.tv_event_list);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        String firstname = sharedPreferences.getString(FIRST_NAME_KEY,"");
        String lastname = sharedPreferences.getString(LAST_NAME_KEY,"");
        String email = sharedPreferences.getString(EMAIL_KEY,"");
        String userId = sharedPreferences.getString(USER_ID_KEY,"");

        Log.d(TAG, "firstname: " + firstname);
        Log.d(TAG, "lastname: " + lastname);
        Log.d(TAG, "email: " + email);
        Log.d(TAG, "userId: " + userId);



        tvEvent.setText("firstname: " + firstname + "\n"
                + "lastname: " + lastname + "\n"
                + "email:" + email + "\n"
                + "userid: " + userId);
    }
}