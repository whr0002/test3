package com.examples.gg.loadMore;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.adapters.EndlessScrollListener;
import com.examples.gg.adapters.VideoArrayAdapter;
import com.examples.gg.data.Video;

public class LoadMore_Base_UP extends LoadMore_Base {

	protected LoadMore_Base mLoadMore;
	protected String nextFragmentAPI;
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Putting the current fragment into stack for later call back
				// get the API corresponding to the item selected
				Video v = videolist.get(position);
				v.setAsChannel();
				
				nextFragmentAPI = videolist.get(position).getRecentVideoUrl();
				String title = videolist.get(position).getTitle();
				String url = videolist.get(position).getThumbnailUrl();

				Intent i = new Intent(sfa,
						LoadMore_Activity_Channel.class);
				i.putExtra("API", nextFragmentAPI);
				i.putExtra("PLAYLIST_API", videolist.get(position).getPlaylistsUrl());
				i.putExtra("title", title);
				i.putExtra("thumbnail", url);
				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			sfa.getSupportFragmentManager().popBackStack();
		}

		return super.onOptionsItemSelected(item);
	}

	public void InitializingNextFragment() {

	}

}
