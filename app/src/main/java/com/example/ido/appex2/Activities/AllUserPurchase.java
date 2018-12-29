package com.example.ido.appex2.Activities;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ido.appex2.Adapter.AudioBookWithKey;
import com.example.ido.appex2.Adapter.UserBooksAdapter;
import com.example.ido.appex2.MenuItemFunctions;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllUserPurchase extends AppCompatActivity implements Interface_OnClickAudioBookCard
{
    public static final String TAG = "AllUserPurchase:";

    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User m_User;
    private FirebaseAuth m_Auth;
    private List<AudioBookWithKey> m_BooksList = new ArrayList<>();
    private UserBooksAdapter mAdapter;
    private DatabaseReference mMyUserRef;
    private DatabaseReference mAllBooksRef;


    private TextView mRatingReview;
    private ImageView mItemBookImage;
    private TextView  mUserItemBookName;
    private TextView  mUserItemAuthorName;
    private TextView  mUserItemGenrekName;
    private TextView mUserReviesCount;
    private ImageView mRatingStar;
    private String m_Key;
    private List<String> mUserBookList;
    private MenuItemFunctions m_MenuFunctions ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_purchase);
        createLayoutConnections();
        createMenuConnections();
        getAllUserBooks();
        Log.e(TAG, "onCreate() <<");
    }

    private void createMenuConnections()
    {
        Log.e(TAG, "createMenuConnections() >>");
        Toolbar toolbar =(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        m_MenuFunctions =new MenuItemFunctions(this);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setSubtitle("Your Orders");
        Log.e(TAG, "createMenuConnections() <<");

    }


    private void createLayoutConnections()
    {
        Log.e(TAG, "createLayoutConnections() >>");
        mRecyclerView = findViewById(R.id.AllUserPurchase_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mUserBookList = new ArrayList<String>();
        mRatingReview = findViewById(R.id.user_rating_review_tv);
        mItemBookImage = findViewById(R.id.user_item_book_image);
        mUserItemBookName = findViewById(R.id.user_item_book_name);
        mUserItemAuthorName = findViewById(R.id.user_item_book_auther);
        mUserItemGenrekName = findViewById(R.id.user_item_book_genre);
        mUserReviesCount = findViewById(R.id.user_item_book_review_count);
        mRatingStar= findViewById(R.id.user_ratingstar_iv);
        m_Auth = FirebaseAuth.getInstance();
        m_Key = getIntent().getStringExtra("Key");
        Log.e(TAG, "createLayoutConnections() <<");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.e(TAG, "onCreateOptionsMenu() >>");
        m_MenuFunctions = new MenuItemFunctions(this);
        m_MenuFunctions.onCreateOptionsMenu(menu);
        m_MenuFunctions.setOnClickSearch();
        Log.e(TAG, "onCreateOptionsMenu() <<");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.e(TAG, "onOptionsItemSelected() >>");
        m_MenuFunctions.onOptionItemSelect(item);
        Log.e(TAG, "onOptionsItemSelected() <<");
        return  super.onOptionsItemSelected(item);
    }


    private void getAllUserBooks()
    {
        Log.e(TAG, "getAllUserBooks() >>");
        m_BooksList.clear();
        mAdapter = new UserBooksAdapter(m_BooksList, m_User, this);
        mRecyclerView.setAdapter(mAdapter);
        getAllBooksUsingChildListenrs();
        Log.e(TAG, "getAllUserBooks <<");
    }

    private void getAllBooksUsingChildListenrs()
    {
        Log.e(TAG, "getAllBooksUsingChildListenrs() >>");

        mMyUserRef=FirebaseDatabase.getInstance().getReference("Users");
        mMyUserRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName)
            {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                User user = snapshot.getValue(User.class);

                if(snapshot.getKey().equals(uid))
                {
                    mUserBookList = user.getMyAudioBooks();
                }

            }



            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName)
            {
            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName)
            {
                Log.e(TAG, "onChildMoved(Songs) >> " + snapshot.getKey());
                Log.e(TAG, "onChildMoved(Songs) << Doing nothing");
            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }

        });

        getTheAudioBooksListFromFirebase();
        Log.e(TAG, "getAllBooksUsingChildListenrs() <<");

    }


    @Override
    public void onAudioBookCardClick(AudioBookWithKey i_book)
    {
        Log.e(TAG, "onAudioBookCardClick >> " + i_book.getAudioBook().getName());
        Intent intent = new Intent(this, AudioBookDetailsActivity.class);
        intent.putExtra("Key", i_book.getKey());
        intent.putExtra("AudioBook", i_book.getAudioBook());
        startActivity(intent);
        Log.e(TAG, "onAudioBookCardClick <<");
    }

    private void getTheAudioBooksListFromFirebase()
    {
        Log.e(TAG, "getTheAudioBooksListFromFirebase >>");
        mAllBooksRef = FirebaseDatabase.getInstance().getReference("AudioBooks");
        mAllBooksRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName)
            {

                AudioBookWithKey bookWithKey = new AudioBookWithKey(snapshot.getKey(),snapshot.getValue(AudioBook.class));
                for(String s : mUserBookList)
                {
                    if (s.equals(bookWithKey.getKey()))
                    {
                        m_BooksList.add(bookWithKey);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
                Log.e(TAG, "onChildAdded(Books) <<");

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildChanged(Books) >> " + snapshot.getKey());

                AudioBook book =snapshot.getValue(AudioBook.class);
                String key = snapshot.getKey();

                for (int i = 0 ; i < m_BooksList.size() ; i++)
                {
                    AudioBookWithKey bookWithKey = (AudioBookWithKey) m_BooksList.get(i);

                    for(String s : mUserBookList)
                    {
                        if(s.equals(bookWithKey.getKey()))
                        {
                            if (bookWithKey.getKey().equals(key))
                            {
                                bookWithKey.setAudioBook(book);
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;
                            }
                        }
                    }


                }

                Log.e(TAG, "onChildChanged(Books) <<");

            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName)
            {

                Log.e(TAG, "onChildMoved(Books) >> " + snapshot.getKey());


                Log.e(TAG, "onChildMoved(Books) << Doing nothing");

            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot)
            {

                Log.e(TAG, "onChildRemoved(Books) >> " + snapshot.getKey());
                String key = snapshot.getKey();
                for (int i = 0 ; i < m_BooksList.size() ; i++)
                {
                    AudioBookWithKey songWithKey = (AudioBookWithKey) m_BooksList.get(i);
                    if (songWithKey.getKey().equals(key))
                    {
                        m_BooksList.remove(i);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onChildRemoved(Books) >> i=" + i);
                        break;
                    }
                }

                Log.e(TAG, "onChildRemoved(Books) <<");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "onCancelled(Books) >>" + databaseError.getMessage());
            }

        });
        Log.e(TAG, "getTheAudioBooksListFromFirebase <<");

    }
}
