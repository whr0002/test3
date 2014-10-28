package com.examples.gg.loadMore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.data.Tip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rs.dota.R;

public class TipViewerActivity extends SherlockActivity{
	
	private ActionBar mActionBar;
	private Tip mTip;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tipviewer);
		
		// Get ads view
		adView = (AdView) this.findViewById(R.id.ad);
		if(adView !=null){
			AdRequest adRequest = new AdRequest.Builder()
		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		    .addTestDevice("5E4CA696BEB736E734DD974DD296F11A")
		    .build();
			adView.loadAd(adRequest);
		}
		
		Intent intent = getIntent();
		mTip = intent.getParcelableExtra("tip");
		
		TextView title = (TextView) findViewById(R.id.title);
		TextView content = (TextView) findViewById(R.id.tip);
		
		if(mTip != null){
			title.setText(mTip.getTitle());
			content.setText(mTip.getContent());
		}
		
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setTitle("Viewing a tip");
		
		
		
	}
	
    @Override
	public void onPause() {
    	if(adView != null)
    		adView.pause();
        super.onPause();
    }

    @Override
	public void onResume() {
        super.onResume();
        if(adView != null)
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
}
