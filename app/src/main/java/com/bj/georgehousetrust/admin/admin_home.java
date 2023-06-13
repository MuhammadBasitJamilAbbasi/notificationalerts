package com.bj.georgehousetrust.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.activities.SplashScreen;
import com.bj.georgehousetrust.models.Notification;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bj.georgehousetrust.Config.FCM_API;
import static com.bj.georgehousetrust.Config.contentType;
import static com.bj.georgehousetrust.Config.serverKey;
public class admin_home extends AppCompatActivity {
    public EditText title,message;
    Button send;
    static String NOTIFICATION_TITLE;
    static String NOTIFICATION_MESSAGE;
    static String TOPIC;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView= findViewById(R.id.mynavigationdraw);
        navigationView.setItemIconTintList(null);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title=findViewById(R.id.titleEditText);
        message=findViewById(R.id.messageEditText);
        send=findViewById(R.id.sendButton);
        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_notification).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_notification)
                {

                        startActivity(new Intent(admin_home.this,customerdetails.class));

                }
                return false;
            }
        });
  menu.findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                 if(item.getItemId()==R.id.nav_logout)
                {
                    Intent intent = new Intent(admin_home.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificatondata();

            }
        });

    }

    public void notificatondata()
    {

                            DatabaseReference postref=FirebaseDatabase.getInstance().getReference("Notifications");
                            DatabaseReference newuserRef = postref.push();
                            Notification notification=new Notification(newuserRef.getKey(),title.getText().toString(),message.getText().toString());
                            newuserRef.setValue(notification);
                             sendnotificationalerts(admin_home.this);

                            // Redirect to the main activity or do something else

    }

    public  void sendnotificationalerts( Context c)
    {
        String sho = "Alerts";
        TOPIC = "/topics/" + sho; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = title.getText().toString();

            NOTIFICATION_MESSAGE = message.getText().toString();

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notification.put("priority", "high");
            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);

        } catch (
                JSONException e) {
            Log.e("hi", "onCreate: " + e.getMessage());
        }
        sendNotification(notification,c);
    }
    private  void sendNotification(JSONObject notification, Context c) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(admin_home.this, "Sended...", Toast.LENGTH_SHORT).show();
                        //  Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                        // Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}