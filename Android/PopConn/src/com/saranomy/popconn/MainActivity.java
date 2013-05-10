package com.saranomy.popconn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button activity_main_facebook;
	private Button activity_main_twitter;
	private Button activity_main_instagram;
	private Button activity_main_start;
	
	MainFragment mainFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		syncViewById();
		init();
	}

	private void syncViewById() {
		activity_main_facebook = (Button) findViewById(R.id.activity_main_facebook);
		activity_main_twitter = (Button) findViewById(R.id.activity_main_twitter);
		activity_main_instagram = (Button) findViewById(R.id.activity_main_instagram);
		activity_main_start = (Button) findViewById(R.id.activity_main_start);
	}

	private void init() {
		activity_main_facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),
						LoginFacebookActivity.class));
			}
		});
		activity_main_twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),
						LoginTwitterActivity.class));
			}
		});
		activity_main_instagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),
						LoginInstagramActivity.class));
			}
		});
		activity_main_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),
						FeedActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


}
