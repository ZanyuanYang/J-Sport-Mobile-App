package edu.neu.madcourse.team_j_sport.about_me.avatar;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import edu.neu.madcourse.team_j_sport.navi_bar.MeFragment;

public class PhotoUtil {

    public final static String TAG = "PhotoUtil";

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public PhotoUtil() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void uploadBitmap(String userId, byte[] data) {
        Log.d(TAG, "Start bitmap uploading");

        StorageReference targetRef = storageRef.child("avatars/" + userId + ".jpg");
        UploadTask uploadTask = targetRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Bitmap Upload Failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d(TAG, "Bitmap Upload Success");
            }
        });
    }

    public void uploadPhoto(String userId, Uri photoUri) {
        Log.d(TAG, "Start photo uploading");
        Uri file = photoUri;

        StorageReference avatarRef = storageRef.child("avatars/" + userId + ".jpg");
        UploadTask uploadTask = avatarRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Upload Failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d(TAG, "Upload success");
            }
        });
    }
}
