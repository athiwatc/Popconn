package com.saranomy.popconn.core;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.saranomy.popconn.DatabaseHandler;
import com.saranomy.popconn.LoginInstagramActivity;

public class InstagramCore {
	private static String apiKey = "f80b9378f0834d1e84b6efd613bdec5d";
	private static String apiSecret = "f9ddb8fa40c6446089bccdc439b9f75e";
	private static String callback_url = "http://google.com";

	private static InstagramCore instagramCore;
	public Instagram instagram = null;

	private InstagramService service = null;
	private String authUrl = null;
	private Token token = null;

	private Activity currentActivity;
	private WebView webview;
	public boolean active = false;

	private InstagramCore() {
	}

	public static InstagramCore getInstance() {
		if (instagramCore == null)
			instagramCore = new InstagramCore();
		return instagramCore;
	}
	
	public void loadDatabase(Context context) {
		try {
			active = Boolean.parseBoolean(DatabaseHandler.loadFile(context, "isg"));
		} catch (Exception e) {
			// cannot find 'isg'
			try {
				DatabaseHandler.saveFile(context, "isg", "false");
			} catch (Exception e2) {

			}
		}
	}
	
	public void saveDatabase(Context context) {
		try {
			DatabaseHandler.saveFile(context, "isg", "" + active);
		} catch (Exception e) {
		}
	}

	public void createInstagramInstance() {
		service = new InstagramAuthService().apiKey(apiKey).apiSecret(apiSecret).callback(callback_url).build();
		authUrl = service.getAuthorizationUrl(token);
	}

	public void generateAccessToken(Activity activity) {
		currentActivity = activity;
		webview = new WebView(activity);

		createInstagramInstance();
		webview.loadUrl(authUrl);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith(callback_url)) {
					new VerifyTokenAsyncTask().execute(url);
				} else
					view.loadUrl(url);
				return true;
			}
		});
		currentActivity.setContentView(webview);
	}

	private String cutTokenFromUrl(String url) {
		String token = url.substring(url.indexOf("code=") + 5, url.length());
		return token;
	}

	public class VerifyTokenAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... url) {
			token = service.getAccessToken(token, new Verifier(cutTokenFromUrl(url[0])));
			instagram = new Instagram(token);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			active = true;
			saveDatabase(currentActivity);
			if (currentActivity instanceof LoginInstagramActivity)
				currentActivity.finish();
			Toast.makeText(currentActivity, "Instagram is connected.", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}
}
