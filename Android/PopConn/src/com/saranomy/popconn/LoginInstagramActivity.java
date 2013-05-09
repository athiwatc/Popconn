package com.saranomy.popconn;

import android.app.Activity;
import android.os.Bundle;

import com.saranomy.popconn.core.InstagramCore;

public class LoginInstagramActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InstagramCore.getInstance().generateAccessToken(this);
	}
}