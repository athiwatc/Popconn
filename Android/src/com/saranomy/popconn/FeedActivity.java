package com.saranomy.popconn;

import java.util.ArrayList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.saranomy.popconn.imagecache.ImageLoader;
import com.saranomy.popconn.socialnetwork.InstagramCore;
import com.saranomy.popconn.socialnetwork.InstagramItemAdapter;

public class FeedActivity extends Activity {
	private static int maxItems = 5;
	private ImageView activity_feed_thumbnail;
	private TextView textview_name;
	private TextView textview_picture;
	private ListView activity_feed_list;
	private Instagram instagram;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		
		// debug, clear image cache
		ImageLoader imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.clearCache();
		
		syncViewById();
		testLoadSocialNetwork();
	}

	private void syncViewById() {
		activity_feed_thumbnail = (ImageView) findViewById(R.id.activity_feed_thumbnail);
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
			
			ImageLoader imageLoader = new ImageLoader(getApplicationContext());
			imageLoader.DisplayImage(temp, activity_feed_thumbnail);
		}
	}

	public class GetInstagramPostAsyncTask extends
			AsyncTask<Instagram, Void, List<MediaFeedData>> {

		@Override
		protected List<MediaFeedData> doInBackground(Instagram... instagram) {
			try {
				MediaFeed mediaFeed = instagram[0].getUserFeeds();
				List<MediaFeedData> mediaFeeds = mediaFeed.getData();

				List<MediaFeedData> mediaDataList = new ArrayList<MediaFeedData>();
				for (int i = 0; i < 10; i++) {
					MediaFeedData mediaData = mediaFeeds.get(i);
					mediaDataList.add(mediaData);
				}
				return mediaDataList;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<MediaFeedData> mediaDataList) {
			// TODO Auto-generated method stub
			LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			InstagramItemAdapter adapter = new InstagramItemAdapter(mInflater, mediaDataList.size()	, mediaDataList);
			activity_feed_list.setAdapter(adapter);
			activity_feed_list.setItemsCanFocus(true);
			
			
			super.onPostExecute(mediaDataList);
		}
	}
}
