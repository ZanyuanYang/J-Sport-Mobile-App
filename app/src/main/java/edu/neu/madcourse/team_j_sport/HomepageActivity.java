package edu.neu.madcourse.team_j_sport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import edu.neu.madcourse.team_j_sport.SendNotificationPack.APIService;
import edu.neu.madcourse.team_j_sport.navi_bar.EventFragment;
import edu.neu.madcourse.team_j_sport.navi_bar.MeFragment;
import edu.neu.madcourse.team_j_sport.navi_bar.PostFragment;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener{

//    public static final String TAG = "EventListActivity";
//    public static final String USERS_TABLE_KEY = "users";
//    public static final String FIRST_NAME_KEY = "firstname";
//    public static final String LAST_NAME_KEY = "lastname";
//    public static final String EMAIL_KEY = "email";
//    public static final String USER_ID_KEY = "user id";
//    public static final String GET_USER_KEY = "get user";
//
//    TextView textView_tv;
//
//    SharedPreferences sp;

    private final Fragment postFragment = new PostFragment();
    private final Fragment eventFragment = new EventFragment();
    private final Fragment meFragment = new MeFragment();

    private FragmentManager fm;

    private LinearLayout postLinearLayout;
    private LinearLayout eventLinearLayout;
    private LinearLayout meLinearLayout;

    private ImageButton postImgButton;
    private ImageButton eventImgButton;
    private ImageButton meImgButton;

    // notification
    EditText UserTB,Title,Message;
    Button send;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initView();
        initFragment();
        initEvent();
        selectFragment(0);
        initTextView();
    }

    //--------------------------notification end--------------------------------

    private void initView() {
        postLinearLayout = findViewById(R.id.tab_post);
        eventLinearLayout = findViewById(R.id.tab_event);
        meLinearLayout = findViewById(R.id.tab_me);

        postImgButton = findViewById(R.id.ib_navi_post);
        eventImgButton = findViewById(R.id.ib_navi_event);
        meImgButton = findViewById(R.id.ib_navi_me);
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.id_content, postFragment);
        fragmentTransaction.add(R.id.id_content, eventFragment);
        fragmentTransaction.add(R.id.id_content, meFragment);
        fragmentTransaction.commit();
    }

    private void initEvent() {
        postLinearLayout.setOnClickListener(this);
        eventLinearLayout.setOnClickListener(this);
        meLinearLayout.setOnClickListener(this);
    }

    private void selectFragment(int i){
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 0:
                Log.d("setSelect","1");
                transaction.show(postFragment);
                postImgButton.setImageResource(R.drawable.navi_post_pressed);
                break;
            case 1:
                Log.d("setSelect","2");
                transaction.show(eventFragment);
                eventImgButton.setImageResource(R.drawable.navi_event_pressed);
                break;
            case 2:
                Log.d("setSelect","3");
                transaction.show(meFragment);
                meImgButton.setImageResource(R.drawable.navi_me_pressed);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        transaction.hide(postFragment);
        transaction.hide(eventFragment);
        transaction.hide(meFragment);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Log.d("onClick","1");
        resetImages();
        switch (view.getId()){
            case R.id.tab_post:
                selectFragment(0);
                break;
            case R.id.tab_event:
                selectFragment(1);
                break;
            case R.id.tab_me:
                selectFragment(2);
                break;
            default:
                break;
        }
    }

    public void resetImages(){
        postImgButton.setImageResource(R.drawable.navi_post_normal);
        eventImgButton.setImageResource(R.drawable.navi_event_normal);
        meImgButton.setImageResource(R.drawable.navi_me_normal);
    }

    private void initTextView() {
//        textView_tv = findViewById(R.id.test);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//        String firstname = sharedPreferences.getString(FIRST_NAME_KEY,"");
//        String lastname = sharedPreferences.getString(LAST_NAME_KEY,"");
//        String email = sharedPreferences.getString(EMAIL_KEY,"");
//        String userId = sharedPreferences.getString(USER_ID_KEY,"");
//
//        Log.d(TAG, "firstname: " + firstname);
//        Log.d(TAG, "lastname: " + lastname);
//        Log.d(TAG, "email: " + email);
//        Log.d(TAG, "userId: " + userId);
//
//
//
//        textView_tv.setText("firstname: " + firstname + "\n"
//                + "lastname: " + lastname + "\n"
//                + "email:" + email + "\n"
//                + "userid: " + userId);
    }
}