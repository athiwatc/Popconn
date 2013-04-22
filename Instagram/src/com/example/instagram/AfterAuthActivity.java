package com.example.instagram;

import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Caption;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.common.ImageData;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.Likes;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import com.example.instagram.MainActivity.getTokenAsyncTask;

import tools.instagram.InstagramController;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AfterAuthActivity extends Activity {
	private TextView name;
	private InstagramController igc;
	private Instagram ig;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		igc = InstagramController.getInstance();
		ig = igc.getInstagram();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_authen);

		//Bundle bundle = getIntent().getExtras();
		//String accessToken = bundle.getString("accessToken");
		
		
		syncViewById();
		content();
	}

	private void syncViewById() {
		name = (TextView) findViewById(R.id.name);
	}

	private void content() {
		new getUserInfoAsyncTask().execute();
	}
	
	public class getUserInfoAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... token_code) {
			UserInfo userInfo;
			try {
				userInfo = ig.getCurrentUserInfo();
				Log.e("Username",""+userInfo.getData().getUsername());
				printFeeds();
			} catch (InstagramException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		private void printFeeds() throws InstagramException{
			MediaFeed mediaFeed = ig.getUserFeeds();
			
			//retrieve FEED
			List<MediaFeedData> mediaFeeds = mediaFeed.getData();
			
			//First feed only (IF need all feeds, just loop)
			MediaFeedData mediaData = mediaFeeds.get(0);
			//-----
			
			Log.e("Feed count: ",""+mediaFeeds.size());
			Log.e("id : ", mediaData.getId());
			Log.e("created time : " , mediaData.getCreatedTime());
			Log.e("link : " , mediaData.getLink());
			Log.e("tags : " , mediaData.getTags().toString());
			Log.e("filter : " , mediaData.getImageFilter());
			Log.e("type : " , mediaData.getType());
			
			Log.e("Post by ",mediaData.getUser().getUserName());
			//retrieve comment
			Comments comments = mediaData.getComments();
			List<CommentData> c_data = comments.getComments();
			if(c_data.size() > 0)
				Log.e("Comment","C: "+c_data.get(0).getText());
			else
				Log.e("Comment","NULL");
			
			Likes likes = mediaData.getLikes();
			Log.e("Likes", likes.getCount() +"");
			
			//image with specific resolution
			Images images = mediaData.getImages();
			ImageData lowResolutionImg = images.getLowResolution();
			ImageData standardResolutionImg = images.getStandardResolution();
			ImageData thumbnailImg = images.getThumbnail();
			
			Location location = mediaData.getLocation();
			if(location != null)
				Log.e("Location",location.getName());
			else
				Log.e("Location","NULL");
		}
		

		
	}
}
