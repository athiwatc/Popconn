package com.saranomy.popconn.core;

import android.app.Activity;
import android.util.Log;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class FacebookCore {
	private static String appId = "105708102927645";
	private static FacebookCore facebookCore = null;
	private GraphUser graphUser;
	private Facebook facebook;

	private Activity currentActivity;
	public boolean active = false;
	private Session session = null;

	public static FacebookCore getInstance() {
		if (facebookCore == null)
			facebookCore = new FacebookCore();
		return facebookCore;
	}
	
	public void setSession(Session session) {
		this.session = session;
		Log.e("SESSIONSET", session.getAccessToken().toString());
	}
	
	public Session getSession() {
		return session;
	}

	public void callLogin(final Activity activity) {
		
	}
}