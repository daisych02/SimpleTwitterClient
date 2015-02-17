package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private TweetsListFragment fragmentTweetsList;
    static int page = 1;
    final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the ViewPager
        ViewPager vpViewpager = (ViewPager) findViewById(R.id.viewpager);
        // set viewpager adapter for viewpager
        vpViewpager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach viewpager to tab strip
        tabStrip.setViewPager(vpViewpager);


//        if(savedInstanceState == null) {
//            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//        }

//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to your AdapterView
//
//                customLoadMoreDataFromApi(TimelineActivity.page);
//                // or customLoadMoreDataFromApi(totalItemsCount);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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

        if(id == R.id.miCompose) {
            Intent i = new Intent(this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_CODE);
        }

        if(id == R.id.miProfile) {
            onProfileView();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onProfileView() {
        // launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("current", true);

        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String status = data.getStringExtra("status");
            User user = (User) data.getSerializableExtra("user");
            Tweet tweet = new Tweet();
            tweet.setUser(user);
            tweet.setBody(status);
            tweet.setCreatedAt("just now");
            fragmentTweetsList.insert(tweet, 0);
            fragmentTweetsList.notifyChange();
        }
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = {"Home","@Mentions"};

        // Adapter gets manager
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // order and creation of fragments
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            } else if(position ==1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
