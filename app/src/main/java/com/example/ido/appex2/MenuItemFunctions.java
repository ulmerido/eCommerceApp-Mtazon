package com.example.ido.appex2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ido.appex2.Activities.AllProductsActivity;
import com.example.ido.appex2.Activities.AllUserPurchase;
import com.example.ido.appex2.Activities.MainActivity;
import com.example.ido.appex2.Activities.UserActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class MenuItemFunctions
{
    AppCompatActivity m_Activity;
    Menu m_Menu;
    SearchView m_SearchView;
    private final String TAG = "Menu_Item_Functions" ;
    public MenuItemFunctions(AppCompatActivity i_Activity)
    {
        m_Activity=i_Activity;
        Log.e(TAG, "Ctr() >>");

    }

    public boolean onOptionItemSelect(MenuItem item)
    {
        Log.e(TAG, "onItemSelect()");

        switch(item.getItemId())
        {
            case R.id.action_search:
                return true;
            case R.id.action_navigate_Store:
                goToStore();
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_myprofile:
                goToProfile();
                return true;
            case R.id.action_logout:
                onClickLogOut();
                return true;
            case R.id.action_mycart:
                goToCart();
            default:
                return false;
        }

    }

    private void onClickLogOut()
    {
        Log.e(TAG, "onClickLogOut() >>");
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent_Back = new Intent(m_Activity.getApplicationContext(), MainActivity.class);
        m_Activity.startActivity(intent_Back);
        m_Activity.finish();
        Log.e(TAG, "onClickLogOut() <<");
    }

    private void refresh()
    {
        Log.e(TAG, "refresh() >>");
        m_Activity.finish();
        m_Activity.startActivity(m_Activity.getIntent());
        Log.e(TAG, "refresh() <<");

    }

    public void setOnClickSearch()
    {
        Log.e(TAG, "setOnClickSearch() >>");
        m_SearchView.setQueryHint("Audio Book name");
        EditText searchEditText = (EditText) m_SearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // Do something
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(m_Activity.getApplicationContext(), "Search triggered", Toast.LENGTH_LONG).show();
                Intent i = new Intent(m_Activity.getApplicationContext(), AllProductsActivity.class);
                i.putExtra("search", m_SearchView.getQuery().toString());
                m_Activity.startActivity(i);
                return true;
            }
        };

        m_SearchView.setOnQueryTextListener(queryTextListener);
        Log.e(TAG, "setOnClickSearch() <<");

    }


    private void goToStore()
    {
        Intent i = new Intent(m_Activity.getApplicationContext(), AllProductsActivity.class);
        m_Activity.startActivity(i);
        m_Activity.finish();
    }

    private void goToProfile()
    {
        Intent i = new Intent(m_Activity.getApplicationContext(), UserActivity.class);
        m_Activity.startActivity(i);
        m_Activity.finish();


    }

    private void goToCart()
    {
        Intent i = new Intent(m_Activity.getApplicationContext(), AllUserPurchase.class);
        m_Activity.startActivity(i);
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = m_Activity.getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        m_Menu = menu;
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        m_SearchView = searchView;

        return true;
    }
}

