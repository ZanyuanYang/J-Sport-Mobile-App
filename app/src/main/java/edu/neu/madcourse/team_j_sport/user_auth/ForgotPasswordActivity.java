package edu.neu.madcourse.team_j_sport.user_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.madcourse.team_j_sport.MainActivity;
import edu.neu.madcourse.team_j_sport.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView back_to_login_btn, emailEditText;
    private ProgressBar progressBar;
    private Button resetPasswordButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.et_old_password);
        resetPasswordButton = findViewById(R.id.submit_reset);
        back_to_login_btn = findViewById(R.id.back_to_login);
        progressBar = findViewById(R.id.progressBar);

        back_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetPassword();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run(){
                        Intent mainToStart = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                        ForgotPasswordActivity.this.startActivity(mainToStart);
                        ForgotPasswordActivity.this.finish();
                    }
                }, 2000);
            }
        });
    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();
        Log.d("email", "email: " + email);

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task){

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your to reset your password!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}