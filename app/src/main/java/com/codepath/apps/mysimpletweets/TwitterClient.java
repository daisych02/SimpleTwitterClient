package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */

//dev.twitter.com

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "r7BwYAgC9h6jmqXonmOVCZeRQ";       // Change this
	public static final String REST_CONSUMER_SECRET = "SJnHjKWz2OK6yNfuwkrB1FXATp3e6UMcLU13XhUwEceXjt0h8L"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

    // Each method is an api endpoint

    // Get Home timeline
    public void getHomeTimeline(AsyncHttpResponseHandler handler, int count) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");

        // params
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", 1);

        // execute request
        getClient().get(apiUrl, params, handler);
    }

    // Compose tweet
    public void composeTweet(AsyncHttpResponseHandler handler, String status) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", status);

        getClient().post(apiUrl, params, handler);
    }

    // Get current user info
    public void getUserInfo(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();

        getClient().get(apiUrl, params, handler);
    }

    // Get current user timeline
    public void getUserTimeline(AsyncHttpResponseHandler handler, String screen_name){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if(screen_name != "") {
            params.put("screen_name", screen_name);
        }

        getClient().get(apiUrl, params, handler);
    }


    // Get mentions
    public void getMentions(AsyncHttpResponseHandler handler, int count) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();

        getClient().get(apiUrl, params, handler);
    }

    // Get user profile for specific user
    public void getUserProfile(AsyncHttpResponseHandler handler, String screen_name) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screen_name);

        getClient().get(apiUrl, params, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}