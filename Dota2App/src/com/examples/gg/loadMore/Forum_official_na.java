package com.examples.gg.loadMore;



public class Forum_official_na extends Forum_fragment {

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Forums";

		// Give API URLs

		// initialize the fragments in the Menu
		FragmentAll = new Forum_reddit();
		FragmentUploader = new Forum_official_na();
//		FragmentPlaylist = new LoadMore_H_New_Playlist();

		// set a feed manager

		// Show menu		
		setHasOptionsMenu(true);
		setOptionMenu(true, true);

		currentPosition = 1;
		// Set retry button listener		
		mUri = "http://forums.na.leagueoflegends.com/board/";

	}
	

}
