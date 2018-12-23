package com.example.ido.appex2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.ido.appex2.Adapter.AudioBookAdapter;
import com.example.ido.appex2.Adapter.AudioBookWithKey;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AllProductsActivity extends AppCompatActivity  implements Interface_OnClickAudioBookCard
{
    public static final String TAG = "AllProductsActivity";
    private AudioBookAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    public RecyclerView mRecyclerView;
    private DatabaseReference mAllBooksRef;
    private DatabaseReference mMyUserRef;
    private List<AudioBookWithKey> m_BooksList = new ArrayList<>();
    private User mUser;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);


        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fbUser != null)
        {
            mMyUserRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUser.getUid());
            mMyUserRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {

                    Log.e(TAG, "onDataChange(User) >> " + snapshot.getKey());
                    mUser = snapshot.getValue(User.class);
                    getAllBooks();
                    Log.e(TAG, "onDataChange(User) <<");
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(TAG, "onCancelled(Users) >>" + databaseError.getMessage());
                }
            });
            Log.e(TAG, "onCreate() <<");
        }
        else
        {
            getAllBooks();
        }

    }


    private void getAllBooks()
    {
        Log.e(TAG, "getAllBooks() >>");

        m_BooksList.clear();
        mAdapter = new AudioBookAdapter(m_BooksList, mUser,this);
        mRecyclerView.setAdapter(mAdapter);
        getAllBooksUsingChildListenrs();
        Log.e(TAG, "getAllBooks <<");

    }

    private void getAllBooksUsingChildListenrs()
    {
        Log.e(TAG, "getAllBooksUsingChildListenrs() >>");

        mAllBooksRef = FirebaseDatabase.getInstance().getReference("AudioBooks");

        mAllBooksRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName)
            {
                Log.e(TAG, "onChildAdded(Books) >> " + snapshot.getKey());

                AudioBookWithKey bookWithKey = new AudioBookWithKey(snapshot.getKey(),snapshot.getValue(AudioBook.class));

                m_BooksList.add(bookWithKey);
                mRecyclerView.getAdapter().notifyDataSetChanged();

                Log.e(TAG, "onChildAdded(Books) <<");

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildChanged(Songs) >> " + snapshot.getKey());

                AudioBook book =snapshot.getValue(AudioBook.class);
                String key = snapshot.getKey();

                for (int i = 0 ; i < m_BooksList.size() ; i++) {
                    AudioBookWithKey bookWithKey = (AudioBookWithKey) m_BooksList.get(i);
                    if (bookWithKey.getKey().equals(snapshot.getKey())) {
                        bookWithKey.setAudioBook(book);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }

                Log.e(TAG, "onChildChanged(Songs) <<");

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

                Log.e(TAG, "onChildRemoved(Songs) >> " + snapshot.getKey());

                AudioBook book =snapshot.getValue(AudioBook.class);
                String key = snapshot.getKey();

                for (int i = 0 ; i < m_BooksList.size() ; i++)
                {
                    AudioBookWithKey songWithKey = (AudioBookWithKey) m_BooksList.get(i);
                    if (songWithKey.getKey().equals(snapshot.getKey()))
                    {
                        m_BooksList.remove(i);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onChildRemoved(Songs) >> i=" + i);
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


    public void onSearchButtonClick(View v)
    {

        String searchString = ((EditText) findViewById(R.id.et_searchBook)).getText().toString();
        String orderBy = ((RadioButton) findViewById(R.id.radioButtonByReviews)).isChecked() ? "reviewCount" : "price";
        Query searchBook;

        Log.e(TAG, "onSearchButtonClick() >> searchString=" + searchString + ",orderBy=" + orderBy);

        m_BooksList.clear();

        if (searchString != null && !searchString.isEmpty())
        {
            searchBook = mAllBooksRef.orderByChild("name").startAt(searchString).endAt(searchString + "\uf8ff");

        }
        else
        {
            searchBook = mAllBooksRef.orderByChild(orderBy);
        }


        searchBook.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {

                Log.e(TAG, "onDataChange(Query) >> " + snapshot.getKey());

                updateSongsList(snapshot);

                Log.e(TAG, "onDataChange(Query) <<");

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

                Log.e(TAG, "onCancelled() >>" + databaseError.getMessage());
            }

        });
        Log.e(TAG, "onSearchButtonClick() <<");
    }


    private void updateSongsList(DataSnapshot snapshot)
    {
        for (DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            AudioBook book = dataSnapshot.getValue(AudioBook.class);
            Log.e(TAG, "updateSongList() >> adding song: " + book.getName());
            String key = dataSnapshot.getKey();
            m_BooksList.add(new AudioBookWithKey(key, book));
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    public void onRadioButtonCLick(View v)
    {
        switch (v.getId())
        {
            case R.id.radioButtonByPrice:
                ((RadioButton) findViewById(R.id.radioButtonByReviews)).setChecked(false);
                break;
            case R.id.radioButtonByReviews:
                ((RadioButton) findViewById(R.id.radioButtonByPrice)).setChecked(false);
                break;
        }
    }

    @Override
    public void onAudioBookCardClick(AudioBookWithKey i_book)
    {
        Log.e(TAG, "onAudioBookCardClick >> " + i_book.getAudioBook().getName());
        Intent intent = new Intent(this, AudioBookDetailsActivity.class);
        intent.putExtra("Key", i_book.getKey());
        intent.putExtra("User", mUser);
        intent.putExtra("AudioBook", i_book.getAudioBook());
        //Bundle bundle = new Bundle();
        //bundle.putParcelable("AudioBook", i_book.getAudioBook());
        //intent.putExtras(bundle);
        startActivity(intent);
        //finish();
        Log.e(TAG, "onAudioBookCardClick <<");



    }


}
