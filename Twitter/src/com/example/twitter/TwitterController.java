package com.example.twitter;

import android.os.AsyncTask;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterController {
	
	// Constants
    static String TWITTER_CONSUMER_KEY = "8z1hdeObpSJ3LYSQoqShTg";
    static String TWITTER_CONSUMER_SECRET = "IaMMHm9FryGrusjBkDK7yJblukCTdY1PKRkmAO4tG7g";
    
    static final String TWITTER_CALLBACK_URL = "http://www.google.com";
    
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	private AccessToken accessToken = null;
	private final AccessToken EMPTY_TOKEN = null;
	private static RequestToken requestToken = null;
	private String authorizationUrl;
	private String redirectUrl;
	private static TwitterController INSTANCE;
	private Twitter twitter;
	
	private TwitterController(){
		this.redirectUrl = TWITTER_CALLBACK_URL;
		createTwitterInstance();
	}
	
	public static TwitterController getInstance(){
		if(INSTANCE == null)
			INSTANCE = new TwitterController();
		return INSTANCE;
	}
	
	public Twitter getTwitter(){
		return twitter;
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
	
	public String getAuthorizationUrl(){
		return authorizationUrl;
	}

	public String getRedirectUrl(){
		return redirectUrl;
	}
	
	/**
	 * Generate token code from instagram redirect URL
	 * @param code - redirect url with instagram's token 
	 */
	public String generateTokenVerifier(String code){
		int start = code.indexOf(URL_TWITTER_OAUTH_VERIFIER) + URL_TWITTER_OAUTH_VERIFIER.length() + 1;
		int end = code.length();
		return code.substring(start, end);
	}
	
	public void setAccessToken(String verifier){
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class getRequestTokenTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... callback) {
			// TODO Auto-generated method stub
			try {
                requestToken = twitter.getOAuthRequestToken(callback[0]);
                authorizationUrl = requestToken.getAuthorizationURL();
                //System.out.println("AuthorizationURL = " + requestToken.getAuthorizationURL());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
			return null;
		}
		
	}

}
