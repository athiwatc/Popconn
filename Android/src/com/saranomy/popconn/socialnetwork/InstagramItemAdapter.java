package com.saranomy.popconn.socialnetwork;

import java.util.List;

import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.User;
import org.jinstagram.entity.users.feed.MediaFeedData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
			holder.listview_instagram_item_thumbnail = (ImageView) listView
					.findViewById(R.id.listview_instagram_item_thumbnail);
			holder.listview_instagram_item_owner = (TextView) listView
					.findViewById(R.id.listview_instagram_item_owner);
			holder.listview_instagram_item_time = (TextView) listView
					.findViewById(R.id.listview_instagram_item_time);
			holder.listview_instagram_item_image = (ImageView) listView
					.findViewById(R.id.listview_instagram_item_image);
			holder.listview_instagram_item_like = (TextView) listView
					.findViewById(R.id.listview_instagram_item_like);
			holder.listview_instagram_item_comment = (TextView) listView
					.findViewById(R.id.listview_instagram_item_comment);

			listView.setTag(holder);
		} else {
			holder = (ViewHolder) listView.getTag();
		}

		// fill in each item
		try {
			MediaFeedData mediaFeedData = (MediaFeedData) getItem(position);
			User user = mediaFeedData.getUser();
			String ownerThumbnail = user.getProfilePictureUrl();
			String ownerUsername = user.getUserName();
			String time = InstagramCore.convertSecondToInstagramTime(System
					.currentTimeMillis()
					/ 60000
					- (Integer.parseInt(mediaFeedData.getCreatedTime()) / 60));
			Images images = ((MediaFeedData) getItem(position)).getImages();
			String imagesUrl = images.getStandardResolution().getImageUrl();
			String like = mediaFeedData.getLikes().getCount() + " likes";
			String commentString = "";
			List<CommentData> comments = mediaFeedData.getComments()
					.getComments();
			for (CommentData s : comments) {
				commentString += s.getCommentFrom().getUsername() + " : " + s.getText() + "\n";
			}

			imageLoader.DisplayImage(ownerThumbnail,
					holder.listview_instagram_item_thumbnail);
			holder.listview_instagram_item_owner.setText(ownerUsername);
			holder.listview_instagram_item_time.setText(time);
			imageLoader.DisplayImage(imagesUrl,
					holder.listview_instagram_item_image);
			Log.e("imageLoader sent", imagesUrl);
			holder.listview_instagram_item_like.setText(like);
			holder.listview_instagram_item_comment.setText(commentString);

		} catch (Exception e) {
		}
		return listView;
	}

	public static class ViewHolder {
		private ImageView listview_instagram_item_thumbnail;
		private TextView listview_instagram_item_owner;
		private TextView listview_instagram_item_time;
		private ImageView listview_instagram_item_image;
		private TextView listview_instagram_item_like;
		private TextView listview_instagram_item_comment;
	}
}