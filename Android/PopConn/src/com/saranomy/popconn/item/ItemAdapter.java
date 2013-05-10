package com.saranomy.popconn.item;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saranomy.popconn.R;
import com.saranomy.popconn.imagecache.ImageLoader;

public class ItemAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Item> items;
	private ImageLoader imageLoader;
	private int size;

	public ItemAdapter(LayoutInflater inflater, List<Item> items) {
		this.inflater = inflater;
		this.items = items;
		imageLoader = new ImageLoader(inflater.getContext().getApplicationContext());
		size = items.size();
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View listview = convertView;
		if (listview == null) {
			listview = inflater.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();
			holder.listview_item_item = (LinearLayout) listview.findViewById(R.id.listview_item_item);

			holder.listview_item_name = (TextView) listview.findViewById(R.id.listview_item_name);
			holder.listview_item_action = (TextView) listview.findViewById(R.id.listview_item_action);
			holder.listview_item_thumbnail = (ImageView) listview.findViewById(R.id.listview_item_thumbnail);
			holder.listview_item_time = (TextView) listview.findViewById(R.id.listview_item_time);

			holder.listview_item_content = (TextView) listview.findViewById(R.id.listview_item_content);
			holder.listview_item_image = (ImageView) listview.findViewById(R.id.listview_item_image);

			holder.listview_item_image1 = (ImageView) listview.findViewById(R.id.listview_item_image1);
			holder.listview_item_feature1 = (TextView) listview.findViewById(R.id.listview_item_feature1);
			holder.listview_item_image2 = (ImageView) listview.findViewById(R.id.listview_item_image2);
			holder.listview_item_feature2 = (TextView) listview.findViewById(R.id.listview_item_feature2);
			listview.setTag(holder);
		} else {
			holder = (ViewHolder) listview.getTag();
		}
		// assign item
		Item item = items.get(position);

		if (item.socialId == 0) {
			holder.listview_item_item.setBackgroundResource(R.drawable.rect_facebook);
			holder.listview_item_content.setVisibility(View.GONE);
			holder.listview_item_image.setVisibility(View.GONE);
			holder.listview_item_image1.setVisibility(View.GONE);
			holder.listview_item_feature1.setVisibility(View.GONE);
			holder.listview_item_image2.setVisibility(View.GONE);
			holder.listview_item_feature2.setVisibility(View.GONE);

			if (item.content != null) {
				holder.listview_item_content.setVisibility(View.VISIBLE);
				holder.listview_item_content.setText(item.content);
			}
			if (item.image_url != null) {
				holder.listview_item_image.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(item.image_url, holder.listview_item_image);
			}
			if (item.feature != null) {
				holder.listview_item_image1.setVisibility(View.VISIBLE);
				holder.listview_item_feature1.setVisibility(View.VISIBLE);
				holder.listview_item_image1.setImageResource(R.drawable.ic_good);
				holder.listview_item_feature1.setText(item.feature);
			}

		} else if (item.socialId == 1) {
			holder.listview_item_item.setBackgroundResource(R.drawable.rect_twitter);

			holder.listview_item_content.setVisibility(View.VISIBLE);
			holder.listview_item_image.setVisibility(View.GONE);
			holder.listview_item_image1.setVisibility(View.GONE);
			holder.listview_item_feature1.setVisibility(View.GONE);
			holder.listview_item_image2.setVisibility(View.GONE);
			holder.listview_item_feature2.setVisibility(View.GONE);

			if (item.feature != "") {
				holder.listview_item_image1.setImageResource(R.drawable.ic_repeat);
				holder.listview_item_feature1.setText(item.feature);
			}
			holder.listview_item_content.setText(item.content);

		} else if (item.socialId == 2) {
			holder.listview_item_item.setBackgroundResource(R.drawable.rect_instagram);

			holder.listview_item_content.setVisibility(View.GONE);
			holder.listview_item_image.setVisibility(View.VISIBLE);
			holder.listview_item_image1.setVisibility(View.GONE);
			holder.listview_item_feature1.setVisibility(View.GONE);
			holder.listview_item_image2.setVisibility(View.GONE);
			holder.listview_item_feature2.setVisibility(View.GONE);

			if (item.feature != "") {
				holder.listview_item_image1.setVisibility(View.VISIBLE);
				holder.listview_item_feature1.setVisibility(View.VISIBLE);
				holder.listview_item_image1.setImageResource(R.drawable.ic_favorite);
				holder.listview_item_feature1.setText(item.feature);
			}

			if (item.comment != "") {
				holder.listview_item_image2.setVisibility(View.VISIBLE);
				holder.listview_item_feature2.setVisibility(View.VISIBLE);
				holder.listview_item_image2.setImageResource(R.drawable.ic_chat);
				holder.listview_item_feature2.setText(item.comment);
			}
			imageLoader.DisplayImage(item.image_url, holder.listview_item_image);

		}

		// every should have THUMBNAIL,NAME,ACTION,TIME
		imageLoader.DisplayImage(item.thumbnail_url, holder.listview_item_thumbnail);
		holder.listview_item_name.setText(item.name);
		holder.listview_item_action.setText(item.action);
		holder.listview_item_time.setText(item.time);
		return listview;
	}

	public class ViewHolder {
		public LinearLayout listview_item_item;

		public TextView listview_item_name;
		public TextView listview_item_action;
		public ImageView listview_item_thumbnail;
		public TextView listview_item_time;

		public TextView listview_item_content;
		public ImageView listview_item_image;

		public ImageView listview_item_image1;
		public TextView listview_item_feature1;
		public ImageView listview_item_image2;
		public TextView listview_item_feature2;
	}
}
