package com.example.ido.appex2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private User mUser = null;

    /// xml buttons
    private Button m_userInfo_btn;
    private Button m_product_btn;
    private EditText m_et_searchBook;
    private Button m_button_search;
    private RadioButton m_radioButtonByPrice;
    private RadioButton m_radioButtonByReviews;
    private Spinner m_search_spinner;
    private TextView m_orderby_label;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        m_userInfo_btn = (Button)findViewById(R.id.userInfo_btn);
        m_product_btn = (Button)findViewById(R.id.product_btn);
        m_et_searchBook =(EditText)findViewById(R.id.et_searchBook);
        m_button_search =(Button)findViewById(R.id.button_search);
        m_radioButtonByPrice =(RadioButton)findViewById(R.id.radioButtonByPrice);
        m_radioButtonByReviews = (RadioButton)findViewById(R.id.radioButtonByRating);
        m_search_spinner =(Spinner)findViewById(R.id.search_spinner);
        m_orderby_label =(TextView )findViewById(R.id.orderby_label);

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
                    Log.e(TAG, "onDataChange(User) >> " + snapshot.getValue(User.class).toString());
                    Toast.makeText(getApplicationContext(), "Welcome : " +
                            snapshot.getValue(User.class).toString()
                            , Toast.LENGTH_SHORT).show();
                    mUser = snapshot.getValue(User.class);
                    Log.e(TAG, "onDataChange(User) After "
                            + mUser.getFullName());
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
       Toast.makeText(getApplicationContext(), "Welcome : "
                , Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
       // String searchString = mSearch_text.getText().toString();
        String orderBy = ((RadioButton) findViewById(R.id.radioButtonByRating)).isChecked() ? "rating" : "price";
        Query searchBook;
        //Toast.makeText(AllProductsActivity.this, spiner_text, Toast.LENGTH_SHORT).show();
        m_BooksList.clear();


        searchBook = mAllBooksRef.orderByChild(orderBy);
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

        boolean isAutor = false;
        Spinner mySpinner = (Spinner) findViewById(R.id.search_spinner);
        String spiner_text = mySpinner.getSelectedItem().toString();
        String searchString = ((EditText) findViewById(R.id.et_searchBook)).getText().toString();

        if (searchString != null && !searchString.isEmpty())
        {
            if (spiner_text.equals("Author"))
            {
                isAutor = true;
            }
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                AudioBook book = dataSnapshot.getValue(AudioBook.class);
                Log.e(TAG, "updateSongList() >> adding song: " + book.getName());
                String key = dataSnapshot.getKey();
                if (isAutor)
                {
                    if (book.getAuthor().startsWith(searchString))
                    {
                        m_BooksList.add(new AudioBookWithKey(key, book));
                    }
                } else
                {
                    if (book.getName().startsWith(searchString))
                    {
                        m_BooksList.add(new AudioBookWithKey(key, book));
                    }
                }
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void onRadioButtonCLick(View v)
    {
        switch (v.getId())
        {
            case R.id.radioButtonByPrice:
                ((RadioButton) findViewById(R.id.radioButtonByRating)).setChecked(false);
                break;
            case R.id.radioButtonByRating:
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
        //intent.putExtra("User", i_UserRef.getUser());

        intent.putExtra("AudioBook", i_book.getAudioBook());
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("User", i_User);
//        intent.putExtra("Bundle", bundle);
        //Log.e(TAG, ">>>>>>>>>>>>>##" + i_UserRef.getUser().getFullName());
        startActivity(intent);
        //finish();
        Log.e(TAG, "onAudioBookCardClick <<");
    }
}