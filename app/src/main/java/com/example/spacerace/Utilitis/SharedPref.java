package com.example.spacerace.Utilitis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.spacerace.Logic.GameData;

import java.util.ArrayList;
import java.util.Map;

public class SharedPref {

    private static final String DB_FILE = "SharedPref_DB";
    private static SharedPref instance;
    private SharedPreferences preferences;

    // Initialize the instance of SharedPref
    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);
        }
    }

    // Get the instance of SharedPref
    public static SharedPref getInstance() {
        return instance;
    }

    // Private constructor
    private SharedPref(Context context) {
        preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    // Save a string value to shared preferences
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Load a string value from shared preferences
    public String loadString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }


    // Load player data from one row
    public String loadPlayerData() {
        return preferences.getString("player_data", "");
    }

    // Save player data for a single game
    public void saveGameData(GameData gameData) {
        SharedPreferences.Editor editor = preferences.edit();
        String key = "game_data_" + System.currentTimeMillis(); // Unique key for each game
        String value = gameData.getName() + "," +
                gameData.getMode() + "," +
                gameData.getScore() + "," +
                gameData.getLongitude() + "," +
                gameData.getLatitude();
        editor.putString(key, value);
        editor.apply();

        // Log debug message
        Log.d("SaveGameData", "Saved game data with key: " + key + ", value: " + value);
    }



    // Load all game data from SharedPreferences
    public ArrayList<GameData> loadAllGameData() {
        ArrayList<GameData> gameDataList = new ArrayList<>();

        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            // Check if the entry represents game data
            if (key.startsWith("game_data_")) {
                // Parse the entry value to GameData object and add it to the list
                String[] data = entry.getValue().toString().split(",");
                String playerName = data[0];
                String mode = data[1];
                int score = Integer.parseInt(data[2]);
                double longitude = Double.parseDouble(data[3]);
                double latitude = Double.parseDouble(data[4]);
                GameData gameData = new GameData(playerName, mode, score, longitude, latitude);
                gameDataList.add(gameData);
            }
        }
        Log.d("LoadGameData", "Loaded " + gameDataList.size() + " game data entries");
        return gameDataList;
    }

}


