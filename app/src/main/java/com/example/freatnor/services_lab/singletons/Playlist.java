package com.example.freatnor.services_lab.singletons;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jonathan Taylor on 8/8/16.
 */
public class Playlist {

    private ArrayList<String> mSongUrls;
    private int mPosition;
    private boolean mWillRepeat;

    private static Playlist ourInstance = new Playlist();

    public static Playlist getInstance() {
        return ourInstance;
    }

    private Playlist() {
        mSongUrls = new ArrayList<>();
        mSongUrls.add("https://archive.org/download/KanaHanazawa-RenaiCirculation/01-RenaiCirculation.mp3");
        mSongUrls.add("http://archive.forunesia.com/downloads/mp3_6/%5BForunesia%5DMousou%20Express%20%5BSengoku%20Nadeko%5D.mp3");
        mPosition = 0;
        mWillRepeat = false;
    }

    public String getNext(){
        int current = mPosition;
        if(mPosition == -1){
            return null;
        }
        if(mPosition >= mSongUrls.size() - 1 && mWillRepeat){
            mPosition = 0;
        }
        else if(mPosition < mSongUrls.size() - 1){
            mPosition++;
        }else{
            mPosition = -1;
        }
        return mSongUrls.get(current);
    }

    public String getCurrent(){
        return mSongUrls.get(mPosition);
    }

    public String getSongAtPosition(int position){
        return mSongUrls.get(position);
    }

    public boolean hasNext(){
        if(mPosition < mSongUrls.size()){
            return true;
        }
        else if(mPosition == mSongUrls.size() -1 && mWillRepeat){
            return true;
        }
        else{
            return false;
        }
    }

    public void resetPlaylist(){
        mPosition = 0;
    }
    public boolean isWillRepeat() {
        return mWillRepeat;
    }

    public void setWillRepeat(boolean willRepeat) {
        mWillRepeat = willRepeat;
    }
}
