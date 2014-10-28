package com.examples.gg.loadMore;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;

import com.examples.gg.adapters.MatchArrayAdapter;
import com.examples.gg.data.Match;
import com.examples.gg.data.MyAsyncTask;
import com.examples.gg.data.Video;
import com.rs.dota.R;

public class LoadMore_MatchInfo_Base extends LoadMore_Base{
	
	protected ArrayList<MatchLoadMoreTask> mLoadMoreTasks = new ArrayList<MatchLoadMoreTask>();
	protected MatchArrayAdapter mArrayAdatper;
	protected ArrayList<Match> matchArray = new ArrayList<Match>();
	
	
	class MatchLoadMoreTask extends MyAsyncTask {

		public MatchLoadMoreTask(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
		}

		@Override
		public void handleCancelView() {
//			((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

			if (isException) {

				DisplayView(retryView, contentView, loadingView);
			}

		}

		@Override
		public void setRetryListener(final int type) {
			mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);

			mRetryButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					MatchLoadMoreTask newTask = (MatchLoadMoreTask) new MatchLoadMoreTask(
							type, contentView, loadingView, retryView);
					newTask.DisplayView(loadingView, contentView, retryView);
					newTask.execute(API.get(API.size() - 1));
					mLoadMoreTasks.add(newTask);

				}
			});

		}

		@Override
		protected void onPostExecute(String result) {
			// Do anything with response..
			// System.out.println(result);

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..
				try {
					feedManager.setmJSON(result);

					List<Video> newVideos = feedManager.getVideoPlaylist();
//					Log.d("debug", "size of list: " + newVideos.size());
					// adding new loaded videos to our current video list
					for (Video v : newVideos) {
						// System.out.println("new id: " + v.getVideoId());
						if (needFilter) {
							filtering(v);
							// System.out.println("need filter!");
						} else {
							titles.add(v.getTitle());
							videolist.add(v);
						}
					}

					// put the next API in the first place of the array
					API.add(feedManager.getNextApi());
					// nextAPI = feedManager.getNextApi();
					if (API.get(API.size() - 1) == null) {
						// No more videos left
						isMoreVideos = false;
					}
				} catch (Exception e) {

				}
				mArrayAdatper.notifyDataSetChanged();

				// loading done
				DisplayView(contentView, retryView, loadingView);
				if (!isMoreVideos) {
					gv.setOnScrollListener(null);
				}

			} else {
				handleCancelView();
			}

		}

	}
}
