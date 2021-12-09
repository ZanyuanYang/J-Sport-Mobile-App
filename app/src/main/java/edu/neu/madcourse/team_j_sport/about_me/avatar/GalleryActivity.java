package edu.neu.madcourse.team_j_sport.about_me.avatar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    public static final int RESULT_GALLERY = 0;
    public final static int PICK_PHOTO_CODE = 1046;

    ActivityResultLauncher<Intent> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        mGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Handle the returned Uri
                        Log.d(TAG, "onActivityResult New");
                        if (result != null) {
                            Intent data = result.getData();
                            Log.d(TAG, "Result Data: " + data);
                            Uri photoUri = data.getData();
                            Log.d(TAG, "Uri: " + photoUri);

                            // Load the image located at photoUri into selectedImage
                            Bitmap selectedImage = loadFromUri(photoUri);
                            Log.d(TAG, String.valueOf(photoUri));

                            // Load the selected image into a preview
                            ImageView ivPreview = (ImageView) findViewById(R.id.iv_preview);
                            ivPreview.setImageBitmap(selectedImage);
                        }
                    }
                });

        pickPhoto();
    }

    public void pickPhoto() {

        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent , RESULT_GALLERY );

        mGetContent.launch(galleryIntent);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Log.d(TAG, "Loading from URI");
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 29) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}