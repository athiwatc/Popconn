package com.saranomy.popconn.core;

import android.app.Activity;
import android.util.Log;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class FacebookCore {
	private static String appId = "422619067834909";
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
	}
	
	public Session getSession() {
		return session;
	}

	public void callLogin(final Activity activity) {
		
	}
}