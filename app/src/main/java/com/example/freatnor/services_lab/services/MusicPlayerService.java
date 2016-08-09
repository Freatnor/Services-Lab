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
        if(intent.getAction().equals(MainActivity.ACTION_PAUSE)){
            mMediaPlayer.pause();
        }
        else if(mMediaPlayer != null && intent.getBooleanExtra("wasPaused", false)){
            mMediaPlayer.start();
        }
        else {
            mMediaPlayer = new MediaPlayer();
        }
        if(mPlaylist.hasNext()){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //Try to play the song
            try{
                mMediaPlayer.setDataSource(mPlaylist.getNext());
            }
            catch (IOException e){
                Log.e(TAG, "onStartCommand: Bad song URL", e);
            }

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();
        }
        else {
            stopSelf();
        }
        Log.i(TAG, "onStartCommand: finished the playlist, exiting");
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        if (mMediaPlayer != null) mMediaPlayer.release();
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
