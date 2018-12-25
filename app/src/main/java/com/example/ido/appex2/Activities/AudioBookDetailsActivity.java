package com.example.ido.appex2.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private float m_PrevRating = -1;
    private RatingBar m_UserRating;

    private DatabaseReference m_AudioBookReviewsRef;
    private DatabaseReference m_AudioBookRef;
    private List<Review> reviewsList = new ArrayList<>();

    private boolean m_AudioBookWasPurchased;



    //------Media Player---------
    private Button b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private  int m_lengthOfSound = 0;
    public static int oneTimeOnly = 0;


    //---------------------------

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
        m_AudioBookRef = FirebaseDatabase.getInstance().getReference("AudioBooks/" + m_Key);
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
        //m_UserRating = findViewById(R.id.new_user_rating);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        //iv = (ImageView)findViewById(R.id.imageView);

        tx1 = (TextView)findViewById(R.id.textView2);
        tx2 = (TextView)findViewById(R.id.textView3);


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
        //m_addReview = findViewById(R.id.details_AddNewReview);
        m_Buy = findViewById(R.id.details_buy);
        m_btBack = findViewById(R.id.btBack);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        populate();
        createAndInvokeMediaPlayer();
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

        addListenerOnClickAddReview();

        m_btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
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


    public void addListenerOnClickAddReview(){


        m_UserRating = findViewById(R.id.new_rating_user);
        m_addReview = findViewById(R.id.details_AddNewReview);

        m_addReview.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                onClickAddReview();
            }
        });

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }




    private void createAndInvokeMediaPlayer()
    {


        //tx3 = (TextView)findViewById(R.id.textView4);
        //tx3.setText("Song.mp3");

                seekbar = (SeekBar)findViewById(R.id.seekBar);
                seekbar.setClickable(false);


        b2.setEnabled(false);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioBook(m_AudioBook.getFile());
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound"
                        ,Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                m_lengthOfSound = mediaPlayer.getCurrentPosition();
                Toast.makeText(getApplicationContext(),"Pausing at : " + m_lengthOfSound
                        ,Toast.LENGTH_SHORT).show();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds"
                            ,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds"
                            ,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) startTime))));
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private  void playAudioBook(String i_AudioBookFile)
    {
        Toast.makeText(getApplicationContext(), "Playing sound " +  i_AudioBookFile,Toast.LENGTH_SHORT).show();

        if(m_lengthOfSound > 0)
        {
            mediaPlayer.seekTo(m_lengthOfSound);
            mediaPlayer.start();
            whatToDoAfterPlayAudioBook();
        }

        else {
            FirebaseStorage.getInstance().getReference("AudioBooks/" + i_AudioBookFile)
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            Log.e(TAG, "onSuccess() >> " + downloadUrl.toString());

                            try {

                                mediaPlayer.setDataSource(downloadUrl.toString());
                                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                                mediaPlayer.start();
                                //buyPlay.setText("STOP");
                            } catch (Exception e) {
                                Log.w(TAG, "playSong() error:" + e.getMessage());
                            }

                            whatToDoAfterPlayAudioBook();

                            Log.e(TAG, "onSuccess() <<");
                        }
                    });
            Log.e(TAG, "playCurrentSong() << ");
        }
    }


private void whatToDoAfterPlayAudioBook()
{
    finalTime = mediaPlayer.getDuration();
    startTime = mediaPlayer.getCurrentPosition();

    if (oneTimeOnly == 0) {
        seekbar.setMax((int) finalTime);
        oneTimeOnly = 1;
    }

    tx2.setText(String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                            finalTime)))
    );

    tx1.setText(String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                            startTime)))
    );

    seekbar.setProgress((int) startTime);
    myHandler.postDelayed(UpdateSongTime, 100);
    b2.setEnabled(true);
    b3.setEnabled(false);
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

      if(checkReviewParams(m_etReviewHeader.getText().toString(),
              m_etReviewBody.getText().toString())) {
          whenAddedReviwWithRating();
          try {
              InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
          } catch (Exception e) {
              Log.e(TAG, "Exception " +e.getMessage());
          }
         }
         else {
          if (m_etReviewHeader.getText().toString().isEmpty()) {
              m_etReviewHeader.setHintTextColor(Color.RED);
          }
          if (m_etReviewBody.getText().toString().isEmpty()) {
              m_etReviewBody.setHintTextColor(Color.RED);
          }

      }
        Log.e(TAG, "onClickAddReview<<");
    }


private void whenAddedReviwWithRating() {
    m_AudioBookRef.runTransaction(new Transaction.Handler() {

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {

            Log.e(TAG, "doTransaction() >>");


            AudioBook audioBook = mutableData.getValue(AudioBook.class);

            if (audioBook == null) {
                Log.e(TAG, "doTransaction() << song is null");
                return Transaction.success(mutableData);
            }

            if (m_PrevRating == -1) {
                // Increment the review count and rating only in case the user enters a new review
                audioBook.incrementReviewCount();
                //audioBook.incrementRating((int) m_UserRating.getRating());
                double newRating = avgRatingOfAudioBook(audioBook.getRating(), m_UserRating.getRating(),
                        audioBook.getReviewsCount());
                audioBook.setRating(newRating);
            } else {
                //audioBook.incrementRating((int) (m_UserRating.getRating() - m_PrevRating));
            }

            mutableData.setValue(audioBook);
            Log.e(TAG, "doTransaction() << song was set");
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {

            Log.e(TAG, "onComplete() >>");

            if (databaseError != null) {
                Log.e(TAG, "onComplete() << Error:" + databaseError.getMessage());
                return;
            }

            if (committed) {
                String header = m_etReviewHeader.getText().toString();
                String body = m_etReviewBody.getText().toString();
                FirebaseUser user = m_Auth.getCurrentUser();
                float rating = m_PrevRating;
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                rating = m_UserRating.getRating();
                Review review = new Review(header, body, rating, user.getEmail(), user.getUid(), m_Key, dateFormat.format(date));
                //DatabaseReference reviewsRef = ref.child("Reviews");
                DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference();
                reviewRef.child("Review").push().setValue(review);
                reviewSetComlete();
            }

            Log.e(TAG, "onComplete() <<");
        }
    });
}

    private void reviewSetComlete()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AudioBookDetailsActivity.this,
                R.style.DialogeTheme);

        builder.setCancelable(true);
        builder.setTitle("Thank You!!");
        builder.setMessage("For given review for " + " ' " + m_AudioBook.getName()+ " ' ");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //alertTextView.setVisibility(View.VISIBLE);
                m_etReviewHeader.setText("");
                m_etReviewBody.setText("");
                m_UserRating.setIsIndicator(false);
                dialogInterface.cancel();
            }
        });
        builder.show();


    }


        private double avgRatingOfAudioBook(double i_LastRating, float i_NewRating, int i_NumOfRating)
        {
            return ((i_LastRating * (i_NumOfRating - 1) + i_NewRating) /i_NumOfRating);
        }


        public boolean checkReviewParams(String i_Header, String i_Body) {


            Log.e(TAG, "@@  Header--->> "+ i_Header + " Body " + i_Body);
            if ( !i_Header.isEmpty() && !i_Body.isEmpty() &&  m_UserRating.getRating() != 0) {

                return true;
            }

            return false;
        }

        public void onClickRating() {

            Log.e(TAG, "onClickRating >> ");
            Intent intent = new Intent(this, AllReviewsActivity.class);
            intent.putExtra("Key", m_Key);
            startActivity(intent);
            //finish();
            Log.e(TAG, "onClickRating <<");
        }
    }
