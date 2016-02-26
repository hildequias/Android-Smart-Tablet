package com.pixonsoft.dailyselfielab.broadcastReceiver;

import com.pixonsoft.dailyselfie.R;
import com.pixonsoft.dailyselfielab.MainActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class DailySelfieBroadcastReceiver extends BroadcastReceiver{
	
	private static final int TAG_NOTIFICATION_ID = 1;
	
	public void startAlarm(Context context){
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent alarmIntent = getAlarmIntent(context);
		alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + (60 * 1000),
				60 * 1000, 
				alarmIntent);
	}
	
	public void cancelAlarm(Context context)
	{
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent alarmIntent = getAlarmIntent(context);
		
		alarmMgr.cancel(alarmIntent);
	}
	
	private PendingIntent getAlarmIntent(Context context)
	{
		Intent intent = new Intent(context, DailySelfieBroadcastReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		return alarmIntent;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent intentNotification = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, 
				intentNotification, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		long[] vibratePattern = { 0, 200, 200, 300 };
		
		// Define the Notification's
		Notification.Builder notificationBuilder = new Notification.Builder(
				context)
				.setAutoCancel(true)
				.setContentTitle("Daily Selfie")
				.setContentText("Time for another selfie")
				.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.icon_selfie_daily)
				.setVibrate(vibratePattern);

		// NotificationManager receiving Notification
		NotificationManager mNotificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// I used 'getNotigication' because 'build' is only API maxSdkVersion 16.
		mNotificationManager.notify(TAG_NOTIFICATION_ID, notificationBuilder.getNotification());
	}

}
