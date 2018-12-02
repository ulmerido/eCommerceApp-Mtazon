package com.example.ido.appex2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity
{
    public static final String             TAG ="Main Activity:";
    private FirebaseAuth                   m_Auth;
    private GoogleSignInClient             m_GoogleSignInClient;
    private SignInButton                   m_GoogleSignInButton;
    private CallbackManager                m_CallbackManager;
    private AccessTokenTracker             m_AccessTokenTracker;
    private LoginButton                    m_FacebookLogin_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Auth = FirebaseAuth.getInstance();
        m_FacebookLogin_btn = findViewById(R.id.login_button);
        m_CallbackManager = CallbackManager.Factory.create();
        m_GoogleSignInButton = (SignInButton)findViewById(R.id.googleSignInButton);
        googleSignInBuilder();
        facebookLoginInit();


        m_GoogleSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
    }

    ////////////////////////////////////////////////////////////
    private  void googleSignInBuilder()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        m_GoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this,gso);
    }
    private void signIn()
    {
        Intent signInIntent = m_GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101)
        {
            Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                Log.w("Sign IN:", "Google sign in failed", e);
                // ...
            }
        }

        m_CallbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount i_Account)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + i_Account.getId());
        firebaseAuthWithGoogleAndFacebook(GoogleAuthProvider.getCredential(i_Account.getIdToken(), null));
    }

    /////////////////////////////////////////////////////

    private void facebookLoginInit()
    {
        Log.e(TAG, "facebookLoginInit() >>");

        m_FacebookLogin_btn.setReadPermissions("email", "public_profile");
        m_FacebookLogin_btn.registerCallback(m_CallbackManager, new FacebookCallback<LoginResult>()
        {
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
                Log.e(TAG, "facebook:onCancel() <<");
            }

            @Override
            public void onError(FacebookException error)
            {
                Log.e(TAG, "facebook:onError () >>" + error.getMessage());
                Log.e(TAG, "facebook:onError <<");
            }
        });

        m_AccessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken)
            {
                if (currentAccessToken == null)
                {
                    m_Auth.signOut();
                }

                Log.e(TAG,"onCurrentAccessTokenChanged() >> currentAccessToken="+ (currentAccessToken !=null ? currentAccessToken.getToken():"Null") + " ,oldAccessToken=" + (oldAccessToken != null ? oldAccessToken.getToken():"Null"));
            }
        };

        Log.e(TAG, "facebookLoginInit() <<");
    }

    private void handleFacebookAccessToken(AccessToken token)
    {

        Log.e(TAG, "handleFacebookAccessToken () >>" + token.getToken());

        firebaseAuthWithGoogleAndFacebook(FacebookAuthProvider.getCredential(token.getToken()));

        Log.e(TAG, "handleFacebookAccessToken () <<");

    }

    private void firebaseAuthTaskCheck(Task<AuthResult> i_Task)
    {
        if (i_Task.isSuccessful())
        {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            Intent intent_LogedIn = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent_LogedIn);
            finish();
            Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();
            ;
        }
        else
        {
            // If sign in fails, display a message to the user.
            Toast.makeText(getApplicationContext(), i_Task.getException().toString(), Toast.LENGTH_SHORT).show();
            ;
            Log.w(TAG, "signInWithCredential:failure", i_Task.getException());
        }

    }

    private void firebaseAuthWithGoogleAndFacebook(AuthCredential i_Credential)
    {
        m_Auth.signInWithCredential(i_Credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        firebaseAuthTaskCheck(task);
                    }
                });
    }

}
