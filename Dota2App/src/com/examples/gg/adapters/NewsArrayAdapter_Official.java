package com.examples.gg.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.gg.data.News;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rs.dota.R;


public class NewsArrayAdapter_Official extends ArrayAdapter<String> {
	
	private ArrayList<News> mNews;
	private LayoutInflater inflater;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private ImageLoader imageLoader;

	public NewsArrayAdapter_Official(Context context, ArrayList<String> values, ArrayList<News> mNews,
			ImageLoader imageLoader) {
		super(context, R.layout.videolist, values);

		this.mNews = mNews;
		this.imageLoader = imageLoader;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (!this.imageLoader.isInited()) {
			this.imageLoader.init(ImageLoaderConfiguration
					.createDefault(context));
		}
		// imageLoader=new ImageLoader(context.getApplicationContext());

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading)
				.showImageOnFail(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true)
				// .displayer(new RoundedBitmapDisplayer(20))
				.build();
		//
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.newslist, parent, false);

			holder = new ViewHolder();

			holder.titleView = (TextView) convertView
					.findViewById(R.id.videotitle);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.thumbnail);
			holder.dateView = (TextView) convertView.findViewById(R.id.Desc);

			// set the author
			holder.subTitleView = (TextView) convertView
					.findViewById(R.id.videouploader);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.titleView.setText(mNews.get(position).getTitle());
		holder.subTitleView.setText(mNews.get(position).getSubTitle());
		holder.dateView.setText(mNews.get(position).getDate());

		imageLoader.displayImage(mNews.get(position).getImageUri(),
				holder.imageView, options, animateFirstListener);

		return convertView;
	}

	static class ViewHolder {
		TextView titleView;
		TextView subTitleView;
		TextView dateView;
		TextView videoLength;
		ImageView imageView;

	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}