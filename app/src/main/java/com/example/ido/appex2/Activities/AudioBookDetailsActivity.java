package com.example.ido.appex2.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ido.appex2.R;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class AudioBookDetailsActivity extends AppCompatActivity
{

        public static final String TAG = "AudioBookDetailsActivity:";
        private EditText m_etSearch;
        private EditText m_etReviewHeader;
        private EditText m_etReviewBody;

        private TextView m_tvBookName;
        private TextView m_tvBookAuther;
        private TextView m_tvBookGenre;
        private TextView m_tvBookReviewCount;
        private TextView m_tvBookReviewAvg;
        private TextView m_tvPlaySample;
        private TextView m_tvBookPrice;

        private ImageView m_ivBookImage;
        private ImageView m_ivRatingImage;
        private Button    m_btnSearch;
        private Button    m_btnPlay;
        private Button    m_addReview;
        private Button    m_Buy;






    @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_details);

            m_etSearch = findViewById(R.id.details_searchBook);
            m_etReviewHeader = findViewById(R.id.details_ReviewHeader);
            m_etReviewBody = findViewById(R.id.details_ReviewBody);

            m_tvBookName = findViewById(R.id.details_book_name);
            m_tvBookAuther = findViewById(R.id.details_auther);
            m_tvBookGenre = findViewById(R.id.details_genre);
            m_tvBookPrice = findViewById(R.id.details_price);
            m_tvBookReviewCount = findViewById(R.id.details_ReviewCount);
            m_tvBookReviewAvg = findViewById(R.id.details_ReviewAvg);
            m_tvPlaySample = findViewById(R.id.details_playSampleText);

            m_btnSearch = findViewById(R.id.details_button_search);
            m_btnPlay = findViewById(R.id.details_Play);
            m_addReview = findViewById(R.id.details_AddNewReview);
            m_Buy = findViewById(R.id.details_buy);

        }
}
