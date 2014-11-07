package com.examples.gg.loadMore;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.examples.gg.feedManagers.FeedManager_Twitch;
import com.examples.gg.settings.FlashInstallerActivity;
import com.examples.gg.twitchplayers.TwitchPlayer;
import com.examples.gg.twitchplayers.VideoBuffer;
import com.rs.dota.R;

public class LoadMore_Twitch extends LoadMore_Base{
	private SharedPreferences prefs;

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Twitch Dota2";

		// Give API URLs
		API.add("https://api.twitch.tv/kraken/streams?game=Dota+2&limit=10");

		// set a feed manager
		feedManager = new FeedManager_Twitch();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		// Get preference
//		prefs = this.getSherlockActivity().getSharedPreferences(
//				"com.examples.gg", this.getSherlockActivity().MODE_PRIVATE);
		prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		SearchView searchView = new SearchView(sfa.getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint("Search Twitch");
		searchView.setOnQueryTextListener(this);

		menu.add(0, 20, 0, "Search")
				.setIcon(R.drawable.abs__ic_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

//		menu.add(0, 0, 0, "")
//				.setIcon(R.drawable.ic_refresh)
//				.setShowAsAction(
//						MenuItem.SHOW_AS_ACTION_IF_ROOM);

	}

	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// Getting the preferred player
//				String preferredPlayer = prefs.getString("preferredPlayer", "-1");
////				Log.i("debug prefs", preferredPlayer);
//				final Context mContext = sfa;
//				if (preferredPlayer.equals("-1")) {
//					// No preference
//					final CharSequence[] colors_radio = {
//							"New Player(No Flash needed)", "Old Player(Flash needed)" };
//
//					new AlertDialog.Builder(sfa)
//							.setSingleChoiceItems(colors_radio, 0, null)
//							.setPositiveButton("Just once",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int whichButton) {
//											dialog.dismiss();
//											int selectedPosition = ((AlertDialog) dialog)
//													.getListView()
//													.getCheckedItemPosition();
//											// Do something useful withe the position of
//											// the selected radio button
//											openPlayer(selectedPosition, mContext,
//													position, false);
//										}
//									})
//							.setNegativeButton("Always",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int whichButton) {
//											dialog.dismiss();
//											int selectedPosition = ((AlertDialog) dialog)
//													.getListView()
//													.getCheckedItemPosition();
//											// Do something useful withe the position of
//											// the selected radio button
//											openPlayer(selectedPosition, mContext,
//													position, true);
//
//										}
//									}).show();
//				} else {
//					// Got preferred player
//					openPlayer(Integer.parseInt(preferredPlayer), mContext, position, false);
//				}
				openPlayer(0, sfa, position, false);
			}
		});
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
	public boolean onQueryTextSubmit(String query) {
		// start search activity
		Intent intent = new Intent(sfa, LoadMore_Activity_Search_Twitch.class);
		intent.putExtra("query", query);
		startActivity(intent);

		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
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
}
