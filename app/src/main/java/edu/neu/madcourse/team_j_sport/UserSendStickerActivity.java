package edu.neu.madcourse.team_j_sport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserSendStickerActivity extends AppCompatActivity {
    private static final String TAG = "UserSendStickerActivity";
    private GridView stickerGridView;
    private List<StickerGridViewCell> cellList;
    private List<StorageReference> refList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_sticker);
        initView();
    }
    public void initView(){
        refList = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                            Log.w(TAG, "!!!"+ prefix.toString());
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            refList.add(item);
                            Log.w(TAG, " "+ item.toString());

                        }
                        loadStickers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.w(TAG, "Failed.");
                    }
                });
    }
    public void loadStickers(){
        stickerGridView = (GridView) findViewById(R.id.SendSticker_GridView);
        cellList = new ArrayList<>();

        for (int i = 0; i < refList.size(); i++) {
            StickerGridViewCell cell = new StickerGridViewCell();
            cell.imageReference = refList.get(i);
            cellList.add(cell);
        }
        //        .notifyItemInserted(0)
        stickerGridView.setAdapter(new StickerGridViewAdapter(this, cellList));
    }

}