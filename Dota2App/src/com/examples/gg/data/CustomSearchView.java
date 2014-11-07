package com.examples.gg.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.widget.SearchView;

public class CustomSearchView extends SearchView{
	public CustomSearchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private ListView mLv;

	
	@Override
	public void onActionViewCollapsed()
    {
        super.onActionViewCollapsed();
        if (mLv != null)
        {
        	mLv.setVisibility(View.GONE);
        }
        setQuery("", false);
    }
	
	public void setListView(ListView lv){
		this.mLv = lv;
	}
	


}
