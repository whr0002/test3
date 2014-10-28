package com.examples.gg.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.rs.dota.R;

public class FlashInstallerActivity extends SherlockActivity {

	private ActionBar mActionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the current activity
		setContentView(R.layout.flashinstaller);
		
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		TextView flashNotice = (TextView) findViewById(R.id.flashnotice);
		ImageButton flashButton = (ImageButton) findViewById(R.id.flashButton);
		
		
		// Get flash player apk link based on android version
		final String apkLink = (Build.VERSION.SDK_INT >= 14) ?
	   "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.115.63/install_flash_player_ics.apk" :
	   "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.111.59/install_flash_player_pre_ics.apk";

		flashNotice.setText("Adobe Flash Player is not installed in your Android phone. In order to watch the stream, we recommend you to press the icon below to install the Adobe Flash Player");
		
		flashButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent("android.intent.action.VIEW", Uri.parse(apkLink)));
				
			}});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	

}
