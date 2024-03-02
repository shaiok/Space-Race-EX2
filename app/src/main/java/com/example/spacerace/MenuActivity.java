package com.example.spacerace;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {

        // Initialize views
        private Button highScoresButton;
        private EditText playerNameEditText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_menu);

                // Find views by their IDs
                highScoresButton = findViewById(R.id.button_high_scores);
                playerNameEditText = findViewById(R.id.edit_text_name);

                // Set onClickListener to the High Scores button
                highScoresButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Navigate to HighScoreActivity
                                Intent intent = new Intent(MenuActivity.this, HighScoreActivity.class);
                                startActivity(intent);
                        }
                });

                // Find the buttons for each game mode
                Button playButton = findViewById(R.id.button2);
                Button tiltModeButton = findViewById(R.id.button);
                Button fastButtonMode = findViewById(R.id.button3);

                // Set onClickListeners for each button
                playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Start the GameActivity for regular play mode
                                startGameActivity(false, false);
                        }
                });

                tiltModeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Start the GameActivity with tilt mode flag
                                startGameActivity(true, false);
                        }
                });

                fastButtonMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Start the GameActivity with fast mode flag
                                startGameActivity(false, true);
                        }
                });
        }

        private void startGameActivity(boolean tiltMode, boolean fastMode) {
                String playerName = playerNameEditText.getText().toString(); // Get the player's name from EditText
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("tilt_mode", tiltMode);
                intent.putExtra("fast_mode", fastMode);
                intent.putExtra("player_name", playerName); // Pass the player's name
                startActivity(intent);
        }
}
