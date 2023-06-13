package com.bj.georgehousetrust.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.adapters.NotificationAdapter_second;
import com.bj.georgehousetrust.models.LoadingProgressDialog;
import com.bj.georgehousetrust.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class customerdetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerdetails);
        RecyclerView recyclerView = findViewById(R.id.notification_recyclerView);
        LoadingProgressDialog progressDialog=new LoadingProgressDialog (this);
        progressDialog.startAlertDialog ();
        List<Notification> notificationList = new ArrayList<>();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        NotificationAdapter_second adapter = new NotificationAdapter_second(notificationList,this);
        database.getReference().child("Notifications")
                .limitToLast(100)
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0) {

                            // Room Available
                            notificationList.clear();
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                Notification groups = new Notification(
                                        childSnap.child("id").getValue().toString()
                                        , childSnap.child("title").getValue().toString(),
                                        childSnap.child("message").getValue().toString());
                                notificationList.add(groups);
                            }
                            progressDialog.dismissDialog();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismissDialog();

                    }
                });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}