package com.saranomy.popconn;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.saranomy.popconn.socialnetwork.InstagramCore;

public class FeedActivity extends Activity {
	private static int maxItems = 5;
	private TextView textview_name;
	private TextView textview_picture;
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
		textview_picture = (TextView) findViewById(R.id.activity_feed_picture);
	}

	private void testLoadSocialNetwork() {
		instagram = InstagramCore.getInstance().getInstagram();
		new TestGetInstagramUserInfoDataAsyncTask().execute(instagram);
	}

	public class TestGetInstagramUserInfoDataAsyncTask extends AsyncTask<Instagram, Void, UserInfoData> {

		@Override
		protected UserInfoData doInBackground(Instagram... instagram) {
			try {
				// print username
				UserInfo userInfo = instagram[0].getCurrentUserInfo();
				return userInfo.getData();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserInfoData userInfoData) {
			String temp = userInfoData.getUsername();
			if (temp != null)
				textview_name.setText(temp);
			temp = userInfoData.getProfile_picture();
			textview_picture.setText("imageurl: " + temp);
			super.onPostExecute(userInfoData);
		}
	}
}
