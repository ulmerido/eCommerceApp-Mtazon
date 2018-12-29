package com.example.ido.appex2.Adapter;

import android.util.Log;

import com.example.ido.appex2.entities.AudioBook;

public class AudioBookWithKey
{

    private String key;
    private AudioBook audioBook;
    private final String           TAG = "AudioBookWithKey";

    public AudioBookWithKey(String key, AudioBook audioBook)
    {
        Log.e(TAG, "AudioBookWithKey() >>");
        this.key = key;
        this.audioBook = audioBook;
        Log.e(TAG, "AudioBookWithKey() <<");
    }

    public String getKey()
    {
        Log.e(TAG, "getKey() >>");
        return key;
    }

    public void setKey(String key)
    {
        Log.e(TAG, "setKey() >>");
        this.key = key;
        Log.e(TAG, "setKey() <<");

    }

    public AudioBook getAudioBook()
    {
        Log.e(TAG, "AudioBook() >>");
        return audioBook;
    }

    public void setAudioBook(AudioBook audioBook)
    {
        Log.e(TAG, "setAudioBook() >>");
        this.audioBook = audioBook;
        Log.e(TAG, "setAudioBook() <<");
    }
}