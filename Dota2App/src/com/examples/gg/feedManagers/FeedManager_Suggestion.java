package com.examples.gg.feedManagers;

import java.util.ArrayList;

import org.json.JSONArray;

import android.util.Log;

public class FeedManager_Suggestion extends FeedManager_Base {

	private JSONArray whole;
	private ArrayList<String> words;
	private JSONArray items;

	public ArrayList<String> getSuggestionList() {
		try {
			words = new ArrayList<String>();
			processJSON(mJSON);

			items = whole.getJSONArray(1);
			
			for (int i = 0; i < items.length(); i++) {
				words.add(items.getJSONArray(i).getString(0));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return words;
	}

	// convert a JSON string to a JSON object
	@Override
	protected void processJSON(String json) {
		try {
			whole = new JSONArray(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
