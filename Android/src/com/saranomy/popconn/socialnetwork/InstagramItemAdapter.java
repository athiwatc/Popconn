package com.saranomy.popconn.socialnetwork;

import java.util.List;

import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.users.feed.MediaFeedData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.saranomy.popconn.R;
import com.saranomy.popconn.imagecache.ImageLoader;

public class InstagramItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int amount;
	private List<MediaFeedData> mediaFeedData;
	private ImageLoader imageLoader;

	public InstagramItemAdapter(LayoutInflater mInflater, int amount,
			List<MediaFeedData> mediaFeedData) {
		this.mInflater = mInflater;
		this.amount = amount;
		this.mediaFeedData = mediaFeedData;
		imageLoader = new ImageLoader(mInflater.getContext()
				.getApplicationContext());
	}

	@Override
	public int getCount() {
		return amount;
	}

	@Override
	public Object getItem(int position) {
		try {
			return mediaFeedData.get(position);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View listView = convertView;
		final ViewHolder holder;

		if (listView == null) {
			listView = mInflater
					.inflate(R.layout.listview_instagram_item, null);

			holder = new ViewHolder();
			holder.listview_instagram_item_image = (ImageView) listView
					.findViewById(R.id.listview_instagram_item_image);
			listView.setTag(holder);
		} else {
			holder = (ViewHolder) listView.getTag();
		}

		// fill in each item
		try {
			Images images = ((MediaFeedData) getItem(position)).getImages();
			String imageUrl = images.getStandardResolution().getImageUrl();
			imageLoader.DisplayImage(imageUrl,
					holder.listview_instagram_item_image);

		} catch (Exception e) {
		}
		return listView;
	}

	public static class ViewHolder {
		private ImageView listview_instagram_item_image;
	}

}