package com.example.bopit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private String difficulty;
    private TextView diffDisplay;
    private Button start;
    private Button setDifficulty;
    private Button diffSelectionButton;
    private AlertDialog difficultySelection;
    private View mView;
    private RadioButton easy, medium, hard;
    private ListView highScoreView;
    DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         manager = new DataManager(getFilesDir());
        difficulty="Easy";
        loadDefaultScores();
        initDifficultySelect();
        initUI();
        initOnClickListeners();
    }
    private void initDifficultySelect (){
        mView = getLayoutInflater().inflate(R.layout.dialog_difficulty_selection, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
        difficultySelection = mBuilder.create();
    }
    private void loadDefaultScores(){
        try {
            manager.saveScore(new Score("Default #1", 500));
            manager.saveScore(new Score("Default #2", 350));
            manager.saveScore(new Score("Default #3", 100));
        }catch(Exception e){
            ModalDialogs.notifyException(this,e);
        }
    }

    private void initUI(){
        Log.d(TAG,"initUI: initializing UI components");
        diffDisplay = findViewById(R.id.diffDisplay);
        displayDiff();
        start = findViewById(R.id.start);
        setDifficulty = findViewById(R.id.difficulty);
        easy = mView.findViewById(R.id.easy);
        medium = mView.findViewById(R.id.medium);
        hard = mView.findViewById(R.id.hard);
        diffSelectionButton = mView.findViewById(R.id.difficultySelectButton);
        highScoreView = findViewById(R.id.highScoreView);
        loadHighScores();
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
                displayDiff();
                difficultySelection.dismiss();
                Toast.makeText(getApplicationContext(),"Difficulty set to "+difficulty+"!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGameActivity() {
        Log.d(TAG,"openGameActivity: Opening game activity");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY",difficulty);
        startActivity(intent);
    }
    private void displayDiff(){
        diffDisplay.setText("Difficulty: "+ difficulty);
    }
    private void loadHighScores(){


        Log.d(TAG,"loadHighScores: initializing highscores listview");
        try {
            ArrayList<Score> scores = manager.loadAllScores();
            final ArrayList<String> scoreDisp = scoreArrToStringArr(scores);
            initListComponents(scoreDisp);
        }catch(Exception e){
            ModalDialogs.notifyException(MainActivity.this,e);
        }
    }
    private ArrayList<String> scoreArrToStringArr(ArrayList<Score> arr){
        ArrayList<String> newArr = new ArrayList<>();
        for (Score s:arr) {
            newArr.add(s.dispScore());
        }
        return newArr;
    }
    private void initListComponents(ArrayList<String> arr){
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, arr);

        highScoreView.setAdapter(arrayAdapter);
    }
}
