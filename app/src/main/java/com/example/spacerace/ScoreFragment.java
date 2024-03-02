package com.example.spacerace;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.spacerace.Logic.GameData;
import com.example.spacerace.Utilitis.SharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreFragment extends Fragment {

    private SharedPref sharedPref;

    public ScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SharedPref
        sharedPref = SharedPref.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score, container, false);

        // Get references to table layout
        TableLayout tableLayout = rootView.findViewById(R.id.table_layout);

        // Load all game data
        ArrayList<GameData> gameDataList = sharedPref.loadAllGameData();

        // Sort the game data list by score
        Collections.sort(gameDataList, new Comparator<GameData>() {
            @Override
            public int compare(GameData gameData1, GameData gameData2) {
                // Assuming scores are integers, modify this based on the actual type of score
                return Integer.compare(gameData2.getScore(), gameData1.getScore()); // Descending order
            }
        });
        // Add data to table
        for (GameData gameData : gameDataList) {
            createTableRow(tableLayout, gameData.getName(), gameData.getMode(), String.valueOf(gameData.getScore()),false);
        }

        return rootView;
    }


    private void createTableRow(TableLayout tableLayout, String playerName, String mode, String score, boolean isFirstRow) {
        // Create a new row
        TableRow tableRow = new TableRow(requireContext());

        // Create text views for each column
        TextView playerNameTextView = new TextView(requireContext());
        playerNameTextView.setText(isFirstRow ? "Name" : (playerName != null ? playerName : ""));
        playerNameTextView.setPadding(8, 8, 8, 8);
        playerNameTextView.setTypeface(null, Typeface.BOLD);
        playerNameTextView.setTextSize(28);
        tableRow.addView(playerNameTextView);

        TextView modeTextView = new TextView(requireContext());
        modeTextView.setText(isFirstRow ? "Mode" : (mode != null ? mode : ""));
        modeTextView.setPadding(8, 8, 8, 8);
        modeTextView.setTypeface(null, Typeface.BOLD);
        modeTextView.setTextSize(28);
        tableRow.addView(modeTextView);

        TextView scoreTextView = new TextView(requireContext());
        scoreTextView.setText(isFirstRow ? "Score" : (score != null ? score : ""));
        scoreTextView.setPadding(8, 8, 8, 8);
        scoreTextView.setTypeface(null, Typeface.BOLD);
        scoreTextView.setTextSize(28);
        tableRow.addView(scoreTextView);

        // Add row to the table layout
        tableLayout.addView(tableRow);
    }

}
