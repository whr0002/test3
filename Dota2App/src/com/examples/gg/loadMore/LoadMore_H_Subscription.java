package com.examples.gg.loadMore;

import com.examples.gg.feedManagers.FeedManager_Subscription;


public class LoadMore_H_Subscription extends LoadMore_Base {

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Highlights";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json");
		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
//		FragmentPlaylist = new LoadMore_H_New_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu		
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		currentPosition = 0;
		// Set retry button listener
		

	}

}
