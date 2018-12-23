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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ido.appex2.Adapter.AudioBookWithKey;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AudioBookDetailsActivity extends AppCompatActivity
{

    public static final String TAG = "AudBookDetActiv:";

    private AudioBook m_AudioBook;
    private String m_Key;
    private User m_User;
    private FirebaseAuth m_Auth;
    private DatabaseReference m_MyUserRef;

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
    private Button m_btnSearch;
    private Button m_btnPlay;
    private Button m_addReview;
    private Button m_Buy;
    private Button m_btBack;

    private DatabaseReference m_AudioBookReviewsRef;

    private List<Review> reviewsList = new ArrayList<>();

    private boolean m_AudioBookWasPurchased;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        m_Auth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        m_Key = getIntent().getStringExtra("Key");
        //m_User = getIntent().getParcelableExtra("User");
        m_AudioBook = getIntent().getParcelableExtra("AudioBook");

        m_MyUserRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUser.getUid());
        m_MyUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                Log.e(TAG, "onDataChange(User) >> " + snapshot.getKey());
                Log.e(TAG, "onDataChange(User) >> " + snapshot.getValue(User.class).toString());
                Toast.makeText(getApplicationContext(), "Welcome : " +
                                snapshot.getValue(User.class).toString()
                        , Toast.LENGTH_SHORT).show();
                m_User = snapshot.getValue(User.class);
                Log.e(TAG, "onDataChange(User) After--->>> " + m_User.getFullName());
                Log.e(TAG, "onDataChange(User) <<");

                m_Buy.setText("BUY $" + m_AudioBook.getPrice());
                Iterator i = m_User.getMyAudioBooks().iterator();
                while (i.hasNext()) {
                    if (i.next().equals(m_Key)) {
                        m_AudioBookWasPurchased = true;
                        m_Buy.setText("PLAY");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "onCancelled(Users) >>" + databaseError.getMessage());
            }
        });

        //Log.e(TAG, " Key : " + m_Key);
        //Log.e(TAG, " AudioBook Name: " + m_AudioBook.getName());

        //m_etSearch = findViewById(R.id.details_searchBook);
        m_etReviewHeader = findViewById(R.id.details_ReviewHeader);
        m_etReviewBody = findViewById(R.id.details_ReviewBody);
        m_ivRatingImage =findViewById(R.id.details_ratingstar_iv);
        m_tvBookName = findViewById(R.id.details_book_name);
        m_tvBookAuther = findViewById(R.id.details_auther);
        m_tvBookGenre = findViewById(R.id.details_genre);
        m_tvBookPrice = findViewById(R.id.details_price);
        m_tvBookReviewCount = findViewById(R.id.details_ReviewCount);
        m_tvBookReviewAvg = findViewById(R.id.details_ReviewAvg);
        m_tvPlaySample = findViewById(R.id.details_playSampleText);
        m_ivBookImage = findViewById(R.id.details_book_image);
        //m_btnSearch = findViewById(R.id.details_button_search);
        m_btnPlay = findViewById(R.id.details_Play);
        m_addReview = findViewById(R.id.details_AddNewReview);
        m_Buy = findViewById(R.id.details_buy);
        m_btBack = findViewById(R.id.btBack);
        populate();

        m_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "buyPlay.onClick() >> file=" + m_AudioBook.getName());

                if (m_AudioBookWasPurchased) {
                    Log.e(TAG, "buyPlay.onClick() >> Playing purchased song");
                    //User purchased the song so he can play it
                    //playCurrentSong(song.getFile());

                } else {
                    //Purchase the song.
                    Log.e(TAG, "buyPlay.onClick() >> Purchase the song");
                    m_User.getMyAudioBooks().add(m_Key);
                    m_User.upgdateTotalPurchase(m_AudioBook.getPrice());
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(m_User);
                    m_AudioBookWasPurchased = true;
                    m_Buy.setText("PLAY");
                }
                Log.e(TAG, "playSong.onClick() <<");
            }
        });

        m_btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
        m_addReview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickAddReview();
            }
        });

        m_ivRatingImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickRating();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


    private void populate()
    {
        Log.e(TAG, "populate>>");
        m_tvBookName.setText(m_AudioBook.getName());
        m_tvBookGenre.setText(m_AudioBook.getGenre());
        m_tvBookAuther.setText(m_AudioBook.getAuthor());
        m_tvBookReviewCount.setText("(" + Integer.toString(m_AudioBook.getReviewsCount()) + ")");
        m_tvBookPrice.setText(Integer.toString(m_AudioBook.getPrice()) + "$");
        m_tvBookReviewAvg.setText("[" + Double.toString(m_AudioBook.getRating()) + "]");

        Log.e(TAG, "updateProfilePicInTheActivityView() >>");


        Glide.with(this.getApplicationContext())
                .load(m_AudioBook.getThumbImage())
                .thumbnail(Glide.with(this.getApplicationContext()).load(R.drawable.sppiner_loading))
                .fallback(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(m_ivBookImage);


        Log.e(TAG, "updateProfilePicInTheActivityView() <<");
        Log.e(TAG, "Hello World " + m_AudioBook.getThumbImage());

        Log.e(TAG, "populate<<");

    }


    public void onClickAddReview()
    {
        Log.e(TAG, "onClickAddReview>>");

        String header = m_etReviewHeader.getText().toString();
        String body = m_etReviewBody.getText().toString();
        FirebaseUser user = m_Auth.getCurrentUser();
        int rating = 3;

        if (checkReviewParams(header, body, rating))
        {

            Review review = new Review(header, body, rating, user.getEmail().toString(), user.getUid(),m_Key );
            //DatabaseReference reviewsRef = ref.child("Reviews");
            DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference();
            reviewRef.child("Review").push().setValue(review);
        }
        Log.e(TAG, "onClickAddReview<<");

    }

    public boolean checkReviewParams(String i_Header, String i_Body, int rating)
    {
        return true;
    }

    public void onClickRating()
    {

       Log.e(TAG, "onClickRating >> " );
        Intent intent = new Intent(this, AllReviewsActivity.class);
        intent.putExtra("Key", m_Key);
        startActivity(intent);
        //finish();
        Log.e(TAG, "onClickRating <<");
    }
}
