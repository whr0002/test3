package com.examples.gg.feedManagers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.examples.gg.data.Video;

/**
 * This class is used to parse JSON from Youtube server 
 * */
public class FeedManager_Base {

	protected JSONObject feed;
	protected String mJSON;

	// Get the JSON String
	public String getmJSON() {
		return mJSON;
	}

	// Set the JSON String
	public void setmJSON(String mJSON) {
		this.mJSON = mJSON;
	}

	// Return a list of Video objects according to the JSON String provided
	public ArrayList<Video> getVideoPlaylist() {

		processJSON(mJSON);

		ArrayList<Video> videos = new ArrayList<Video>();

		try {
			// get the playlist
			JSONArray playlist = feed.getJSONArray("entry");

			for (int i = 0; i < playlist.length(); i++) {
				// get a video in the playlist
				JSONObject oneVideo = playlist.getJSONObject(i);
				// get the title of this video
				String videoTitle = oneVideo.getJSONObject("title").getString(
						"$t");
				String videoLink = null;
				String videoId = null;
				videoLink = oneVideo.getJSONObject("content").getString("src");
				videoId = videoLink.substring(videoLink.indexOf("/v/") + 3,
						videoLink.indexOf("?"));
				String videoDesc = oneVideo.getJSONObject("media$group")
						.getJSONObject("media$description").getString("$t");
				String thumbUrl = oneVideo.getJSONObject("media$group")
						.getJSONArray("media$thumbnail").getJSONObject(2)
						.getString("url");
				String updateTime = oneVideo.getJSONObject("published")
						.getString("$t");
				String author = oneVideo.getJSONArray("author")
						.getJSONObject(0).getJSONObject("name").getString("$t");
				String vCount = oneVideo.getJSONObject("yt$statistics")
						.getString("viewCount") + " views";
				String inSecs = oneVideo.getJSONObject("media$group")
						.getJSONObject("yt$duration").getString("seconds");
				String convertedDuration = formatSecondsAsTime(inSecs);

				updateTime = handleDate(updateTime);

				Video video = new Video();

				// store title and link
				video.setTitle(videoTitle);
				video.setVideoId(videoId);
				video.setThumbnailUrl(thumbUrl);
				video.setVideoDesc(videoDesc);
				video.setUpdateTime(updateTime);
				video.setAuthor(author);
				video.setViewCount(vCount);
				video.setDuration(convertedDuration);
				video.setAsVideo();
				// push it to the list
				videos.add(video);

			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return videos;
	}

	// Return the next data api from Youtube
	public String getNextApi() throws JSONException {
		JSONArray link = feed.getJSONArray("link");
		for (int i = 0; i < link.length(); i++) {
			JSONObject jo = link.getJSONObject(i);
			if (jo.getString("rel").equals("next")) {
				// there are more videos in this playlist
				String nextUrl = jo.getString("href");
				return nextUrl;
			}
		}
		return null;

	}

	// Get the whole JSON object
	public JSONObject getFeed() {
		return feed;
	}

	// Set the whole JSON object
	public void setFeed(JSONObject feed) {
		this.feed = feed;
	}

	// Given a String number(seconds), return a formatted String time in
	// 00:00:00
	protected String formatSecondsAsTime(String secs) {
		int totalSecs = Integer.parseInt(secs);

		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;

		if (hours == 0) {
			return twoDigitString(minutes) + ":" + twoDigitString(seconds);
		} else {
			return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":"
					+ twoDigitString(seconds);
		}

	}

	// Given a number (1 digit or 2 digits), return a formatted string
	// in "00", "0X", "XX"
	private String twoDigitString(int number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}

	// Given a date string from JSON, return date difference
	protected String handleDate(String s) {
		String temp = s.replace("T", " ");
		String dateInString = temp.substring(0, temp.indexOf("."));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
		Date d1 = new Date();
		Date d2 = new Date();
		try {
			d2 = dateFormat.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return calculateDateDifference(d1, d2);

	}

	// Given a past date and current date, return date difference
	private String calculateDateDifference(Date today, Date past) {
		long diff = today.getTime() - past.getTime();
		long diffSec = (diff / 1000L) % 60L;
		long diffMin = (diff / (60L * 1000L)) % 60L;
		long diffHour = (diff / (60L * 60L * 1000L)) % 24L;
		long diffDay = (diff / (24L * 60L * 60L * 1000L)) % 30L;
		long diffWeek = (diff / (7L * 24L * 60L * 60L * 1000L)) % 7L;
		long diffMonth = (diff / (30L * 24L * 60L * 60L * 1000L)) % 12L;
		long diffYear = (diff / (12L * 30L * 24L * 60L * 60L * 1000L));

		if (diffYear == 1) {
			return diffYear + " year ago";
		} else if (diffYear > 1) {
			return diffYear + " years ago";
		} else {
			// less than 1 year
			if (diffMonth == 1) {
				return diffMonth + " month ago";
			} else if (diffMonth > 1) {
				return diffMonth + " months ago";
			} else {
				// less than 1 week
				if (diffWeek == 1) {
					return diffWeek + " week ago";
				} else if (diffWeek > 1) {
					return diffWeek + " weeks ago";
				} else {
					// less than 1 month
					if (diffDay == 1) {
						return diffDay + " day ago";
					} else if (diffDay > 1) {
						return diffDay + " days ago";
					} else {

						// less than 1 day
						if (diffHour == 1) {
							return diffHour + " hour ago";
						} else if (diffHour > 1) {
							return diffHour + " hours ago";
						} else {
							// less than 1 hour
							if (diffMin == 1) {
								return diffMin + " minute ago";
							} else if (diffMin > 1) {
								return diffMin + " minutes ago";
							} else {
								// less than 1 minute
								if (diffSec == 0 || diffSec == 1) {
									return diffSec + " second ago";
								} else if (diffSec > 1) {
									return diffSec + " seconds ago";
								}
							}
						}
					}
				}
			}
		}

		return "";
	}

	// convert a JSON string to a JSON object
	protected void processJSON(String json) {
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject wholeJson = (JSONObject) jsonParser.nextValue();
			this.feed = wholeJson.getJSONObject("feed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
