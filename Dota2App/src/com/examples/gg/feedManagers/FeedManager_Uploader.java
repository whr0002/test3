package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.examples.gg.data.Video;

public class FeedManager_Uploader extends FeedManager_Base{
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
				// get a video in the playlist
				JSONObject oneVideo = playlist.getJSONObject(i);
				// get the title of this video
				String videoTitle = oneVideo.getJSONObject("title").getString(
						"$t");
				String videoLink = null;
				String videoId = null;

				videoLink = oneVideo.getJSONArray("link").getJSONObject(0).getString("href");
				
				videoId = videoLink.substring(0, videoLink.length()-4) + "/uploads?start-index=1&max-results=10&v=2&alt=json";
				
				String playlistsUrl = videoLink.substring(0, videoLink.length()-4) + "/playlists?start-index=1&max-results=10&v=2&alt=json";

				String thumbUrl = oneVideo.getJSONObject("media$thumbnail").getString("url");

				String author = oneVideo.getJSONObject("yt$username").getString("display");

				Video video = new Video();

				video.setTitle(videoTitle);
				
				video.setVideoId(videoId);
				
				video.setThumbnailUrl(thumbUrl);

				video.setAuthor(author);

				video.setRecentVideoUrl(videoId);
				
				video.setPlaylistsUrl(playlistsUrl);
				
				video.setAsChannel();
				
				// push it to the list
				videos.add(video);
			}

		} catch (Exception ex) {

			//ex.printStackTrace();
		}

		return videos;

	}
}
