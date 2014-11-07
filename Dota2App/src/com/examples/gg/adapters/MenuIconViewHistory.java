package com.examples.gg.adapters;

import android.content.Context;

import com.examples.gg.data.Video;

public class MenuIconViewHistory extends MenuIconViewFavorites{

	public MenuIconViewHistory(Context context)
    {
        super(context);
        prefName = "History";
    }

    public MenuIconViewHistory(Context context, VideoArrayAdapter.ViewHolder viewholder, Video video)
    {
        super(context);
        mContext = context;
        mViewholder = viewholder;
        mVideo = video;
        prefName = "History";
    }

    public MenuIconViewHistory(Context context, VideoArrayAdapter.ViewHolder viewholder, Video video, FavoriteVideoRemovedCallback favoritevideoremovedcallback)
    {
        super(context);
        mContext = context;
        mViewholder = viewholder;
        mVideo = video;
        mfc = favoritevideoremovedcallback;
        prefName = "History";
    }

}
