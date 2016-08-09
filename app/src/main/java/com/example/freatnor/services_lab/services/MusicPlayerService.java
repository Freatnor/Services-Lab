package com.example.freatnor.services_lab.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.freatnor.services_lab.MainActivity;
import com.example.freatnor.services_lab.singletons.Playlist;

import java.io.IOException;

/**
 * Created by Jonathan Taylor on 8/8/16.
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "MusicPlayerService";

//    private HandlerThread mHandlerThread;
//    private Handler mHandler;

    private MediaPlayer mMediaPlayer;
    private Playlist mPlaylist;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        mPlaylist = Playlist.getInstance();
        //check if it's supposed to pause
        if(intent.getAction().equals(MainActivity.ACTION_PAUSE)){
            Log.d(TAG, "onStartCommand: pausing in thread");
            mMediaPlayer.pause();
        }
        //otherwise check if it's supposed to restart from pause
        else if(mMediaPlayer != null && intent.getBooleanExtra("wasPaused", false)){
            Log.d(TAG, "onStartCommand: trying to restart in thread");
            mMediaPlayer.start();
        }
        else {
            Log.d(TAG, "onStartCommand: creating a new player");
            mMediaPlayer = new MediaPlayer();
            mPlaylist.resetPlaylist();

            //if the playlist hasn't restarted get it going
            Log.d(TAG, "onStartCommand: Playlist is at index of song " + mPlaylist.getCurrent());
            if(mMediaPlayer != null && !mMediaPlayer.isPlaying() && mPlaylist.hasNext()){
                playSong(mMediaPlayer);
            }
            else {
                Log.d(TAG, "onStartCommand: stopping");
                stopSelf();
            }
        }


        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playSong(mediaPlayer);
    }

    public void playSong(MediaPlayer mediaPlayer){
        if(mPlaylist.hasNext()){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //Try to play the song
            try{
                mediaPlayer.setDataSource(mPlaylist.getNext());
            }
            catch (IOException e){
                Log.e(TAG, "onStartCommand: Bad song URL", e);
            }

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepareAsync();
        }
        else {
            stopSelf();
        }
    }
}
