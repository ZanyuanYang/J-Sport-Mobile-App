package edu.neu.madcourse.team_j_sport.navi_bar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.APIService;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.Client;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.Data;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.MyResponse;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.NotificationSender;
import edu.neu.madcourse.team_j_sport.SendNotificationPack.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    public static final String TAG = "EventListActivity";
    public static final String USERS_TABLE_KEY = "users";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    public static final String GET_USER_KEY = "get user";

    View view;

    // notification
    EditText UserTB,Title,Message;
    Button send;
    private APIService apiService;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post, container, false);

        send();
        // Inflate the layout for this fragment
        return view;
    }

    private void send(){
        //--------------------------notification start--------------------------------
        UserTB=view.findViewById(R.id.UserID);
        Title=view.findViewById(R.id.Title);
        Message=view.findViewById(R.id.Message);
        send=view.findViewById(R.id.sendNotification);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Tokens").child(UserTB.getText().toString()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String usertoken = dataSnapshot.getValue(String.class);
                        sendNotifications(usertoken, Title.getText().toString().trim(),Message.getText().toString().trim());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        UpdateToken();
        //--------------------------notification end--------------------------------
    }


    //--------------------------notification start--------------------------------
    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        System.out.println("fail");
//                        Toast.makeText(View.HomepageActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }else{
                        System.out.println("success");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}