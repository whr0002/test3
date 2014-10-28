package com.examples.gg.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rs.dota.R;

public class Dialog_Disclaimer extends DialogPreference{

	public Dialog_Disclaimer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setDialogLayoutResource(R.layout.open_source_preference);
		

	}
	
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            //builder.setTitle(R.string.pin_changepin_title);
            builder.setPositiveButton(null, null);
            builder.setNegativeButton(null, null);
            super.onPrepareDialogBuilder(builder);  
    }
	
    @Override
    public void onBindDialogView(View view){
    		TextView notice = (TextView) view.findViewById(R.id.notices);
    		notice.setVisibility(View.GONE);
    		
    		TextView mLink1 = (TextView) view.findViewById(R.id.open_source_link1);
    		mLink1.setVisibility(View.GONE);

    		
    		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
    		mLink2.setVisibility(View.GONE);
    		
    		TextView mLink4 = (TextView) view.findViewById(R.id.open_source_link4);
    		mLink4.setVisibility(View.GONE);

    		
    		TextView mLink5 = (TextView) view.findViewById(R.id.open_source_link5);
    		mLink5.setVisibility(View.GONE);
    		
    		ViewGroup jsoupLayout = (ViewGroup) view.findViewById(R.id.jsoup_layout);
    		jsoupLayout.setVisibility(View.GONE);
    		
    		ViewGroup imageLayout = (ViewGroup) view.findViewById(R.id.image_layout);
    		imageLayout.setVisibility(View.GONE);
    		
    		
    		
    		    		
    		TextView license = (TextView) view.findViewById(R.id.apache_license);
    		license.setText(Html.fromHtml(   	        
    				"<p>All the videos and news or other media are collected from Internet autonomously by programs. We only provide the links to other media. "+
    	            "This application does not generate any media contents.</p>" + 
            		"<p>If there are any contents that violate your rights, please contact us.</p>"));
    		license.setMovementMethod(LinkMovementMethod.getInstance());

            super.onBindDialogView(view);
    }
    

    
}
