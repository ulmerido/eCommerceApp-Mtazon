package com.example.ido.appex2.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Review {
    private String userReview;
    private int userRating;
    private String userEmail;

    public Review(String userReview, int userRating, String userEmail) {
        this.userReview = userReview;
        this.userRating = userRating;
        this.userEmail = userEmail;
    }

    public Review() {
    }

    public String getUserReview() {
        return userReview;
    }

    public int getUserRating() {
        return userRating;
    }

    public String getUserEmail() {
        return userEmail;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("userReview", userReview);
        result.put("userRating", userRating);
        result.put("userEmail", userEmail);
        return result;
    }

}
