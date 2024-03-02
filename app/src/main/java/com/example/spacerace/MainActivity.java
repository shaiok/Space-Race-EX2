package com.example.spacerace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spacerace.Logic.GameManager;
import com.example.spacerace.Utilitis.SharedPref;

public class MainActivity extends AppCompatActivity {

    private ImageView main_img_background;
    private TextView odometerTextView;
    private int odometerValue = 0;
    private Handler handler;
    private Runnable odometerUpdater;
    private SharedPref sharedPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        main_img_background = findViewById(R.id.main_background);


        // Load background image
        Glide
                .with(this)
                .load(R.drawable.space_background)
                .centerCrop()
                .into(main_img_background);

        // Retrieve intent extras
        Intent intent = getIntent();
        boolean tiltMode = intent.getBooleanExtra("tilt_mode", false);
        boolean fastMode = intent.getBooleanExtra("fast_mode", false);
        String playerName =  intent.getStringExtra("player_name");

        // Initialize SharedPref
        sharedPref = SharedPref.getInstance();
        SharedPref.init(getApplicationContext());

        // Initialize GameManager
        GameManager gameManager = new GameManager(
                findViewById(R.id.gameGrid),
                findViewById(R.id.lives_layout),
                this,
                tiltMode,
                fastMode,
                playerName,
                sharedPref
        );
        if (tiltMode){
            findViewById(R.id.btnLeft).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnRight).setVisibility(View.INVISIBLE);
        }

        gameManager.initializeGame();

        // Add onClickListeners for the buttons
        findViewById(R.id.btnLeft).setOnClickListener(view -> gameManager.movePlayerLeft());
        findViewById(R.id.btnRight).setOnClickListener(view -> gameManager.movePlayerRight());


    }



}
