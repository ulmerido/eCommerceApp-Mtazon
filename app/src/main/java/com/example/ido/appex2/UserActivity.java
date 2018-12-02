package com.example.ido.appex2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class UserActivity extends AppCompatActivity
{
    private FirebaseAuth m_Auth;
    private TextView m_Status;
    private ImageView m_ProfileImage;
    private TextView m_Email;
    private TextView m_UserName;
    private Button m_Logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        m_Auth = FirebaseAuth.getInstance();
        m_Status = findViewById(R.id.tvStatusUser);
        m_UserName = findViewById(R.id.tvUserNameFacebook);
        m_Email =  findViewById(R.id.tvEmailFacebook);
        m_ProfileImage = findViewById(R.id.ivFacebook);
        m_Logout_btn = findViewById(R.id.btn_Logout);

        m_Logout_btn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                m_Auth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent_LogedIn = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_LogedIn);
                finish();
            }
        });

        updateLoginStatus("kaki");

    }


    private void updateLoginStatus(String details)
    {
        String profilePicUrl ="";

        FirebaseUser user = m_Auth.getCurrentUser();
        Toast.makeText(getApplicationContext(), user.getProviderId(), Toast.LENGTH_SHORT).show();


        if (user.isAnonymous())
        {
            m_Status.setText("Anonymous signed");
            m_UserName.setText("Name: Anoni Mos");
            m_Email.setText("email: mos@ano.ni" );
            m_ProfileImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }
        else
        {
            for(String a: user.getProviders() )
            {
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();

                if (a.contains("facebook"))
                {
                    profilePicUrl = user.getPhotoUrl().toString() + "/picture?type=large";
                }
                else if (a.contains("google"))
                {
                    profilePicUrl = user.getPhotoUrl().toString();
                }
            }

            m_Status.setText("SIGNED-IN");
            m_UserName.setText("NAME: " + user.getDisplayName());
            m_Email.setText("EMAIL: " + user.getEmail());

            // if (mConfig.getBoolean("display_profile_image")) {
            Glide.with(this)
                    .load(profilePicUrl)
                     .into(m_ProfileImage);
        }


    }
}
