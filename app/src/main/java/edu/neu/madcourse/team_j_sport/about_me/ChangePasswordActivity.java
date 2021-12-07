package edu.neu.madcourse.team_j_sport.about_me;

import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.R;

import android.os.Bundle;
import android.widget.TextView;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tvOldPassword, tvNewPassword, tvNewPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }
}