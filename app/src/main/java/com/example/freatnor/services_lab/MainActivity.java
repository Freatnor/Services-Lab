package com.example.freatnor.services_lab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.freatnor.services_lab.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";

    private boolean mIsPlaying = false;
    private boolean mWasPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void playPause(View view){
        Intent intent = new Intent(this, MusicPlayerService.class);
        if(!mIsPlaying){
            Log.i(TAG, "playPause: now starting service to play and wasPaused = " + mWasPaused);
            intent.setAction(ACTION_PLAY);
            intent.putExtra("wasPaused", mWasPaused);
            mWasPaused = false;
            mIsPlaying = true;
        }
        else {
            Log.i(TAG, "playPause: now pausing service and wasPaused = " + mWasPaused);
            intent.setAction(ACTION_PAUSE);
            mWasPaused = true;
            mIsPlaying = false;
        }
        startService(intent);
    }

    public void stop(View view){
        Log.i(TAG, "stop: stopping meda player");
        Intent intent = new Intent(this, MusicPlayerService.class);
        stopService(intent);
    }
}
