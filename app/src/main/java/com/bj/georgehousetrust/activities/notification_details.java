package com.bj.georgehousetrust.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.models.Notification;
import com.bj.georgehousetrust.models.ResponseCustomer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notification_details extends AppCompatActivity {
    private TextView titleTextView;
    private TextView messageTextView;
    private Button acceptButton;
    FirebaseDatabase database;
    private Button rejectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        Notification notification = (Notification) getIntent().getParcelableExtra("notification");

        database=FirebaseDatabase.getInstance();

        titleTextView = findViewById(R.id.titleTextView);
        messageTextView = findViewById(R.id.messageTextView);
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);

        titleTextView.setText(notification.getTitle());
        messageTextView.setText(notification.getMessage());

        SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        String name=sharedPreferences.getString("name", "");
        String email=sharedPreferences.getString("email", "");


        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Assuming you have a Firebase Realtime Database reference named "responsesRef"
               // Replace with the actual customer ID
                database.getReference().child("Response").child(notification.getId()).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            showCustomAlertDialog();
                            // ID exists in the response table, show "already added" toast
                            Toast.makeText(getApplicationContext(), "Already added", Toast.LENGTH_SHORT).show();
                        } else {

                            ResponseCustomer response= new ResponseCustomer(userId,name,email,"bb",notification.getId(),notification.getTitle(),notification.getMessage(),"Accept");
                            // Save the response to the Firebase Realtime Database
                            database.getReference().child("Response").child(notification.getId()).child(userId).setValue(response);

                            // Show success toast or perform any other action
                            Toast.makeText(getApplicationContext(), "Response Added Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                        // Handle the error, if any
                    }
                });




            }
        });
    }

    private void showCustomAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        Button button=dialogView.findViewById(R.id.ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        // Customize the AlertDialog appearance and behavior


        // Create and show the AlertDialog

        alertDialog.show();
    }

}