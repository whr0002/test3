package com.examples.gg.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rs.dota.R;

public class ListViewAdapter extends ArrayAdapter<String> {

	private ArrayList<String> mWords;
	private LayoutInflater inflater;

	public ListViewAdapter(Context context, ArrayList<String> words) {
		super(context, R.layout.suggested_item, words);

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mWords = words;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.suggested_item, parent,
					false);
			holder.wordsView = (TextView) convertView.findViewById(R.id.word);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.wordsView.setText(mWords.get(position));
		return convertView;
	}

	static class ViewHolder {
		TextView wordsView;
	}
}
