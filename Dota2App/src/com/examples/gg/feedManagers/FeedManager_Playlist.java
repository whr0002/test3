package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.examples.gg.data.Video;

public class FeedManager_Playlist extends FeedManager_Base {

	@Override
	public ArrayList<Video> getVideoPlaylist() {
		
		processJSON(mJSON);
		
		ArrayList<Video> videos = new ArrayList<Video>();

		try {
			

			// System.out.println(plTitle);
			// get the playlist
			JSONArray playlist = feed.getJSONArray("entry");
			// System.out.println("Length: "+ playlist.length());

			for (int i = 0; i < playlist.length(); i++) {
				Video video = new Video();
				// get a video in the playlist
				JSONObject oneVideo = playlist.getJSONObject(i);
				// get the title of this video
				String videoTitle = oneVideo.getJSONObject("title").getString(
						"$t");
				String videoLink = oneVideo.getJSONObject("content").getString(
						"src");
				String videoId = videoLink.substring(
						videoLink.indexOf("/v/") + 3, videoLink.indexOf("?"));
				String author = oneVideo.getJSONArray("author")
						.getJSONObject(0).getJSONObject("name").getString("$t");
				// String videoDesc =
				// oneVideo.getJSONObject("media$group").getJSONObject("media$description").getString("$t");
				String videoDesc = oneVideo.getJSONObject("summary").getString(
						"$t");
				String thumbUrl = oneVideo.getJSONObject("media$group")
						.getJSONArray("media$thumbnail").getJSONObject(2)
						.getString("url");
				String updateTime = oneVideo.getJSONObject("updated")
						.getString("$t");

				video.setAsPlaylist();
				video.setTitle(videoTitle);
				video.setVideoId(videoId);
				video.setThumbnailUrl(thumbUrl);
				video.setAuthor(author);
				video.setRecentVideoUrl(videoLink
						+ "&start-index=1&max-results=10&orderby=published&alt=json");
				video.setVideoDesc(videoDesc);
				video.setUpdateTime(updateTime);
				// System.out.println(video.getTitle());
				// push it to the list
				videos.add(video);
				// System.out.println(videoTitle+"***"+videoLink);

			}

		} catch (Exception ex) {
			//ex.printStackTrace();
		}

		return videos;
	}

}
