package com.examples.gg.loadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.examples.gg.adapters.EndlessScrollListener;
import com.examples.gg.adapters.VideoArrayAdapter;
import com.examples.gg.data.MyAsyncTask;
import com.examples.gg.feedManagers.FeedManager_Subscription;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.rs.dota.R;
import com.rs.dota.SideMenuActivity;

@SuppressLint({ "HandlerLeak", "NewApi" })
public class LoadMore_News extends LoadMore_Base{

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private boolean isEnd = false;
	private ViewGroup group;
	private ArrayList<String> matches = new ArrayList<String>();
	private ArrayList<String> results = new ArrayList<String>();
	private Elements links = new Elements();
	boolean isPagerSet = false;
	private getMatchInfo mMatchInfo;

	private View pagerContent;
	private View pagerLoading;
	private View pagerRetry;
	private View listLoading;
	private View listRetry;
	private String url = "http://www.gosugamers.net/dota2/gosubet";
	private AdvAdapter myAdvAdapter;

	private int position = 0;

	DisplayImageOptions options;

	private Random random;

	private SideMenuActivity sma;
	private final int[] myDrawables = new int[] { R.drawable.dota1,
			R.drawable.dota2, R.drawable.dota3, R.drawable.dota4,
			R.drawable.dota5, R.drawable.dota6, R.drawable.dota7,
			R.drawable.dota8, R.drawable.dota9, R.drawable.dota10,
			R.drawable.dota11, R.drawable.dota12, R.drawable.dota13,
			R.drawable.dota14, R.drawable.dota15, R.drawable.dota16,
			R.drawable.dota17, R.drawable.dota18, R.drawable.dota19,
			R.drawable.dota20, R.drawable.dota21, R.drawable.dota22,
			R.drawable.dota23, R.drawable.dota24, R.drawable.dota25,
			R.drawable.dota26, R.drawable.dota27, R.drawable.dota28,
			R.drawable.dota29, R.drawable.dota30, R.drawable.dota31,
			R.drawable.dota32, R.drawable.dota33, R.drawable.dota34,
			R.drawable.dota35, R.drawable.dota36, R.drawable.dota37,
			R.drawable.dota38, R.drawable.dota39, R.drawable.dota40,
			R.drawable.dota41, R.drawable.dota42, R.drawable.dota43,
			R.drawable.dota44, R.drawable.dota45, R.drawable.dota46,
			R.drawable.dota47, R.drawable.dota48, R.drawable.dota49,
			R.drawable.dota50, R.drawable.dota51, R.drawable.dota52,
			R.drawable.dota53, R.drawable.dota54, R.drawable.dota55,
			R.drawable.dota56, R.drawable.dota57, R.drawable.dota58,
			R.drawable.dota59, R.drawable.dota60, R.drawable.dota61,
			R.drawable.dota62, R.drawable.dota63, R.drawable.dota64,
			R.drawable.dota65, R.drawable.dota66, R.drawable.dota67,
			R.drawable.dota68, R.drawable.dota69, R.drawable.dota70,
			R.drawable.dota71, R.drawable.dota72, R.drawable.dota73,
			R.drawable.dota74, R.drawable.dota75, R.drawable.dota76,
			R.drawable.dota77, R.drawable.dota78, R.drawable.dota79,
			R.drawable.dota80, R.drawable.dota81, R.drawable.dota82,
			R.drawable.dota83, R.drawable.dota84, R.drawable.dota85,
			R.drawable.dota86, R.drawable.dota87, R.drawable.dota88,
			R.drawable.dota89, R.drawable.dota90, R.drawable.dota91,
			R.drawable.dota92, R.drawable.dota93, R.drawable.dota94,
			R.drawable.dota95, R.drawable.dota96, R.drawable.dota97,
			R.drawable.dota98, R.drawable.dota99, R.drawable.dota100,
			R.drawable.dota101, R.drawable.dota102, R.drawable.dota103 };

	private List<View> views = new ArrayList<View>();
	private int copyRands[];
	private int curPosPager = 0;
	private int rand_1;
	private int rand_2;

	@Override
	public void Initializing() {
		// Inflating view
		view = mInflater.inflate(R.layout.whatsnew, null);
		gv = (GridView) view.findViewById(R.id.gridview);
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(sfa, YoutubeActionBarActivity.class);
//				i.putExtra("isfullscreen", true);
				i.putExtra("video", videolist.get(position));
				startActivity(i);
			}
		});
		// Give a title for the action bar
		abTitle = "What's New";

		// Give API URLs
		// API.add("https://gdata.youtube.com/feeds/api/users/cpGJHANGum7tFm0kg6fh7g/newsubscriptionvideos?max-results=10&alt=json");
		API.add("https://gdata.youtube.com/feeds/api/users/cpGJHANGum7tFm0kg6fh7g/newsubscriptionvideos?max-results=10&alt=json");

		// API.add("http://youtube-rss.f-y.name/rss/55590a90-dcb3-11e3-a44f-0401096cca01/?alt=json");
		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		// Get sidemenuactivity
		sma = (SideMenuActivity) sfa;

		random = new Random();

	}


	@Override
	public void setListView() {

		pagerContent = sfa.findViewById(R.id.pageContent);
		pagerLoading = sfa.findViewById(R.id.pagerLoadingIndicator);
		pagerRetry = sfa.findViewById(R.id.pagerRetryView);
		listLoading = sfa.findViewById(R.id.listViewLoadingIndicator);
		listRetry = sfa.findViewById(R.id.ListViewRetryView);

		// myLoadMoreListView = (LoadMoreListView) this.getListView();
		// myLoadMoreListView.setDivider(null);

		// setBannerInHeader();

		vaa = new VideoArrayAdapter(sfa, videolist, imageLoader);
		gv.setAdapter(vaa);

		// reduce lag for gridview
		boolean pauseOnScroll = false;
		boolean pauseOnFling = true;
		PauseOnScrollListener pauseListener = new PauseOnScrollListener(
				imageLoader, pauseOnScroll, pauseOnFling);
		gv.setOnScrollListener(pauseListener);

		if (isMoreVideos) {
			gv.setOnScrollListener(new EndlessScrollListener() {

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					// // Do the work to load more items at the end of
					// // list

					if (isMoreVideos == true) {
						// new LoadMoreTask().execute(API.get(0));
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(
								LoadMoreTask.LOADMORETASK, myLoadMoreListView,
								listLoading, listRetry);
						newTask.execute(API.get(API.size() - 1));
						mLoadMoreTasks.add(newTask);
					}
				}

			});

		} else {
			gv.setOnScrollListener(null);
		}

		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			// show loading screen
			doRequest();
		}

		initViewPager();

	}

	private void setViewPager() {
		// SharedPreferences tipsPrefs = getSherlockActivity()
		// .getSharedPreferences("Tips", 0);
		// String json = tipsPrefs.getString("json", "");
		// Gson gson = new Gson();
		// Type listType = new TypeToken<ArrayList<Tip>>() {
		// }.getType();
		//
		// if (!json.equals("")) {
		//
		// final ArrayList<Tip> tips = gson.fromJson(json, listType);
		// // Log.d("debug", "Number of tips is " + tips.size());
		//
		// final int numOfViews = 2;
		// int myRandom = random.nextInt(tips.size() - 1);
		// int lastRand = -1;
		// copyRands = new int[2];
		// for (int i = 0; i < numOfViews; i++) {
		// while (myRandom == lastRand)
		// myRandom = random.nextInt(tips.size() - 1);
		// lastRand = myRandom;
		// copyRands[i] = myRandom;
		// View tipView = new View(sfa);
		//
		// final LayoutInflater inflater = (LayoutInflater) sfa
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//
		// tipView = inflater.inflate(R.layout.tipsinpager, null, false);
		//
		// TextView tipTitle = (TextView) tipView.findViewById(R.id.title);
		// TextView tip = (TextView) tipView.findViewById(R.id.tip);
		//
		// tipTitle.setText(tips.get(myRandom).getTitle());
		// tip.setText(tips.get(myRandom).getContent());
		//
		// // Set up click event
		// tipView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // Set the drawer indicator in position
		// // "Upcoming Matches"
		// // sma.setDrawerIndicator(9);
		//
		// // Replacing the current fragment
		// // FragmentTransaction ft = getFragmentManager()
		// // .beginTransaction();
		// // ft.replace(R.id.content_frame,
		// // new LoadMore_UpcomingMatch());
		// // ft.commit();
		// Intent i = new Intent(sfa, TipViewerActivity.class);
		// i.putExtra("tip", tips.get(copyRands[curPosPager]));
		// startActivity(i);
		// }
		// });
		//
		// views.add(tipView);
		// }
		// }

		String[] matcharray = matches.toArray(new String[matches.size()]);
		String[] resultarray = results.toArray(new String[results.size()]);

		View v1 = new View(sfa);
		View v2 = new View(sfa);
		final LayoutInflater inflater = (LayoutInflater) sfa
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v1 = inflater.inflate(R.layout.livetext, null, false);
		// v1.setBackgroundResource(myDrawables[rand_1]);

		ImageView bkgImg1 = (ImageView) v1.findViewById(R.id.thumbnail);
		TextView liveTitle = (TextView) v1.findViewById(R.id.livetitle);
		TextView liveMatch1 = (TextView) v1.findViewById(R.id.lineup1);
		TextView liveMatch2 = (TextView) v1.findViewById(R.id.lineup2);
		TextView liveMatch3 = (TextView) v1.findViewById(R.id.lineup3);
		TextView live1 = (TextView) v1.findViewById(R.id.live1);
		TextView live2 = (TextView) v1.findViewById(R.id.live2);
		TextView live3 = (TextView) v1.findViewById(R.id.live3);

		bkgImg1.setImageResource(myDrawables[rand_1]);
		liveTitle.setText("Upcoming Matches");

		if (matcharray.length >= 1) {
			if (matcharray[0].toLowerCase().endsWith("live")) {
				liveMatch1.setText(matcharray[0].substring(0,
						matcharray[0].length() - 4));
			} else {
				liveMatch1.setText(matcharray[0]);
				live1.setVisibility(View.GONE);
			}
		} else {
			liveMatch1.setVisibility(View.GONE);
			live1.setVisibility(View.GONE);
		}
		// System.out.println(matcharray[0]);

		if (matcharray.length >= 2) {
			if (matcharray[1].toLowerCase().endsWith("live")) {
				liveMatch2.setText(matcharray[1].substring(0,
						matcharray[1].length() - 4));
			} else {
				liveMatch2.setText(matcharray[1]);
				live2.setVisibility(View.GONE);
			}
		} else {
			liveMatch2.setVisibility(View.GONE);
			live2.setVisibility(View.GONE);
		}

		if (matcharray.length >= 3) {
			if (matcharray[2].toLowerCase().endsWith("live")) {
				liveMatch3.setText(matcharray[2].substring(0,
						matcharray[2].length() - 4));
			} else {
				liveMatch3.setText(matcharray[2]);
				live3.setVisibility(View.GONE);
			}
		} else {
			liveMatch3.setVisibility(View.GONE);
			live3.setVisibility(View.GONE);
		}

		v1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Set the drawer indicator in position "Upcoming Matches"
				sma.setDrawerIndicator(11);

				// Replacing the current fragment
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_UpcomingMatch());
				ft.commit();
			}
		});

		views.add(v1);

		v2 = inflater.inflate(R.layout.livetext, null, false);
		// v2.setBackgroundResource(myDrawables[rand_2]);
		ImageView bkgImg2 = (ImageView) v2.findViewById(R.id.thumbnail);
		liveTitle = (TextView) v2.findViewById(R.id.livetitle);
		liveMatch1 = (TextView) v2.findViewById(R.id.lineup1);
		liveMatch2 = (TextView) v2.findViewById(R.id.lineup2);
		liveMatch3 = (TextView) v2.findViewById(R.id.lineup3);
		live1 = (TextView) v2.findViewById(R.id.live1);
		live2 = (TextView) v2.findViewById(R.id.live2);
		live3 = (TextView) v2.findViewById(R.id.live3);

		bkgImg2.setImageResource(myDrawables[rand_2]);
		liveTitle.setText("Recent Results");

		if (resultarray.length >= 1) {
			liveMatch1.setText(resultarray[0]);
		} else {
			liveMatch1.setVisibility(View.GONE);
		}
		live1.setVisibility(View.GONE);

		if (resultarray.length >= 2) {
			liveMatch2.setText(resultarray[1]);
		} else {
			liveMatch2.setVisibility(View.GONE);
		}
		live2.setVisibility(View.GONE);

		if (resultarray.length >= 3) {
			liveMatch3.setText(resultarray[2]);
		} else {
			liveMatch3.setVisibility(View.GONE);
		}
		live3.setVisibility(View.GONE);

		v2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Set the drawer indicator in position "Recent Result"
				sma.setDrawerIndicator(12);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_Result());
				ft.commit();
			}
		});

		views.add(v2);
	}

	private void initViewPager() {

		rand_1 = random.nextInt(myDrawables.length - 1);
		// System.out.println("New random:"+ rand_1);
		do {
			rand_2 = random.nextInt(myDrawables.length - 1);
		} while (rand_1 == rand_2);

		if (!isPagerSet) {
			advPager = (ViewPager) sfa.findViewById(R.id.adv_pager);
			group = (ViewGroup) sfa.findViewById(R.id.viewGroup);
		} else {
			views.clear();
		}

		setViewPager();

		if (!isPagerSet) {

			imageViews = new ImageView[views.size()];
			for (int i = 0; i < views.size(); i++) {
				imageView = new ImageView(sfa);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageView.setPadding(5, 5, 5, 5);
				imageViews[i] = imageView;
				if (i == 0) {
					imageViews[i].setBackgroundResource(R.drawable.d2_selected);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.d2_unselected);
				}
				group.addView(imageViews[i]);
			}

			advPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						isContinue = false;
						break;
					case MotionEvent.ACTION_UP:
						isContinue = true;
						break;
					default:
						isContinue = true;
						break;
					}
					return false;
				}
			});

			handler.postDelayed(runnable, 10000);
			isPagerSet = true;
		}

		myAdvAdapter = new AdvAdapter();

		advPager.setAdapter(myAdvAdapter);

		advPager.setOnPageChangeListener(new GuidePageChangeListener());

		advPager.setCurrentItem(0);

		imageViews[0].setBackgroundResource(R.drawable.d2_selected);
		imageViews[1].setBackgroundResource(R.drawable.d2_unselected);

	}

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			if (position == 0) {
				position = 1;
			} else {
				position = 0;
			}
			advPager.setCurrentItem(position, true);
			// refreshFragment();
			handler.postDelayed(runnable, 10000);
		}
	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			curPosPager = arg0;
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.d2_selected);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.d2_unselected);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		// private List<View> views = null;

		public AdvAdapter() {
			// this.views = views;
		}

		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			// ((ViewPager) arg0).removeView(views.get(arg1));
			collection.removeView((View) view);
			view = null;
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(ViewGroup collection, int position) {

			collection.addView(views.get(position), 0);
			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	private class getMatchInfo extends MyAsyncTask {

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

					mMatchInfo = (getMatchInfo) new getMatchInfo(type,
							contentView, loadingView, retryView);
					mMatchInfo.execute(url);
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
			links = doc.select("tr:has(span.opp)");
			if (!links.isEmpty()) {

				for (Element link : links) {

					String match;

					match = link.select("span.opp").first().text().trim()
							+ " vs "
							+ link.select("span.opp").get(1).text().trim()
							+ " ";
					if (link.select("span.hidden").isEmpty()) {
						if (link.select("td").get(1).text().trim()
								.toLowerCase().matches("live")
								|| link.select("td").get(1).text().trim()
										.matches("")) {
							// Game is live, append "live" text to it
							match += "Live";
						} else {
							// Game is not live
							match += link.select("td").get(1).text().trim();
						}

						matches.add(match);
					} else {
						match += link.select("span.hidden").first().text()
								.trim();
						results.add(match);
					}
				}

			} else {
				handleCancelView();
			}
		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..
				try {
					 initViewPager();
				} catch (Exception e) {

				}
				DisplayView(contentView, retryView, loadingView);

			} else {
				handleCancelView();
			}

		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		mMatchInfo = new getMatchInfo(getMatchInfo.INITTASK, pagerContent,
				pagerLoading, pagerRetry);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		} else {
			mMatchInfo.execute(url);
		}

		for (String s : API) {
			LoadMoreTask newTask = new LoadMoreTask(LoadMoreTask.INITTASK,
					myLoadMoreListView, listLoading, listRetry);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				newTask.execute(s);
			}

			mLoadMoreTasks.add(newTask);

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {

		super.onDestroy();

		views.clear();
		// advPager.removeAllViews();
		// group.removeAllViews();
		// advPager = null;
		isEnd = true;
		// group = null;
		// myThread.interrupt();

		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}

		if (mMatchInfo != null && mMatchInfo.getStatus() == Status.RUNNING)
			mMatchInfo.cancel(true);

	}


}
