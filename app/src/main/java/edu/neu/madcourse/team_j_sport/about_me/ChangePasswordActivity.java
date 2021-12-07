package edu.neu.madcourse.team_j_sport.about_me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;
import edu.neu.madcourse.team_j_sport.navi_bar.MeFragment;
import edu.neu.madcourse.team_j_sport.user_auth.ForgotPasswordActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    private static final String EMAIL_KEY = "email";

    private EditText etOldPassword, etNewPassword, etNewPasswordRetype;
    private Button btnSubmit;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;

    private String email, newPassword, retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();

        if (auth == null) {
            Log.e(TAG, "Firebase auth is null");
        }

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etNewPasswordRetype = findViewById(R.id.et_new_password_retype);
        progressBar = findViewById(R.id.pb_change_password);

        btnSubmit = findViewById(R.id.btn_change_password_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = etNewPassword.getText().toString();
                retypePassword = etNewPasswordRetype.getText().toString();

                if (isIllegalPassword(newPassword)) {
                    etNewPassword.setError("Password must have at least 6 characters");
                    etNewPassword.requestFocus();
                } else if (!isSame(newPassword, retypePassword)) {
                    etNewPasswordRetype.setError("Password does not match!");
                    etNewPasswordRetype.requestFocus();
                } else {
                    email = sharedPreferences.getString(EMAIL_KEY, "email");
                    changePassword(newPassword, email);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent mainToStart = new Intent(ChangePasswordActivity.this, MainActivity.class);
                            ChangePasswordActivity.this.startActivity(mainToStart);
                            ChangePasswordActivity.this.finish();
                        }
                    }, 2000);
                }
            }
        });
    }

    private boolean isIllegalPassword(String s) {
        return s == null || s.length() < 6;
    }

    private boolean isSame(String s, String r) {
        return s.equals(r);
    }

    private void changePassword(String email, String newPassword) {
        Log.d(TAG, String.format("email:%s, new password:%s", email, newPassword));

        progressBar.setVisibility(View.VISIBLE);

        auth.getCurrentUser().updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("You have successfully changed your password!");
                        } else {
                            showToast("Try again! Something wrong happened!");
                        }
                    }
                });
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}