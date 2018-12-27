package com.example.ido.appex2.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ido.appex2.Adapter.ReviewAdapter;
import com.example.ido.appex2.MenuItemFunctions;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.Review;
import com.example.ido.appex2.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllReviewsActivity extends AppCompatActivity
{
    public static final String TAG = "AllReviewsActivity:";

    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User m_User;
    private FirebaseAuth m_Auth;
    private List<Review> m_ReviewList = new ArrayList<>();
    private ReviewAdapter mAdapter;
    private DatabaseReference mMyUserRef;
    private DatabaseReference mAllReviewRef;
    private MenuItemFunctions m_MenuFunctions ;


    private TextView m_userName;
    private TextView m_ReviewHeader;
    private TextView m_ReviewBody;
    private TextView m_ReviewDate;
    private TextView m_ReviewRating;

    private ImageView m_UserImage;
    private ImageView m_ivRatingImage;
    private String m_Key;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        mRecyclerView = findViewById(R.id.Review_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        m_userName = findViewById(R.id.UserReview_user_name);
        m_ReviewHeader = findViewById(R.id.UserReview_header);
        m_ReviewBody = findViewById(R.id.UserReview_body);
        m_ReviewDate = findViewById(R.id.UserReview_Date);
        m_ReviewRating = findViewById(R.id.UserReview_rating_tv);

        m_UserImage = findViewById(R.id.UserReview_user_image);
        m_ivRatingImage = findViewById(R.id.UserReview_ratingstar_iv);
        m_Auth = FirebaseAuth.getInstance();
        m_Key = getIntent().getStringExtra("Key");

        Toolbar toolbar =(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        m_MenuFunctions =new MenuItemFunctions(this);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setSubtitle("Reviews");

        getAllReviews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.e("Test", "onCreateOptionsMenu() >>");

        m_MenuFunctions = new MenuItemFunctions(this);
        Log.e("Test", "onCreateOptionsMenu() <<");
        m_MenuFunctions.onCreateOptionsMenu(menu);
        m_MenuFunctions.setOnClickSearch();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        m_MenuFunctions.onOptionItemSelect(item);
        return  super.onOptionsItemSelected(item);
    }
    private void getAllReviews()
    {
        Log.e(TAG, "getAllReviews() >>");

        m_ReviewList.clear();
        mAdapter = new ReviewAdapter(m_ReviewList, m_User);
        mRecyclerView.setAdapter(mAdapter);
        getAllBooksUsingChildListenrs();
        Log.e(TAG, "getAllReviews <<");

    }

    private void getAllBooksUsingChildListenrs()
    {
        Log.e(TAG, "getAllBooksUsingChildListenrs() >>");

        mAllReviewRef = FirebaseDatabase.getInstance().getReference("Review");

        mAllReviewRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName)
            {
                Log.e(TAG, "onChildAdded(Review) >> " + snapshot.getKey());

                Review review = snapshot.getValue(Review.class);
                String bookKey = review.getBookID();
                Log.e(TAG, "onChildAdded(Review)m_Key >> " + m_Key);
                Log.e(TAG, "onChildAdded(Review)bookKey >> " + bookKey);

                if (!bookKey.equals(m_Key))
                {
                    Log.e(TAG, "return>> " + snapshot.getKey());

                    return;
                }
                m_ReviewList.add(snapshot.getValue(Review.class));
                mRecyclerView.getAdapter().notifyDataSetChanged();

                Log.e(TAG, "onChildAdded(Review) <<");

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName)
            {

                Log.e(TAG, "onChildChanged(Review) >> " + snapshot.getKey());

                Review review = snapshot.getValue(Review.class);
                String key = review.getUserEmail();
                String bookKey = review.getBookID();
                if (!bookKey.equals(m_Key))


                    for (int i = 0; i < m_ReviewList.size(); i++)
                    {
                        Review r = (Review) m_ReviewList.get(i);
                        if (r.getUserEmail().equals(key))
                        {
                            r = review;
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            break;
                        }
                    }

                Log.e(TAG, "onChildChanged(Review) <<");

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName)
            {

                Log.e(TAG, "onChildMoved(Review) >> " + snapshot.getKey());


                Log.e(TAG, "onChildMoved(Review) << Doing nothing");

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot)
            {

                Log.e(TAG, "onChildRemoved(Review) >> " + snapshot.getKey());

                Review review = snapshot.getValue(Review.class);
                String key = review.getUserEmail();

                for (int i = 0; i < m_ReviewList.size(); i++)
                {
                    Review r = (Review) m_ReviewList.get(i);
                    if (r.getUserEmail().equals(key))
                    {
                        m_ReviewList.remove(i);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onChildRemoved(Review) >> i=" + i);
                        break;
                    }
                }

                Log.e(TAG, "onChildRemoved(Songs) <<");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "onCancelled(Songs) >>" + databaseError.getMessage());
            }

        });


        Log.e(TAG, "getAllBooksUsingChildListenrs <<");

    }

}


