package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.adapters.VaaForHistory;
import com.examples.gg.data.CustomSearchView;
import com.examples.gg.data.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.dota.R;

public class HistoryFragment extends FavoritesFragment {
	private VaaForHistory vaah;
	@Override
	public void Initializing() {
		abTitle = "History";
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getSherlockActivity());
		
		setHasOptionsMenu(true);
		setOptionMenu(true, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		searchView = new CustomSearchView(ab.getThemedContext());
		searchView.setQueryHint("Search Youtube");
		searchView.setOnQueryTextListener(this);

		menu.add(0, 20, 0, "Search")
				.setIcon(R.drawable.abs__ic_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
		menu.add(0, 19, 0, "Delete").setIcon(R.drawable.trash)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		switch (item.getItemId()) {

		case 0:
			// Menu option 1
			// Toast.makeText(sfa, "Refreshing", Toast.LENGTH_SHORT).show();
			refreshFragment();
			break;

		case 19:
			openDeleteDialog();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		ft.commit();

		return true;
	}
	
	private void emptyHistory() {
		SharedPreferences historyPrefs = sfa.getSharedPreferences("History",
				0);
		SharedPreferences.Editor editor= historyPrefs.edit();
		editor.putString("json", null);
		editor.commit();
		videolist.clear();
		vaah.notifyDataSetChanged();
	}

	private void openDeleteDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				sfa);
 
			// set title
			alertDialogBuilder.setTitle("Delete all history?");
 
			// set dialog message
			alertDialogBuilder
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// Delete all history
						emptyHistory();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		
	}

	@Override
	public void setFavoriteVideos(ArrayList<Video> vl) {
		Gson gson = new Gson();

		SharedPreferences favoritePrefs = sfa.getSharedPreferences("History",
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
				vl.add(v);

			}

		}

	}
	
	@Override
	public void setListView() {

		vaah = new VaaForHistory(sfa, videolist, imageLoader,
				HistoryFragment.this);
		gv.setAdapter(vaah);

		// Get the favorites
		setFavoriteVideos(videolist);
		// Refresh adapter
		vaah.notifyDataSetChanged();
		// printVideoLog(videolist);
	}
	
	@Override
	public void onCallback(Video v) {
		// Log.d("debug", "called back");
		removeTheVideo(videolist, v);
		vaah.notifyDataSetChanged();

	}
	
	@Override
	public void setDropdown() {

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

	}
	
//	@Override
//	protected void setGridViewItemClickListener() {
//		gv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					final int position, long id) {
//				Video v = videolist.get(position);
//
//				if (v.isVideo) {
//					// This is a video
//					Intent i = new Intent(sfa, YoutubeActionBarActivity.class);
////					i.putExtra("isfullscreen", true);
//					i.putExtra("video", videolist.get(position));
//					startActivity(i);
//				}
//			}
//		});
//	}

}
