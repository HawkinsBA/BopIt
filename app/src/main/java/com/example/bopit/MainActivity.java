package com.example.bopit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private String difficulty;
    private Button start;
    private Button setDifficulty;
    private Button diffSelectionButton;//rename so it's not redundant
    private AlertDialog difficultySelection;
    private View mView;
    private RadioButton easy, medium, hard;
    private RadioGroup difficulties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty="Easy";
        initDifficultySelect();
        initUI();
        initOnClickListeners();


    }

    private void openGameActivity() {
        Log.d(TAG,"openGameActivity: Opening game activity");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY",difficulty);
        startActivity(intent);
    }

    private void initUI(){
        Log.d(TAG,"initUI: initializing UI components");
        start = findViewById(R.id.start);
        setDifficulty = findViewById(R.id.difficulty);
        difficulties = mView.findViewById(R.id.difficulties);
        easy = mView.findViewById(R.id.easy);
        medium = mView.findViewById(R.id.medium);
        hard = mView.findViewById(R.id.hard);
        diffSelectionButton = mView.findViewById(R.id.difficultySelectButton);
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
        diffSelectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(easy.isChecked()){
                    difficulty = "Easy";
                }else if(medium.isChecked()){
                    difficulty = "Medium";
                }else if(hard.isChecked()){
                    difficulty = "Hard";
                }
                difficultySelection.dismiss();
                Toast.makeText(getApplicationContext(),"Difficulty set to "+difficulty+"!",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"diffSelectionButton onClick: Changed difficulty to: "+ difficulty);
            }
        });
    }

    private void initDifficultySelect (){
        mView = getLayoutInflater().inflate(R.layout.dialog_difficulty_selection, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
         difficultySelection = mBuilder.create();

    }

}
