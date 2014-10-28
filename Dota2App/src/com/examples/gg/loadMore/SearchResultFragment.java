package com.examples.gg.loadMore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.examples.gg.feedManagers.FeedManager_Search_Youtube;


public class SearchResultFragment extends LoadMore_Base{
	
	private String mQuery;
	private String mTitle;
	
	public SearchResultFragment(String query, String title){
		this.mQuery = query;
		this.mTitle = title;
	}
	
	@Override
	public void Initializing() {
		if(mTitle != null){
			abTitle = mTitle;
		}
		
		// Get the query for search
		if(mQuery != null){
			// Add the complete API
			String cAPI = buildAPI(mQuery);
			if(cAPI != null){
				API.add(cAPI);
			}
		}
		// set a feed manager
		feedManager = new FeedManager_Search_Youtube();
		
		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);
		
	}
	
	
	public String buildAPI(String q){
		String api = null;
		try {
			api = "https://gdata.youtube.com/feeds/api/videos?q="+URLEncoder.encode(q,"UTF-8")+"&orderby=relevance&start-index=1&max-results=10&v=2&alt=json";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return api;
	}
}
