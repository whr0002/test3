package com.examples.gg.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

	public String title;
	public String videoId;
	public String videoDesc;
	public String thumbnailUrl;
	public String author;
	public String uploaderThumUrl;
	public String viewCount;
	public String duration;

	public String recentVideoUrl;
	public String playlistsUrl;

	public String updateTime;
	
	public boolean isVideo = false;
	public boolean isChannel = false;
	public boolean isPlaylist = false;
	public boolean isTwitch = false;
	public boolean isNews = false;
	
	public void setAsVideo(){
		isVideo = true;
		isChannel = false;
		isPlaylist = false;
		isTwitch = false;
		isNews = false;
	}
	public void setAsTwitch(){
		isVideo = false;
		isChannel = false;
		isPlaylist = false;
		isTwitch = true;
		isNews = false;
	}
	public void setAsNews(){
		isVideo = false;
		isChannel = false;
		isPlaylist = false;
		isTwitch = false;
		isNews = true;
	}
	
	public void setAsChannel(){
		isVideo = false;
		isChannel = true;
		isPlaylist = false;
		isTwitch = false;
		isNews = false;
	}
	public void setAsPlaylist(){
		isChannel = false;
		isVideo = false;
		isPlaylist = true;
		isTwitch = false;
		isNews = false;
	}
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getUploaderThumUrl() {
		return uploaderThumUrl;
	}

	public void setUploaderThumUrl(String uploaderThumUrl) {
		this.uploaderThumUrl = uploaderThumUrl;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRecentVideoUrl() {
		return recentVideoUrl;
	}

	public void setRecentVideoUrl(String recentVideoUrl) {
		this.recentVideoUrl = recentVideoUrl;
	}
	
	public String getPlaylistsUrl() {
		return playlistsUrl;
	}

	public void setPlaylistsUrl(String playlistsUrl) {
		this.playlistsUrl = playlistsUrl;
	}




	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Video() {

	}

	public Video(Parcel in) {
		title = in.readString();
		videoId = in.readString();
		videoDesc = in.readString();
		thumbnailUrl = in.readString();
		author = in.readString();
		uploaderThumUrl = in.readString();
		viewCount = in.readString();
		duration = in.readString();

	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getVideoId() {

		return videoId;
	}

	public void setVideoId(String vid) {

		this.videoId = vid;
	}

	public String getVideoDesc() {
		return videoDesc;
	}

	public void setVideoDesc(String videoDesc) {
		this.videoDesc = videoDesc;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
		public Video createFromParcel(Parcel in) {
			return new Video(in);
		}

		public Video[] newArray(int size) {
			return new Video[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(videoId);
		dest.writeString(videoDesc);
		dest.writeString(thumbnailUrl);
		dest.writeString(author);
		dest.writeString(uploaderThumUrl);
		dest.writeString(viewCount);
		dest.writeString(duration);

	}
}
