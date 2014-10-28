package com.examples.gg.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.gg.data.Match;
import com.examples.gg.loadMore.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rs.dota.R;

public class MatchArrayAdapter extends ArrayAdapter<Match> {
	private LayoutInflater inflater;
	private boolean isResult;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = (ImageLoadingListener) new AnimateFirstDisplayListener();
	private ImageLoader imageLoader;

	private ArrayList<Match> matches;

	public MatchArrayAdapter(Context context, ArrayList<Match> matches,
			ImageLoader imageLoader, boolean isResult) {

		super(context, R.layout.matchtable, matches);

		this.isResult = isResult;
		this.imageLoader = imageLoader;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// imageLoader=new ImageLoader(context.getApplicationContext());

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading)
				.showImageOnFail(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).build();

		this.matches = matches;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.matchtable, parent, false);

			holder = new ViewHolder();

			if (!isResult)
				holder.time = (TextView) convertView
						.findViewById(R.id.matchTime);
			else {
				convertView.findViewById(R.id.myTimeLayout).setVisibility(
						View.GONE);
				holder.score = (TextView) convertView.findViewById(R.id.versus);
			}
			holder.teamName1 = (TextView) convertView
					.findViewById(R.id.teamName1);
			holder.teamName2 = (TextView) convertView
					.findViewById(R.id.teamName2);

			holder.teamIcon1 = (ImageView) convertView
					.findViewById(R.id.teamIcon1);
			holder.teamIcon2 = (ImageView) convertView
					.findViewById(R.id.teamIcon2);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!isResult) {
			if (matches.get(position).getMatchStatus() == Match.LIVE) {
				holder.time.setTextColor(Color.RED);
				holder.time.setText("Live");
			} else {
				holder.time.setTextColor(Color.BLACK);
				holder.time.setText(matches.get(position).getTime());
			}
		} else
			holder.score.setText(matches.get(position).getScore());

		holder.teamName1.setText(matches.get(position).getTeamName1());
		holder.teamName2.setText(matches.get(position).getTeamName2());

//		imageLoader.displayImage(matches.get(position).getTeamIcon1(),
//				holder.teamIcon1, options, animateFirstListener);
//
//		imageLoader.displayImage(matches.get(position).getTeamIcon2(),
//				holder.teamIcon2, options, animateFirstListener);

		return convertView;
	}

	static class ViewHolder {

		TextView time;
		TextView score;
		TextView teamName1;
		TextView teamName2;
		ImageView teamIcon1;
		ImageView teamIcon2;

	}

}
