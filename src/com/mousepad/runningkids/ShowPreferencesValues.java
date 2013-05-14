package com.mousepad.runningkids;

import com.mousepad.runningkids.util.MPreferencesValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowPreferencesValues extends Activity {
	private TextView preferencesInformation;
	MPreferencesValues mpv;
	StringBuilder sb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferencesInformation = new TextView(getBaseContext());
		preferencesInformation.setText(constructSharedPreferencesInformation());
		setContentView(preferencesInformation);
	}

	private String constructSharedPreferencesInformation() {
		mpv = new MPreferencesValues(getBaseContext());
		sb = new StringBuilder();
		sb.append("Child's Name : " + mpv.getChildName() + "\n");
		sb.append("SMS Permission: " + mpv.getSMSPermission() + "\n");
		sb.append("Primary Phone Number: " + mpv.getMainPhoneNumber() + "\n");
		sb.append("Secondary Phone Number: " + mpv.getSecondaryPhoneNumber()
				+ "\n");
		sb.append("Email Permission: " + mpv.getEmailPermission() + "\n");
		sb.append("Recepient Email Address: " + mpv.getRecepientEmailAddress()
				+ "\n");
		sb.append("Sender Email Address: " + mpv.getSenderEmailAddress() + "\n");
		sb.append("Sender Password: " + mpv.getSenderEmailAddressPassword()
				+ "\n\n"+"2nd Preferences Value: \n");
		sb.append("Notification Interval: "+mpv.getNotificationInterval()+"\n");
		sb.append("Battery Limit: "+mpv.getBatteryLimit()+"\n");
		sb.append("Travel Time: "+mpv.getTravelTimeLimit()+"\n");
		sb.append("Max Distance: "+mpv.getMaxDistance()+"\n\n");
		sb.append("Message Content: "+mpv.getMessageContent()+"\n");
		
		return sb.toString();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "GO TO APPS");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case 0:
			goToMainApplication();
			break;

		default:
			break;
		}
		return true;
	}
	
	private void goToMainApplication(){
		Intent mainApp = new Intent(getBaseContext(), MainApplicationActivity.class);
		startActivity(mainApp);
		finish();
	}

}
