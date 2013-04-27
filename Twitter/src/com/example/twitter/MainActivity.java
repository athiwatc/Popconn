package com.example.twitter;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	// Constants
    static String TWITTER_CONSUMER_KEY = "8z1hdeObpSJ3LYSQoqShTg";
    static String TWITTER_CONSUMER_SECRET = "IaMMHm9FryGrusjBkDK7yJblukCTdY1PKRkmAO4tG7g";
 
    static final String TWITTER_CALLBACK_URL = "http://www.google.com";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    
    // Twitter    
    private Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;
    
    private TwitterWebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webview = new TwitterWebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
	
		createTwitterInstance();
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(TWITTER_CALLBACK_URL)){
                	String oauth_verifier = url.substring(url.indexOf(URL_TWITTER_OAUTH_VERIFIER)+URL_TWITTER_OAUTH_VERIFIER.length()+1,url.length());
                	//System.out.println("OAuth_verifier = " + oauth_verifier);
                	//System.out.println("URL = " + url);
                	new getAccessTokenTask().execute(oauth_verifier);
                	//String oauth_verifier = twitterController.generateTokenVerifier(url);
                	new getAccessTokenTask().execute(oauth_verifier);
                }
                else
                   view.loadUrl(url);        
                
                return true;
            }
		});
		
	}


	private void createTwitterInstance(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        
        new getRequestTokenTask().execute(TWITTER_CALLBACK_URL);
        
	}
	
	private class getRequestTokenTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... callback) {
			// TODO Auto-generated method stub
			try {
                requestToken = twitter.getOAuthRequestToken(callback[0]);
                //System.out.println("AuthorizationURL = " + requestToken.getAuthorizationURL());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
			return null;
		}
		
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			webview.loadUrl(requestToken.getAuthorizationURL());
			setContentView(webview);
		}
		
	}
	
	private class getAccessTokenTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//twitter.setOAuthAccessToken(accessToken);
			//System.out.println(accessToken.toString());
			return null;
		}
		
		protected void onPostExecute(Void result){
			Intent intent = new Intent(getApplicationContext(),TimelineActivity.class);
            intent.putExtra("AccessToken", accessToken.getToken());
            intent.putExtra("TokenSecret", accessToken.getTokenSecret());
			startActivity(intent);
            super.onPostExecute(result);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
