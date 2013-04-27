package com.example.twitter;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

public class TwitterWebView extends WebView{

	public TwitterWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

	    if (event.getAction() == MotionEvent.ACTION_DOWN){

	        int temp_ScrollY = getScrollY();
	        scrollTo(getScrollX(), getScrollY() + 1);
	        scrollTo(getScrollX(), temp_ScrollY);

	    }

	    return super.onTouchEvent(event);
	}

}
