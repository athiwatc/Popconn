package com.saranomy.popconn;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.saranomy.popconn.core.InstagramCore;
import com.saranomy.popconn.core.TwitterCore;
import com.saranomy.popconn.debug.KeyHash;

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

	@SuppressWarnings("deprecation")
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
		case (R.id.menu_about):
			AlertDialog di = new AlertDialog.Builder(MainActivity.this).create();
			di.setIcon(R.drawable.ic_launcher);
			di.setCancelable(false);
			di.setTitle("About");
			di.setMessage("made by Saranomy.");
			di.setButton3("More", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q=pub:Saranomy"));
						startActivity(intent);
					} catch (ActivityNotFoundException anfe) {
						Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://play.google.com/store/apps/developer?id=Saranomy"));
						startActivity(intent);
					}
				}
			});
			di.setButton2("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			di.show();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
