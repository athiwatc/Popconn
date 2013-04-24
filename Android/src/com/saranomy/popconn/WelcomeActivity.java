package com.saranomy.popconn;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity {
	private Button button_instagram_login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		syncViewById();
	}
	
	private void syncViewById() {
		button_instagram_login = (Button) findViewById(R.id.activity_welcome_instagram_login_button);
		button_instagram_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// open instagram loggin in
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
