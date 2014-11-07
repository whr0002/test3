package com.examples.gg.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.gg.data.Video;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.rs.dota.R;

public class VideoArrayAdapter extends ArrayAdapter<Video> {

	// private final ArrayList<String> values;
	protected ArrayList<Video> videos;
	private LayoutInflater inflater;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private ImageLoader imageLoader;

	protected Context mContext;

	protected ViewHolder holder;

	public VideoArrayAdapter(Context context, ArrayList<Video> videos,
			ImageLoader imageLoader) {
		super(context, R.layout.videolist, videos);

		this.mContext = context;
		// this.values = values;
		this.videos = videos;
		this.imageLoader = imageLoader;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (!this.imageLoader.isInited()) {
			// this.imageLoader.init(ImageLoaderConfiguration.createDefault(context));

			HttpParams params = new BasicHttpParams();
			// Turn off stale checking. Our connections break all the time
			// anyway,
			// and it's not worth it to pay the penalty of checking every time.
			HttpConnectionParams.setStaleCheckingEnabled(params, false);
			// Default connection and socket timeout of 10 seconds. Tweak to
			// taste.
			HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
			HttpConnectionParams.setSoTimeout(params, 10 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			// Don't handle redirects -- return them to the caller. Our code
			// often wants to re-POST after a redirect, which we must do
			// ourselves.
			HttpClientParams.setRedirecting(params, false);
			// Set the specified user agent and register standard protocols.
			HttpProtocolParams.setUserAgent(params, "some_randome_user_agent");
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					params, schemeRegistry);

			File cacheDir = StorageUtils.getCacheDirectory(context);

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(480, 800)
					.threadPoolSize(3)
					.threadPriority(Thread.NORM_PRIORITY - 1)
					.tasksProcessingOrder(QueueProcessingType.FIFO)
					.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
					.diskCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
					.memoryCacheSize(2 * 1024 * 1024)
					.memoryCacheSizePercentage(13).build();
			this.imageLoader.init(config);
		}

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.imageholder3)
				.showImageForEmptyUri(R.drawable.imageholder3)
				.showImageOnFail(R.drawable.imageholder3).cacheInMemory(true)
				.delayBeforeLoading(300).cacheOnDisk(true)
				.resetViewBeforeLoading(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.videolist, parent, false);

			holder = new ViewHolder();

			holder.titleView = (TextView) convertView
					.findViewById(R.id.videotitle);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.thumbnail);
			holder.countView = (TextView) convertView.findViewById(R.id.Desc);
			holder.videoLength = (TextView) convertView
					.findViewById(R.id.videolength);

			// set the author
			holder.authorView = (TextView) convertView
					.findViewById(R.id.videouploader);

			holder.menuIcon = (ImageView) convertView
					.findViewById(R.id.popupIcon);
			
			holder.watchingIcon = (ImageView) convertView
					.findViewById(R.id.watching);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// if(!isMenuVisible){
		// holder.menuIcon.setVisibility(View.GONE);
		// }else{
		// holder.menuIcon.setVisibility(View.VISIBLE);
		// }
		holder.titleView.setText(videos.get(position).getTitle());
		holder.authorView.setText(videos.get(position).getAuthor());
		holder.watchingIcon.setVisibility(View.GONE);
		Video theVideo = videos.get(position);
		// values for time and view counts should not be null
		if (theVideo.isVideo) {

			// For Youtube videos, showing update date and views
			holder.countView.setText("   "+videos.get(position).getUpdateTime()
					+ " | " + videos.get(position).getViewCount());

		} else if (theVideo.isPlaylist) {
			holder.countView.setText(theVideo.getViewCount());

		} else if (videos.get(position).isTwitch) {

			// For Twitch, only showing number of viewers
			holder.watchingIcon.setVisibility(View.VISIBLE);
			holder.countView.setText(videos.get(position).getViewCount());

		} else if (videos.get(position).isNews) {
			// For News
			holder.countView.setText(null);
			holder.authorView.setMaxLines(2);
		} else {
			holder.countView.setText(null);
		}
		holder.videoLength.setText(videos.get(position).getDuration());

		// If the menu popup listener is not set, set it

		// register a listener for the menu icon
		// holder.menuIcon.setOnClickListener(new MenuIconView(mContext, holder,
		// videos.get(position)));
		setMenuListener(position);

		imageLoader.displayImage(videos.get(position).getThumbnailUrl(),
				holder.imageView, options, animateFirstListener);

		return convertView;
	}

	static class ViewHolder {
		TextView titleView;
		TextView authorView;
		TextView countView;
		TextView videoLength;
		ImageView imageView;
		ImageView watchingIcon;
		ImageView menuIcon;
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

	public void setMenuListener(int pos) {
		holder.menuIcon.setOnClickListener(new MenuIconView(mContext, holder,
				videos.get(pos)));

	}

}