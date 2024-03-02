package com.example.spacerace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spacerace.Utilitis.SharedPref;

public class HighScoreActivity extends AppCompatActivity {
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Find the buttons
        Button btnScore = findViewById(R.id.btnHighScores);
        Button btnLocation = findViewById(R.id.btnPlayerLocations);
        Button btnReturnToMenu = findViewById(R.id.btnReturnToMenu); // Find the "Return to Menu" button

        // Initialize SharedPref
        sharedPref = SharedPref.getInstance();
        SharedPref.init(getApplicationContext());

        // Set onClick listener for the Score button
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the ScoreFragment
                replaceFragment(new ScoreFragment());
            }
        });

        // Set onClick listener for the Location button
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the LocationFragment
                replaceFragment(new LocationFragment());
            }
        });

        // Set onClick listener for the "Return to Menu" button
        btnReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main menu activity
                Intent intent = new Intent(HighScoreActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to replace the current fragment with a new fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // R.id.fragment_container is the ID of the FrameLayout where fragments will be replaced
        fragmentTransaction.addToBackStack(null); // Add the transaction to the back stack
        fragmentTransaction.commit(); // Commit the transaction
    }
}
