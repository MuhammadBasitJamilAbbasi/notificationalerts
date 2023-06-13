package com.bj.georgehousetrust.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.adapters.UserresponseAdapter;
import com.bj.georgehousetrust.models.LoadingProgressDialog;
import com.bj.georgehousetrust.models.Notification;
import com.bj.georgehousetrust.services.CSVUtils;
import com.bj.georgehousetrust.services.PdfGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class responseusers extends AppCompatActivity {
    FirebaseDatabase database;
    List<Notification> notificationList;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responseusers);
        database = FirebaseDatabase.getInstance();
       notification= (Notification) getIntent().getParcelableExtra("notification");
        TextView title = findViewById(R.id.titleTextView);
        title.setText(notification.getTitle());
        RecyclerView recyclerView = findViewById(R.id.recyclerView_response);
        notificationList= new ArrayList<>();
        UserresponseAdapter adapter = new UserresponseAdapter(notificationList, this);
        Button export = findViewById(R.id.Export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomAlertDialog();
            }
        });
        LoadingProgressDialog progressDialog = new LoadingProgressDialog(this);
        progressDialog.startAlertDialog();
        database.getReference().child("Response").child(notification.getId())
                .limitToLast(100)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {

                            // Room Available
                            notificationList.clear();
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                Notification groups = new Notification(
                                        childSnap.child("cusname").getValue().toString()
                                        , childSnap.child("cusemail").getValue().toString(),
                                        childSnap.child("custphone").getValue().toString());
                                notificationList.add(groups);
                            }
                            progressDialog.dismissDialog();
                            adapter.notifyDataSetChanged();
                        } else {
                            progressDialog.dismissDialog();
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
    private void showCustomAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exportdialoge, null);
        builder.setView(dialogView);


        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        alertDialog.getWindow().setAttributes(layoutParams);
        LinearLayout button=dialogView.findViewById(R.id.exportpdf);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfGenerator.generatePDF(responseusers.this,notificationList,notification.getTitle());

                alertDialog.dismiss();
            }
        });

        LinearLayout csvexport=dialogView.findViewById(R.id.exportcsv);
        csvexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"TiTle", notification.getTitle()});
                data.add(new String[]{"Name", "Email"});
                for (int i = 0; i < notificationList.size(); i++) {
                    data.add(new String[]{notificationList.get(i).getId(), notificationList.get(i).getTitle()});
                }


                // Write data to CSV file
                CSVUtils.writeDataToCSV(data, responseusers.this);
                alertDialog.dismiss();
            }
        });
        // Customize the AlertDialog appearance and behavior


        // Create and show the AlertDialog

        alertDialog.show();
    }
}