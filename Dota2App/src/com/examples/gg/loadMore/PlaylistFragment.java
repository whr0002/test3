package com.examples.gg.loadMore;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.examples.gg.feedManagers.FeedManager_Base;

public class PlaylistFragment extends LoadMore_Base {

	private String mPlaylistID;

	@Override
	public void Initializing() {
		mPlaylistID = "PLYV9VR5fkME8K4xMi4zVt55eSImbPKY58";
		abTitle = "X-minute Series";
		String cAPI = "http://gdata.youtube.com/feeds/api/playlists/PLYV9VR5fkME8K4xMi4zVt55eSImbPKY58?v=2&start-index=1&max-results=10&alt=json";

		API.add(cAPI);

		// set a feed manager
		feedManager = new FeedManager_Base();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

	}

	@Override
	protected void setGridViewItemClickListener() {

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Playlist activity
				Intent i = new Intent(sfa, YoutubeActionBarActivity.class);
				i.putExtra("isfullscreen", true);
				i.putExtra("videoId", videolist.get(position).getVideoId());
				i.putExtra("playlistID", mPlaylistID);
				i.putParcelableArrayListExtra("videoList", videolist);
				i.putExtra("positionOfList", position);
				startActivity(i);
			}
		});

	}

	public String buildAPI(String q) {

		return "http://gdata.youtube.com/feeds/api/playlists/" + q
				+ "?start-index=1&max-results=10&orderby=published&alt=json";

	}
}
