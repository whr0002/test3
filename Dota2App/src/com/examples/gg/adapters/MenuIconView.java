package com.examples.gg.adapters;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.examples.gg.data.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.dota.R;

public class MenuIconView extends ImageView implements OnClickListener{

	protected Context mContext;
	protected VideoArrayAdapter.ViewHolder mViewholder;
	protected Video mVideo;
	
	protected String prefName = "Favorites";
	
	public MenuIconView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    public MenuIconView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MenuIconView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
	public MenuIconView(Context context, VideoArrayAdapter.ViewHolder viewholder, Video video){
		super(context);
		this.mContext = context;
		this.mViewholder = viewholder;
		this.mVideo = video;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onClick(View v) {
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			// Android OS version ICE_CREAM_SANDWICH and up
			PopupMenu popupMenu = new PopupMenu(mContext, mViewholder.menuIcon);
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				
				@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				@Override
				public boolean onMenuItemClick(android.view.MenuItem item) {
					switch(item.getItemId()){
					case R.id.item_favorite:
						
						dealData();
						break;
					default:
						return false;
					}
					return true;
				}
			});
			popupMenu.inflate(R.menu.popup_menu);
			popupMenu.show();
		}else{
			// Versions less than ICE_CREAM_SANDWICH
			// No preference
			
			if(mContext != null){
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						mContext);
		 
					// set title
					alertDialogBuilder.setTitle("Add to Favorites?");
		 
					// set dialog message
					alertDialogBuilder
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// Add the media to Favorites
								dealData();
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
		}

		
	}

	protected void dealData() {
		Gson gson = new Gson();
		ArrayList<Video> videos;
		
		SharedPreferences favoritePrefs = mContext.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor favEditor = favoritePrefs.edit();
		
		String result = favoritePrefs.getString("json", "");
		if(result.equals("")){
			// Favorites is empty
			videos = new ArrayList<Video>();				
			
		}else{
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>(){}.getType();
			videos = gson.fromJson(favoritePrefs.getString("json", ""), listType);
	
		}
		
		if(!isExist(videos, mVideo)){
			if(mVideo.isTwitch){
				mVideo.setViewCount("----");
			}
			videos.add(mVideo);
			String json = gson.toJson(videos);
			favEditor.putString("json", json);
			favEditor.commit();
			
			Toast.makeText(
			mContext,
			"Added.",
			Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(
			mContext,
			"Already exists.",
			Toast.LENGTH_SHORT).show();
		}
	}

//	protected void checkVideos(){
//		SharedPreferences fPrefs = mContext.getSharedPreferences(prefName, 0);
//		Type listType = new TypeToken<ArrayList<Video>>(){}.getType();
//		Gson g = new Gson();
//		ArrayList<Video> vs; 
//		String result = fPrefs.getString("json", "");
//		if(result.equals("")){
//			vs = new ArrayList<Video>();
//		}else{
//			vs = g.fromJson(result, listType);
//		}
////		Log.d("debug", "# of Videos: " + vs.size());
//		
//		
//	}
	
	// Check whether the provided video exists in stored videos
	protected boolean isExist(ArrayList<Video> videos, Video aVideo){
		for(Video v : videos){
			if(v.getVideoId().equals(aVideo.getVideoId()))
				return true;
		}
		
		return false;
	}

}
