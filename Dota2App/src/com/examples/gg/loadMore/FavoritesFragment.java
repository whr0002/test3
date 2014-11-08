package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.examples.gg.adapters.FavoriteVideoRemovedCallback;
import com.examples.gg.adapters.VaaForFavorites;
import com.examples.gg.data.Video;
import com.examples.gg.twitchplayers.VideoBuffer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.dota.R;

//import com.examples.gg.twitchplayers.VideoBuffer;

public class FavoritesFragment extends LoadMore_Base implements
		FavoriteVideoRemovedCallback {

	protected VaaForFavorites vaaf;
	protected SharedPreferences prefs;
	protected String nextFragmentAPI;
	protected int searchType;

	@Override
	public void Initializing() {
		abTitle = "Favorites";
		setHasOptionsMenu(true);
		setOptionMenu(true, true);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getSherlockActivity());
	}

	public void setDropdown() {

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] catagory = { "All", "Videos", "Channels", "Playlists" , "News", "Twitch"};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ab.getThemedContext(), R.layout.sherlock_spinner_item,
				android.R.id.text1, catagory);

		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		ab.setListNavigationCallbacks(adapter, this);

		ab.setSelectedNavigationItem(currentPosition);

	}

	@Override
	public void refreshFragment() {
//		titles.clear();
		videolist.clear();
		this.setListView();
	}

	@Override
	protected void setGridViewItemClickListener() {
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				Video v = videolist.get(position);

				if (v.isVideo) {
					// This is a video
					Intent i = new Intent(sfa, YoutubeActionBarActivity.class);
//					i.putExtra("isfullscreen", true);
					i.putExtra("video", videolist.get(position));
					startActivity(i);
				} else if (v.isChannel) {
					// This is a channel
					nextFragmentAPI = videolist.get(position)
							.getRecentVideoUrl();
					String title = videolist.get(position).getTitle();
					String url = videolist.get(position).getThumbnailUrl();

					Intent i = new Intent(sfa, LoadMore_Activity_Channel.class);
					i.putExtra("API", nextFragmentAPI);
					i.putExtra("PLAYLIST_API", videolist.get(position)
							.getPlaylistsUrl());
					i.putExtra("title", title);
					i.putExtra("thumbnail", url);
					startActivity(i);

				} else if (v.isPlaylist) {
					// This is a playlist
					Intent i1 = new Intent(sfa, LoadMore_Activity_Base.class);

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
				} else if(v.isTwitch){
					// Using new video player
					Intent i = new Intent(sfa, VideoBuffer.class);
					i.putExtra("video", videolist.get(position).getVideoId());
					startActivity(i);
				} else if(v.isNews){
					// Start News activity
					String url = videolist.get(position).getVideoId();
					Intent i = new Intent(sfa,NewsViewerActivity.class);
					i.putExtra("uri", url);
					startActivity(i);
				}
			}
		});
	}

	@Override
	public void setListView() {

		vaaf = new VaaForFavorites(sfa, videolist, imageLoader,
				FavoritesFragment.this);
		gv.setAdapter(vaaf);

		// Get the favorites
		setFavoriteVideos(videolist);
		// Refresh adapter
		vaaf.notifyDataSetChanged();
		// printVideoLog(videolist);
	}

	public void setFavoriteVideos(ArrayList<Video> vl) {
		Gson gson = new Gson();

		SharedPreferences favoritePrefs = sfa.getSharedPreferences("Favorites",
				0);
		ArrayList<Video> videos;
		String result = favoritePrefs.getString("json", "");
		if (result.equals("")) {
			// Favorites is empty
			vl = new ArrayList<Video>();

		} else {
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>() {
			}.getType();
			videos = gson.fromJson(favoritePrefs.getString("json", ""),
					listType);

			for (int i = videos.size() - 1; i >= 0; i--) {
				Video v = videos.get(i);
				if (searchType == 0) {
					// Show all
					vl.add(v);

				} else if (searchType == 1 && v.isVideo) {
					// only show videos
					vl.add(v);


				} else if (searchType == 2 && v.isChannel) {
					// only show channels
					vl.add(v);
	
				} else if (searchType == 3 && v.isPlaylist) {
					// only show playlists
					vl.add(v);
			
				} else if (searchType == 4 && v.isNews){
					vl.add(v);
				} else if (searchType == 5 && v.isTwitch){
					vl.add(v);
				}

			}

		}

	}

	@Override
	public void onCallback(Video v) {
		// Log.d("debug", "called back");
		removeTheVideo(videolist, v);
		vaaf.notifyDataSetChanged();

	}

	protected void removeTheVideo(ArrayList<Video> videos, Video mVideo) {
		int index = -1;
		if (mVideo != null) {
			for (int i = 0; i < videos.size(); i++) {
				if (videos.get(i).getVideoId().equals(mVideo.getVideoId())) {
					index = i;
					break;
				}
			}

			if (index != -1) {
				videos.remove(index);

			}
		}

	}


	private boolean check() {
		PackageManager pm = sfa.getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		if (firstTime) {
			firstTime = false;
			return true;
		}

		searchType = itemPosition;
		refreshFragment();
		return true;

	}

}
