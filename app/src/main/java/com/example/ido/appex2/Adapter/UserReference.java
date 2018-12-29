package com.example.ido.appex2.Adapter;

import android.util.Log;

import com.example.ido.appex2.entities.User;

public class UserReference
{
    private final String           TAG = "UserReference";
    private User user;

    public UserReference(User i_User)
    {
        Log.e(TAG, "UserReference() >>");
        this.user = i_User;
        Log.e(TAG, "UserReference() <<");

    }


    public User getUser()
    {
        Log.e(TAG, "getUser() >>");
        return user;
    }

    public void setUser(User user)
    {
        Log.e(TAG, "setUser() >>");
        this.user = user;
        Log.e(TAG, "setUser() <<");
    }
}
