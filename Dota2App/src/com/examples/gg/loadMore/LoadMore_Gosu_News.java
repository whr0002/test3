package com.examples.gg.loadMore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.examples.gg.adapters.EndlessScrollListener;
import com.examples.gg.adapters.NewsArrayAdapter;
import com.examples.gg.data.News;
import com.rs.dota.R;


public class LoadMore_Gosu_News extends LoadMore_Base {
	private ArrayList<News> mNews = new ArrayList<News>();

	private NewsArrayAdapter mArrayAdatper;
	private getMatchInfo mgetMatchInfo;
	private int pageNum;
	private final String baseUri = "http://www.gosugamers.net";

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Latest News";

		// Give API URLs
		API.add("http://www.gosugamers.net/dota2/news/archive");

		pageNum = 1;

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, true);
		
		currentPosition = 1;

	}
	@Override
	public void setDropdown() {
		if (hasDropDown) {

			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

			final String[] catagory = { "JD", "GG" };

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					mActionBar.getThemedContext(),
					R.layout.sherlock_spinner_item, android.R.id.text1,
					catagory);

			adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

			mActionBar.setListNavigationCallbacks(adapter, this);

			mActionBar.setSelectedNavigationItem(currentPosition);

		} else {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		if (firstTime) {
			firstTime = false;
			return true;
		}

		FragmentTransaction ft = getFragmentManager().beginTransaction();

		switch (itemPosition) {

		case 0:
			// Menu option 1
			ft.replace(R.id.content_frame, new LoadMore_WorkoutNews(
					"http://www.joindota.com/en/start",
					"http://beta.na.leagueoflegends.com", 0));
			break;

		case 1:
			// Menu option 2
			ft.replace(R.id.content_frame, new LoadMore_Gosu_News());
			break;


		}

		ft.commit();

		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void refreshFragment() {
		String firstApi = API.get(0);
		API.clear();
		API.add(firstApi);
		isMoreVideos = true;
		pageNum = 1;
		mNews.clear();
		setListView();
	}

	@Override
	public void setListView() {

//		myLoadMoreListView = (LoadMoreListView) this.getListView();
		// myLoadMoreListView.setDivider(null);

		//setBannerInHeader();

		mArrayAdatper = new NewsArrayAdapter(sfa, mNews);
//		setListAdapter(mArrayAdatper);
		gv.setAdapter(mArrayAdatper);
		
		if (isMoreVideos) {
			gv.setOnScrollListener(new EndlessScrollListener(){

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
//					// Do the work to load more items at the end of
//					// list

					if (isMoreVideos == true) {
						// new LoadMoreTask().execute(API.get(0));
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(
								LoadMoreTask.LOADMORETASK, gv,
								fullscreenLoadingView, mRetryView);
						newTask.execute(API.get(API.size() - 1));
						mLoadMoreTasks.add(newTask);}}
					
				});

		} else {
			gv.setOnScrollListener(null);
		}

		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			// show loading screen
			doRequest();
		}

	}

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//
//		// Toast.makeText(this.getSherlockActivity(),
//		// matchArray.get(position).getGosuLink(), Toast.LENGTH_SHORT)
//		// .show();
//
//		// Intent i = new Intent(this.getSherlockActivity(),
//		// MatchDetailsActivity.class);
//		// i.putExtra("match", matchArray.get(position - 1));
//		// startActivity(i);
//
//		String url = mNews.get(position).getLink();
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setData(Uri.parse(url));
//		startActivity(i);
////		startActivity(Intent.createChooser(i, "Choose a browser"));
//
//	}
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				String url = mNews.get(position).getLink();
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);
				String url = mNews.get(position).getLink();
				Intent i = new Intent(sfa,NewsViewerActivity.class);
				i.putExtra("uri", url);
				startActivity(i);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		// System.out.println("DO!!!!!");
		for (String s : API) {
			mgetMatchInfo = new getMatchInfo(getMatchInfo.INITTASK,
					gv, fullscreenLoadingView, mRetryView);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mgetMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						s);
			} else {
				mgetMatchInfo.execute(s);
			}
		}
	}

	private class getMatchInfo extends LoadMoreTask {

		public getMatchInfo(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setRetryListener(final int type) {
			mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);

			mRetryButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					mgetMatchInfo = (getMatchInfo) new getMatchInfo(type,
							contentView, loadingView, retryView);
					mgetMatchInfo.DisplayView(loadingView, contentView,
							retryView);
					mgetMatchInfo.execute(API.get(API.size() - 1));

				}
			});

		}

		@Override
		public String doInBackground(String... uri) {

			super.doInBackground(uri[0]);

			if (!taskCancel && responseString != null) {
				try {
					pull(responseString);
				} catch (Exception e) {

				}
			}

			return responseString;
		}

		private void pull(String responseString) {
			Document doc = Jsoup.parse(responseString);
			// get all links
			Elements links = new Elements();
			links = doc.select("tr:has(td)");
			if (!links.isEmpty()) {
				String href = "";
				String newsTitle = "";
				String date = "";
				for (Element link : links) {

					// get the value from href attribute
					href = link.select("a").first().attr("href");
					newsTitle = link.select("a").first().text();
					date = link.select("td").get(1).text();
					if (href.contains("news")) {

						News aNews = new News();
						aNews.setLink(baseUri + href);
						aNews.setTitle(newsTitle);
						aNews.setDate(processDate(date));
						mNews.add(aNews);
					}
				}

			}

//			Elements pages = new Elements();
//			pages = doc.select("div.pages");
//			if (pages != null) {
//				Elements page_indicators = pages.select("a");
//				if (page_indicators != null) {
//					isMoreVideos = false;
//
//					if ((page_indicators.size() > 7) || (pageNum == 1)) {
//						isMoreVideos = true;
//						pageNum++;
//						API.add("http://www.gosugamers.net/dota2/news/archive?page="
//								+ pageNum);
//					} else {
//						isMoreVideos = false;
//					}
//				}
//			}else{
//				isMoreVideos = false;
//			}
			isMoreVideos = false;
		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				mArrayAdatper.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task has
				// finished
//				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				// loading done
				DisplayView(contentView, retryView, loadingView);

				if (!isMoreVideos) {
//					((LoadMoreListView) myLoadMoreListView).onNoMoreItems();

					gv.setOnScrollListener(null);
				}

			} else {

				handleCancelView();
			}

		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mgetMatchInfo != null
				&& mgetMatchInfo.getStatus() == Status.RUNNING)
			mgetMatchInfo.cancel(true);

	}

	@SuppressLint("SimpleDateFormat")
	public String processDate(String s) {
		Date today = new Date();
		Date pastDate = new Date();
		// Calendar c = new Calendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));

		try {
			pastDate = sdf.parse(s);
//			today = sdf.parse(sdf.format(today));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

//		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		sdf.setTimeZone(TimeZone.getDefault());
		

		return calculateDateDifference(today,pastDate);

	}
	
	private String calculateDateDifference(Date today, Date past) {
		long diff = today.getTime() - past.getTime();
		// System.out.println("diff: " + diff);
		long diffSec = (diff / 1000L) % 60L;
		long diffMin = (diff / (60L * 1000L)) % 60L;
		long diffHour = (diff / (60L * 60L * 1000L)) % 24L;
		long diffDay = (diff / (24L * 60L * 60L * 1000L)) % 30L;
		long diffWeek = (diff / (7L * 24L * 60L * 60L * 1000L)) % 7L;
		long diffMonth = (diff / (30L * 24L * 60L * 60L * 1000L)) % 12L;
		long diffYear = (diff / (12L * 30L * 24L * 60L * 60L * 1000L));

		if (diffYear == 1) {
			return diffYear + " year ago";
		} else if (diffYear > 1) {
			return diffYear + " years ago";
		} else {
			// less than 1 year
			if (diffMonth == 1) {
				return diffMonth + " month ago";
			} else if (diffMonth > 1) {
				return diffMonth + " months ago";
			} else {
				// less than 1 week
				if (diffWeek == 1) {
					return diffWeek + " week ago";
				} else if (diffWeek > 1) {
					return diffWeek + " weeks ago";
				} else {
					// less than 1 month
					if (diffDay == 1) {
						return diffDay + " day ago";
					} else if (diffDay > 1) {
						return diffDay + " days ago";
					} else {

						// less than 1 day
						if (diffHour == 1) {
							return diffHour + " hour ago";
						} else if (diffHour > 1) {
							return diffHour + " hours ago";
						} else {
							// less than 1 hour
							if (diffMin == 1) {
								return diffMin + " minute ago";
							} else if (diffMin > 1) {
								return diffMin + " minutes ago";
							} else {
								// less than 1 minute
								if (diffSec == 0 || diffSec == 1) {
									return diffSec + " second ago";
								} else if (diffSec > 1) {
									return diffSec + " seconds ago";
								}
							}
						}
					}
				}
			}
		}

		return "";
	}

}
