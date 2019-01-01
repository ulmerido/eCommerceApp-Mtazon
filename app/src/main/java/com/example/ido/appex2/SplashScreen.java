package com.example.ido.appex2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.ido.appex2.Activities.AllProductsActivity;
import com.example.ido.appex2.Activities.MainActivity;
import com.example.ido.appex2.Analytics.AnalyticsManager;
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
                .withSplashTimeOut(2000)
                .withBackgroundResource(R.drawable.grdnt)
                .withLogo(R.drawable.logo3)
                .withHeaderText("Welcome to our App: Ex03")
                .withFooterText("© HBU")
                .withBeforeLogoText("MTA Class 2018A")
                .withAfterLogoText("\nTeam members:\n\n   Ido Ulmer\n   Shai Nirel Hassan\n   Lior Ben-Yehuda");
        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        View view =config.create();
        setContentView(view);
        AnalyticsManager.getInstance().init(getApplicationContext());
        Log.e(TAG, "onCreate() <<");
    }
}