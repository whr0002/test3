package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.examples.gg.adapters.VideoArrayAdapter;
import com.examples.gg.data.Video;
import com.examples.gg.feedManagers.FeedManager_Base_v3_PlaylistItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.dota.R;

//import com.examples.gg.twitchplayers.VideoBuffer;

public class SubscriptionFragment extends FavoritesFragment {

	private FeedManager_Base_v3_PlaylistItem mFeedManager;

	private String browserKey;
	protected int numOfTasks;

	@Override
	public void Initializing() {
		super.Initializing();
		abTitle = "Subscriptions";
		browserKey = sfa.getResources().getString(R.string.browserKey);
	}

	@Override
	public void setDropdown() {

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] catagory = { "Recent", "Channels" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ab.getThemedContext(), R.layout.sherlock_spinner_item,
				android.R.id.text1, catagory);

		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		ab.setListNavigationCallbacks(adapter, this);

		ab.setSelectedNavigationItem(currentPosition);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void setFavoriteVideos(ArrayList<Video> vl) {
		int channelCounter = 0;
		Gson gson = new Gson();

		SharedPreferences favoritePrefs = sfa.getSharedPreferences("Favorites",
				0);
		ArrayList<Video> videos;
		String result = favoritePrefs.getString("json", "");
		if (result.equals("")) {
			// Subs is empty
			vl = new ArrayList<Video>();

		} else {
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>() {
			}.getType();
			videos = gson.fromJson(favoritePrefs.getString("json", ""),
					listType);

			numOfTasks = 0;
			for (int i = videos.size() - 1; i >= 0; i--) {
				Video v = videos.get(i);
				if (searchType == 0 && v.isChannel) {
					// Get sub channels, do second task
					SecondTask aTask = new SecondTask(LoadMoreTask.INITTASK,
							myLoadMoreListView, fullscreenLoadingView,
							mRetryView, v.getRecentVideoUrl());
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						aTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
								v.getRecentVideoUrl());
					} else {
						aTask.execute(v.getRecentVideoUrl());
					}
					mLoadMoreTasks.add(aTask);
					// new SecondTask(LoadMoreTask.INITTASK, myLoadMoreListView,
					// fullscreenLoadingView, mRetryView,
					// v.getRecentVideoUrl()).execute(v
					// .getRecentVideoUrl());
					numOfTasks += 1;
					channelCounter++;

				} else if (searchType == 1 && v.isChannel) {
					// only show channels
					vl.add(v);
					channelCounter++;
				}

			}

		}
		if(channelCounter == 0){
			// Show message
			showMessage();		
		}
	}
	
	public void showMessage(){
		TextView message;
		message = (TextView) sfa.findViewById(R.id.message);
		message.setVisibility(View.VISIBLE);
	}

	public class SecondTask extends LoadMoreTask {
		public SecondTask(int type, View contentView, View loadingView,
				View retryView, String api) {
			super(type, contentView, loadingView, retryView, api);
			this.mRecentAPI = api;
		}

		private String mRecentAPI;

		@Override
		protected void onPostExecute(String result) {
			// Log.d("debug", result);
			if (!taskCancel && result != null) {
				// Do anything with response..
				try {
					mFeedManager = new FeedManager_Base_v3_PlaylistItem(
							"video", mRecentAPI, browserKey, gv, numOfResults);
					mFeedManager.setmJSON(result);
					videolist.addAll(mFeedManager.getVideoPlaylist());
					if (numOfTasks > 0) {
						numOfTasks--;
					}

				} catch (Exception e) {

				}

				if (numOfTasks == 0) {
					doSort(videolist);
					DisplayView(contentView, retryView, loadingView);
					gv.setOnScrollListener(null);
				}

			} else {
				handleCancelView();
			}

		}

	}

	public void doSort(ArrayList<Video> vl) {
		// Get rid of useless videos according to time
		ArrayList<Video> minuteList = eleminateVideos(0, vl);
		ArrayList<Video> hourList = eleminateVideos(1, vl);
		ArrayList<Video> dayList = eleminateVideos(2, vl);
		ArrayList<Video> weekList = eleminateVideos(3, vl);
		ArrayList<Video> monthList = eleminateVideos(4, vl);
		ArrayList<Video> yearList = eleminateVideos(5, vl);

//		Collections.shuffle(minuteList);
//		Collections.shuffle(hourList);
//		Collections.shuffle(dayList);
//		Collections.shuffle(weekList);
//		Collections.shuffle(monthList);
//		Collections.shuffle(yearList);
		// Adding sub list to full list
		vl.clear();
		vl.addAll(minuteList);
		vl.addAll(hourList);
		vl.addAll(dayList);
		vl.addAll(weekList);
		vl.addAll(monthList);
		vl.addAll(yearList);

	}

	public ArrayList<Video> eleminateVideos(int type, ArrayList<Video> vl) {
		ArrayList<Video> tempList = new ArrayList<Video>();
		for (Video v : vl) {

			String updateTime = v.getUpdateTime();

			if (updateTime != null) {

				switch (type) {
				case 0:
					// videos which contain minute left
					if (updateTime.contains("minute"))
						tempList.add(v);
					break;

				case 1:
					// videos which contain hour left
					if (updateTime.contains("hour"))
						tempList.add(v);
					break;

				case 2:
					// videos which contain day left
					if (updateTime.contains("day"))
						tempList.add(v);
					break;

				case 3:
					// videos which contain week left
					if (updateTime.contains("week"))
						tempList.add(v);
					break;

				case 4:
					// videos which contain month left
					if (updateTime.contains("month"))
						tempList.add(v);
					break;

				case 5:
					// videos which contain year left
					if (updateTime.contains("year"))
						tempList.add(v);
					break;

				default:
					break;
				}

			}
		}

		return new ArrayList<Video>(tempList);

	}
	
	@Override
	public void setListView() {

		vaa = new VideoArrayAdapter(sfa, videolist, imageLoader);
		gv.setAdapter(vaa);

		// Get the favorites
		setFavoriteVideos(videolist);
		// Refresh adapter
		vaa.notifyDataSetChanged();
	}
}
