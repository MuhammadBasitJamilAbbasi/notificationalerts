package com.bj.georgehousetrust.services;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtils {

    public static void writeDataToCSV(List<String[]> data, Context context) {
        // Create the CSV file

        long currentMilliseconds = System.currentTimeMillis();
        String name= "GEORGE"+String.valueOf(currentMilliseconds)+".csv";
        File csvFile = new File(Environment.getExternalStorageDirectory(), name);

        try {
            // Create FileWriter and CSVWriter objects
            FileWriter fileWriter = new FileWriter(csvFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            // Write data to CSV file
            csvWriter.writeAll(data);

            // Close writers
            csvWriter.close();
            Toast.makeText(context, "Generated CSV", Toast.LENGTH_SHORT).show();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

