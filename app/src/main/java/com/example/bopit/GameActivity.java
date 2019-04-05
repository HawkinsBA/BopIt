package com.example.bopit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final static String TAG = "GameActivity";
    ImageView moveImage;
    TextView moveName;
    TextView timerText;
    CountDownTimer timer;
    int points;
    double nextMoveDelay, previousX, previousY, previousZ;
    ArrayList<Move> movesList;
    Sensor accelerometer;
    SensorManager sensorManager;
    Random rand;
    Move move;

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
        rand = new Random();
        initMovesList();
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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
                play();
            }
        };
        timer.start();
    }

    private void initMovesList() {
        Log.d(TAG, "initMovesList: Creating moves and adding them to movesList.");
        movesList = new ArrayList<>();
        Move up = new Move("Up", 3.0, 1.0);
        Move down = new Move("Down", -3.0, 1.0);
        Move left = new Move("Left", 4.0, 2.0);
        Move right = new Move("Right", -4.0, 2.0);
        Move twist = new Move("Twist", 8.0, 2.0);
        movesList.add(up);
        movesList.add(down);
        movesList.add(left);
        movesList.add(right);
        movesList.add(twist);
    }

    private void play() {
        Log.d(TAG, "play: New round started.");
        timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinish) {
                Log.d(TAG, "onTick: Updating timerText.");
                Integer secUntilFinish = (int) (millisUntilFinish / 1000);
                timerText.setText(secUntilFinish.toString());
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: Game over.");
                showGameOverDialog();
            }
        };

        move = initMove();
        final double successThreshold = move.getSuccessThreshold();
        final double acceptableDeviationMargin = move.getAcceptableDeviationMargin();

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(move.getName().equals("Up")) {
                    if(sensorEvent.values[1] - previousY >= successThreshold) {
                        Log.d(TAG, "onSensorChanged: UP success.");
                        processSuccess();
                    }else if(sensorEvent.values[0] > acceptableDeviationMargin || sensorEvent.values[2] > acceptableDeviationMargin) {
                        Log.d(TAG, "onSensorChanged: UP acceptable deviation exceeded.");
                        showGameOverDialog();
                        sensorManager.unregisterListener(this);
                    }else{
                        previousY = sensorEvent.values[1];
                    }
                }else if(move.getName().equals("Down")){
                    if(sensorEvent.values[1] - previousY <= successThreshold) {
                        processSuccess();
                    }else if(sensorEvent.values[0] > acceptableDeviationMargin || sensorEvent.values[2] > acceptableDeviationMargin) {
                        showGameOverDialog();
                        sensorManager.unregisterListener(this);
                    }else{
                        previousY = sensorEvent.values[1];
                    }
                }else if(move.getName().equals("Left")){
                    if(sensorEvent.values[0] - previousX >= successThreshold) {
                        processSuccess();
                    }else if(sensorEvent.values[1] > acceptableDeviationMargin || sensorEvent.values[2] > acceptableDeviationMargin) {
                        showGameOverDialog();
                        sensorManager.unregisterListener(this);
                    }else{
                        previousX = sensorEvent.values[0];
                    }
                }else if(move.getName().equals("Right")){
                    if(sensorEvent.values[0] - previousX <= successThreshold) {
                        processSuccess();
                    }else if(sensorEvent.values[1] > acceptableDeviationMargin || sensorEvent.values[2] > acceptableDeviationMargin) {
                        showGameOverDialog();
                        sensorManager.unregisterListener(this);
                    }else{
                        previousX = sensorEvent.values[0];
                    }
                }else if(move.getName().equals("Twist")){
                    if(sensorEvent.values[2] - previousZ <= successThreshold) {
                        processSuccess();
                    }else if(sensorEvent.values[1] > acceptableDeviationMargin || sensorEvent.values[0] > acceptableDeviationMargin) {
                        showGameOverDialog();
                        sensorManager.unregisterListener(this);
                    }else{
                        previousZ = sensorEvent.values[2];
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        }, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Move initMove() {
        move = movesList.get(rand.nextInt(5));
        moveName.setText(move.getName());
        return move;
    }

    private void showGameOverDialog() {
        Log.d(TAG, "showGameOverDialog: Showing game over dialog.");
        View mView = getLayoutInflater().inflate(R.layout.dialog_game_over, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
        mBuilder.setView(mView);
        AlertDialog gameOver = mBuilder.create();
        gameOver.show();
    }

    private void processSuccess() {
        points++;
    }
}
