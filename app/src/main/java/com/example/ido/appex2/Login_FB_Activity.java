package com.example.ido.appex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.concurrent.ForkJoinTask;

public class Login_FB_Activity extends AppCompatActivity {

    public static final String TAG = "FacebookAuthActivity";

    private FirebaseAuth m_Auth;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseRemoteConfig m_Config;

    private TextView m_Status;
    private ImageView m_ProfileImage;
    private TextView m_Email;
    private TextView m_UserFullName;
    private CallbackManager m_CallbackManager;
    private AccessTokenTracker m_AccessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__fb_);
        m_Status = findViewById(R.id.tvStatusUser);
        m_UserFullName = findViewById(R.id.tvUserNameFacebook);
        m_Email =  findViewById(R.id.tvEmailFacebook);
        m_ProfileImage = findViewById(R.id.ivFacebook);
        m_Auth = FirebaseAuth.getInstance();
        m_Config = FirebaseRemoteConfig.getInstance();
        //////////////////

        /////////////////
        m_Config.fetch(3600).addOnCompleteListener(
                this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "onComplete() Remote Config fetch isSuccessful=>>"+ task.isSuccessful());
                        m_Config.activateFetched();

                    }
                });

        facebookLoginInit();

    }
    @Override
    protected void onStart()
    {

        Log.e(TAG, "onStart() >>");

        super.onStart();

        updateLoginStatus("N.A");

        Log.e(TAG, "onStart() <<");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e(TAG, "onActivityResult () >>" );
        super.onActivityResult(requestCode, resultCode, data);
        m_CallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult () <<" );
    }

    private void updateLoginStatus(String details)
    {
        FirebaseUser user = m_Auth.getCurrentUser();
        if (user == null) {
            m_Status.setText("SIGNED-OUT");
            m_UserFullName.setText("NAME: " + details);
            m_Email.setText("EMAIL: " + details);
            m_ProfileImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        } else
            {
            m_Status.setText("SIGNED-IN");
            m_UserFullName.setText("NAME: " + user.getDisplayName());
            m_Email.setText("EMAIL: " + user.getEmail());

           // if (m_Config.getBoolean("display_profile_image")) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString() +"/picture?type=large")
                        .into(m_ProfileImage);
          //  }
           // else {
             //   Log.e(TAG, "not pic !!!!!!!!!!!" );
           // }

            }

    }

    private void facebookLoginInit()
    {
        Log.e(TAG, "facebookLoginInit() >>");

        m_CallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(m_CallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.e(TAG, "facebook:onSuccess () >>" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.e(TAG, "facebook:onSuccess () <<");
            }

            @Override
            public void onCancel()
            {
                Log.e(TAG, "facebook:onCancel() >>");
                updateLoginStatus("Facebook login canceled");
                Log.e(TAG, "facebook:onCancel() <<");

            }

            @Override
            public void onError(FacebookException error)
            {
                Log.e(TAG, "facebook:onError () >>" + error.getMessage());
                updateLoginStatus(error.getMessage());
                Log.e(TAG, "facebook:onError <<");
            }
        });

        m_AccessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
                if (currentAccessToken == null)
                {
                    m_Auth.signOut();
                    updateLoginStatus("Facebook signuout");
                }
                Log.e(TAG,"onCurrentAccessTokenChanged() >> currentAccessToken="+ (currentAccessToken !=null ? currentAccessToken.getToken():"Null") + " ,oldAccessToken=" + (oldAccessToken != null ? oldAccessToken.getToken():"Null"));

            }
        };
        Log.e(TAG, "facebookLoginInit() <<");
    }

    private void handleFacebookAccessToken(AccessToken token)
    {

        Log.e(TAG, "handleFacebookAccessToken () >>" + token.getToken());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        m_Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "Facebook: onComplete() >> " + task.isSuccessful());

                        updateLoginStatus(task.isSuccessful() ? "N.A" : task.getException().getMessage());

                        Log.e(TAG, "Facebook: onComplete() <<");
                    }
                });

        Log.e(TAG, "handleFacebookAccessToken () <<");

    }


}
