package com.examples.gg.notificationservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * This class implements an alarm function. It sends the information to
 * AlarmReceiver according to the given time
 * */
public class AlarmService {
	private Context context;
	private PendingIntent mAlarmSender;

	public AlarmService(Context context, String mTitle, int mRand) {
		this.context = context;
		Intent broadcast_intent = new Intent(context, AlarmReceiver.class);
		broadcast_intent.putExtra("msg", mTitle);
		broadcast_intent.putExtra("rand", mRand);
		mAlarmSender = PendingIntent.getBroadcast(context, mRand,
				broadcast_intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void startAlarm(long millseconds) {
		// Schedule the alarm!
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, millseconds, mAlarmSender);
	}
}
