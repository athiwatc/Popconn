package com.saranomy.popconn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.saranomy.popconn.imagecache.ImageLoader;
import com.saranomy.popconn.start.LoginInstagramActivity;

public class WelcomeActivity extends Activity {
	private Button button_instagram_login;
	private Button button_feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new ImageLoader(getApplicationContext()).clearCache();
		setContentView(R.layout.activity_welcome);
		syncViewById();
	}

	private void syncViewById() {
		button_instagram_login = (Button) findViewById(R.id.activity_welcome_instagram_login);
		button_instagram_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// open instagram loggin in
				Intent intent = new Intent(getApplicationContext(), LoginInstagramActivity.class);
				startActivity(intent);
			}
		});
		button_feed = (Button) findViewById(R.id.activity_welcome_feed);
		button_feed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}

}
