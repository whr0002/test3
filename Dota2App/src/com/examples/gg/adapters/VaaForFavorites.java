package com.examples.gg.adapters;

import java.util.ArrayList;

import android.content.Context;

import com.examples.gg.data.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VaaForFavorites extends VideoArrayAdapter{

	private FavoriteVideoRemovedCallback mfc;
	private ArrayList<Video> mVideos;
	public VaaForFavorites(Context context, ArrayList<String> values,
			ArrayList<Video> videos, ImageLoader imageLoader) {
		super(context, values, videos, imageLoader);
	}
	public VaaForFavorites(Context context, ArrayList<String> values,
			ArrayList<Video> videos, ImageLoader imageLoader, FavoriteVideoRemovedCallback fc) {
		super(context, values, videos, imageLoader);
		this.mfc = fc;
		this.mVideos = videos;
	}
	
	@Override
	public int getCount() {
	    return mVideos.size();
	}
	
	@Override
	public void setMenuListener(int pos){
		holder.menuIcon.setOnClickListener(new MenuIconViewFavorites(this.mContext, holder,
				videos.get(pos), mfc));
		
	}

}
