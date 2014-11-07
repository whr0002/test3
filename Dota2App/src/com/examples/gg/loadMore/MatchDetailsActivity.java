package com.examples.gg.loadMore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.examples.gg.data.Match;
import com.examples.gg.data.MyAsyncTask;
import com.examples.gg.data.Video;
import com.examples.gg.notificationservice.AlarmService;
import com.examples.gg.settings.FlashInstallerActivity;
import com.examples.gg.twitchplayers.TwitchPlayer;
import com.examples.gg.twitchplayers.VideoBuffer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rs.dota.R;

public class MatchDetailsActivity extends SherlockListActivity {

	private ActionBar mActionBar;
	private Match match;
	private String baseUrl = "http://www.gosugamers.net";
	protected View fullscreenLoadingView;
	private Activity mActivity;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	DisplayImageOptions options;
	private ArrayList<String> lives = new ArrayList<String>();
	private ArrayList<String> videoIds = new ArrayList<String>();
	private View mRetryView;
	private TextView myTimer;
	private View contentLayout;
	private getMatchDetails mMatchDetails;
	private Date gameStartDate;
	private Random rand;
	private int mRandNum;
	private String tName1;
	private String tName2;
	private SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchdisplay);

		Intent intent = getIntent();
		match = intent.getParcelableExtra("match");

		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setTitle("Match Details");

		contentLayout = findViewById(R.id.contentLayout);

		fullscreenLoadingView = findViewById(R.id.fullscreen_loading_indicator);

		// imageLoader.init(ImageLoaderConfiguration.createDefault(activityContext));
		//
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading)
				.showImageOnFail(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.build();

		mActivity = this;

		// Get Retry view
		mRetryView = mActivity.findViewById(R.id.mRetry);

		// Set a listener for the button Retry
		mMatchDetails = new getMatchDetails(MyAsyncTask.INITTASK,
				contentLayout, fullscreenLoadingView, mRetryView);

		mMatchDetails.execute(match.getGosuLink());
		rand = new Random();
		mRandNum = rand.nextInt(50000);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 1, "")
				.setIcon(R.drawable.ic_refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);

		if (match.getMatchStatus() == Match.NOTSTARTED) {
			menu.add(0, 1, 0, "").setIcon(R.drawable.ic_action_bell)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

		return true;

	}

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		// shareIntent.setType("image/*");
		// Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		// shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setAction(Intent.ACTION_SEND);

		if (match.getMatchStatus() == Match.NOTSTARTED) {
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					match.getTeamName1() + " vs " + match.getTeamName2()
							+ " will start " + match.getTime()
							+ " later");
		} else if (match.getMatchStatus() == Match.LIVE) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, match.getTeamName1()
					+ " vs " + match.getTeamName2()
					+ " is live now");
		} else if (match.getMatchStatus() == Match.ENDED) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, match.getTeamName1() + " "
					+ match.getScore() + " " + match.getTeamName2());

		}
		shareIntent.setType("text/plain");
		return shareIntent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		if (item.getItemId() == 0) {
			refreshActivity();
		}

		if (item.getItemId() == 1) {
			try {
				// Setting up notification
//				Date currentDate = new Date();
				long dateInMill = gameStartDate.getTime();
//				Log.i("debug cur/star/diff",
//						Long.toString(currentDate.getTime()) + "/"
//								+ Long.toString(dateInMill) + "/"
//								+ Long.toString(dateInMill - currentDate.getTime()));
				String msg = tName1 + " vs " + tName2;

				// Create an instance of AlarmService
				AlarmService as = new AlarmService(this, msg, mRandNum);
				as.startAlarm(dateInMill);

				Toast.makeText(this,
						"You will get notified when the match starts.",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(this, "Loading data, please try again later.",
						Toast.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {

		super.onListItemClick(l, v, position, id);

		if (match.getMatchStatus() == Match.ENDED) {
			Intent i = new Intent(this, YoutubeActionBarActivity.class);
			// Toast.makeText(this, videoIds.get(position), Toast.LENGTH_SHORT)
			// .show();

//			i.putExtra("isfullscreen", true);
			Video aVideo = new Video();
			aVideo.setVideoId(videoIds.get(position));
			aVideo.setVideoDesc("");
			aVideo.setTitle("");
			if(tName1 != null && tName2 != null){
				int gameNum = position + 1;
				aVideo.setTitle("Game " + gameNum + " " + tName1 + " vs " + tName2);
				aVideo.setVideoDesc("Game " + gameNum + " " + tName1 + " vs " + tName2);
			}
			i.putExtra("video", aVideo);
			startActivity(i);
		} else {
			
//			// Getting the preferred player
//			String preferredPlayer = prefs.getString("preferredPlayer", "-1");
////			Log.i("debug prefs", preferredPlayer);
//			final Context mContext = this;
//			if (preferredPlayer.equals("-1")) {
//				// No preference
//				final CharSequence[] colors_radio = {
//						"New Player(No flash needed)", "Old Player(Flash needed)" };
//
//				new AlertDialog.Builder(this)
//						.setSingleChoiceItems(colors_radio, 0, null)
//						.setPositiveButton("Just once",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										dialog.dismiss();
//										int selectedPosition = ((AlertDialog) dialog)
//												.getListView()
//												.getCheckedItemPosition();
//										// Do something useful withe the position of
//										// the selected radio button
//										openPlayer(selectedPosition, mContext,
//												position, false);
//									}
//								})
//						.setNegativeButton("Remember my selection",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										dialog.dismiss();
//										int selectedPosition = ((AlertDialog) dialog)
//												.getListView()
//												.getCheckedItemPosition();
//										// Do something useful withe the position of
//										// the selected radio button
//										openPlayer(selectedPosition, mContext,
//												position, true);
//
//									}
//								}).show();
//			} else {
//				// Got preferred player
//				openPlayer(Integer.parseInt(preferredPlayer), mContext, position, false);
//			}
			
			openPlayer(0, this, position, false);
		}

	}
	
	private void openPlayer(int selectedPosition, Context mContext,
			int videoPostion, boolean isSave) {
		switch (selectedPosition) {
		case 0:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "0").commit();
			}
			// Using new video player
			Intent i = new Intent(mContext, VideoBuffer.class);
			i.putExtra("video", lives.get(videoPostion));
			startActivity(i);
			break;

		case 1:
			// save pref
			if (isSave) {
				prefs.edit().putString("preferredPlayer", "1").commit();
			}

			// Using old player
			if (check()) {
				Intent intent1 = new Intent(mContext, TwitchPlayer.class);
				intent1.putExtra("video", lives.get(videoPostion));
				startActivity(intent1);

			} else {
				Intent intent2 = new Intent(mContext,
						FlashInstallerActivity.class);
				startActivity(intent2);
			}
			break;
		}
	}

	public void initMatchView() {

	}

	private class getMatchDetails extends MyAsyncTask {
		Document doc;

		public getMatchDetails(int type, View contentView, View loadingView,
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

					mMatchDetails = new getMatchDetails(type, contentView,
							loadingView, retryView);

					mMatchDetails.execute(match.getGosuLink());

				}
			});

		}

		@Override
		public String doInBackground(String... uri) {

			super.doInBackground(uri[0]);

			if (!taskCancel && responseString != null) {
				doc = Jsoup.parse(responseString);
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// Checking network status first
			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				try {
					/* start of new code */
					Elements links = new Elements();
					links = doc.select("div#col1");
					Element link = links.first();
					String date = "";
					if (!link.select("p.datetime").isEmpty()) {
						date = link.select("p.datetime").first().text();
					}
					String bestof = "";
					if (!link.select("p.bestof").isEmpty()) {
						bestof = link.select("p.bestof").first().text();
					}

					/* Get streams */
					// Elements streamEles = new Elements();
					// if(match.getMatchStatus() == Match.NOTSTARTED){
					//
					// streamEles =
					// link.select("p.streamlist").first().select("a");
					// for(Element e : streamEles){
					// lives.add(e.select("a").first().ownText());
					// //System.out.println("stream: " + e.ownText());
					// }
					// }else if(match.getMatchStatus() == Match.LIVE){
					// streamEles =
					// link.select("div[class=tab-wrapper]").first().select("a");
					// for(Element e : streamEles){
					// lives.add(e.select("span").first().ownText());
					// //System.out.println("stream: " + e.ownText());
					// }
					//
					// }
					/* end of new code */

					Element opp_1 = null;

					opp_1 = doc.select("div.opponent").first();

					if (opp_1 != null) {

						Element opp_2 = doc.select("div.opponent").get(1);
						// Element scoreDiv_1 = doc
						// .select("div[class=center-column]").first()
						// .select("div").get(1);
						//
						// Element scoreDiv_2 = doc
						// .select("div[class=center-column]").first()
						// .select("div").get(2);
						//
						// Element details = doc.select("table#match-details")
						// .first();
						//
						Elements flash = doc.select("object");
						// //
						Elements videos = doc.select("a[class=image modal]");

						/* Get Twitch objects */
						if (match.getMatchStatus() != Match.ENDED) {
							if (!flash.isEmpty())
								for (Element f : flash) {
									if (!flash.isEmpty()) {
										String data = f.attr("data");
										String mData = data.substring(
												data.indexOf("=") + 1,
												data.length());
										// String pattern = "(.*?)=(.*?)";
										// data.replace(pattern, "$2");
										lives.add(mData);
									}
								}
						} else if (match.getMatchStatus() == Match.ENDED) {
							if (!videos.isEmpty()) {
								int i = 1;
								for (Element v : videos) {

									// String src = v.attr("src");
									String imgurl = v.select("img").first()
											.attr("src");

									String mImgurl = imgurl
											.replaceAll(
													"https://i1?.ytimg.com/vi/(.*?)/(.*)",
													"$1");

									String title = "Game " + i;
									i++;
									lives.add(title);
									// videoIds.add(src.substring(
									// src.indexOf("/embed/") + 7,
									// src.indexOf("?")));
									videoIds.add(mImgurl);
								}
							}

						}

						myTimer = (TextView) findViewById(R.id.myTimer);

						ImageView icon_1 = (ImageView) findViewById(R.id.icon1);
						ImageView icon_2 = (ImageView) findViewById(R.id.icon2);

						TextView teamName_1 = (TextView) findViewById(R.id.tName1);
						TextView teamName_2 = (TextView) findViewById(R.id.tName2);

						TextView team1score = (TextView) findViewById(R.id.team1score);
						TextView team2score = (TextView) findViewById(R.id.team2score);

						TextView tournamentName = (TextView) findViewById(R.id.tournamentName);
						TextView format = (TextView) findViewById(R.id.format);
						TextView startTime = (TextView) findViewById(R.id.startTime);
						TextView liveLabel = (TextView) findViewById(R.id.livelabel);
						TextView noLive = (TextView) findViewById(R.id.nolive);
						TextView liveStatus = (TextView) findViewById(R.id.liveStatus);

						tName1 = opp_1.select("a").first().text().trim();
						tName2 = opp_2.select("a").first().text().trim();
						teamName_1.setText(tName1);
						teamName_2.setText(tName2);

						// if (scoreDiv_1.className().trim().endsWith("winner"))
						// {
						// team1score.setTextColor(Color.RED);
						// }

						// if (scoreDiv_2.className().trim().endsWith("winner"))
						// {
						// team2score.setTextColor(Color.RED);
						// }
						if (match.getMatchStatus() == Match.LIVE) {
							Elements scoreDiv = link.select("p.vs");
							if (!scoreDiv.isEmpty()) {
								Elements spans = scoreDiv.first()
										.select("span");
								String theScore = spans.get(1).text().trim();
								String t1s = theScore.substring(0, 1);
								String t2s = theScore.substring(4, 5);
								team1score.setText(t1s);
								team2score.setText(t2s);
							}

						} else if (match.getMatchStatus() == Match.ENDED) {

							team1score.setText(link
									.select("span.hidden > span").first()
									.ownText());
							team2score.setText(link
									.select("span.hidden > span").get(1)
									.ownText());

						} else if (match.getMatchStatus() == Match.NOTSTARTED) {
							team1score.setText("0");
							team2score.setText("0");
						}

						imageLoader.displayImage(baseUrl
								+ opp_1.select("img").first().attr("src"),
								icon_1, options, animateFirstListener);

						imageLoader.displayImage(baseUrl
								+ opp_2.select("img").first().attr("src"),
								icon_2, options, animateFirstListener);

						// Elements detailTd = details.select("td");

						tournamentName.setText("Tournament: "
								+ link.select("a").first().ownText());
						format.setText("Format: " + bestof);
						// String dateInString = detailTd.first().text()
						// .trim();
						if (!date.equals("")) {
							startTime.setText("Start Time: "
									+ processDate(date) + " (Your place)");
						} else {

							startTime.setText("Start Time: " + "");
						}

						if (match.getMatchStatus() == Match.LIVE) {
							liveStatus.setVisibility(View.VISIBLE);
						} else if (match.getMatchStatus() == Match.NOTSTARTED) {
							myTimer.setText(match.getTime());
							myTimer.setVisibility(View.VISIBLE);
						}

						if (match.getMatchStatus() == Match.ENDED) {
							noLive.setText("No Videos Released Yet.");
							liveLabel.setText("VODs");
						}

						if (lives.isEmpty()) {
							liveLabel.setVisibility(View.GONE);
							noLive.setVisibility(View.VISIBLE);
						} else {

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									mActivity,
									android.R.layout.simple_list_item_1, lives);

							setListAdapter(adapter);
						}

						DisplayView(contentView, retryView, loadingView);

					} else {
						handleCancelView();
					}
				} catch (Exception e) {

				}
			} else {

				handleCancelView();

			}
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			// Log.d("AsyncDebug", "Into OnCancelled!");
			handleCancelView();
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String processDate(String s) {

		Date date = new Date();
		// Calendar c = new Calendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));

		try {
			date = sdf.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		gameStartDate = date;

		sdf = new SimpleDateFormat("MMMM d 'at' hh:mm a");
		sdf.setTimeZone(TimeZone.getDefault());
		// date.setHours(Integer.parseInt(hourInString));
		return sdf.format(date);

	}


	public void refreshActivity() {
		// Destroy current activity
		finish();

		// Toast.makeText(mActivity, "Refreshing", Toast.LENGTH_SHORT).show();

		// Start a new activity
		Intent i = new Intent(mActivity, MatchDetailsActivity.class);
		i.putExtra("match", match);
		startActivity(i);

	}

	// set a listener for "Retry" button

	public void hideAllViews() {
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.GONE);
		if (contentLayout != null)
			contentLayout.setVisibility(View.GONE);
		if (mRetryView != null)
			mRetryView.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Log.d("UniversalImageLoader", "clear from MatchDetail");
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();

		// check the state of the task
		cancelAllTask();
		hideAllViews();

	}

	public void cancelAllTask() {

		if (mMatchDetails != null
				&& mMatchDetails.getStatus() == Status.RUNNING) {
			mMatchDetails.cancel(true);

			// Log.d("AsyncDebug", "Task cancelled!!!!!!!!");
		} else {
			// Log.d("AsyncDebug", "Task cancellation failed!!!!");
		}
	}

	private boolean check() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

}
