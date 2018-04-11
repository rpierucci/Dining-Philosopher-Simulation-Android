package com.example.robert.diningphilosopher;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    public static int height, width;
    public static Thread philosopher[] = new Thread[5];
    public static int[] forks = {0, 0, 0, 0, 0};
    public static int[] philStatus = {0, 0, 0, 0, 0};
    MediaPlayer button;
    MediaPlayer song;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        button = MediaPlayer.create(MainActivity.this, R.raw.button);
        song = MediaPlayer.create(MainActivity.this, R.raw.song);
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        setContentView(new CustomView(this, forks, philStatus, philosopher, button, song));
        Monitor monitor = new Monitor(forks, philStatus);
        for (int i = 0; i < 5; i++) {
            philosopher[i] = new Thread(new Philosopher(i, monitor));
            philosopher[i].start();
        }
    }
}