package com.example.ido.appex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText     m_etPassword;
    private EditText     m_etEmail;
    private EditText     m_etFirstName;
    private EditText     m_etLastName;
    private FirebaseAuth m_Auth;
    private FirebaseUser m_FirsebaseUser;
    private Button       m_btnBack;
    private Button       m_btnRegister;
    public static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        m_Auth = FirebaseAuth.getInstance();
        m_etPassword = findViewById(R.id.etPassword);
        m_etEmail = findViewById(R.id.etEmail);
        m_etFirstName = findViewById(R.id.etFname);
        m_etLastName = findViewById(R.id.etLname);

        m_btnRegister = (Button) findViewById(R.id.btnSignUp);
        m_btnBack = (Button) findViewById(R.id.btnBack);
        m_btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickRegiser();
            }
        });
        m_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickBackBtn();
            }

        });

        ValidationChecker.CheckFullName(m_etFirstName);
        ValidationChecker.CheckFullName(m_etLastName);
        ValidationChecker.CheckEmail(m_etEmail);
        ValidationChecker.CheckPassword(m_etPassword);

    }

    private boolean successfullValidation()
    {
        if(!ValidationChecker.CheckValidEmail(m_etEmail)||!ValidationChecker.CheckValidName(m_etLastName) || !ValidationChecker.CheckValidName(m_etFirstName) || !ValidationChecker.CheckValidPassword(m_etPassword))
        {
            return false;
        }

        return true;
    }

    private void onClickRegiser()
    {
        if(!successfullValidation())
        {
            Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e(TAG, "registerUser() >>");
        final String passString = m_etPassword.getText().toString().trim();
        final String emailString = m_etEmail.getText().toString().trim();

        m_Auth.createUserWithEmailAndPassword(emailString, passString)
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
                                    m_FirsebaseUser = m_Auth.getCurrentUser();
                                    m_FirsebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Log.d(TAG, "Email sent.");
                                                        Toast.makeText(SignUpActivity.this, "Email sent to: " + m_FirsebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                        finishSignUp();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
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
        m_Auth.signOut();
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
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        m_Auth.signOut();
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        Log.e(TAG, "onClickBackBtn() <<");
    }

    private void updateUserNameInDB()
    {
        Log.e(TAG, "updateUserNameInDB() >>");

        final  String userName = m_etFirstName.getText().toString().trim() + " " + m_etLastName.getText().toString();
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
        Log.e(TAG, "updateUserNameInDB() <<");

    }


}
