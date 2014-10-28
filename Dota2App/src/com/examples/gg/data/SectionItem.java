package com.examples.gg.data;

public class SectionItem implements Item {

	private final String title;

	public SectionItem(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public boolean isSection() {
		return true;
	}

	@Override
	public void setChecked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUnchecked() {
		// TODO Auto-generated method stub
		
	}

}
