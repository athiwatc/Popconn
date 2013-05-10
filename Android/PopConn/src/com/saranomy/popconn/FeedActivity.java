package com.saranomy.popconn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Paging;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Response;
import com.github.jeremiemartinez.refreshlistview.RefreshListView;
import com.github.jeremiemartinez.refreshlistview.RefreshListView.OnRefreshListener;
import com.saranomy.popconn.core.FacebookCore;
import com.saranomy.popconn.core.InstagramCore;
import com.saranomy.popconn.core.TwitterCore;
import com.saranomy.popconn.item.Item;
import com.saranomy.popconn.item.ItemAdapter;

public class FeedActivity extends Activity {
	private RefreshListView activity_feed_listview;
	private ProgressBar activity_feed_refresh;
	private FacebookCore facebookCore;
	private TwitterCore twitterCore;
	private InstagramCore instagramCore;

	private List<Item> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		syncViewById();
		init();
	}

	private void syncViewById() {
		activity_feed_listview = (RefreshListView) findViewById(R.id.activity_feed_listview);
		activity_feed_listview.setEnabledDate(true, new Date());
		activity_feed_listview.setRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshListView listView) {
				init();
			}
		});
		// activity_feed_listview.setOnRefreshListener(new
		// OnRefreshListener<ListView>() {
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// // Do work to refresh the list here.
		// init();
		// }
		// });
		activity_feed_refresh = (ProgressBar) findViewById(R.id.activity_feed_refresh);
	}

	private void init() {
		items = new ArrayList<Item>();
		facebookCore = FacebookCore.getInstance();
		twitterCore = TwitterCore.getInstance();
		instagramCore = InstagramCore.getInstance();

		new LoadFacebookItem().execute();
//		if (twitterCore.active) {
//			new LoadTwitterItem().execute();
//		} else {
//			new LoadInstagramItem().execute();
//		}
	}
	
	public class LoadFacebookItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			Request req = new Request(facebookCore.getSession(), "/me/home");
			Response res = Request.executeAndWait(req);
			try {
				JSONArray data = res.getGraphObject().getInnerJSONObject().getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject thisObject = data.getJSONObject(i);
					Item item = new Item();
					item.socialId = 0;
					item.name = thisObject.getJSONObject("from").getString("name");
					item.content = thisObject.getString("message");
					//Date facebookDate = 100L; 
					item.time = "12";
					item.date = 100L;
					Log.i("aaa", item.content = thisObject.getString("message"));
					items.add(item);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			new LoadTwitterItem().execute();
			super.onPostExecute(result);
		}
	}

	public class LoadTwitterItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				getHomeTimeline(new Paging(1));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		private void getHomeTimeline(Paging paging) throws TwitterException {
			List<twitter4j.Status> statuses = twitterCore.twitter
					.getHomeTimeline(paging);
			for (twitter4j.Status status : statuses) {
				Item item = new Item();
				item.socialId = 1;
				item.name = status.getUser().getScreenName();
				item.action = "@" + status.getUser().getName();
				item.thumbnail_url = status.getUser().getProfileImageURL();
				item.date = status.getCreatedAt().getTime() / 1000L;
				item.time = countDown(item.date);
				item.content = status.getText();

				String feature = "";
				if (status.isRetweet()) {
					feature = status.getRetweetCount() + " Retweets";
				}
				item.feature = feature;
				items.add(item);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			new LoadInstagramItem().execute();
			super.onPostExecute(result);
		}
	}

	public class LoadInstagramItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			if (instagramCore.active) {
				try {
					MediaFeed mediaFeed = instagramCore.instagram
							.getUserFeeds();
					List<MediaFeedData> mediaFeedData = mediaFeed.getData();
					int minSize = Math.min(mediaFeedData.size(), 20);
					for (int i = 0; i < minSize; i++) {
						MediaFeedData media = mediaFeedData.get(i);
						Item item = new Item();
						item.socialId = 2;
						item.name = media.getUser().getUserName();
						item.action = "";
						if (media.getLocation() != null)
							item.action = media.getLocation().getName();
						item.thumbnail_url = media.getUser()
								.getProfilePictureUrl();
						item.date = Long.parseLong(media.getCreatedTime());
						item.time = countDown(item.date);
						item.image_url = media.getImages()
								.getStandardResolution().getImageUrl();

						item.feature = media.getLikes().getCount() + " likes\n";

						item.comment = "";
						List<CommentData> comments = media.getComments()
								.getComments();
						if (comments.size() > 0) {
							StringBuffer sb = new StringBuffer();
							for (CommentData comment : comments) {
								sb.append(
										comment.getCommentFrom().getUsername())
										.append(": ").append(comment.getText())
										.append("\n");
							}
							item.comment = sb.toString();
						}
						items.add(item);
					}
				} catch (InstagramException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ItemAdapter adapter = new ItemAdapter(inflater, items);
			Collections.sort(items, Item.comparator);
			activity_feed_listview.setAdapter(adapter);

			activity_feed_refresh.setVisibility(View.GONE);
			activity_feed_listview.setVisibility(View.VISIBLE);
			activity_feed_listview.finishRefreshing();
			super.onPostExecute(result);
		}
	}

	public static String countDown(long time) {
		long delta = (System.currentTimeMillis() / 60000) - (time / 60);
		if (delta < 60)
			return delta + "m";
		else if (delta < 1440)
			return Math.round(delta / 60) + "h";
		else
			return Math.round(delta / 1440) + "d";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_feed, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}
}
