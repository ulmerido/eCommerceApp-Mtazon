package com.example.ido.appex2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignIn extends AppCompatActivity
{
    public static final String TAG ="Google Sign IN:";
    private FirebaseAuth m_Auth;
    private GoogleSignInClient m_GoogleSignInClient;
    private SignInButton m_SignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        m_Auth = FirebaseAuth.getInstance();
        googleSignInBuilder();
        m_SignInButton = (SignInButton)findViewById(R.id.googleSignInButton);
        m_SignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
    }

    private  void googleSignInBuilder()
    {
        Log.e(TAG, "googleSignInBuilder >>");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        m_GoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this,gso);
        Log.e(TAG, "googleSignInBuilder <<");
    }
    private void signIn()
    {
        Log.e(TAG, "signIn >>");
        Intent signInIntent = m_GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
        Log.e(TAG, "signIn <<");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e(TAG, "onActivityResult >>");
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
        Log.e(TAG, "onActivityResult <<");
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount i_Account)
    {
        Log.e(TAG, "firebaseAuthWithGoogle >>");
        Log.d(TAG, "firebaseAuthWithGoogle:" + i_Account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(i_Account.getIdToken(), null);
        m_Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = m_Auth.getCurrentUser();
                            Intent intent_LogedIn = new Intent(getApplicationContext(),UserActivity.class);
                            startActivity(intent_LogedIn);
                            finish();
                            Toast.makeText(getApplicationContext(),"User logged in successfully", Toast.LENGTH_SHORT).show();;
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"User login Fail", Toast.LENGTH_SHORT).show();;
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
        Log.e(TAG, "firebaseAuthWithGoogle <<");
    }
}
