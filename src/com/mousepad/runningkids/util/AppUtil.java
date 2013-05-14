package com.mousepad.runningkids.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class AppUtil {
	private Context context;
	private Intent batteryIntent;
	
	public AppUtil(Context ctx) {
		this.context = ctx;
		this.batteryIntent = initializeBattery();
	}

	public Intent initializeBattery(){
		Intent nowBatteryIntent = this.context.getApplicationContext()
				.registerReceiver(null,
						new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		return nowBatteryIntent;
	}
	
	public int getBatteryLevel(){
		return batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	}
	
	public void deleteCache() {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
