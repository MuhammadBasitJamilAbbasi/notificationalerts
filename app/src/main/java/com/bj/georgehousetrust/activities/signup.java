package com.bj.georgehousetrust.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bj.georgehousetrust.databinding.ActivitySignupBinding;
import com.bj.georgehousetrust.models.LoadingProgressDialog;
import com.bj.georgehousetrust.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LoadingProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        String name=getIntent().getStringExtra("name");
        String email=getIntent().getStringExtra("email");
        binding.nameEditText.setText(name);
        binding.emailEditText.setText(email);
        auth=FirebaseAuth.getInstance();
        progressDialog=new LoadingProgressDialog (this);
        if(auth.getCurrentUser()!=null)
        {
            auth.signOut();
        }
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.emailEditText.getText().toString().isEmpty())
                {
                    binding.emailEditText.setError("Enter vaild Email");
                }else if(binding.phoneEditText.getText().toString().isEmpty())
                {
                    binding.phoneEditText.setError("Enter valid Phonenumber");
                }
                else if(binding.nameEditText.getText().toString().isEmpty())
                {
                    binding.nameEditText.setError("Enter Name");
                }
                else if(binding.passwordEditText.getText().toString().isEmpty())
                {
                    binding.passwordEditText.setError("Enter Password");
                }
                else {
                    signIn(binding.emailEditText.getText().toString());
                }
            }
        });

    }
    public void signupdata()
    {


                            // User account created successfully
                            Users User=new Users(
                                    binding.nameEditText.getText().toString(),
                                    binding.emailEditText.getText().toString(),
                                    binding.passwordEditText.getText().toString(),
                                    binding.phoneEditText.getText().toString(),
                                    binding.skillsEditText.getText().toString(),
                                    binding.intrestEditText.getText().toString());
                            Intent i=new Intent(signup.this, otp.class);
                            i.putExtra("email",binding.emailEditText.getText().toString());
                            i.putExtra("user", (Parcelable) User);
                            startActivity(i);



    }
    private void signIn(String email) {
        progressDialog.startAlertDialog ();
        DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("Profiles");

        profilesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot profileSnapshot : dataSnapshot.getChildren()) {

                    String userEmail = profileSnapshot.child("email").getValue(String.class);

                    if (userEmail != null && userEmail.equals(email)) {

                        progressDialog.dismissDialog();
                        Toast.makeText(signup.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismissDialog();
                        signupdata();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                progressDialog.dismissDialog();
                Toast.makeText(signup.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void checkExistingUser(String email) {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//        mAuth.fetchSignInMethodsForEmail(email)
//                .addOnCompleteListener(this, new OnCompleteListener<SignInMethodQueryResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//                        if (task.isSuccessful()) {
//                            // Check if there are any existing sign-in methods for the email address
//                            SignInMethodQueryResult result = task.getResult();
//                            if (result != null && result.getSignInMethods() != null
//                                    && result.getSignInMethods().size() > 0) {
//                                // Existing user
//                                Log.d(TAG, "User with email " + email + " already exists");
//                                Toast.makeText(signup.this, "User already exists", Toast.LENGTH_SHORT).show();
//                                // Show an error message or take appropriate action
//                            } else {
//
//                            }
//                        } else {
//                            Log.w(TAG, "Error fetching sign-in methods for email", task.getException());
//                            // Show an error message or take appropriate action
//                            Toast.makeText(signup.this, "Error fetching sign-in methods for email", Toast.LENGTH_SHORT).show();
//                            // Show an error message or take appropriate action
//                        }
//                    }
//                });
//    }

}