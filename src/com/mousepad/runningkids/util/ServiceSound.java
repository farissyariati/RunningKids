package com.mousepad.runningkids.util;

import com.mousepad.runningkids.R;

import android.app.Service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServiceSound extends Service {
	private static final String TAG = "MusicID";
	
	MediaPlayer player;
	
	@Override
	public IBinder onBind (Intent intent){
		return null;
	}
	@Override
	public void onCreate(){
		
	}
	@Override
	public void onDestroy(){
		player.stop();
	}
	@Override
	public void onStart(Intent intent, int startid){
		String idS = intent.getStringExtra(TAG);
		int id = Integer.parseInt(idS);
		
		if(id == 1){
			player = MediaPlayer.create(this, R.raw.sfxa);
			player.setLooping(false);
			player.start();
		}
		else if(id == 2){
			player = MediaPlayer.create(this, R.raw.sfxb);
			player.setLooping(false);
			player.start();
		}
		else if(id == 3){
			player = MediaPlayer.create(this, R.raw.sfxc);
			player.setLooping(false);
			player.start();
		}
	}
	
}
