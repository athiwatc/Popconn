package com.saranomy.popconn;

import com.saranomy.popconn.core.TwitterCore;

import android.app.Activity;
import android.os.Bundle;

public class LoginTwitterActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterCore.getInstance().generateAccessToken(this);
	}
}
