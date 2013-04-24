package com.saranomy.popconn.start;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.saranomy.popconn.socialnetwork.InstagramCore;

@SuppressLint("SetJavaScriptEnabled")
public class LoginInstagramActivity extends Activity {
	private InstagramCore instagramCore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create webview to let use login through instagram interface
		instagramCore = InstagramCore.getInstance();
		final WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(instagramCore.getAuthUrl());
		webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	// cut the given token from webview's address
                if(url.startsWith(instagramCore.getRedirectedUrl())){
                	new VerifyTokenAsyncTask().execute(url);
                }
                else
                   view.loadUrl(url);
                
                return true;
            }
        });
		setContentView(webview);
	}
	
	private String cutTokenFromUrl(String url){
		return url.substring(url.indexOf("code=") + 5, url.length());
	}
	
	public class VerifyTokenAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... url) {
			instagramCore.setAccessToken(cutTokenFromUrl(url[0]));
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(getApplicationContext(), "PopConn now linked with your Instagram.", Toast.LENGTH_SHORT).show();
			Log.e("Access Instagram with token",instagramCore.getAccessToken().getToken());
			// back to welcome when finish login
			finish();
			super.onPostExecute(result);
		}
	}
}
