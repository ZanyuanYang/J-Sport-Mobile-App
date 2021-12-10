package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.about_me.ChangePasswordActivity;
import edu.neu.madcourse.team_j_sport.about_me.EditProfileActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyEventsActivity;
import edu.neu.madcourse.team_j_sport.about_me.MyPostsActivity;
import edu.neu.madcourse.team_j_sport.about_me.avatar.GalleryActivity;

import edu.neu.madcourse.team_j_sport.about_me.avatar.CameraActivity;
import edu.neu.madcourse.team_j_sport.about_me.avatar.GalleryActivity;



public class MeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MeFragment";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    private static final String USER_ID = "user id";

    private static final int RC_CODE_AVATAR = 666;

    private View view;
    private SharedPreferences sharedPreferences;
    private String userId;
    private ImageView ivAvatar;

    private FirebaseStorage storage;

    ActivityResultLauncher launcher;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_me, container, false);

        storage = FirebaseStorage.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "userId");

        init();

        return view;
    }

    private void init() {
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

        ivAvatar = view.findViewById(R.id.iv_avatar);

        loadAvatar();
    }

    public void loadAvatar() {
        Log.d(TAG, "Load avatar");
        StorageReference photoRef = storage.getReference().child("avatars/" + userId + ".jpg");

        Glide.with(this)
                .load(photoRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivAvatar);
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

                // TODO: Add a dialog to let user choose from Camera and Gallery
//                intent = new Intent(getActivity(), GalleryActivity.class);
                intent = new Intent(getActivity(), CameraActivity.class);
//                startActivity(intent);

                startActivityForResult(intent, RC_CODE_AVATAR);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult (" + requestCode + "," + resultCode + ")");

        if (requestCode == RC_CODE_AVATAR) {
            Log.d(TAG, "Trying to refresh");

            // Wait for 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                }
            }, 8000);

//            Fragment frg = null;
//            frg = getFragmentManager().findFragmentById(R.id.frg_me);
            getFragmentManager().beginTransaction().detach(this).commitNow();
            getFragmentManager().beginTransaction().attach(this).commitNow();
            Log.d(TAG, "Done refreshing");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i(TAG,"IsRefresh = Yes");
        }
    }
}