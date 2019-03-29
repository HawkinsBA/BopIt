package com.example.bopit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private final static String TAG = "GameActivity";
    ImageView moveImage;
    TextView moveName;
    TextView timerText;
    CountDownTimer timer;
    int points;
    double nextMoveDelay;
    ArrayList<String> moveSequence;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initUIComponents();
        initGameComponents();
        initAccelerometer();
        initStartCountdown();
    }


    private void initUIComponents() {
        Log.d(TAG, "initUIComponents: Initializing UI components.");
        moveImage = findViewById(R.id.moveImageView);
        moveName = findViewById(R.id.moveNameView);
        timerText = findViewById(R.id.moveTimeView);
    }

    private void initGameComponents() {
        Log.d(TAG, "initGameComponents: Initializing game components.");
        points = 0;
        nextMoveDelay = processDifficulty();
        moveSequence = new ArrayList<>();
    }

    private double processDifficulty() {
        Bundle extras = getIntent().getExtras();
        String difficulty = extras.getString("DIFFICULTY");
        Log.d(TAG, "processDifficulty: Setting game difficulty to " + difficulty + ".");
        if (difficulty.equals("Easy")) return 4.0;
        else if (difficulty.equals("Medium")) return 3.0;
        else return 2.0;
    }

    private void initAccelerometer() {
        Log.d(TAG, "initAccelerometer: Initializing accelerometer.");
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initStartCountdown() {
        Log.d(TAG, "initStartCountdown: Initializing start countdown.");
        timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinish) {
                Integer secUntilFinish = (int) (millisUntilFinish / 1000);
                timerText.setText(secUntilFinish.toString());
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "timer.onFinish(): Starting the game.");
                Toast.makeText(GameActivity.this, "Game should start now.", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }

    //TODO: Figure out how to represent moves. Right now I'm leaning towards procedurally (each move is a data structure).
    //Reasoning behind procedural implementation -> Never going to make more moves but might want to add functions to the moves
    //we have represented.
    //Might want to meet with Dr. Ferrer to ask about this.
    //Does that create too much code replication?
}
