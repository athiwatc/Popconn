package com.saranomy.popconn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.Paging;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
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
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

	int socialNetworkFinishedLoading;
	int totalSocialNetwork;
	Handler counter;

	private void init() {
		items = new ArrayList<Item>();
		Log.i("Core", "Facebook Core Loaded");
		facebookCore = FacebookCore.getInstance();
		Log.i("Core", "Twitter Core Loaded");
		twitterCore = TwitterCore.getInstance();
		Log.i("Core", "Instagram Core Loaded");
		instagramCore = InstagramCore.getInstance();

		socialNetworkFinishedLoading = 0;
		totalSocialNetwork = 0;
		// A counter for the social network loader to call after they have
		// loaded.
		counter = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					// For every social network finished loading it increase the
					// counter by one.
					socialNetworkFinishedLoading++;
					// When the number of social network loaded is equal to the
					// number of total social network active it will display to
					// result.
					if (socialNetworkFinishedLoading == totalSocialNetwork) {
						displayView();
					}
					break;

				default:
					break;
				}
			}
		};

		if (facebookCore.active) {
			new LoadFacebookItem().execute();
			totalSocialNetwork++;
		}
		if (twitterCore.active) {
			new LoadTwitterItem().execute();
			totalSocialNetwork++;
		}
		if (instagramCore.active) {
			new LoadInstagramItem().execute();
			totalSocialNetwork++;
		}
	}

	public class LoadFacebookItem extends AsyncTask<Void, Void, Void> {

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... arg0) {
			Request req = new Request(facebookCore.getSession(), "/me/home");
			Response res = Request.executeAndWait(req);
			try {
				JSONArray jsonData = res.getGraphObject().getInnerJSONObject().getJSONArray("data");
				Log.e("data", jsonData.toString());
				for (int i = 0; i < jsonData.length(); i++) {
					JSONObject jsonItem = jsonData.getJSONObject(i);
					// depends on if it is null, CONTENT, IMAGE_URL,
					Item item = new Item();
					item.socialId = 0;
					item.name = jsonItem.getJSONObject("from").getString("name");
					item.date = isoToSecond(jsonItem.getString("created_time"));
					item.time = countDown(item.date);
					item.action = "";
					String userId = jsonItem.getJSONObject("from").getString("id");
					item.thumbnail_url = "https://graph.facebook.com/" + userId + "/picture";
					try {
						item.action = "via " + jsonItem.getJSONObject("application").getString("name");
					} catch (Exception e) {

					}
					try {
						item.content = jsonItem.getString("message");
					} catch (Exception e) {
						try {
							item.content = jsonItem.getString("description");
						} catch (Exception e1) {
							try {
								item.content = jsonItem.getString("link");
							} catch (Exception e2) {

							}
						}
					}
					try {
						item.image_url = jsonItem.getString("picture");
					} catch (Exception e) {

					}
					try {
						item.feature = jsonItem.getJSONObject("likes").getString("count");
					} catch (Exception e) {

					}

					try {
						DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
						Date convertedDate = parser.parse(jsonItem.getString("created_time"));

						convertedDate.setHours(convertedDate.getHours() + 7);
						item.date = convertedDate.getTime() / 1000;
						item.time = countDown(item.date);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					items.add(item);
				}
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			counter.sendEmptyMessage(0);
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
			List<twitter4j.Status> statuses = twitterCore.twitter.getHomeTimeline(paging);
			for (twitter4j.Status status : statuses) {
				Item item = new Item();
				item.socialId = 1;
				item.name = status.getUser().getName();
				item.action = "@" + status.getUser().getScreenName();
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
			counter.sendEmptyMessage(0);
			super.onPostExecute(result);
		}
	}

	public class LoadInstagramItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			if (instagramCore.active) {
				try {
					MediaFeed mediaFeed = instagramCore.instagram.getUserFeeds();
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
						item.thumbnail_url = media.getUser().getProfilePictureUrl();
						item.date = Long.parseLong(media.getCreatedTime());
						item.time = countDown(item.date);
						item.image_url = media.getImages().getStandardResolution().getImageUrl();

						item.feature = media.getLikes().getCount() + " likes\n";

						item.comment = "";
						List<CommentData> comments = media.getComments().getComments();
						if (comments.size() > 0) {
							StringBuffer sb = new StringBuffer();
							for (CommentData comment : comments) {
								sb.append(comment.getCommentFrom().getUsername()).append(": ").append(comment.getText()).append("\n");
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
			counter.sendEmptyMessage(0);
			super.onPostExecute(result);
		}
	}

	public void displayView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ItemAdapter adapter = new ItemAdapter(inflater, items);
		Collections.sort(items, Item.comparator);
		activity_feed_listview.setAdapter(adapter);

		activity_feed_refresh.setVisibility(View.GONE);
		activity_feed_listview.setVisibility(View.VISIBLE);
		activity_feed_listview.finishRefreshing();
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

	@SuppressWarnings("deprecation")
	public static long isoToSecond(String dateString) {
		try {
			DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
			Date convertedDate = parser.parse(dateString);
			convertedDate.setHours(convertedDate.getHours() + 7);
			// TODO: auto time zone detect
			return convertedDate.getTime() / 1000L;
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
