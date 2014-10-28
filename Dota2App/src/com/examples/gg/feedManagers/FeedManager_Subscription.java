package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.examples.gg.data.Video;

public class FeedManager_Subscription extends FeedManager_Base {

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

				videoLink = oneVideo.getJSONObject("id").getString("$t");
				videoId = videoLink.substring(
						videoLink.indexOf("/videos/") + 8, videoLink.length());

				// System.out.println("Working 2: "+videoLink);
				String videoDesc = oneVideo.getJSONObject("media$group")
						.getJSONObject("media$description").getString("$t");
				String thumbUrl = oneVideo.getJSONObject("media$group")
						.getJSONArray("media$thumbnail").getJSONObject(0)
						.getString("url");
				String updateTime = oneVideo.getJSONObject("published")
						.getString("$t");
				// System.out.println("Working 4");
				String author = oneVideo.getJSONArray("author")
						.getJSONObject(0).getJSONObject("name").getString("$t");
				String vCount = oneVideo.getJSONObject("yt$statistics")
						.getString("viewCount") + " views";
				String inSecs = oneVideo.getJSONObject("media$group")
						.getJSONObject("yt$duration").getString("seconds");
				String convertedDuration = formatSecondsAsTime(inSecs);

				// System.out.println("date: " +
				// updateTime.substring(0,updateTime.indexOf("T")));
				// System.out.println("time: " +
				// updateTime.substring(updateTime.indexOf("T")+1,
				// updateTime.indexOf(".")));
				updateTime = handleDate(updateTime);

				Video video = new Video();

				// System.out.println("converted duration: " +
				// convertedDuration);
				// System.out.println(videoDesc);
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
