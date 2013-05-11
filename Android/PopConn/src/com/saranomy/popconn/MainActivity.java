package com.saranomy.popconn;

import com.saranomy.popconn.core.FacebookCore;
import com.saranomy.popconn.core.InstagramCore;
import com.saranomy.popconn.core.TwitterCore;
import com.saranomy.popconn.debug.KeyHash;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	MainFragment mainFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}
	}

	@Override
	protected void onResume() {
		mainFragment.updateButton();
		super.onResume();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case (R.id.menu_clear):
			KeyHash.clearApplicationData(getApplicationContext());
			TwitterCore.getInstance().active = false;
			InstagramCore.getInstance().active = false;
			TwitterCore.getInstance().saveDatabase(getApplicationContext());
			InstagramCore.getInstance().saveDatabase(getApplicationContext());
			mainFragment.updateButton();
			Toast.makeText(getApplicationContext(), "Data cleared.", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
