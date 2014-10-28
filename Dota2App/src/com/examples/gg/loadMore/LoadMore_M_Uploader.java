package com.examples.gg.loadMore;

import android.view.View;

import com.examples.gg.feedManagers.FeedManager_Uploader;
import com.rs.dota.R;

public class LoadMore_M_Uploader extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// show loading screen
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
				View.VISIBLE);

		// Give a title for the action bar
		abTitle = "Matches";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/GJoABYYxwoGsl6TuP0DGnw/subscriptions?v=2&max-results=10&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_M_Subscription();
		FragmentUploader = new LoadMore_M_Uploader();
//		FragmentPlaylist = new LoadMore_M_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Uploader();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, true);
		
		// Set retry button listener
		currentPosition = 1;
		
	}

	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment

}
