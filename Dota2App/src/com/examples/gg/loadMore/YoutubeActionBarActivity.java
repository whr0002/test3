package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.examples.gg.data.Video;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.dota.R;

public class YoutubeActionBarActivity extends SherlockFragmentActivity
		implements YouTubePlayer.OnInitializedListener {

	private ActionBar mActionBar;
	private Video video;
	private String videoId;
	private TextView desc;
	private View myContent;
	private RelativeLayout otherViews;
	private RelativeLayout playerView;

	private boolean isfullscreen;
	private boolean isFullscreenMode;
	private Activity sfa;
	private YouTubePlayerSupportFragment fragment;
	private InterstitialAd mInterstitial;
	private Random mRandom;
	private String interstitialID;
	// private ActionBar mActionBar;

	private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	private String mPlaylistID;
	private ArrayList<Video> mVideoList;
	private int mPositionOfList;
	private String apiKey;
	private YouTubePlayer mYtp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sfa = this;

		mActionBar = getSupportActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		if (isFullscreenMode = intent.getBooleanExtra("isfullscreen", false)) {
			videoId = intent.getStringExtra("videoId");
			mPlaylistID = intent.getStringExtra("playlistID");
			mVideoList = intent.getParcelableArrayListExtra("videoList");
			mPositionOfList = intent.getIntExtra("positionOfList", 0);
			setRequestedOrientation(LANDSCAPE_ORIENTATION);

			setContentView(R.layout.fullscreenyoutube);

		} else {
			video = intent.getParcelableExtra("video");
			mPlaylistID = intent.getStringExtra("playlistID");
			mVideoList = intent.getParcelableArrayListExtra("videoList");
			mPositionOfList = intent.getIntExtra("positionOfList", 0);

			setContentView(R.layout.videoplayer);
			myContent = (View) findViewById(R.id.videoContent);
			desc = (TextView) findViewById(R.id.videodesc);
			otherViews = (RelativeLayout) findViewById(R.id.scrolllayout);
			playerView = (RelativeLayout) findViewById(R.id.youtubeplayer);

			desc.setText(video.getVideoDesc());

			mActionBar.setTitle(video.getTitle());

		}

		interstitialID = this.getResources().getString(R.string.interstitialID);
		apiKey = this.getResources().getString(R.string.apiKey);
		mRandom = new Random();
//		if (mRandom.nextInt(10) > 7) {
			// Loading Ad when close a video at 20%
			mInterstitial = new InterstitialAd(this);
			mInterstitial.setAdUnitId(interstitialID);
			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("5E4CA696BEB736E734DD974DD296F11A").build();
			mInterstitial.loadAd(adRequest);
//		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragment = new YouTubePlayerSupportFragment();
		fragmentTransaction.add(R.id.youtubeplayer, fragment);
		fragmentTransaction.commit();
		fragment.initialize(apiKey, this);
		doLayout();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

		// XXX: For now, ShareActionProviders must be displayed on the action
		// bar
		// Set file with share history to the provider and set the share intent.
		// MenuItem overflowItem =
		// menu.findItem(R.id.menu_item_share_action_provider_overflow);
		// ShareActionProvider overflowProvider =
		// (ShareActionProvider) overflowItem.getActionProvider();
		// overflowProvider.setShareHistoryFileName(
		// ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		// overflowProvider.setShareIntent(createShareIntent());

		return true;
	}

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		// shareIntent.setType("image/*");
		// Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		// shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setAction(Intent.ACTION_SEND);
		if (isFullscreenMode) {
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"http://www.youtube.com/watch?v=" + videoId);
		} else {
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"http://www.youtube.com/watch?v=" + video.getVideoId());
		}
		shareIntent.setType("text/plain");
		return shareIntent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// if (isfullscreen) {
		// // Checks the orientation of the screen for landscape and portrait
		// // and set portrait mode always
		// //System.out.println("FULL!!!!!!!!!!!!!!!!!!!!!!!!");
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }
		//
		// if (!isFullscreenMode){
		// // doLayout();
		//
		// }

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) // To
																			// fullscreen
		{
			isfullscreen = true;
			if (mYtp != null)
				mYtp.setFullscreen(true);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			isfullscreen = false;
			if (mYtp != null)
				mYtp.setFullscreen(false);
		}
		doLayout();
	}

	private void doLayout() {
		if (isfullscreen) {
			if (myContent != null)
				myContent.setVisibility(View.GONE);
			if (desc != null)
				desc.setVisibility(View.GONE);
			if (otherViews != null)
				otherViews.setVisibility(View.GONE);
			if (fragment != null) {
				if (fragment.getView() != null)
					fragment.getView().setLayoutParams(
							new RelativeLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
			}
			// mActionBar.hide();
		} else {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// User using Landscape to enter this activity
				// Put the description on right of playerview

				if (myContent != null)
					myContent.setVisibility(View.VISIBLE);
				if (desc != null)
					desc.setVisibility(View.VISIBLE);
				if (fragment != null) {
					if (fragment.getView() != null) {
						// View fragView = fragment.getView();
						// RelativeLayout.LayoutParams fragmentParams =
						// (RelativeLayout.LayoutParams)fragView.getLayoutParams();
						// fragmentParams.addRule(RelativeLayout.LEFT_OF,
						// otherViews.getId());
						// fragmentParams.addRule(RelativeLayout.CENTER_VERTICAL);
						// fragmentParams.width = LayoutParams.MATCH_PARENT;
						// fragmentParams.height = LayoutParams.WRAP_CONTENT;
						// fragView.setLayoutParams(fragmentParams);

						 fragment.getView().setLayoutParams(
						 new RelativeLayout.LayoutParams(
						 LayoutParams.MATCH_PARENT,
						 LayoutParams.WRAP_CONTENT));

					}

				}

				if (playerView != null) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) playerView
							.getLayoutParams();
					layoutParams.addRule(RelativeLayout.LEFT_OF,
							otherViews.getId());
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
					layoutParams.width = LayoutParams.MATCH_PARENT;
					layoutParams.height = LayoutParams.WRAP_CONTENT;
					playerView.setLayoutParams(layoutParams);
				}
				if (otherViews != null) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) otherViews
							.getLayoutParams();
					layoutParams.addRule(RelativeLayout.BELOW, 0);
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					layoutParams.width = 300;
					layoutParams.height = LayoutParams.MATCH_PARENT;
					otherViews.setLayoutParams(layoutParams);
					otherViews.setVisibility(View.VISIBLE);
				}
			} else {
				if (myContent != null)
					myContent.setVisibility(View.VISIBLE);
				if (desc != null)
					desc.setVisibility(View.VISIBLE);
				if (otherViews != null)
					otherViews.setVisibility(View.VISIBLE);
				if (fragment != null) {
					if (fragment.getView() != null)
						fragment.getView().setLayoutParams(
								new RelativeLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
				}

				if (playerView != null) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) playerView
							.getLayoutParams();
					layoutParams.addRule(RelativeLayout.LEFT_OF, 0);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 0);
					layoutParams.width = LayoutParams.MATCH_PARENT;
					layoutParams.height = LayoutParams.WRAP_CONTENT;
					playerView.setLayoutParams(layoutParams);
				}
				if (otherViews != null) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) otherViews
							.getLayoutParams();
					layoutParams.addRule(RelativeLayout.BELOW,
							playerView.getId());
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					layoutParams.width = LayoutParams.MATCH_PARENT;
					layoutParams.height = LayoutParams.MATCH_PARENT;
					otherViews.setLayoutParams(layoutParams);
					otherViews.setVisibility(View.VISIBLE);
				}
			}
			// mActionBar.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(apiKey, this);
		}
	}

	public YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerSupportFragment) fragment;
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer ytp,
			boolean wasRestored) {

		mYtp = ytp;
		ytp.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		ytp.setPlaylistEventListener(new PlaylistEventListener() {

			@Override
			public void onNext() {
				mPositionOfList++;
				Video v = mVideoList.get(mPositionOfList);
				mActionBar.setTitle(v.getTitle());
				desc.setText(v.getVideoDesc());

				saveToHistory(v);

			}

			@Override
			public void onPlaylistEnded() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPrevious() {
				mPositionOfList--;
				Video v = mVideoList.get(mPositionOfList);
				mActionBar.setTitle(v.getTitle());
				desc.setText(v.getVideoDesc());

				saveToHistory(v);
			}
		});
		ytp.setOnFullscreenListener(new OnFullscreenListener() {

			@Override
			public void onFullscreen(boolean _isFullScreen) {
				isfullscreen = _isFullScreen;
				if (isfullscreen) {
					// setRequestedOrientation(LANDSCAPE_ORIENTATION);
					mYtp.setFullscreen(true);
				} else {
					if (isFullscreenMode)
						finish();
					mYtp.setFullscreen(false);
					// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					// doLayout();
				}

				doLayout();

			}
		});
		// Toast.makeText(sfa, "Initialization  Success",
		// Toast.LENGTH_LONG).show();
		if (!wasRestored) {
			if (isFullscreenMode) {
				ytp.setFullscreen(true);
				if (mPlaylistID == null) {
					// It's a video
					ytp.loadVideo(videoId);
				} else {
					// A list of videos to play
					List<String> ids = new ArrayList<String>();
					for (Video v : mVideoList) {
						ids.add(v.getVideoId());
					}
					ytp.loadVideos(ids, mPositionOfList, 0);
				}
			} else {
				if (mPlaylistID == null) {
					// It's a video
					ytp.loadVideo(video.getVideoId());

				} else {
					// A list of videos to play
					List<String> ids = new ArrayList<String>();
					for (Video v : mVideoList) {
						ids.add(v.getVideoId());
					}
					ytp.loadVideos(ids, mPositionOfList, 0);
				}
				// ytp.setFullscreen(true);
				// ytp.loadVideo(video.getVideoId());
				// ytp.loadVideo(video.getVideoId());
			}
			saveToHistory(video);
		}

	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(sfa, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			// Toast.makeText(sfa, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy() {
		// Show Ads
		if (mInterstitial != null)
			if (mInterstitial.isLoaded()) {
				mInterstitial.show();
			}

		super.onDestroy();

	}

	public void saveToHistory(Video v) {
		v.setAsVideo();
		Gson gson = new Gson();
		SharedPreferences prefs = sfa.getSharedPreferences("History", 0);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		ArrayList<Video> videos;
		String result = prefs.getString("json", "");
		if (result.equals("")) {
			// History is empty
			videos = new ArrayList<Video>();
			videos.add(v);
		} else {
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>() {
			}.getType();
			videos = gson.fromJson(prefs.getString("json", ""), listType);

			Video tempVideo = getExistVideo(videos, v);
			if (tempVideo != null) {
				videos.remove(tempVideo);
				videos.add(v);
			} else {
				videos.add(v);
			}
		}

		String json = gson.toJson(videos);
		prefsEditor.putString("json", json);
		prefsEditor.commit();

	}

	// Check whether the provided video exists in stored videos
	protected Video getExistVideo(ArrayList<Video> videos, Video aVideo) {
		for (Video v : videos) {
			if (v.getVideoId().equals(aVideo.getVideoId()))
				return v;
		}

		return null;
	}
}
