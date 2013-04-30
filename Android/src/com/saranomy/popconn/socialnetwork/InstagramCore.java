package com.saranomy.popconn.socialnetwork;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;

import android.util.Log;

public class InstagramCore {
	private static InstagramCore instagramCore = null;
	private static Instagram instagram = null;
	private static String apiKey = "f80b9378f0834d1e84b6efd613bdec5d";
	private static String apiSecret = "f9ddb8fa40c6446089bccdc439b9f75e";
	private static String redirectedUrl = "http://google.com";

	private InstagramService service = null;
	private String authUrl = null;
	private Token token = null;

	public static InstagramCore getInstance() {
		if (instagramCore == null)
			instagramCore = new InstagramCore();
		return instagramCore;
	}

	private InstagramCore() {
		initInstagramAuth();
	}

	private void initInstagramAuth() {
		service = new InstagramAuthService().apiKey(apiKey).apiSecret(apiSecret).callback(redirectedUrl).build();
		authUrl = service.getAuthorizationUrl(token);
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public String getRedirectedUrl() {
		return redirectedUrl;
	}

	public void setAccessToken(String tokenString) {
		Log.e("InstagramCore set token", tokenString);
		token = service.getAccessToken(token, new Verifier(tokenString));
	}

	public Token getAccessToken() {
		return token;
	}

	public Instagram getInstagram() {
		if (token == null)
			return null;
		if (instagram == null)
			instagram = new Instagram(token);
		return instagram;
	}

	public static String convertSecondToInstagramTime(long time) {
		if (time < 60)
			return time + " minutes ago";
		else if (time < 1440)
			return Math.round(time / 60) + " hours ago";
		else
			return Math.round(time / 1440) + " days ago";
	}
}
