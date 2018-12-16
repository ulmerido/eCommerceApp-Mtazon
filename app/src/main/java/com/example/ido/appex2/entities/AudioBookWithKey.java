package com.example.ido.appex2.entities;

public class AudioBookWithKey {

    private String key;
    private AudioBook audioBook;

    public AudioBookWithKey(String key, AudioBook audioBook) {
        this.key = key;
        this.audioBook = audioBook;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AudioBook getAudioBook() {
        return audioBook;
    }

    public void setAudioBook(AudioBook audioBook) {
        this.audioBook = audioBook;
    }
}
