package com.example.spacerace.Logic;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.spacerace.R;

public class SoundManager {
    private static MediaPlayer crashMediaPlayer;
    private static MediaPlayer coinMediaPlayer;

    public static void playCrashSound(Context context) {
        if (crashMediaPlayer == null) {
            crashMediaPlayer = MediaPlayer.create(context, R.raw.crashsound);
        } else {
            crashMediaPlayer.reset();
            crashMediaPlayer = MediaPlayer.create(context, R.raw.crashsound);
        }
        crashMediaPlayer.start();
    }

    public static void playCoinSound(Context context) {
        if (coinMediaPlayer == null) {
            coinMediaPlayer = MediaPlayer.create(context, R.raw.coinsound);
        } else {
            coinMediaPlayer.reset();
            coinMediaPlayer = MediaPlayer.create(context, R.raw.coinsound);
        }
        coinMediaPlayer.start();
    }

    public static void releaseMediaPlayer() {
        if (crashMediaPlayer != null) {
            crashMediaPlayer.release();
            crashMediaPlayer = null;
        }
        if (coinMediaPlayer != null) {
            coinMediaPlayer.release();
            coinMediaPlayer = null;
        }
    }
}
