package edu.neu.madcourse.team_j_sport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSendStickerActivity extends AppCompatActivity {
    private static final String TAG = "UserSendStickerActivity";
    private GridView stickerGridView;
    private List<StickerGridViewCell> cellList;
    private List<StorageReference> refList;
    public static final String GET_USER_KEY = "get user";
    public static final String GET_USER_ID = "get user id";

    private String userName;
    private Long userId;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_sticker);
        initView();

    }
    public void initView(){
        refList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        userName = sharedPreferences.getString(GET_USER_KEY,"");
        userId = sharedPreferences.getLong(UserLoginActivity.GET_USER_ID,1L);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            refList.add(item);

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
        stickerGridView.setAdapter(new StickerGridViewAdapter(this, cellList));
        stickerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setAlertDialog(i);
//                Toast.makeText(UserSendStickerActivity.this, "" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAlertDialog(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setIcon(R.drawable.tiger);
        builder.setTitle("Choose Receive User");
        LayoutInflater in = LayoutInflater.from(this);
        View v= in.inflate(R.layout.user_send_sticker_dialog,null);
        builder.setView(v);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText dt = v.findViewById(R.id.Sticker_Dialog_Username);
                String receivedUsername = dt.getText().toString();
                findUserIdByUserName(receivedUsername, position);
                writeSent(position,receivedUsername);
                Toast.makeText(UserSendStickerActivity.this, "Sent Sticker Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(UserSendStickerActivity.this, "Cancel" + i, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void writeReceived(int position, Long receivedUserId){
        StickerGridViewCell cell = (StickerGridViewCell) cellList.get(position);
        String imageName = cell.imageReference.getName();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childCnt = snapshot.getChildrenCount();
                ReceivedMessages item = new ReceivedMessages(imageName, userName, getDate());
                if (snapshot.exists()){

                    mDatabase.child("Users")
                            .child(String.valueOf(receivedUserId))
                            .child("ReceivedMessages")
                            .child(String.valueOf(childCnt + 1)).setValue(item);
                }else{
                    mDatabase.child("Users")
                            .child(String.valueOf(receivedUserId))
                            .child("ReceivedMessages")
                            .child(String.valueOf(1)).setValue(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.child("Users")
                .child(String.valueOf(receivedUserId))
                .child("ReceivedMessages").addListenerForSingleValueEvent(listener);

    }
    private void writeSent(int position, String receivedUserName){
        StickerGridViewCell cell = (StickerGridViewCell) cellList.get(position);
        String imageName = cell.imageReference.getName();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long childCnt = snapshot.getChildrenCount();
                SentMessages item = new SentMessages(imageName, receivedUserName, getDate());
                if (snapshot.exists()){


                    mDatabase.child("Users")
                            .child(String.valueOf(userId))
                            .child("SentMessages")
                            .child(String.valueOf(childCnt + 1)).setValue(item);
                }else{
                    mDatabase.child("Users")
                            .child(String.valueOf(userId))
                            .child("SentMessages")
                            .child(String.valueOf(1)).setValue(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("Users")
                .child(String.valueOf(userId))
                .child("SentMessages").addListenerForSingleValueEvent(listener);
    }
    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String dstr = "" + simpleDateFormat.format(date);
        return dstr;
    }

    private class SentMessages{
        public String imageName;
        public String receiverName;
        public String date;
        public SentMessages(String imageName, String userName, String date){
            this.imageName = imageName;
            this.receiverName = userName;
            this.date = date;
        }
        public SentMessages(){

        }
    }
    private class ReceivedMessages{
        public String imageName;
        public String senderName;
        public String date;
        public ReceivedMessages(String imageName, String userName, String date){
            this.imageName = imageName;
            this.senderName = userName;
            this.date = date;
        }
        public  ReceivedMessages(){

        }
    }
    private  void findUserIdByUserName(String userName, int position){

        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()){
                    if(ss.child("username").getValue().equals(userName)){
                        Long receivedUserId = (Long) ss.child("id").getValue();
                        writeReceived(position, receivedUserId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}