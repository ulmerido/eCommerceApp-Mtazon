package com.example.ido.appex2.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ido.appex2.Adapter.AudioBookAdapter;
import com.example.ido.appex2.Adapter.AudioBookWithKey;
import com.example.ido.appex2.MenuItemFunctions;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.example.ido.appex2.entities.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
    private String m_Key;
    /// xml buttons

    private EditText m_et_searchBook;
    private FirebaseUser m_fbUser;
    private FirebaseAuth m_Auth;
    private MenuItemFunctions m_MenuFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        createLayoutConnections();
        createMenuConnetions();
        Log.e(TAG, "onCreate() <<");
    }

    private void createLayoutConnections()
    {
        Log.e(TAG, "createLayoutConnections() >>");
        m_et_searchBook = (EditText) findViewById(R.id.et_searchBook);
        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        m_fbUser = FirebaseAuth.getInstance().getCurrentUser();
        m_Auth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.e(TAG, "createLayoutConnections() <<");

    }

    private void createMenuConnetions()
    {
        Log.e(TAG, "createMenuConnetions() >>");
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setSubtitle("All products");
        Intent intent = getIntent();
        if (intent != null)
        {
            String search = intent.getStringExtra("search");
            if (search != null)
            {
                m_et_searchBook.setText(search);
            }
        }

        m_MenuFunctions = new MenuItemFunctions(this);

        if (m_fbUser != null)
        {
            onCreateWithUser();
        } else
        {
            onCreateWithoutUser();
        }
        ButterKnife.bind(this);
        m_Key = getIntent().getStringExtra("Key");
        Log.e(TAG, "createMenuConnetions() <<");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.e(TAG, "onCreateOptionsMenu() >>");
        Log.e("Test", "onCreateOptionsMenu() >>");
        m_MenuFunctions = new MenuItemFunctions(this);
        Log.e("Test", "onCreateOptionsMenu() <<");
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
        return super.onOptionsItemSelected(item);
    }

    private void onCreateWithUser()
    {
        Log.e(TAG, "onCreateWithUser() >>");
        getAllBooks();
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                mMyUserRef = FirebaseDatabase.getInstance().getReference("Users/" + m_fbUser.getUid());
                mMyUserRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot snapshot)
                    {
                        Log.e(TAG, "onDataChange(User) >> " + snapshot.getKey());
                        mUser = snapshot.getValue(User.class);
                        Log.e(TAG, "onDataChange(User) << " + mUser.getFullName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.e(TAG, "onCancelled(Users) >>" + databaseError.getMessage());
                    }
                });
            }
        }, 2000);
        Log.e(TAG, "onCreateWithUser() <<");
    }

    private void onCreateWithoutUser()
    {
        Log.e(TAG, "onCreate() >>");
        getAllBooks();
        Log.e(TAG, "onCreate() <<");
    }


    @Override
    public void onBackPressed()
    {
        Log.e(TAG, "onBackPressed() >>");
        logOutOrNot();
        Log.e(TAG, "onBackPressed() <<");
    }

    private void logOutOrNot()
    {
        Log.e(TAG, "logOutOrNot() >>");
        AlertDialog.Builder builder = new AlertDialog.Builder(AllProductsActivity.this, R.style.DialogeTheme);
        builder.setCancelable(true);
        builder.setTitle("Confirm Log Out");
        builder.setMessage("Are you sure you want to logout??");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                m_Auth.signOut();
                LoginManager.getInstance().logOut();
                dialogInterface.cancel();
                Intent intent_LogOut = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_LogOut);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
        Log.e(TAG, "logOutOrNot() <<");

    }

    private void getAllBooks()
    {
        Log.e(TAG, "getAllBooks() >>");
        m_BooksList.clear();
        mAdapter = new AudioBookAdapter(m_BooksList, mUser, this);
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
                AudioBookWithKey bookWithKey = new AudioBookWithKey(snapshot.getKey(), snapshot.getValue(AudioBook.class));
                String bookToSearch = m_et_searchBook.getText().toString();
                String currentBookName = bookWithKey.getAudioBook().getName();

                if (currentBookName.toLowerCase().startsWith(bookToSearch.toLowerCase()))
                {
                    m_BooksList.add(bookWithKey);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }

                Log.e(TAG, "onChildAdded(Books) <<");
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName)
            {
                Log.e(TAG, "onChildChanged >> " + snapshot.getKey());
                AudioBook book = snapshot.getValue(AudioBook.class);
                String key = snapshot.getKey();
                for (int i = 0; i < m_BooksList.size(); i++)
                {
                    AudioBookWithKey bookWithKey = (AudioBookWithKey) m_BooksList.get(i);
                    if (bookWithKey.getKey().equals(key))
                    {
                        bookWithKey.setAudioBook(book);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }

                Log.e(TAG, "onChildChanged <<");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName)
            {
                Log.e(TAG, "onChildMoved(book) >> " + snapshot.getKey());
                Log.e(TAG, "onChildMoved(book) << Doing nothing");
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot)
            {

                Log.e(TAG, "onChildRemoved(Books) >> " + snapshot.getKey());

                String key = snapshot.getKey();

                for (int i = 0; i < m_BooksList.size(); i++)
                {
                    AudioBookWithKey bookWithKey = (AudioBookWithKey) m_BooksList.get(i);
                    if (bookWithKey.getKey().equals(key))
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
                Log.e(TAG, "onCancelled(Books) >><<" + databaseError.getMessage());
            }

        });
        Log.e(TAG, "getAllBooksUsingChildListenrs <<");

    }

    public void onSearchButtonClick(View v)
    {
        Log.e(TAG, "onSearchButtonClick() >>");
        String orderBy = ((RadioButton) findViewById(R.id.radioButtonByRating)).isChecked() ? "rating" : "price";
        Query searchBook;
        m_BooksList.clear();
        searchBook = mAllBooksRef.orderByChild(orderBy);
        searchBook.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                Log.e(TAG, "onDataChange(Query) >> " + snapshot.getKey());
                updateAudioBooksList(snapshot);
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

    private void updateAudioBooksList(DataSnapshot snapshot)
    {
        Log.e(TAG, "updateAudioBooksList() >>");
        boolean isAutor = false, isPrice = false;
        Spinner mySpinner = (Spinner) findViewById(R.id.search_spinner);
        String spiner_text = mySpinner.getSelectedItem().toString();
        String searchString = ((EditText) findViewById(R.id.et_searchBook)).getText().toString();
        Deque<AudioBookWithKey> stack = new ArrayDeque<AudioBookWithKey>();
        String orderBy = ((RadioButton) findViewById(R.id.radioButtonByRating)).isChecked() ? "rating" : "price";
        if (orderBy.equals("price"))
        {
            isPrice = true;
        }
        if (searchString != null)
        {
            if (spiner_text.equals("Author"))
            {
                isAutor = true;
            }
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                AudioBook book = dataSnapshot.getValue(AudioBook.class);
                Log.e(TAG, "updateAudioBooksList() >> adding book: " + book.getName());
                String key = dataSnapshot.getKey();
                if (isAutor)
                {
                    if (book.getAuthor().toLowerCase().startsWith(searchString.toLowerCase()))
                    {
                        if (isPrice)
                        {
                            m_BooksList.add(new AudioBookWithKey(key, book));
                        } else
                        {
                            stack.push(new AudioBookWithKey(key, book));
                        }
                    }
                } else
                {
                    if (book.getName().toLowerCase().startsWith(searchString.toLowerCase()))
                    {
                        if (isPrice)
                        {
                            m_BooksList.add(new AudioBookWithKey(key, book));
                        } else
                        {
                            stack.push(new AudioBookWithKey(key, book));
                        }
                    }
                }
            }
            while (!stack.isEmpty())
            {
                m_BooksList.add(stack.pop());
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        Log.e(TAG, "updateAudioBooksList() <<");

    }

    public void onRadioButtonCLick(View v)
    {
        Log.e(TAG, "onRadioButtonCLick() >>");
        switch (v.getId())
        {
            case R.id.radioButtonByPrice:
                ((RadioButton) findViewById(R.id.radioButtonByRating)).setChecked(false);
                break;
            case R.id.radioButtonByRating:
                ((RadioButton) findViewById(R.id.radioButtonByPrice)).setChecked(false);
                break;
        }
        Log.e(TAG, "onRadioButtonCLick() <<");

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
}