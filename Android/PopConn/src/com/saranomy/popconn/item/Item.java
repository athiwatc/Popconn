package com.saranomy.popconn.item;

import java.util.Comparator;

public class Item implements Comparable<Item> {
	public int socialId;
	public String name;
	public String action;
	public String thumbnail_url;
	public String time;
	public long date;

	public String content;
	public String image_url;
	public String feature;
	public String comment;

	@Override
	public int compareTo(Item another) {
		return (int) (another.date - this.date);
	}

	public static Comparator<Item> comparator = new Comparator<Item>() {

		public int compare(Item a, Item b) {
			return a.compareTo(b);
		}

	};
}
