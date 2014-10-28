package com.examples.gg.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.rs.dota.R;

public class Dialog_Libraries extends DialogPreference {

	public Dialog_Libraries(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setDialogLayoutResource(R.layout.open_source_preference);

	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		// builder.setTitle(R.string.pin_changepin_title);
		builder.setPositiveButton(null, null);
		builder.setNegativeButton(null, null);
		super.onPrepareDialogBuilder(builder);
	}

	@Override
	public void onBindDialogView(View view) {
		// Apache 2.0 license
		TextView mLink1 = (TextView) view.findViewById(R.id.open_source_link1);
		mLink1.setText(Html
				.fromHtml("<a href=\"https://github.com/nostra13/Android-Universal-Image-Loader\">Using Universal Image Loader (c) 2011-2013, Sergey Tarasevich</a>"));
		mLink1.setMovementMethod(LinkMovementMethod.getInstance());

		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
		mLink2.setText(Html
				.fromHtml("<a href=\"https://github.com/shontauro/android-pulltorefresh-and-loadmore\">Load More Listview (c) 2012 Fabian Leon</a>"));
		mLink2.setMovementMethod(LinkMovementMethod.getInstance());

		TextView mLink3 = (TextView) view.findViewById(R.id.open_source_link3);
		mLink3.setText(Html
				.fromHtml("<a href=\"https://github.com/JakeWharton/ActionBarSherlock\">ActionBarSherlock developed by Jake Wharton</a>"));
		mLink3.setVisibility(View.VISIBLE);
		mLink3.setMovementMethod(LinkMovementMethod.getInstance());
		TextView apacheLicense = (TextView) view
				.findViewById(R.id.apache_license);
		apacheLicense
				.setText(Html
						.fromHtml("These projects are licensed under the Apache License v2.0.<br><br>"
								+ "You may obtain a copy of the License at:<br><br>"
								+ "<a href=\"http://www.apache.org/licenses/\">http://www.apache.org/licenses</a><br>"));
		apacheLicense.setMovementMethod(LinkMovementMethod.getInstance());

		// Jsoup license
		TextView mLink4 = (TextView) view.findViewById(R.id.open_source_link4);
		mLink4.setText(Html
				.fromHtml("<a href=\"http://jsoup.org/license\">Jsoup</a>"));
		mLink4.setMovementMethod(LinkMovementMethod.getInstance());

		TextView jsoupLicense = (TextView) view
				.findViewById(R.id.jsoup_license);
		jsoupLicense
				.setText(Html
						.fromHtml("<h3>The MIT License</h3>"
								+ "<p>Copyright &copy; 2009 - 2013 <a href=\"http://jonathanhedley.com\">Jonathan Hedley</a> (<a href=\"mailto:jonathan@hedley.net\">jonathan@hedley.net</a>)</p> "
								+ "<p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the &quot;Software&quot;), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:</p> "
								+ "<p>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.</p> "
								+ "<p>THE SOFTWARE IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p> "));
		jsoupLicense.setMovementMethod(LinkMovementMethod.getInstance());

		// Image License
		TextView mLink5 = (TextView) view.findViewById(R.id.open_source_link5);
		mLink5.setText(Html
				.fromHtml("<a href=\"http://www.pixelledesigns.com\">Icons and graphics elements by Happy Icon Studio</a>"));
		mLink5.setMovementMethod(LinkMovementMethod.getInstance());

		TextView imageLicense = (TextView) view
				.findViewById(R.id.image_license);
		imageLicense
				.setText(Html
						.fromHtml("This collection is licensed under the Creative Commons Attribution 3.0 United States License.<br><br>"
								+ "To view a copy of this license, visit:<br><br>"
								+ "<a href=\"http://creativecommons.org/licenses/by/3.0/us/\">http://creativecommons.org/licenses/by/3.0/us/</a><br>"));
		imageLicense.setMovementMethod(LinkMovementMethod.getInstance());
		
		// Image License2
		TextView mLink6 = (TextView) view.findViewById(R.id.open_source_link6);
		mLink6.setText(Html
				.fromHtml("<a href=\"http://www.freepik.com/free-photos-vectors/logo\">Logo vector designed by Freepik</a>"));
		mLink6.setMovementMethod(LinkMovementMethod.getInstance());

		super.onBindDialogView(view);
	}

}
