package com.example.ido.appex2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private String email;
    private String fullName;
    private String password;
    private String profilePicURL;
    private int totalPurchase;
    private List<String> myAudioBooks = new ArrayList<>();
    private String deviceToken ;

    public String getDeviceToken()
    {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken)
    {
        this.deviceToken = deviceToken;
    }

    public User() {
    }

    public User(String email, String fullName, String password, String profilePicURL, int totalPurchase, List<String> myAudioBooks, String i_deviceToken) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.profilePicURL = profilePicURL;
        this.totalPurchase = totalPurchase;
        this.myAudioBooks = myAudioBooks;
        this.deviceToken = i_deviceToken;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        parcel.writeString(fullName);
        parcel.writeString(password);
        parcel.writeList(myAudioBooks);
        parcel.writeString(profilePicURL);//maybe not needed

    }

    public User(Parcel in) {
        this.email = in.readString();
        this.fullName = in.readString();
        this.password = in.readString();
        this.profilePicURL = in.readString();
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


    public int getTotalPurchase() {
        return totalPurchase;
    }
}