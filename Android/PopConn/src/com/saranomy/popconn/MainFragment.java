package com.saranomy.popconn;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class MainFragment extends Fragment {

	private UiLifecycleHelper uiHelper;

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private static final String TAG = "MainFragment";

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		Log.e("TST", "TEST");
		
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");

			Request.executeGraphPathRequestAsync(session, "me/home",
					new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							Log.i("asd", response.toString());

						}
					});

			// (new FacebookGetRequest()).execute(session);

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("read_stream"));
		
		syncViewById(view);
		init();

		return view;
	}
	
	private Button activity_main_facebook;
	private Button activity_main_twitter;
	private Button activity_main_instagram;
	private Button activity_main_start;
	
	private void syncViewById(View view) {
		Log.e("HAHAHA", "FESFWEFWEfwefwef");
		activity_main_twitter = (Button) view.findViewById(R.id.activity_main_twitter);
		activity_main_instagram = (Button) view.findViewById(R.id.activity_main_instagram);
		activity_main_start = (Button) view.findViewById(R.id.activity_main_start);
	}

	private void init() {
		Log.e("TESTAAA", "ASDASDS");
		activity_main_facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity().getApplicationContext(),
						LoginFacebookActivity.class));
			}
		});
		activity_main_twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity().getApplicationContext(),
						LoginTwitterActivity.class));
			}
		});
		activity_main_instagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity().getApplicationContext(),
						LoginInstagramActivity.class));
			}
		});
		activity_main_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity().getApplicationContext(),
						FeedActivity.class));
			}
		});
	}

}
