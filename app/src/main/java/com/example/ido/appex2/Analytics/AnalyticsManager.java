package com.example.ido.appex2.Analytics;

import android.content.Context;
import android.os.Bundle;

import com.appsee.Appsee;
import com.example.ido.appex2.entities.AudioBook;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


import java.util.HashMap;
import java.util.Map;

public class AnalyticsManager {

    private static String TAG = "AnalyticsManager";
    private static AnalyticsManager mInstance = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private  MixpanelAPI mMixpanel;


    private AnalyticsManager() {
    }

    public static AnalyticsManager getInstance() {

        if (mInstance == null) {
            mInstance = new AnalyticsManager();
        }
        return (mInstance);
    }

    public void init(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mMixpanel = MixpanelAPI.getInstance(context,
                "be0f2f05ebd6d1139cb5c26489d103de");
    }

    public void audioBookSearchByRadioChoiceEvent(String searchChoice) {

        String eventName = "search";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchChoice);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanels
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("search radio choice", searchChoice);
        mMixpanel.trackMap(eventName,eventParams2);

    }

    public void audioBookSearchbyWordEvent(String searchString) {

        String eventName = "search";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchString);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanels
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("search word", searchString);
        mMixpanel.trackMap(eventName,eventParams2);

    }



    public void audioBookSignupEvent(String signupMethod) {

        String eventName = "signup";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, signupMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,params);

        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("signup method", signupMethod);

        mMixpanel.trackMap(eventName,eventParams2);

    }


    public void audioBookLoginEvent(String loginMethod) {

        String eventName = "login";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,params);
        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("signup method", loginMethod);

        mMixpanel.trackMap(eventName,eventParams2);

    }

    public void audioBookEvent(String event , AudioBook book) {
        Bundle params = new Bundle();

        params.putString("book_genre", book.getGenre());
        params.putString("book_name", book.getName());
        params.putString("book_author", book.getAuthor());
        params.putDouble("book_price",book.getPrice());
        params.putDouble("book_rating",book.getRating());

        mFirebaseAnalytics.logEvent(event,params);


        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("book_genre", book.getGenre());
        eventParams2.put("book_name", book.getName());
        eventParams2.put("book_author", book.getAuthor());
        eventParams2.put("book_price",String.valueOf(book.getPrice()));
        eventParams2.put("book_rating",String.valueOf(book.getRating()));

        mMixpanel.trackMap(event,eventParams2);

    }

    public void audioBookPurchase(AudioBook book) {

        String eventName = "purchase";
        Bundle params = new Bundle();
        params.putDouble(FirebaseAnalytics.Param.PRICE,book.getPrice());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE,params);


        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("book_genre", book.getGenre());
        eventParams2.put("book_name", book.getName());
        eventParams2.put("book_author", book.getAuthor());
        eventParams2.put("book_price",String.valueOf(book.getPrice()));
        eventParams2.put("book_rating",String.valueOf(book.getRating()));


        mMixpanel.trackMap(eventName,eventParams2);



    }

    public void audioBookRating(AudioBook book ,int userRating) {

        String eventName = "book_rating";
        Bundle params = new Bundle();

        params.putString("book_genre", book.getGenre());
        params.putString("book_name", book.getName());
        params.putString("book_author", book.getAuthor());
        params.putDouble("book_price",book.getPrice());
        params.putDouble("book_reviews_count",book.getReviewsCount());
        params.putDouble("book_total_rating",book.getRating());
        params.putDouble("book_user_rating",userRating);

        mFirebaseAnalytics.logEvent(eventName,params);


        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("book_genre", book.getGenre());
        eventParams2.put("book_name", book.getName());
        eventParams2.put("book_author", book.getAuthor());
        eventParams2.put("book_price",String.valueOf(book.getPrice()));
        eventParams2.put("book_reviews_count",String.valueOf(book.getReviewsCount()));
        eventParams2.put("book_total_rating",String.valueOf(book.getRating()));
        eventParams2.put("book_user_rating",String.valueOf(userRating));

        mMixpanel.trackMap(eventName,eventParams2);

    }

    public void setUserID(String id, boolean newUser) {

        mFirebaseAnalytics.setUserId(id);


        if (newUser) {
            mMixpanel.alias(id, null);
        }
        mMixpanel.identify(id);
        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());
    }

    public void setUserProperty(String name , String value) {

        mFirebaseAnalytics.setUserProperty(name,value);

        mMixpanel.getPeople().set(name,value);
    }

}
