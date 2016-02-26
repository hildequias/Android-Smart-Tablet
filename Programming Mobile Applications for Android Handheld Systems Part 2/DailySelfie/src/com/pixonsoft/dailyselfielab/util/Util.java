package com.pixonsoft.dailyselfielab.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.pixonsoft.dailyselfielab.broadcastReceiver.DailySelfieBroadcastReceiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Util {
	
	private static final int THUMBNAIL_SIZE = 36;
	DailySelfieBroadcastReceiver selfieAlarm;
	
	// Transforms a File in a Bitmap
	public Bitmap getBitmap(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			int larguraTmp = o.outWidth, height_tmp = o.outHeight;
			while (true) {
				if (larguraTmp / 2 < THUMBNAIL_SIZE || height_tmp / 2 < THUMBNAIL_SIZE)
					break;
				larguraTmp /= 2;
				height_tmp /= 2;
			}
			// Create a bitmap
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	
	// Cancel the alarm in the app. Should be called from onResume method
	public void calcelNotification(Context context){
		selfieAlarm = new DailySelfieBroadcastReceiver(); 
		selfieAlarm.cancelAlarm(context);
	}
	
	// start repeating alarm while the app is stopped. Should be called from onStop method
	public void turnNotification(Context context){
		selfieAlarm = new DailySelfieBroadcastReceiver();
		selfieAlarm.startAlarm(context);
	}
	
	
}
