package com.saranomy.popconn.core;

import android.app.Activity;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class FacebookCore {
	private static FacebookCore facebookCore = null;
	private GraphUser graphUser;

	private Activity currentActivity;
	public boolean active = false;

	public static FacebookCore getInstance() {
		if (facebookCore == null)
			facebookCore = new FacebookCore();
		return facebookCore;
	}

	public void callLogin(final Activity activity) {

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
}