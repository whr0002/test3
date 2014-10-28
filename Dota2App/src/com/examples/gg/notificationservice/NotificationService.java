package com.examples.gg.notificationservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.rs.dota.R;
import com.rs.dota.SideMenuActivity;

/**
 * This class implements creation of a notification.
 * */
public class NotificationService extends WakefulIntentService {

	public NotificationService() {
		super("NotificationService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		// Log.i("debug", "in service");
		NotificationManager mNM;
		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SideMenuActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher_o)
				.setContentTitle("The game has started!")
				.setContentText(intent.getStringExtra("msg"))
				.setContentIntent(contentIntent)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setDefaults(Notification.DEFAULT_LIGHTS).setAutoCancel(true);

		mNM = (NotificationManager) this
				.getSystemService(this.NOTIFICATION_SERVICE);

		// Play the ringtone and vibrate
		try {
			Uri notificationURI = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(this, notificationURI);
			r.play();

			Vibrator v = (Vibrator) this
					.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 500 milliseconds
			v.vibrate(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mNM.notify(intent.getIntExtra("rand", 1), mBuilder.build());
	}

}
