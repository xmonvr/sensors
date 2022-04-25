package com.example.sensors;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.saveData);
        SensorActivity sensorActivity = new SensorActivity(this);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view) {
                writeToFile(sensorActivity.getData());
            }
        });
    }


    private void writeToFile(String data) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "sensorData.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}