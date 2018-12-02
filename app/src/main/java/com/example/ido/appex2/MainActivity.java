package com.example.ido.appex2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
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
    private Button                         m_SignUp_btn;
    private Button                         mBtnSignin;
    private TextView                       m_EtUserEmail;
    private TextView                       tvRecoverPassword;
    private TextView                       m_EtUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Auth = FirebaseAuth.getInstance();
        m_FacebookLogin_btn = (LoginButton)findViewById(R.id.login_button);
        m_CallbackManager = CallbackManager.Factory.create();
        m_GoogleSignInButton = (SignInButton)findViewById(R.id.googleSignInButton);
        m_SignUp_btn = (Button) findViewById(R.id.btn_SignUp);
        mBtnSignin = (Button) findViewById(R.id.btn_SignIn);
        tvRecoverPassword = (TextView) findViewById(R.id.tvForgetPass);
        m_EtUserEmail = (TextView) findViewById(R.id.etEmail);
        m_EtUserPassword = (TextView) findViewById(R.id.et_UserPassword);

        googleSignInBuilder();
        facebookLoginInit();


        m_GoogleSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                googleSignIn();
            }
        });
        m_SignUp_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent_SignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent_SignUp);
                finish();
            }
        });

        tvRecoverPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDialog();
                //recoverPassowrd();
            }
        });

        mBtnSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().getCurrentUser().reload();
                signin();
            }
        });

    }

    private void openDialog()
    {
        ForgotPasswordDialog fpDialog = new ForgotPasswordDialog();
        fpDialog.show(getSupportFragmentManager(), "Forgot pass dialog");
        Toast.makeText(MainActivity.this, "recovery email sent",Toast.LENGTH_SHORT).show();

    }

    //////////////////////////////////////////////////////////////////
    private void signin()
    {
        final String passString = m_EtUserPassword.getText().toString().trim();
        final String emailString = m_EtUserEmail.getText().toString().trim();
        FirebaseAuth.getInstance().getCurrentUser().reload();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //     Toast.makeText(MainActivity.this, String.valueOf(user.isEmailVerified()),
        //             Toast.LENGTH_SHORT).show();

        if(user.isEmailVerified())
        {
            m_Auth.signInWithEmailAndPassword(emailString, passString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = m_Auth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent_SignUp = new Intent(getApplicationContext(), UserActivity.class);
                                startActivity(intent_SignUp);
                                finish();
                                //   updateUI(user);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //  updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this, "unverified email",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void recoverPassowrd()
    {
        m_Auth = FirebaseAuth.getInstance();

        if(m_Auth.getCurrentUser() != null)
        {
            String emailAddress = m_Auth.getCurrentUser().getEmail();
            m_Auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(MainActivity.this, "zzzz",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this, "you are not logged in",
                    Toast.LENGTH_SHORT).show();
        }
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
    private void googleSignIn()
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
