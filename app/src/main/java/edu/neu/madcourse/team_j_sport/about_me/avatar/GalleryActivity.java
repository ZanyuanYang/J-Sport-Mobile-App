package edu.neu.madcourse.team_j_sport.about_me.avatar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.about_me.MyPostsActivity;
import edu.neu.madcourse.team_j_sport.navi_bar.MeFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    private static final String USER_ID = "user id";

    private ActivityResultLauncher<Intent> mGetContent;
    private SharedPreferences sharedPreferences;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String userId;

    private PhotoUtil photoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        init();

        pickPhoto();
    }

    private void init() {

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "userId");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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

                            photoUtil.uploadPhoto(userId, photoUri);

                            // Load the selected image into a preview
//                            ImageView ivPreview = (ImageView) findViewById(R.id.iv_preview);
//                            ivPreview.setImageBitmap(selectedImage);

//                            Fragment frg = null;
//                            frg = getFragmentManager().findFragmentById(R.id.frg_me);
//                            FragmentTransaction ftr = getFragmentManager().beginTransaction();
//                            ftr.detach(frg).attach(frg).commit();
                            finish();
                        }
                    }
                });
    }
//
//    private void uploadPhoto(Uri photoUri) {
//        Log.d(TAG, "Start uploading");
//        Uri file = photoUri;
//
//        StorageReference avatarRef = storageRef.child("avatars/" + userId + ".jpg");
//        UploadTask uploadTask = avatarRef.putFile(file);
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Log.d(TAG, "Upload Failed");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                Log.d(TAG, "Upload success");
//            }
//        });
//
//    }

    public void pickPhoto() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        mGetContent.launch(galleryIntent);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Log.d(TAG, "Loading from URI");
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
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