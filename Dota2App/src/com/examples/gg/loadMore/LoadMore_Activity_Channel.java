package com.examples.gg.loadMore;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.examples.gg.data.Video;
import com.examples.gg.feedManagers.FeedManager_Base_v3_Playlist;
import com.examples.gg.feedManagers.FeedManager_Base_v3_PlaylistItem;
import com.rs.dota.R;

public class LoadMore_Activity_Channel extends LoadMore_Activity_Base implements
		OnNavigationListener {
	private boolean isFirstTimeLoading = true;

	@Override
	public void Initializing() {
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] catagory = { "Recent", "Playlists" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ab.getThemedContext(), R.layout.sherlock_spinner_item,
				android.R.id.text1, catagory);

		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		ab.setListNavigationCallbacks(adapter, this);

		ab.setSelectedNavigationItem(currentPosition);
	}

	@Override
	protected void setGridViewItemClickListener() {

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// First check it is under which section
				switch (section) {
				case 0:
					// In "Recent"
					Intent i = new Intent(mContext,
							YoutubeActionBarActivity.class);
					i.putExtra("video", videolist.get(position));
					// i.putExtra("videoId",
					// videolist.get(position).getVideoId());
					// i.putExtra("isfullscreen", true);
					startActivity(i);
					break;

				case 1:
					// In "Playlists"
					Video v = videolist.get(position);
					Intent i1 = new Intent(mContext,
							LoadMore_Activity_Base.class);
					i1.putExtra("API", videolist.get(position)
							.getRecentVideoUrl());
					i1.putExtra("PLAYLIST_API", videolist.get(position)
							.getPlaylistsUrl());
					i1.putExtra("title", videolist.get(position).getTitle());
					i1.putExtra("thumbnail", videolist.get(position)
							.getThumbnailUrl());
					i1.putExtra("playlistID", videolist.get(position)
							.getVideoId());
					startActivity(i1);
					break;

				}
			}
		});

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		// set section indicator
		section = itemPosition;

		if (isFirstTimeLoading) {
			isFirstTimeLoading = false;
			return true;
		}

		oneStepRefresh();

		return true;
	}

	@Override
	public void refreshActivity() {

		oneStepRefresh();

	}

	public void oneStepRefresh() {
		if (section == 0) {
			// Section "Recent"

			redoRequest(recentAPI, new FeedManager_Base_v3_PlaylistItem(
					"video", recentAPI, browserKey, gv, numOfResults));

		}

		if (section == 1) {
			// Section "Playlists"
//			Log.d("debug", playlistAPI);
			redoRequest(playlistAPI, new FeedManager_Base_v3_Playlist(
					"playlist", playlistAPI, browserKey, gv, numOfResults));

		}
	}

}
