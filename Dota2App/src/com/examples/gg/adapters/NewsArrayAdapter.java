package com.examples.gg.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.examples.gg.data.News;
import com.rs.dota.R;



public class NewsArrayAdapter extends ArrayAdapter<News> {
	private LayoutInflater inflater;

	private ArrayList<News> mNews;

	public NewsArrayAdapter(Context context, ArrayList<News> news) {

		super(context, R.layout.news_adapter_layout, news);
		this.mNews = news;
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.news_adapter_layout, parent, false);

			holder = new ViewHolder();

			holder.newsTitle = (TextView) convertView
					.findViewById(R.id.news_title);
			
			holder.dateView = (TextView) convertView.findViewById(R.id.mydate);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.newsTitle.setText(mNews.get(position).getTitle());
		holder.dateView.setText(mNews.get(position).getDate());


		return convertView;
	}

	static class ViewHolder {

		TextView newsTitle;
		TextView dateView;


	}
}
