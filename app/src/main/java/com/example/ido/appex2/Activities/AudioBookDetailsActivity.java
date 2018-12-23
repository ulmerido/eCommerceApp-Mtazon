package com.example.ido.appex2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.Review;
import com.example.ido.appex2.entities.User;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AudioBookDetailsActivity extends AppCompatActivity
{

    public static final String TAG = "AudBookDetActiv:";

    private AudioBook m_AudioBook;
    private String m_Key;
    private User m_User;

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
    private Button    m_btBack;

    private DatabaseReference m_AudioBookReviewsRef;

    private List<Review> reviewsList =  new ArrayList<>();

    private boolean m_AudioBookWasPurchased;








    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        m_Key = getIntent().getStringExtra("Key");
        m_User = getIntent().getParcelableExtra("User");
        m_AudioBook = getIntent().getParcelableExtra("AudioBook");


        //m_etSearch = findViewById(R.id.details_searchBook);
        m_etReviewHeader = findViewById(R.id.details_ReviewHeader);
        m_etReviewBody = findViewById(R.id.details_ReviewBody);

        m_tvBookName = findViewById(R.id.details_book_name);
        m_tvBookAuther = findViewById(R.id.details_auther);
        m_tvBookGenre = findViewById(R.id.details_genre);
        m_tvBookPrice = findViewById(R.id.details_price);
        m_tvBookReviewCount = findViewById(R.id.details_ReviewCount);
        m_tvBookReviewAvg = findViewById(R.id.details_ReviewAvg);
        m_tvPlaySample = findViewById(R.id.details_playSampleText);
        m_ivBookImage =findViewById(R.id.details_book_image);
        //m_btnSearch = findViewById(R.id.details_button_search);
        m_btnPlay = findViewById(R.id.details_Play);
        m_addReview = findViewById(R.id.details_AddNewReview);
        m_Buy = findViewById(R.id.details_buy);
        m_btBack = findViewById(R.id.btBack);
        populate();

        m_btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        try
//        {
//            Intent intent = getIntent();
//            Bundle bundle = intent.getExtras();
//            if (bundle != null)
//            {
//                m_AudioBook = bundle.getParcelable("AudioBook");
//
//            }
//
//            populate();
//        }
//        catch (Exception e)
//        {
//            Log.e(TAG,e.getMessage().toString());
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void populate()
    {
        Log.e(TAG, "populate>>");
        m_tvBookName.setText(m_AudioBook.getName());
        m_tvBookGenre.setText(m_AudioBook.getGenre());
        m_tvBookAuther.setText(m_AudioBook.getAuthor());
        m_tvBookReviewCount.setText("(" + Integer.toString(m_AudioBook.getReviewsCount()) + ")");
        m_tvBookPrice.setText(Integer.toString(m_AudioBook.getPrice()) +"$");
        m_tvBookReviewAvg.setText("[" + Double.toString(m_AudioBook.getRating()) + "]");




        Log.e(TAG, "updateProfilePicInTheActivityView() >>");


        Glide.with(this.getApplicationContext())
                .load(m_AudioBook.getThumbImage())
                .thumbnail(Glide.with(this.getApplicationContext()).load(R.drawable.sppiner_loading))
                .fallback(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(m_ivBookImage);


        Log.e(TAG, "updateProfilePicInTheActivityView() <<");
        Log.e(TAG,"Hello World "+ m_AudioBook.getThumbImage());

        Log.e(TAG, "populate<<");

    }
}
