package com.examples.gg.data;

public class EntryItem implements Item {

	public final String title;
	public final String subtitle;
	public final int icon;
	public boolean isChecked = false;

	public EntryItem(String title, String subtitle, int icon) {
		this.title = title;
		this.subtitle = subtitle;
		this.icon = icon;
	}

	@Override
	public boolean isSection() {
		return false;
	}

	public boolean isChecked() {
		// TODO Auto-generated method stub
		return isChecked;
	}

	public void setChecked() {
		// TODO Auto-generated method stub
		isChecked = true;
	}

	@Override
	public void setUnchecked() {
		// TODO Auto-generated method stub
		isChecked = false;
	}
	
}
