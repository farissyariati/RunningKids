package com.mousepad.runningkids;

import com.mousepad.runningkids.util.MPreferencesValues;
import com.mousepad.runningkids.util.ServiceSound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class SplashScreenActivity extends Activity {
	private boolean firstInstalation;
	private MPreferencesValues mpv;
	private Thread loadingSplash;
	
	private static String SOUND_TAG = "MusicID";
	//components
	
	private ProgressBar progressLoad;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		mpv = new MPreferencesValues(this);
		this.firstInstalation = mpv.getConditionOnFirstInstall();
		initProgressBar();
		createSplash();
	}

	private void createSplash() {
		initProgressBar();
		loadingSplash = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					createSound("1");
					checkOnFirstInstalation();
				} catch (Exception e) {
				}
			}
		});
		loadingSplash.start();
	}
	
	private void createSound(String id){
		Intent playSound = new Intent(this, ServiceSound.class);
		playSound.putExtra(SOUND_TAG, id);
		startService(playSound);
	}
	
	private void initProgressBar(){
		this.progressLoad = (ProgressBar)findViewById(R.id.loadingApp);
		this.progressLoad = new ProgressBar(getBaseContext());
		this.progressLoad.setVisibility(ProgressBar.VISIBLE);
		this.progressLoad.setProgress(0);
		this.progressLoad.setMax(10);
	}

	private void checkOnFirstInstalation() {
		if (firstInstalation == true) {
			Intent goToMainPrefs = new Intent(this,
					MainPreferencesActivity.class);
			startActivity(goToMainPrefs);
			finish();
			mpv.changeFlagOnFirstInstall(false);
			joinThread.sendMessage(joinThread.obtainMessage());
		} 
		else {
			Intent goToSecondPrefs = new Intent(this,
					SecondPreferencesActivity.class);
			startActivity(goToSecondPrefs);
			finish();
			joinThread.sendMessage(joinThread.obtainMessage());
		}
	}

	private Handler joinThread = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				loadingSplash.join();
				System.out.println("Thread Join");
			} catch (Exception e) {
				System.out.println("Thread Gagal Join");
				e.printStackTrace();
			}
		}
	};

}
