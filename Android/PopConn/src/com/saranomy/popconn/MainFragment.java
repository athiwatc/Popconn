package com.saranomy.popconn;

import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
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

	class FacebookGetRequest extends AsyncTask<Session, Void, Void> {

		@Override
		protected Void doInBackground(Session... params) {

			Request r = new Request(params[0], "me/home");

			Response res = Request.executeAndWait(r);

			GraphObject go = res.getGraphObject();

			// Log.i("result", go.toString()); //HERE
			return null;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.activity_main_facebook);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("read_stream"));

		return view;
	}

}
