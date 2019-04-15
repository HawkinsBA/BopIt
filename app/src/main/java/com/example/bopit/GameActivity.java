package com.example.bopit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final static String TAG = "GameActivity";
    ImageView moveImage;
    TextView moveName, timerText, scoreView;
    Button finish;
    CountDownTimer timer;
    int points, successCount;
    double nextMoveDelay;
    ArrayList<Move> movesList;
    Sensor accelerometer;
    SensorManager sensorManager;
    Random rand;
    Move move;
    EditText playerName;

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
        Move left = new Move("Left", 11.0, 10.0);
        Move right = new Move("Right", -10.0, 10.0);
        Move twist = new Move("Twist", 12.0, 5.0);
        movesList.add(left);
        movesList.add(right);
        movesList.add(twist);
    }

    private void play() {
        Log.d(TAG, "play: New round started.");

        timer = new CountDownTimer((long) (nextMoveDelay * 1000), 1000) {
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
        timer.start();

        move = initMove();
        final double successThreshold = move.getSuccessThreshold();

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(move.getName().equals("Left")){
                    if(sensorEvent.values[0] >= successThreshold) {
                        Log.d(TAG, "onSensorChanged: Made correct move (left).");
                        sensorManager.unregisterListener(this);
                        timer.cancel();
                        processSuccess();
                    }
                }else if(move.getName().equals("Right")){
                    if(sensorEvent.values[0] <= successThreshold) {
                        Log.d(TAG, "onSensorChanged: Made correct move (right).");
                        sensorManager.unregisterListener(this);
                        timer.cancel();
                        processSuccess();
                    }
                }else if(move.getName().equals("Twist")){
                    if(sensorEvent.values[2] >= successThreshold) {
                        Log.d(TAG, "onSensorChanged: Made correct move (twist).");
                        sensorManager.unregisterListener(this);
                        timer.cancel();
                        processSuccess();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        }, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Move initMove() {
        move = movesList.get(rand.nextInt(3));
        moveName.setText(move.getName());
        return move;
    }

    private AlertDialog buildGameOverDialog(){
        Log.d(TAG, "buildDialog: Building game over dialog.");
        View mView = getLayoutInflater().inflate(R.layout.dialog_game_over, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
        mBuilder.setView(mView);
        playerName = mView.findViewById(R.id.playerName);
        finish = mView.findViewById(R.id.finish);
        scoreView = mView.findViewById(R.id.scoreView);
        scoreView.setText("Score: " + points);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Finish pressed.");
                try {
                    if(points>0)
                    saveScore();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        return mBuilder.create();
    }

    private void showGameOverDialog() {
        Log.d(TAG, "showGameOverDialog: Showing game over dialog.");
        AlertDialog gameOver = buildGameOverDialog();
        if (!(GameActivity.this).isFinishing()) {
            gameOver.show();
        }
        //gameOver.show();
    }

    private void processSuccess() {
        Log.d(TAG, "processSuccess: Adding point and reinitializing.");
        if (successCount == 10) {
            nextMoveDelay -= 0.2;
            successCount = 0;
        } else {
            successCount++;
        }
        points++;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                play();
            }
        }, 500);

    }

    private void saveScore() throws IOException {
        DataManager manager= new DataManager(getFilesDir());
        manager.saveScore(new Score(playerName.getText().toString(),points));
        Log.d(TAG,"score is "+points);
    }
}
