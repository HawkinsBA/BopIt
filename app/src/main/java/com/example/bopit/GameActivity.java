package com.example.bopit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private final static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initUIComponents();
        initGameComponents();
        initAccelerometer();
    }


    private void initUIComponents() {
        Log.d(TAG, "initUIComponents: Initializing UI components.");
        ImageView moveImage = findViewById(R.id.moveImageView);
        TextView moveName = findViewById(R.id.moveNameView);
        TextView timer = findViewById(R.id.moveTimeView);
    }

    private void initGameComponents() {
        Log.d(TAG, "initGameComponents: Initializing game components.");
        int points = 0;
        double nextMoveDelay = processDifficulty();
        ArrayList<String> moveSequence = new ArrayList<>();
    }

    //TODO: Change this depending on how difficulty selection is packaged.
    private double processDifficulty() {
        String difficulty = ""; //TODO: Package difficulty selection and handle it, difficulty = "" is a placeholder.
        Log.d(TAG, "processDifficulty: Setting game difficulty to " + difficulty + ".");
        if (difficulty.equals("Easy")) return 4.0;
        else if (difficulty.equals("Medium")) return 3.0;
        else return 2.0;
    }

    private void initAccelerometer() {
        Log.d(TAG, "initAccelerometer: Initializing accelerometer.");
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
}
