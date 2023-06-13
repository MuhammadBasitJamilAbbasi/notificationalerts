package com.bj.georgehousetrust.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.adapters.NotificationAdapter;
import com.bj.georgehousetrust.models.LoadingProgressDialog;
import com.bj.georgehousetrust.models.Notification;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Homescreen extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        FirebaseMessaging.getInstance().subscribeToTopic("Alerts");
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView= findViewById(R.id.mynavigationdraw);
        navigationView.setItemIconTintList(null);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click here
                if (item.getItemId() == R.id.nav_logout) {
                    // Perform logout action



                    SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // Use clear() method to remove all key-value pairs
                    editor.apply();
                    Intent intent = new Intent(Homescreen.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });



        LoadingProgressDialog progressDialog=new LoadingProgressDialog (this);
        progressDialog.startAlertDialog ();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        List<Notification> notificationList = new ArrayList<>();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        NotificationAdapter adapter = new NotificationAdapter(notificationList,this);


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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

      //  Toast.makeText(getApplicationContext(),item.getItemId()+"",Toast.LENGTH_SHORT).show();

         if(item.getItemId()==R.id.nav_logout)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Use clear() method to remove all key-value pairs
            editor.apply();
            Intent intent = new Intent(Homescreen.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}