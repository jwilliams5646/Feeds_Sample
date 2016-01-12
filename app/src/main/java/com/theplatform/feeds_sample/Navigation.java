package com.theplatform.feeds_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.theplatform.feeds_sample.FeedModels.Entry;
import com.theplatform.feeds_sample.FeedModels.Feed;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Callback<Feed> {

    GridView theGrid;
    LinearLayout inputLayout;
    EditText testInput;
    ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        theGrid = (GridView)findViewById(R.id.gridView);
        inputLayout = (LinearLayout)findViewById(R.id.input_layout);
        testInput = (EditText)findViewById(R.id.feed_input);
        searchButton = (ImageButton)findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedUrl = "";
                feedUrl = testInput.getText().toString();

                if(!feedUrl.equals("")){
                    if(feedUrl.contains("?")){
                        feedUrl = feedUrl.substring(0,feedUrl.indexOf('?'));
                    }
                    getFeed(feedUrl);
                }else{
                    Toast.makeText(getApplicationContext(),"No feed",Toast.LENGTH_LONG).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://feed.theplatform.com/f/5MILfC/f0yJi9gv9YPo")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            ThePlatformAPI ThePlatformAPI = retrofit.create(ThePlatformAPI.class);

            Call<Feed> call = ThePlatformAPI.getFeed();
            //asynchronous call
            call.enqueue(this);

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void getFeed(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ThePlatformAPI ThePlatformAPI = retrofit.create(ThePlatformAPI.class);

        Call<Feed> call = ThePlatformAPI.getFeed();
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //item.setChecked(true);
        int id = item.getItemId();
        Retrofit retrofit = null;

        if (id == R.id.nav_default) {
            inputLayout.setVisibility(View.GONE);
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://feed.theplatform.com/f/5MILfC/f0yJi9gv9YPo")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }  else if (id == R.id.nav_test_feed) {
            inputLayout.setVisibility(View.VISIBLE);

        } else if (id == R.id.adk_sample) {
            Intent intent = new Intent(this, PlayerActivity.class);
            startActivity(intent);
        }
/*        else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        // prepare call in Retrofit 2.0
        if(retrofit != null) {
            ThePlatformAPI ThePlatformAPI = retrofit.create(ThePlatformAPI.class);

            Call<Feed> call = ThePlatformAPI.getFeed();
            //asynchronous call
            call.enqueue(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResponse(Response<Feed> response, Retrofit retrofit) {
        ArrayList<Entry> responses = (ArrayList<Entry>) response.body().getEntries();
        FeedsAdapter adapter = new FeedsAdapter(responses,this);
        if (theGrid != null) {
            theGrid.setAdapter(adapter);
        }

    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        Toast.makeText(Navigation.this, "There was a problem with the request", Toast.LENGTH_SHORT).show();
    }
}
