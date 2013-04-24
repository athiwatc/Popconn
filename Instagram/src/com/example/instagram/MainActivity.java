package com.example.instagram;

import org.jinstagram.auth.model.Token;

import tools.instagram.InstagramController;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private Token EMPTY_TOKEN = null;
	private Token accessToken = null;
	public final InstagramController igc = InstagramController.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		
		//load autehn url
		webview.loadUrl(igc.getAuthorizationUrl());
		
		setContentView(webview);

		webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(igc.getRedirectUrl())){
                	String token_code = igc.generateToken(url);
                	new getTokenAsyncTask().execute(token_code);
                }
                else
                   view.loadUrl(url);        
                
                return true;
            }
        });
		
		
	}
	
	public class getTokenAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... token_code) {
			igc.setAccessToken(token_code[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(getApplicationContext(),AfterAuthActivity.class);
            startActivity(intent);
			super.onPostExecute(result);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
