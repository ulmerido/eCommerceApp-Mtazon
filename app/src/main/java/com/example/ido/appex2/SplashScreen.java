package com.example.ido.appex2;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity
{

    private static final String TAG = "Splash screen";
    private FirebaseAuth m_Auth;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundResource(R.drawable.background6)
                .withLogo(R.drawable.loading_logo_android)
                .withHeaderText("Welcome to our App: Ex02")
                .withFooterText("© HBU")
                .withBeforeLogoText("MTA Class 2018A")
                .withAfterLogoText("\nTeam members:\n\n   Ido Ulmer\n   Shai Nirel Hassan\n   Lior Ben-Yehuda");
        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        View view =config.create();
        setContentView(view);
        Log.e(TAG, "onCreate() <<");
    }
}