package com.saranomy.popconn;

import java.util.ArrayList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.saranomy.popconn.socialnetwork.InstagramCore;

public class FeedActivity extends Activity {
	private static int maxItems = 5;
	private TextView textview_name;
	private TextView textview_picture;
	private ListView activity_feed_list;
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
		activity_feed_list = (ListView) findViewById(R.id.activity_feed_list);
	}

	private void testLoadSocialNetwork() {
		instagram = InstagramCore.getInstance().getInstagram();
		new TestGetInstagramUserInfoDataAsyncTask().execute(instagram);
		new GetInstagramPostAsyncTask().execute(instagram);
	}

	public class TestGetInstagramUserInfoDataAsyncTask extends
			AsyncTask<Instagram, Void, UserInfoData> {

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
		}
	}

	public class GetInstagramPostAsyncTask extends
			AsyncTask<Instagram, Void, ArrayAdapter<String>> {

		@Override
		protected ArrayAdapter<String> doInBackground(Instagram... instagram) {
			try {
				MediaFeed mediaFeed = instagram[0].getUserFeeds();
				List<MediaFeedData> mediaFeeds = mediaFeed.getData();

				List<String> temp = new ArrayList<String>();
				for (int i = 0; i < 10; i++) {
					MediaFeedData mediaData = mediaFeeds.get(i);
					temp.add(":D " + mediaData.getLink());
					Log.e("fetch " + i, mediaData.getId());
				}
				String[] temp2 = new String[temp.size()];
				for (int i = 0; i < temp.size(); i++) {
					temp2[i] = temp.get(i);
					Log.e("link " + i, temp2[i]);
				}
				// fetch list view
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1, temp);
				return adapter;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(ArrayAdapter<String> result) {
			// TODO Auto-generated method stub
			activity_feed_list.setAdapter(result);
			super.onPostExecute(result);
		}
	}
}
