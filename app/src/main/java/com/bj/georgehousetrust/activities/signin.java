package com.bj.georgehousetrust.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.admin.admin_home;
import com.bj.georgehousetrust.databinding.ActivityMainBinding;
import com.bj.georgehousetrust.models.LoadingProgressDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class signin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001;
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    LoadingProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth= FirebaseAuth.getInstance();
      progressDialog=new LoadingProgressDialog (this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.startAlertDialog ();

                if (binding.emailEditText.getText().toString().isEmpty() || binding.passwordEditText.getText().toString().isEmpty()) {

                    Toast.makeText(signin.this,"Please enter Email/Password",Toast.LENGTH_SHORT).show();

               progressDialog.dismissDialog();} else {


                    if (binding.emailEditText.getText().toString().equals("admin@bjtech.com")) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        db.getReference("Admin").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String password = dataSnapshot.getValue(String.class);
                                // use the password value here
                                if (password.equals(binding.passwordEditText.getText().toString())) {
                                    startActivity(new Intent(getApplicationContext(), admin_home.class));
                                    finish();
                                    progressDialog.dismissDialog();
                                } else {
                                    Toast.makeText(signin.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismissDialog();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // handle database error here
                                progressDialog.dismissDialog();
                            }
                        });


                    } else {
                        signIn(binding.emailEditText.getText().toString(), binding.passwordEditText.getText().toString());
                    }

                }

            }});

        // Configure sign-in to request the user's ID, email address, and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

// Build a GoogleSignInClient with the options specified by gso
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }

    private void signIn(String email, String password) {


        DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("Profiles");

        profilesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidCredentials = false;

                for (DataSnapshot profileSnapshot : dataSnapshot.getChildren()) {
                    String userId = profileSnapshot.getKey();

                    String userEmail = profileSnapshot.child("email").getValue(String.class);
                    String userPassword = profileSnapshot.child("password").getValue(String.class);

                    String name=profileSnapshot.child("name").getValue(String.class);
                    if (userEmail != null && userEmail.equals(email) && userPassword != null && userPassword.equals(password)) {
                        isValidCredentials = true;
                        SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id",userId);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        progressDialog.dismissDialog();
                        // Save other user data as needed
                        editor.apply();
                        break;
                    }
                }

                if (isValidCredentials) {
                    // Email and password are valid

                    // Proceed with the desired action (e.g., go to home screen)
                    startActivity(new Intent(signin.this,Homescreen.class));
                    finish();
                } else {
                    progressDialog.dismissDialog();
                    // Email or password is invalid
                    Toast.makeText(signin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                progressDialog.dismissDialog();
                Toast.makeText(signin.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkdate(String userId )
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressDialog.dismissDialog();
                    // User data exists in the database
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    // Retrieve other user data as needed

                    // Save user data in SharedPreferences


                    Toast.makeText(signin.this, "User data retrieved successfully",
                            Toast.LENGTH_SHORT).show();

                    // Proceed to the Home screen or perform other actions
                    Intent i = new Intent(getApplicationContext(), Homescreen.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "DatabaseError: " + error.getMessage());
                Toast.makeText(signin.this, "Failed to retrieve user data",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();

            }
        });
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
     mAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();
                           if(user!=null)
                           {
                               String id=user.getUid();
                               DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(id);
                               userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if (snapshot.exists()) {
                                           progressDialog.dismissDialog();
                                           // User data exists in the database
                                           String name = snapshot.child("name").getValue(String.class);
                                           String email = snapshot.child("email").getValue(String.class);
                                           // Retrieve other user data as needed

                                           // Save user data in SharedPreferences
                                           SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
                                           SharedPreferences.Editor editor = sharedPreferences.edit();
                                           editor.putString("id",id);
                                           editor.putString("name", name);
                                           editor.putString("email", email);
                                           // Save other user data as needed
                                           editor.apply();

                                           Toast.makeText(signin.this, "User data retrieved successfully",
                                                   Toast.LENGTH_SHORT).show();

                                           // Proceed to the Home screen or perform other actions
                                           Intent i = new Intent(getApplicationContext(), Homescreen.class);
                                           startActivity(i);
                                           finish();
                                       }else
                                       {
                                           progressDialog.dismissDialog();
                                           Intent i=new Intent(getApplicationContext(),signup.class);
                                           i.putExtra("name",user.getDisplayName());
                                           i.putExtra("email",user.getEmail());
                                           startActivity(i);
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Log.d(TAG, "DatabaseError: " + error.getMessage());
                                       Toast.makeText(signin.this, "Failed to retrieve user data",
                                               Toast.LENGTH_SHORT).show();


                                   }
                               });
                           }

                            // You can retrieve the user's information and perform necessary actions here
                        } else {
                            // Sign in failed, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(signin.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.startAlertDialog();
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            try {
                // Signed in successfully, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Handle Google Sign-In failure
                Log.w(TAG, "Google sign-in failed", e);
            }
        }
    }




}