package com.saranomy.popconn.core;

import android.app.Activity;

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
		
	}
}