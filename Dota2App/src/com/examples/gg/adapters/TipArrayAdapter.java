package com.examples.gg.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.examples.gg.data.Tip;
import com.rs.dota.R;

public class TipArrayAdapter extends ArrayAdapter<Tip> {
	private LayoutInflater inflater;

	private ArrayList<Tip> mTips;

	public TipArrayAdapter(Context context, ArrayList<Tip> tips) {
		super(context, R.layout.tipsinpager, tips);
		this.mTips = tips;
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tipsinpager, parent, false);

			holder = new ViewHolder();

			holder.titleView = (TextView) convertView
					.findViewById(R.id.title);
			
			holder.contentView = (TextView) convertView.findViewById(R.id.tip);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleView.setText(mTips.get(position).getTitle());
		holder.contentView.setText(mTips.get(position).getContent());


		return convertView;
	}

	static class ViewHolder {

		TextView titleView;
		TextView contentView;


	}
}
