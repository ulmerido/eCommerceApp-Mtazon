package com.example.ido.appex2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private String email;
    private String password;
    private String profilePicURL;
    private int totalPurchase;
    private List<String> myAudioBooks = new ArrayList<>();

    public User() {
    }

    public User(String email, String password, String profilePicURL, int totalPurchase, List<String> myAudioBooks) {
        this.email = email;
        this.password = password;
        this.profilePicURL = profilePicURL;
        this.totalPurchase = totalPurchase;
        this.myAudioBooks = myAudioBooks;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void upgdateTotalPurchase(int newPurcahsePrice) {
        this.totalPurchase += newPurcahsePrice;
    }

    public List<String> getMyAudioBooks() {
        return myAudioBooks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeList(myAudioBooks);
        parcel.writeString(profilePicURL);//maybe not needed

    }

    public User(Parcel in) {
        this.email = in.readString();
        in.readList(myAudioBooks,String.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



}