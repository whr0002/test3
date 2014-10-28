package com.examples.gg.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Tip implements Parcelable{
	private String mTitle;
	private String mContent;

	public Tip(String title, String content){
		this.mTitle = title;
		this.mContent = content;
		
	}
	
	public Tip(Parcel in) {
		mTitle = in.readString();
		mContent = in.readString();

	}
	
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		this.mTitle = title;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String content) {
		this.mContent = content;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mContent);
		
	}
	
	public static final Parcelable.Creator<Tip> CREATOR = new Parcelable.Creator<Tip>() {
		public Tip createFromParcel(Parcel in) {
			return new Tip(in);
		}

		public Tip[] newArray(int size) {
			return new Tip[size];
		}
	};
}
