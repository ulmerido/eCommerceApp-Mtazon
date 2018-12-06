package com.example.ido.appex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
public class SignUpActivity extends AppCompatActivity
{
    private EditText     etPassword;
    private EditText     etEmail;
    private EditText     m_etName;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirsebaseUser;
    private Button       m_btnBack;
    private Button       mBtnRegister;
    public static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        m_etName = findViewById(R.id.etUserName);
        mBtnRegister = (Button) findViewById(R.id.btnSignUp);
        m_btnBack = (Button) findViewById(R.id.btnBack);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        m_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }

        });
    }
    private void registerUser()
    {
        Log.e(TAG, "registerUser() >>");
        final String passString = etPassword.getText().toString().trim();
        final String emailString = etEmail.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(emailString, passString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            //User user = new User(emailString, passString);
                            FirebaseDatabase.getInstance().getReference("email")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(emailString).addOnCompleteListener(new OnCompleteListener<Void>()

                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    mAuth = FirebaseAuth.getInstance();
                                    mFirsebaseUser = mAuth.getCurrentUser();
                                    mFirsebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Log.d(TAG, "Email sent.");
                                                        Toast.makeText(SignUpActivity.this, "Email sent to: " + mFirsebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                        finishSignUp();
                                                    }
                                                }
                                            });
                                    if (task.isSuccessful())
                                    {
                                    }
                                    else
                                    {
                                    }
                                }
                            });
                        }
                        //display a failure message{
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        Log.e(TAG, "registerUser() <<");
    }

    private void finishSignUp()
    {
        Log.e(TAG, "finishSignUp() >>");
        updateUserNameInDB();
        Intent intent_SignUp = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent_SignUp);
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        Toast.makeText(getApplicationContext(), "Please verify email and then sign in", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "finishSignUp() <<");

    }

    private void onClickBackBtn()
    {
        Log.e(TAG, "onClickBackBtn() >>");
        Intent intent_SignUp = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent_SignUp);
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        Log.e(TAG, "onClickBackBtn() <<");
    }

    private void updateUserNameInDB()
    {
        final  String userName = m_etName.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName).setPhotoUri(null).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile name updated.");
                        }
                    }
                });

    }







}
