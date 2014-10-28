package com.examples.gg.loadMore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rs.dota.R;

public class NewsViewerActivity extends SherlockActivity {

	private ActionBar mActionBar;
	private String mUri;
	private AdView adView;
	protected WebView mWebView;
	protected ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum);

		// Get ads view
		adView = (AdView) this.findViewById(R.id.ad);
		if (adView != null) {
			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("5E4CA696BEB736E734DD974DD296F11A").build();
			adView.loadAd(adRequest);
		}

		Intent intent = getIntent();
		mUri = intent.getStringExtra("uri");

		TextView title = (TextView) findViewById(R.id.title);
		TextView content = (TextView) findViewById(R.id.tip);

		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setTitle("Viewing news");

		// Initial webview
		if (Build.VERSION.SDK_INT >= 14) {
			this.getWindow().setFlags(0x1000000, 0x1000000);
		}

		mWebView = (WebView) findViewById(R.id.forumView);
		WebSettings streamSettings = mWebView.getSettings();
		streamSettings.setJavaScriptEnabled(true);
		streamSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// streamSettings.setPluginState(PluginState.ON);
		// streamSettings.setPluginsEnabled(true);
		streamSettings.setAllowFileAccess(true);
		streamSettings.setLoadWithOverviewMode(true);
		streamSettings.setUseWideViewPort(true);
		streamSettings.setBuiltInZoomControls(true);
		streamSettings.setSupportZoom(true);
		
		mWebView.setWebChromeClient(new MyWebViewClient());
		mWebView.setWebViewClient(new WebViewClient());

		mWebView.loadUrl(mUri);

		progress = (ProgressBar) findViewById(R.id.myProgressBar);
		progress.setMax(100);
		progress.setProgress(0);

	}

	
	public boolean canGoBack() {
		return this.mWebView != null && this.mWebView.canGoBack();
	}

	public void goBack() {
		if (this.mWebView != null) {
			this.mWebView.goBack();
		}
	}

	public class MyWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progress.setProgress(newProgress);
			if (newProgress == 100) {
				progress.setProgress(0);

			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public void onPause() {
		if (adView != null)
			adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null)
			adView.resume();
	}

	@Override
	public void onDestroy() {
		// Destroy ads when the view is destroyed
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
			// if Back key pressed and webview can navigate to previous page
			goBack();
			// go back to previous page
			return true;
		} else {
			this.finish();
			// finish the activity
		}
		return super.onKeyDown(keyCode, event);
	}
}
