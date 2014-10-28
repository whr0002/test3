package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.examples.gg.data.Video;

public class FeedManager_Twitch extends FeedManager_Base {
	private String twtichNextApi;

	@Override
	public ArrayList<Video> getVideoPlaylist() {
		ArrayList<Video> videos = new ArrayList<Video>();

		try {
			processJSON(mJSON);
			// Json is coming from twitch
			JSONArray streams = feed.getJSONArray("streams");
			// System.out.println("Total: " + streams.length());
			for (int i = 0; i < streams.length(); i++) {
				// get a video in the playlist
				JSONObject oneVideo = streams.getJSONObject(i);
				// get the title of this video
				JSONObject videoChannel = oneVideo.getJSONObject("channel");
				String videoTitle = videoChannel.getString("status");
				String videoLink = oneVideo.getJSONObject("_links").getString(
						"self");
				String videoId = videoLink.substring(
						videoLink.indexOf("/streams/") + 9, videoLink.length());
				String videoDesc = "No Desc";
				String thumbUrl = oneVideo.getJSONObject("preview").getString(
						"medium");
				// String updateTime = videoChannel.getString("updated_at");
				String author = videoChannel.getString("display_name");

				// Get the number of viewers
				String numberOfViewers = oneVideo.getString("viewers");

				// System.out.println("Stream ID: " + videoId);
				// store title and link

				Video video = new Video();
				video.setTitle(videoTitle);
				video.setVideoId(videoId);
				video.setAuthor(author);
				video.setThumbnailUrl(thumbUrl);
				video.setVideoDesc(videoDesc);
				// video.setUpdateTime(updateTime);
				video.setViewCount(numberOfViewers);
				video.setAsTwitch();

				// push it to the list
				videos.add(video);
				// System.out.println(videoTitle+"***"+videoLink);

			}
		} catch (Exception ex) {

			// ex.printStackTrace();
		}

		return videos;
	}

	@Override
	public String getNextApi() {
		return twtichNextApi;
	}

	@Override
	protected void processJSON(String json) {
		// TODO Auto-generated method stub
		try {
			twtichNextApi = null;
			JSONTokener jsonParser = new JSONTokener(json);
			feed = (JSONObject) jsonParser.nextValue();
			twtichNextApi = feed.getJSONObject("_links").getString("next");
		} catch (Exception e) {

		}
	}

}
