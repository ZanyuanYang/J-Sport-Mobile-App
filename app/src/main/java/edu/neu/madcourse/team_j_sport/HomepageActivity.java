package edu.neu.madcourse.team_j_sport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.Lifecycle;
import edu.neu.madcourse.team_j_sport.navi_bar.EventFragment;
import edu.neu.madcourse.team_j_sport.navi_bar.MeFragment;
import edu.neu.madcourse.team_j_sport.navi_bar.PostFragment;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_homepage);

        initView();
        initFragment();
        initEvent();
        selectFragment(1);
    }

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
//        fragmentTransaction.add(R.id.id_content, meFragment);
        fragmentTransaction.commit();

        fm.beginTransaction().add(R.id.id_content, meFragment)
                .setMaxLifecycle(meFragment, Lifecycle.State.RESUMED)
                .commit();
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

}