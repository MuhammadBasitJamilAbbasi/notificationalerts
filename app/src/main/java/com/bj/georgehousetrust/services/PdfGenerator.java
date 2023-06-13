package com.bj.georgehousetrust.services;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.widget.Toast;

import com.bj.georgehousetrust.BuildConfig;
import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.models.Notification;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    private static final int PERMISSION_REQUEST_CODE = 123;

    public static void generatePDF(Context context, List<Notification> names,String titleno) {
        // Check if the required storage permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            return;
        }

        // Create a new PDF document
        PdfDocument document = new PdfDocument();

        // Create a page info with the desired page attributes
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        // Create a page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Create a canvas to draw the content
        Canvas canvas = page.getCanvas();
        int yPos = 50;

        // Set up the paint attributes
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20f);

        Paint heading = new Paint();
        heading.setColor(Color.RED);
        heading.setTextSize(40f);

        canvas.drawText(titleno, 180, yPos, heading);
        yPos += 60;

        // Draw the list name and email in the PDF document

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).getId();
            String email = names.get(i).getTitle();
            canvas.drawText("Name: " + name, 50, yPos, paint);
            canvas.drawText("Email: " + email, 50, yPos + 30, paint);
            yPos += 60;
        }

        // Finish the page
        document.finishPage(page);

        // Save the document
        long currentMilliseconds = System.currentTimeMillis();
        String name= String.valueOf(currentMilliseconds);
        String fileName = context.getString(R.string.app_name)+name+".pdf";
        File outputFile = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            document.writeTo(new FileOutputStream(outputFile));
            Toast.makeText(context, "PDF Generated "+fileName, Toast.LENGTH_SHORT).show();

           // openPDF(context,fileName);

            // Success message or further processing if needed
        } catch (IOException e) {
            e.printStackTrace();
            // Error handling
        }

        // Close the document
        document.close();
    }
    private static  void openPDF(Context context,String name) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/"+name);

        if (pdfFile.exists()) {
            // Create an intent to open the PDF file
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // PDF viewer app is not installed, show error message or handle it accordingly
                Toast.makeText(context, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

