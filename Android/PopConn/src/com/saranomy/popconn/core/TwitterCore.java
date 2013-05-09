package com.saranomy.popconn.core;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterCore {
	static String consumer_key = "7URvxUcNnhNHdrOdfVs6g";
	static String consumer_secret = "kDOaOwX0OmmF2Exb1N7iPA0U5NWEXkGogjvKAHInluI";
	static final String callback_url = "http://stub.com";

	private static TwitterCore twitterCore;
	public Twitter twitter;
	private AccessToken accessToken;
	private static RequestToken requestToken;

	private String accessTokenString;
	private String accessTokenSecretString;
	private SharedPreferences sharedPrefenences;

	private Activity currentActivity;
	private WebView webview;
	public boolean active = false;

	private TwitterCore() {
	}

	public static TwitterCore getInstance() {
		if (twitterCore == null)
			twitterCore = new TwitterCore();
		return twitterCore;
	}

	private void createTwitterInstance() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumer_key);
        builder.setOAuthConsumerSecret(consumer_secret);
        builder.setOAuthAccessToken(accessTokenString);
        builder.setOAuthAccessTokenSecret(accessTokenSecretString);
        Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		if (accessTokenString != null) {
			active = true;
			currentActivity.finish();
			Toast.makeText(currentActivity, "Twitter is connected.", Toast.LENGTH_SHORT).show();
		} else {
			Log.e("is it null?", "yes");
			new getRequestTokenTask().execute(callback_url);
		}

	}

	public void generateAccessToken(Activity activity) {
		// load token in private storage
		sharedPrefenences = activity.getSharedPreferences("com.example.twitter", Context.MODE_PRIVATE);
		accessTokenString = sharedPrefenences.getString("twitter_access_token", null);
		accessTokenSecretString = sharedPrefenences.getString("twitter_access_token_secret", null);
		
		currentActivity = activity;
		webview = new WebView(activity);

		createTwitterInstance();

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith(callback_url)) {
					String oauth_verifier = url.substring(url.indexOf("oauth_verifier") + "oauth_verifier".length() + 1, url.length());
					System.out.println("OAuth_verifier = " + oauth_verifier);
					new getAccessTokenTask().execute(oauth_verifier);
				} else
					view.loadUrl(url);

				return true;
			}
		});
	}

	private class getRequestTokenTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... callback) {
			try {
				requestToken = twitter.getOAuthRequestToken(callback[0]);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				webview.loadUrl(requestToken.getAuthorizationURL());
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentActivity.setContentView(webview);
		}
	}

	private class getAccessTokenTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);

				// update token in private storage
				SharedPreferences.Editor editor = sharedPrefenences.edit();
				editor.putString("twitter_access_token", accessToken.getToken());
				editor.putString("twitter_access_token_secret", accessToken.getTokenSecret());
				editor.commit();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			active = true;
			currentActivity.finish();
			Toast.makeText(currentActivity, "Twitter is connected.", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

	}
}
