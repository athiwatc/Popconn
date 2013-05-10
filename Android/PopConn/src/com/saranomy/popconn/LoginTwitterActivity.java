package com.saranomy.popconn;

import android.app.Activity;
import android.os.Bundle;

import com.saranomy.popconn.core.TwitterCore;

public class LoginTwitterActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterCore.getInstance().generateAccessToken(this);
	}
}
