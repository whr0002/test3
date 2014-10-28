package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.examples.gg.adapters.TipArrayAdapter;
import com.examples.gg.data.Tip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TipsFragment extends LoadMore_Base{

	private TipArrayAdapter taa;
	private ArrayList<Tip> mTips = new ArrayList<Tip>();
	
	@Override
	public void Initializing() {
		abTitle = "Useful Tips";
	}
	
	@Override
	public void setListView() {

		taa = new TipArrayAdapter(sfa,mTips);
		gv.setAdapter(taa);

		// Get the favorites
		setTips(mTips);
		// Refresh adapter
		taa.notifyDataSetChanged();
		// printVideoLog(videolist);
	}

	private void setTips(ArrayList<Tip> mTips2) {
		Gson gson = new Gson();
		String json;
		
		SharedPreferences sp = sfa.getSharedPreferences("Tips", 0);
		json = sp.getString("json", "");
		
		if(!json.equals("")){
			Type listType = new TypeToken<ArrayList<Tip>>(){}.getType();
			ArrayList<Tip> tempTips = gson.fromJson(json, listType);
			
			for(Tip t : tempTips){
				mTips.add(t);
			}
		}
		
	}
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(sfa,
						TipViewerActivity.class);
				i.putExtra("tip", mTips.get(position));
				startActivity(i);
			}
		});
	}

}
