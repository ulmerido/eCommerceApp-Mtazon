package com.example.ido.appex2.Analytics;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.ido.appex2.entities.AudioBook;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


import java.util.HashMap;
import java.util.Map;

public class AnalyticsManager {

    private static String TAG = "AnalyticsManager";
    private static AnalyticsManager mInstance = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private  MixpanelAPI mMixpanel;


    private AnalyticsManager()
    {
    }

    public static AnalyticsManager getInstance() {
        Log.e(TAG, "getInstance() >>");

        if (mInstance == null) {
            mInstance = new AnalyticsManager();
            }

        Log.e(TAG, "getInstance() <<");
        
        return (mInstance);

    }

    public void init(Context context) {
        Log.e(TAG, "init() <<");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mMixpanel = MixpanelAPI.getInstance(context,
                "be0f2f05ebd6d1139cb5c26489d103de");

        Log.e(TAG, "init() >>");
    }

    public void audioBookSearchByRadioChoiceEvent(String searchChoice) {
        Log.e(TAG, "audioBookSearchByRadioChoiceEvent() >>");

        String eventName = "search_Choice_Radio_Button";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchChoice);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanels
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("search radio choice", searchChoice);
        mMixpanel.trackMap(eventName,eventParams2);

        Log.e(TAG, "audioBookSearchByRadioChoiceEvent() <<");
    }

    public void audioBookSearchbyWordEvent(String searchString) {
        Log.e(TAG, "audioBookSearchbyWordEvent() >>");

        String eventName = "search_By_Word";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchString);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanels
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("search word", searchString);
        mMixpanel.trackMap(eventName,eventParams2);

        Log.e(TAG, "audioBookSearchbyWordEvent() <<");
    }

    public void audioBookSortbyEvent(String searchString) {
        Log.e(TAG, "audioBookSortbyEvent() >>");

        String eventName = "sort_by";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchString);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanels
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("sort by", searchString);
        mMixpanel.trackMap(eventName,eventParams2);

        Log.e(TAG, "audioBookSortbyEvent() <<");
    }



    public void audioBookSignupEvent(String signupMethod) {
        Log.e(TAG, "audioBookSignupEvent() >>");

        String eventName = "signup";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, signupMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,params);

        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("signup method", signupMethod);

        mMixpanel.trackMap(eventName,eventParams2);

        Log.e(TAG, "audioBookSignupEvent() <<");
    }


    public void audioBookLoginEvent(String loginMethod) {
        Log.e(TAG, "audioBookLoginEvent() >>");

        String eventName = "login";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,params);
        //Mixpanel
        Map<String, Object> eventParams2 = new HashMap<String, Object>();
        eventParams2.put("signup method", loginMethod);

        mMixpanel.trackMap(eventName,eventParams2);

        Log.e(TAG, "audioBookLoginEvent() <<");
    }

    public void audioBookEvent(String event , AudioBook book) {
        Log.e(TAG, "audioBookEvent() >>");

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

        Log.e(TAG, "audioBookEvent() <<");
    }

    public void audioBookPurchase(AudioBook book) {
        Log.e(TAG, "audioBookPurchase() >>");

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

        Log.e(TAG, "audioBookPurchase() <<");

    }

    public void audioBookRating(AudioBook book ,int userRating) {
        Log.e(TAG, "audioBookRating() >>");

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

        Log.e(TAG, "audioBookRating() <<");
    }

    public void setUserID(String id, boolean newUser) {

        Log.e(TAG, "setUserID() >>");

        mFirebaseAnalytics.setUserId(id);


        if (newUser) {
            mMixpanel.alias(id, null);
        }
        mMixpanel.identify(id);
        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());

        Log.e(TAG, "setUserID() <<");
    }

    public void setUserProperty(String name , String value) {
        Log.e(TAG, "setUserProperty() >>");

        mFirebaseAnalytics.setUserProperty(name,value);

        mMixpanel.getPeople().set(name,value);

        Log.e(TAG, "setUserProperty() <<");
    }

}
