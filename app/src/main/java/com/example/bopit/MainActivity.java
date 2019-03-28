package com.example.bopit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Enum difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.start);
        Button setDifficulty = findViewById(R.id.difficulty);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGameActivity();
            }
        });
        setDifficulty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY",difficulty);
        startActivity(intent);
    }
}
