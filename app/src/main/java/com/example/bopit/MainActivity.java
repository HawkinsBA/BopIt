package com.example.bopit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private String difficulty;
    private Button start;
    private Button setDifficulty;
    private AlertDialog difficultySelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty="Easy";
        initUI();
        initDifficultySelect();
        initOnClickListeners();


    }

    private void openGameActivity() {
        Log.d(TAG,"openGameActivity: Opening game activity");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY",difficulty);
        startActivity(intent);
    }

    private void initUI(){
        Log.d(TAG,"initUI: initiali");
        start = findViewById(R.id.start);
        setDifficulty = findViewById(R.id.difficulty);
    }
    private void initOnClickListeners(){
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openGameActivity();
            }
        });
        setDifficulty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                difficultySelection.show();
            }
        });
    }

    private void initDifficultySelect (){
        final View mView = getLayoutInflater().inflate(R.layout.dialog_difficulty_selection, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
         difficultySelection = mBuilder.create();
    }

}
