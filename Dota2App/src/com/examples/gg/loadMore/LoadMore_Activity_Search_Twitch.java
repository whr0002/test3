package com.examples.gg.loadMore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.widget.SearchView;
import com.examples.gg.feedManagers.FeedManager_Twitch;
import com.examples.gg.settings.FlashInstallerActivity;
import com.examples.gg.twitchplayers.TwitchPlayer;
import com.examples.gg.twitchplayers.VideoBuffer;

public class LoadMore_Activity_Search_Twitch extends LoadMore_Activity_Search
		implements SearchView.OnQueryTextListener {
	private SharedPreferences prefs;
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Search Twitch";
		
		ab.setTitle(abTitle);

		// Get the query
		Intent i = getIntent();
		mQuery = i.getStringExtra("query");
		
		    
		// encoding the query
		try {
			playlistAPI = "https://api.twitch.tv/kraken/search/streams?q="+URLEncoder.encode(mQuery,"UTF-8")+"&limit=10";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		// Give API URLs
		API.clear();
		API.add(playlistAPI);
		

		// set a feed manager
		feedManager = new FeedManager_Twitch();
		
		// No header view in the list
		hasHeader = false;
		
		// set text in search field
		queryHint = "Search streams";
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}



	@Override
	public boolean onQueryTextSubmit(String query) {
		
		// encoding the query
		try {
			playlistAPI = "https://api.twitch.tv/kraken/search/streams?q="+URLEncoder.encode(query,"UTF-8")+"&limit=10";
			refreshActivity();
			
		} catch (UnsupportedEncodingException e) {

		}
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void refreshActivity() {

		redoRequest(playlistAPI, new FeedManager_Twitch());
	}
	
	@Override
	protected void setGridViewItemClickListener() {

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				openPlayer(0, mContext, position, false);
			}
		});
		
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
		PackageManager pm = this.getPackageManager();
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
	protected void forceSet() {
		isMoreVideos = false;
		
	}
}
