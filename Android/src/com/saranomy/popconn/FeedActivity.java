package com.saranomy.popconn;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.saranomy.popconn.socialnetwork.InstagramCore;

public class FeedActivity extends Activity {
	private static int maxItems = 5;
	private TextView textview_name;
	private Instagram instagram;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		syncViewById();
		testLoadSocialNetwork();
	}
	
	private void syncViewById() {
		textview_name = (TextView) findViewById(R.id.activity_feed_name);
	}
	
	private void testLoadSocialNetwork() {
		instagram = InstagramCore.getInstance().getInstagram();
		new TestGetInstagramUsernameAsyncTask().execute(instagram);
	}
	
	public class TestGetInstagramUsernameAsyncTask extends AsyncTask<Instagram, Void, String> {

		@Override
		protected String doInBackground(Instagram... instagram) {
			try {
				// print username
				UserInfo userInfo = instagram[0].getCurrentUserInfo();
				//textview_name.setText(userInfo.getData().getUsername());
				return userInfo.getData().getUsername();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			textview_name.setText(result);
			super.onPostExecute(result);
		}
	}
}
