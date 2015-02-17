package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;
import android.util.Log;

/**
 * Created by daisych on 2/14/15.
 */
public class TweetsListFragment extends Fragment {

    private TwitterClient client = TwitterApplication.getRestClient();
    private TweetsArrayAdapter adapter;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    static int page = 1;

    // Inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet_list, parent, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(adapter);
        setupListViewListenser();

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                customLoadMoreDataFromApi(TweetsListFragment.page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(getActivity(), tweets);

    }

    public void addAll(ArrayList<Tweet> tweets) {
        adapter.addAll(tweets);
    }

    public void insert (Tweet tweet, int index) {
        adapter.insert(tweet, index);
    }

    public void notifyChange() {
        adapter.notifyDataSetChanged();
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int page) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        this.page++;
        int offset = page * 25;
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                adapter.addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        }, offset);
    }

    private void setupListViewListenser() {
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adaptor, View item, int pos, long id) {
                                                Intent i = new Intent(getActivity(), ProfileActivity.class);
                                                String screen_name = tweets.get(pos).getUser().getScreenName();
                                                i.putExtra("screen_name", screen_name);
                                                i.putExtra("current", false);
                                                startActivity(i);
                                            }
                                        }
        );
    }


}
