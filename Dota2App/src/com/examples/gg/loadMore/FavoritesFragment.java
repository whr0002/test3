package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.examples.gg.adapters.FavoriteVideoRemovedCallback;
import com.examples.gg.adapters.VaaForFavorites;
import com.examples.gg.data.Video;
import com.examples.gg.settings.FlashInstallerActivity;
import com.examples.gg.twitchplayers.TwitchPlayer;
import com.examples.gg.twitchplayers.VideoBuffer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import com.examples.gg.twitchplayers.VideoBuffer;

public class FavoritesFragment extends LoadMore_Base implements
		FavoriteVideoRemovedCallback {

	private VaaForFavorites vaaf;
	private SharedPreferences prefs;
	protected String nextFragmentAPI;

	@Override
	public void Initializing() {
		abTitle = "Favorites";
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getSherlockActivity());
	}

	@Override
	public void refreshFragment() {
		titles.clear();
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
					i.putExtra("isfullscreen", true);
					i.putExtra("videoId", videolist.get(position).getVideoId());
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
				} else if (v.isTwitch) {
//					// Getting the preferred player
//					String preferredPlayer = prefs.getString("preferredPlayer",
//							"-1");
//					// Log.i("debug prefs", preferredPlayer);
//					final Context mContext = sfa;
//					if (preferredPlayer.equals("-1")) {
//						// No preference
//						final CharSequence[] colors_radio = {
//								"New Player(No Flash needed)",
//								"Old Player(Flash needed)" };
//
//						new AlertDialog.Builder(sfa)
//								.setSingleChoiceItems(colors_radio, 0, null)
//								.setPositiveButton("Just once",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int whichButton) {
//												dialog.dismiss();
//												int selectedPosition = ((AlertDialog) dialog)
//														.getListView()
//														.getCheckedItemPosition();
//												// Do something useful withe the
//												// position of
//												// the selected radio button
//												openPlayer(selectedPosition,
//														mContext, position,
//														false);
//											}
//										})
//								.setNegativeButton("Always",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int whichButton) {
//												dialog.dismiss();
//												int selectedPosition = ((AlertDialog) dialog)
//														.getListView()
//														.getCheckedItemPosition();
//												// Do something useful withe the
//												// position of
//												// the selected radio button
//												openPlayer(selectedPosition,
//														mContext, position,
//														true);
//
//											}
//										}).show();
//					} else {
//						// Got preferred player
//						openPlayer(Integer.parseInt(preferredPlayer), mContext,
//								position, false);
//					}
					
					openPlayer(0, sfa,
							position, false);
				} else if (v.isNews) {
					String url = videolist.get(position).getVideoId();
					Intent i = new Intent(sfa, NewsViewerActivity.class);
//					i.setData(Uri.parse(url));
					i.putExtra("uri", url);
					startActivity(i);
				}
			}
		});
	}

	@Override
	public void setListView() {

		vaaf = new VaaForFavorites(sfa, titles, videolist, imageLoader,
				FavoritesFragment.this);
		gv.setAdapter(vaaf);

		// Get the favorites
		setFavoriteVideos(titles, videolist);
		// Refresh adapter
		vaaf.notifyDataSetChanged();
		// printVideoLog(videolist);
	}

	private void setFavoriteVideos(ArrayList<String> ts, ArrayList<Video> vl) {
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
				vl.add(videos.get(i));
				ts.add(videos.get(i).getTitle());
			}

		}

	}

	@Override
	public void onCallback(Video v) {
		// Log.d("debug", "called back");
		removeTheVideo(videolist, v);
		vaaf.notifyDataSetChanged();

	}

	private void removeTheVideo(ArrayList<Video> videos, Video mVideo) {
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

	private void openPlayer(int selectedPosition, Context mContext,
			int videoPostion, boolean isSave) {
		switch (selectedPosition) {
		case 0:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "0").commit();
			}
			// Using new video player
			Intent i = new Intent(mContext, VideoBuffer.class);
			i.putExtra("video", videolist.get(videoPostion).getVideoId());
			startActivity(i);
			break;

		case 1:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "1").commit();
			}

			// Using old player
			if (check()) {
				Intent intent1 = new Intent(mContext, TwitchPlayer.class);
				intent1.putExtra("video", videolist.get(videoPostion)
						.getVideoId());
				startActivity(intent1);

			} else {
				Intent intent2 = new Intent(mContext,
						FlashInstallerActivity.class);
				startActivity(intent2);
			}
			break;
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

}
