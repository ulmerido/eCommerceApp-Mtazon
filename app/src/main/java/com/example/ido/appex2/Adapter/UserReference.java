package com.example.ido.appex2.Adapter;

import com.example.ido.appex2.entities.User;

public class UserReference {

    private User user;

    public UserReference(User i_User)
    {
        this.user = i_User;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
