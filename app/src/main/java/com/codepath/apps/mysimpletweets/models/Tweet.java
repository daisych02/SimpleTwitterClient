package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.text.format.DateUtils;
import android.util.Log;
import java.text.ParseException;

/**
 * Created by daisych on 2/6/15.
 */
public class Tweet {
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String body;
    private long uid;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
    private String createdAt;


    // Deserialze json and convert to Tweet object
    public static Tweet fromJSON (JSONObject json) {
        Tweet tweet = new Tweet();

//        Log.e("DEBUG", json.toString());
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = getRelativeTimeAgo(json.getString("created_at"));
            tweet.user = User.fromJson(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray (JSONArray array) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for(int i=0; i<array.length(); i++) {
            Tweet tweet;
            try {
                JSONObject json = array.getJSONObject(i);
                tweet = Tweet.fromJSON(json);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public String toString() {
        return this.uid + " " + this.createdAt + " ";
    }
}
