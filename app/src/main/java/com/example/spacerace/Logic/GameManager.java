package com.example.spacerace.Logic;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.spacerace.MenuActivity;
import com.example.spacerace.R;
import com.example.spacerace.Utilitis.SharedPref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameManager implements SensorEventListener {

    private static final float TILT_THRESHOLD = 1.5f;
    private final int NUM_ROWS = 8;
    private final int NUM_COLS = 5;
    private final Context context;
    private boolean tiltMode;
    private boolean fastMode;
    private String playerName;

    private int playerColumn; // Initial player position in the middle column
    private List<Astroid> obstacleList;
    private List<Coin> coinList;
    private int lives; // Number of lives remaining

    private final GridLayout gameGrid;
    private final LinearLayoutCompat livesLayout;

    private int asteroidCreationCounter = 0;
    private Handler handler;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private SoundManager soundManager;
    private int coinCollected = 0  ;
    private boolean startgame = true ;

    private int odometerValue = 0;
    private Handler odometerHandler;
    private Runnable odometerUpdater;

    private TextView odometerTextView;

    private SharedPref sharedpref ;

    private LocationManager locationManager;





    public GameManager(GridLayout grid, LinearLayoutCompat livesView, Context context, boolean tiltMode, boolean fastMode, String playerName, SharedPref sharedpref) {

        gameGrid = grid;
        livesLayout = livesView;
        this.context = context;
        this.tiltMode = tiltMode;
        this.fastMode = fastMode;
        this.playerName = playerName;
        this.sharedpref=sharedpref.getInstance();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.soundManager = new SoundManager();

    }

    public void initializeGame() {


            // Initialize player and obstacle positions
            playerColumn = 2;
            obstacleList = new ArrayList<>();
            coinList = new ArrayList<>();
            this.lives = 3; // Set initial number of lives

            // Initialize handler for updating the game at regular intervals
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    // Handle messages to update the game state
                    updateGame();
                    int interval = 1000;
                    if (fastMode) interval = 500;  // Fast mode updates every 500ms, regular mode updates every 1000ms

                    if (handler != null) handler.sendEmptyMessageDelayed(0, interval); // Update every 1000 milliseconds (adjust as needed)
                }
            };
            int interval = 1000;
            if (fastMode) interval = 500;  // Fast mode updates every 500ms, regular mode updates every 1000ms
            // Start the game loop

          if(handler != null)handler.sendEmptyMessageDelayed(0, interval);
          odometerTextView = ((Activity) context).findViewById(R.id.main_odometer_textView);
          odometerHandler = new Handler();
          startOdometerUpdate(fastMode);

    if (tiltMode) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (accelerometer != null) {
                    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
                }
            }
        }



    }
    private void startOdometerUpdate(boolean fastMode) {
        odometerUpdater = new Runnable() {
            @Override
            public void run() {
                int delay = fastMode ? 50 : 100;
                odometerValue++; // Increment odometer value
                odometerTextView.setText(String.valueOf(odometerValue)); // Update TextView
                odometerHandler.postDelayed(this, delay); // Schedule next update
            }
        };
        odometerHandler.post(odometerUpdater);
    }

    public String getMode()
    {
        if (tiltMode) return "tilt";
        else if (fastMode )return "fast";
            else return "slow";

    }
    public void stopGame() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (odometerHandler != null) {
            odometerHandler.removeCallbacks(odometerUpdater);
            odometerHandler = null;
        }
        if (tiltMode && sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
    }

    private void updateGame() {
        // moving existing Obstacles values
        updateObstacles();
        //adding new Obstacle and coins
        if (asteroidCreationCounter % 2 == 0) addAstroid();
        if (asteroidCreationCounter % 7 == 0) addcoin();
        // updating the Obstacles images
        updateGridImages();

        checkCollision();
        asteroidCreationCounter++;

    }

    private void addAstroid() {
        // Generate a random column for the asteroid
        int randomColumn = new Random().nextInt(NUM_COLS);

        // Create an asteroid object (assuming Astroid is your class)
        Astroid asteroid = new Astroid(0, randomColumn); // Assuming 0 is the starting row

        // Add the asteroid to the list
        obstacleList.add(asteroid);

    }

    private void addcoin() {
        // Generate a random column for the coin
        int randomColumn = new Random().nextInt(NUM_COLS);

        // Create an asteroid object (assuming Astroid is your class)
        Coin coin = new Coin(0, randomColumn); // Assuming 0 is the starting row

        // Add the coin to the list if the random cell is empty
        if (!isOccopied(randomColumn)) coinList.add(coin);

    }

    private boolean isOccopied(int randomColumn) {
        for (Astroid astroid : obstacleList) {
            int row = astroid.getRow();
            int col = astroid.getCol();
            if (row == 0 && col == randomColumn) return true;
        }
        return false;
    }


    public void clearGrid() {
        int startIndex = 0; // Start index of the range
        int endIndex = Math.min(40, gameGrid.getChildCount()); // End index of the range

        for (int i = startIndex; i < endIndex; i++) {
            ImageView cellImageView = (ImageView) gameGrid.getChildAt(i);
            cellImageView.setImageResource(0); // Clear the image
        }
    }

    private void updateGridImages() {
        clearGrid();

        // Update grid for obstacles (astroids)
        for (Astroid astroid : obstacleList) {
            int row = astroid.getRow();
            int col = astroid.getCol();
            int cellId = context.getResources().getIdentifier("cell" + (row * NUM_COLS + col + 1), "id", context.getPackageName());
            ImageView astroidImageView = ((Activity) context).findViewById(cellId);
            astroidImageView.setImageResource(R.drawable.astroid);
        }

        // Update grid for coins
        for (Coin coin : coinList) {
            int row = coin.getRow();
            int col = coin.getCol();
            int cellId = context.getResources().getIdentifier("cell" + (row * NUM_COLS + col + 1), "id", context.getPackageName());
            ImageView coinImageView = ((Activity) context).findViewById(cellId);
            coinImageView.setImageResource(R.drawable.dollar); // Set the appropriate image resource for coins
        }
    }


    private void updatePlayerImage() {
        // Clear all player images in the grid before updating
        clearPlayerImages();
        int playerId = context.getResources().getIdentifier("player" + (playerColumn + 1), "id", context.getPackageName());
        ImageView cellImageView = ((Activity) context).findViewById(playerId);
        cellImageView.setVisibility(View.VISIBLE);
    }

    private void clearPlayerImages() {
        for (int c = 0; c < NUM_COLS; c++) {
            int playerId = context.getResources().getIdentifier("player" + (c + 1), "id", context.getPackageName());
            ImageView cellImageView = ((Activity) context).findViewById(playerId);
            cellImageView.setVisibility(View.INVISIBLE);

        }
    }

    private void checkCollision() {
        boolean collisionDetected = false; // Initialize collision flag

        // Check for collision with player
        for (Astroid astroid : obstacleList) {
            // Update lives, vibrate, toast message
            if (astroid.getRow() == NUM_ROWS - 1 && astroid.getCol() == playerColumn) {
                collisionDetected = true;
                break; // Exit the loop early if collision detected
            }
        }

        // Check for coin collision
        Iterator<Coin> coinIterator = coinList.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            if (coin.getRow() == NUM_ROWS - 1 && coin.getCol() == playerColumn) {
                coinCollison(coin.getRow() * NUM_COLS + coin.getCol() + 1);
                coinIterator.remove(); // Remove the coin from the list
                break; // Exit the loop early if collision detected
            }
        }

        if (collisionDetected) {
            // Handle collision (reset game, update UI, etc.)
            collision();
        }
    }


    private void coinCollison(int cellid ) {
        coinCollected++;
        soundManager.playCoinSound(context); // Play coin sound

        int cellId = context.getResources().getIdentifier("cell" + cellid, "id", context.getPackageName());
        ImageView cellImageView = ((Activity) context).findViewById(cellId);
        if (cellImageView != null)
            cellImageView.setVisibility(View.INVISIBLE);




    }

    private void collision() {
        // Collision detected, reduce lives
        lives--;



        if (lives == 0) {
            // End the game loop
            stopGame();
            saveGame(playerName,getMode(),calcScore());
            int score = calcScore();

            // Navigate back to the MainActivity with the number of coins
            Intent intent = new Intent(context, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("score", score); // Pass the number of coins
            context.startActivity(intent);
            ((Activity) context).finish();
        }


        clearGrid();
        obstacleList.clear();
        coinList.clear();



        // Update UI to reflect the reduced lives
        updateLivesUI();

        // Play crash sound
        soundManager.playCrashSound(context);

        // Vibrate for 500 milliseconds
        vibrate();

        // Show a "BOOM !!!" toast message
        showToast("BOOM !!! Lives remaining: " + lives);
    }

    private int calcScore() {
        return coinCollected*10 +odometerValue;
    }


    private void updateLivesUI() {
        // Assuming livesLayout is your LinearLayout containing heart1, heart2, and heart3
        switch (lives) {
            case 0:
                // Make the third heart (index 2) invisible
                livesLayout.getChildAt(0).setVisibility(View.INVISIBLE);
                break;
            case 1:
                // Make the second heart (index 1) invisible
                livesLayout.getChildAt(1).setVisibility(View.INVISIBLE);
                break;
            case 2:
                // Make the first heart (index 0) invisible
                livesLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                break;
            // You can add more cases if needed
        }
    }

    private void vibrate() {

        // vibrate phone in case of crash
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_HEAVY_CLICK));
        } else {
            v.vibrate(200);
        }
    }


    private void showToast(String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    // Move existing obstacles down
    private void updateObstacles() {
        updateEntities(coinList);
        updateEntities(obstacleList);
    }

    private void updateEntities(List<? extends Entity> entityList) {
        Iterator<? extends Entity> iterator = entityList.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            // Check if the entity is in the last row
            if (entity.getRow() == NUM_ROWS - 1) {
                // Remove the entity
                iterator.remove();
            } else {
                int row = entity.getRow();
                entity.setRow(row + 1);
            }
        }
    }


    public void movePlayerRight() {
        if (this.playerColumn < NUM_COLS - 1) {
            this.playerColumn += 1;


            checkCollision(); // Check for collision after moving
            updatePlayerImage();
        }
    }

    public void movePlayerLeft() {
        if (this.playerColumn > 0) {
            this.playerColumn -= 1;

            checkCollision(); // Check for collision after moving
            updatePlayerImage();

        }
    }

    public void movePlayerBasedOnTilt(float xTilt, float yTilt) {
        // Check if the tilt exceeds the threshold
        if (Math.abs(xTilt) > TILT_THRESHOLD || Math.abs(yTilt) > TILT_THRESHOLD) {
            // Determine the direction of tilt
            if (Math.abs(xTilt) > Math.abs(yTilt)) {
                // Tilt in the horizontal direction
                if (xTilt > 0) {
                    // Move player right
                    movePlayerRight();
                } else {
                    // Move player left
                    movePlayerLeft();
                }
            } else {
                // Tilt in the vertical direction
                if (yTilt > 0) {
                    // Move player down
                    //    movePlayerDown();
                } else {
                    // Move player up
                    //      movePlayerUp();
                }
            }
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xTilt = event.values[0];
            float yTilt = event.values[1];
            movePlayerBasedOnTilt(xTilt, yTilt);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
    public void saveGame(String playerName, String mode, int score) {
        //Location location = getCurrentLocation();

        // Access SharedPreferences and save the data
        GameData gameData = new GameData(playerName,mode , score);
        sharedpref.saveGameData(gameData );

    }









}


