package edu.neu.madcourse.team_j_sport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserSendStickerActivity extends AppCompatActivity {
    private static final String TAG = "UserSendStickerActivity";
    private static final int STICKER_NUMBER = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_sticker);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference[] refArray = new StorageReference[STICKER_NUMBER + 1];
        for (int i = 1; i <= STICKER_NUMBER; i++) {
            refArray[i] = storageRef.child("sticker_" + String.valueOf(i) + ".png");
        }

        ImageView imageView1 = findViewById(R.id.sticker1);
        ImageView imageView2 = findViewById(R.id.sticker2);
        ImageView imageView3 = findViewById(R.id.sticker3);
        ImageView imageView4 = findViewById(R.id.sticker4);
        ImageView imageView5 = findViewById(R.id.sticker5);
        ImageView imageView6 = findViewById(R.id.sticker6);
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                        .load(refArray[1])
                        .into(imageView1);
        Glide.with(this /* context */)
                .load(refArray[2])
                .into(imageView2);
        Glide.with(this /* context */)
                .load(refArray[3])
                .into(imageView3);
        Glide.with(this /* context */)
                .load(refArray[4])
                .into(imageView4);
        Glide.with(this /* context */)
                .load(refArray[5])
                .into(imageView5);
        Glide.with(this /* context */)
                .load(refArray[6])
                .into(imageView6);

    }
}