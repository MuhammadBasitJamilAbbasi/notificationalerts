package com.bj.georgehousetrust.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.databinding.ActivityOtpBinding;
import com.bj.georgehousetrust.mail.SendMails;
import com.bj.georgehousetrust.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class otp extends AppCompatActivity {
    ActivityOtpBinding binding;

int Code_sended;
    Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String email= getIntent().getStringExtra("email");
         user= getIntent().getParcelableExtra("user");
        Code_sended = new Random().nextInt(900000) + 100000;
       String message="<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>OTP Email</title>\n" +
                "    <style>\n" +
                "      /* Set some global styles */\n" +
                "      body {\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        font-family: 'Open Sans', sans-serif;\n" +
                "        font-size: 16px;\n" +
                "        line-height: 1.5;\n" +
                "        color: #333;\n" +
                "        background-color: #f8f8f8;\n" +
                "      }\n" +
                "      h1, h2, h3, h4, h5, h6 {\n" +
                "        margin-top: 0;\n" +
                "        margin-bottom: 0.5em;\n" +
                "        font-weight: normal;\n" +
                "      }\n" +
                "      p {\n" +
                "        margin-top: 0;\n" +
                "        margin-bottom: 1em;\n" +
                "      }\n" +
                "      a {\n" +
                "        color: #0072c6;\n" +
                "        text-decoration: none;\n" +
                "      }\n" +
                "      a:hover {\n" +
                "        text-decoration: underline;\n" +
                "      }\n" +
                "      \n" +
                "      /* Set some styles specific to this email */\n" +
                "      .email-wrapper {\n" +
                "        max-width: 600px;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 20px;\n" +
                "        background-color: #fff;\n" +
                "        border: 1px solid #ddd;\n" +
                "        box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "      }\n" +
                "      .email-header {\n" +
                "        border-bottom: 1px solid #ddd;\n" +
                "        margin-bottom: 20px;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      .email-header h1 {\n" +
                "        margin: 0;\n" +
                "        font-size: 36px;\n" +
                "        font-weight: bold;\n" +
                "        color: #333;\n" +
                "      }\n" +
                "      .email-body {\n" +
                "        margin-bottom: 20px;\n" +
                "        background-color: #f5f5f5;\n" +
                "        border-radius: 10px;\n" +
                "        padding: 30px;\n" +
                "      }\n" +
                "      .otp-container {\n" +
                "        display: flex;\n" +
                "        justify-content: center;\n" +
                "        align-items: center;\n" +
                "        height: 80px;\n" +
                "        border: 2px solid #0072c6;\n" +
                "        border-radius: 10px;\n" +
                "        font-size: 52px;\n" +
                "        font-weight: bold;\n" +
                "        color: #0072c6;\n" +
                "      }\n" +
                "      .email-footer {\n" +
                "        border-top: 1px solid #ddd;\n" +
                "        padding-top: 20px;\n" +
                "        text-align: center;\n" +
                "        font-size: 14px;\n" +
                "      }\n" +
                "      .email-footer p {\n" +
                "        margin-bottom: 0;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"email-wrapper\">\n" +
                "      <div class=\"email-header\">\n" +
                "        <h1>BJ Technology & Services</h1>\n" +
                "      </div>\n" +
                "      <div class=\"email-body\">\n" +
                "        <p>Dear customer,</p>\n" +
                "        <p>Thank you for signing up for our service. Your OTP is:</p>\n" +
                "        <div class=\"otp-container\">"+Code_sended+"</div>\n" +
                "        <p>Please enter this OTP on the sign-up page to complete your registration.</p>\n" +
                "        <p>If you did not request this OTP, please ignore this email.</p>\n" +
                "      </div>\n" +
                "      <div class=\"email-footer\">\n" +
                "        <p>&copy; BJ Technology & Services (+92)3355739701</p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";

        SendMails sm = new SendMails(otp.this,email, getString(R.string.app_name), message);
        sm.execute();

        binding.buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textotp= Integer.parseInt(binding.editTextOtp.getText().toString());
                if(Code_sended==textotp)
                {
                    Toast.makeText(otp.this, "Verification Completed ", Toast.LENGTH_SHORT).show();

                    signupdata();
                }else {
                    Toast.makeText(otp.this,"Incorrect OTP ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMails sm = new SendMails(otp.this,email, getString(R.string.app_name), message);
                sm.execute();
            }
        });

    }

    public void signupdata()
    {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User account created successfully
                            FirebaseUser userfirebase = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = userfirebase.getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Profiles").child(userId);
                            Users User=new Users(user.getName(),user.getEmail(),user.getPassword(),user.getPhonenumber(),user.getSkills(),user.getIntrest());
                            userRef.setValue(User);
                            Intent i=new Intent(getApplicationContext(), Homescreen.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(otp.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            // Redirect to the main activity or do something else
                        } else {
                            // User account creation failed
                            Toast.makeText(otp.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}