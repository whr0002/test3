package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONObject;

import com.examples.gg.data.Video;

import android.widget.GridView;



public class FeedManager_Base_v3_PlaylistItem extends FeedManager_Base_v3{

	public FeedManager_Base_v3_PlaylistItem(String mediaType, String api,
			String browserKey, GridView gv, String numOfResults) {
		super(mediaType, api, browserKey, gv, numOfResults);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrayList<Video> getVideoPlaylist() {

		processJSON(mJSON);

		videos = new ArrayList<Video>();

		try {

			for (int i = 0; i < items.length(); i++) {
				Video video = new Video();
				// get a video in the playlist
				JSONObject snippet = items.getJSONObject(i).getJSONObject(
						"snippet");
				// get the title of this video
				String videoTitle = snippet.getString("title");

				String videoId = snippet.getJSONObject("resourceId").getString(
						mediaType + "Id");
				String author = "";
				String videoDesc = "";
				String thumbUrl = "";
				if (!snippet.isNull("channelTitle")) {
					author = snippet.getString("channelTitle");
				}

				if (!snippet.isNull("description")) {
					videoDesc = snippet.getString("description");
				}

				if (!snippet.isNull("thumbnails")) {
					thumbUrl = snippet.getJSONObject("thumbnails")
							.getJSONObject("medium").getString("url");
				}
				String updateTime = snippet.getString("publishedAt");

				String recentAPI = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults="
						+ numOfResults
						+ "&playlistId="
						+ videoId
						+ "&key="
						+ browserKey;

				String playlistAPI = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId="
						+ videoId + "&maxResults=10&key=" + browserKey;

				String formatedDate = handleDate(updateTime);
				setMediaType(video);
				video.setTitle(videoTitle);
				video.setVideoId(videoId);
				video.setThumbnailUrl(thumbUrl);
				video.setAuthor(author);
				video.setRecentVideoUrl("");
				video.setViewCount("");
				video.setDuration("");
				video.setVideoDesc(videoDesc);
				video.setUpdateTime(formatedDate);
				video.setRecentVideoUrl(recentAPI);
				video.setPlaylistsUrl(playlistAPI);
				// System.out.println(video.getTitle());
				// push it to the list
				videos.add(video);
				// System.out.println(videoTitle+"***"+videoLink);

				ids += videoId + ",";
				if(i == items.length()-1)
					ids += videoId;
			}
			doSecondTask();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		return videos;
	}

}
