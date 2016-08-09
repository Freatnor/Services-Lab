package com.example.freatnor.services_lab;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.freatnor.services_lab.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_STOP = "STOP";

    private boolean mIsPlaying = false;
    private boolean mWasPaused = false;

    private NotificationManagerCompat mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager =  NotificationManagerCompat.from(this.getApplicationContext());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switch(getIntent().getAction()){
            case ACTION_PLAY:
            case ACTION_PAUSE:
                playPause(null);
                break;
            case ACTION_STOP:
                stop(null);
                break;
            default:
                Toast.makeText(MainActivity.this, "Unkown action", Toast.LENGTH_SHORT).show();
        }
    }

    public void playPause(View view){
        Intent intent = new Intent(this, MusicPlayerService.class);
        //intent of the notification to come back to the main activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_library_music_black_24dp);
        if(!mIsPlaying){
            Log.i(TAG, "playPause: now starting service to play and wasPaused = " + mWasPaused);
            intent.setAction(ACTION_PLAY);
            intent.putExtra("wasPaused", mWasPaused);
            mWasPaused = false;
            mIsPlaying = true;

            notificationIntent.setAction(ACTION_PAUSE);
            pendingIntent = PendingIntent.getActivity(MainActivity.this, (int) System.currentTimeMillis(), notificationIntent, 0);
            builder.addAction(android.R.drawable.ic_media_pause, "Pause", pendingIntent);
        }
        else {
            Log.i(TAG, "playPause: now pausing service and wasPaused = " + mWasPaused);
            intent.setAction(ACTION_PAUSE);
            mWasPaused = true;
            mIsPlaying = false;

            notificationIntent.setAction(ACTION_PLAY);
            pendingIntent = PendingIntent.getActivity(MainActivity.this, (int) System.currentTimeMillis(), notificationIntent, 0);
            builder.addAction(android.R.drawable.ic_media_play, "Play", pendingIntent);
        }

        //add a stop button to the notification
        Intent stopIntent = new Intent(this, MainActivity.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), stopIntent, 0);
        builder.addAction(R.drawable.ic_stop_black_24dp, "Stop", pendingStopIntent);

        //start the music service
        startService(intent);

        //build the notification
        mManager.notify(1977, builder.build());
    }

    public void stop(View view){
        Log.i(TAG, "stop: stopping meda player");
        Intent intent = new Intent(this, MusicPlayerService.class);
        stopService(intent);
        mIsPlaying = false;
        mWasPaused = false;

        mManager.cancel(1977);
    }
}
