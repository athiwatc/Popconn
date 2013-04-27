package com.example.twitter;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import twitter4j.Status;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TimelineActivity extends Activity{

	// Constants
    static String TWITTER_CONSUMER_KEY = "8z1hdeObpSJ3LYSQoqShTg";
    static String TWITTER_CONSUMER_SECRET = "IaMMHm9FryGrusjBkDK7yJblukCTdY1PKRkmAO4tG7g";
 
    static final String TWITTER_CALLBACK_URL = "http://www.google.com";
	
	// Twitter
    private static Twitter twitter;
    
    // Update status button
    Button btnTweet;
    // Logout button
    Button btnLogout;
    // EditText for tweet
    EditText txtTweet;
    
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		Bundle bundle = getIntent().getExtras();
		//System.out.println("AccessToken : " + bundle.get("AccessToken"));
		//System.out.println("AccessTokenSecret : " + bundle.get("TokenSecret"));
		createTwitterInstance(bundle.get("AccessToken").toString(),bundle.get("TokenSecret").toString());
		
		btnTweet = (Button) findViewById(R.id.btnTweet);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        txtTweet = (EditText) findViewById(R.id.txtTweet);
        
        System.out.println(btnTweet.getText());
        
        btnTweet.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View arg0) {
                new tweetTask().execute(txtTweet.getText().toString());
            }
        });
		
		content();

	}
    
    private void content(){
    	new getUserTimelineTask().execute();
    }
    
    private void createTwitterInstance(String token,String token_secret){
		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        builder.setOAuthAccessToken(token);
        builder.setOAuthAccessTokenSecret(token_secret);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        
	}
    
    private class getUserTimelineTask extends AsyncTask<String,Void,Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<twitter4j.Status> statuses;
				statuses = twitter.getHomeTimeline();
				System.out.println("Show Timeline");
				for(twitter4j.Status status : statuses){
					System.out.println(status.getText());
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
    }
    
    private class tweetTask extends AsyncTask<String,Void,Void>{

		@Override
		protected Void doInBackground(String... text) {
			// TODO Auto-generated method stub
			try {
				twitter.updateStatus(text[0]);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
    }
	
}
