package com.saranomy.popconn.core;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class FacebookCore {
	private static String appId = "105708102927645";
	private static FacebookCore facebookCore = null;
	private GraphUser graphUser;
	private Facebook facebook;

	private Activity currentActivity;
	public boolean active = false;

	public static FacebookCore getInstance() {
		if (facebookCore == null)
			facebookCore = new FacebookCore();
		return facebookCore;
	}

	public void callLogin(final Activity activity) {
		new CreateFacebookTask().execute();
		currentActivity = activity;
		Session.openActiveSession(activity, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {

								graphUser = user;
								active = true;
								activity.finish();
								Toast.makeText(currentActivity, "Facebook is connected.", Toast.LENGTH_SHORT).show();
								// TextView welcome = (TextView) findViewById(R.id.welcome);
								// welcome.setText("Hello " + user.getName() + "!");
							}
						}
					});

				}
			}
		});
	}

	public class CreateFacebookTask extends AsyncTask<Void, Void, Void> {

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... arg0) {
			Log.e("CreateFacebookTask", "begin");
			facebook = new Facebook(appId);
			String newsfeed;
			try {
				newsfeed = facebook.request("me/home");
				Log.e("CreateFacebookTask", "newsfeed: "+newsfeed);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}