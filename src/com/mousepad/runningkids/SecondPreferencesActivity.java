package com.mousepad.runningkids;

import com.mousepad.runningkids.util.MPreferencesValues;
import com.mousepad.runningkids.util.ServiceSound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SecondPreferencesActivity extends Activity {
	private String[] intervalValues;
	private String[] batteryLimitValues;
	private String[] travelTimeValues;
	private String[] maxDistanceValues;

	private Spinner intervalSpinner;
	private Spinner batteryLimitSpinner;
	private Spinner travelTimeSpinner;
	private Spinner maxDistanceSpinner;
	private Button messageContentButton;

	private String selectedInterval;
	private String selectedBatteryLimit;
	private String selectedTravelTime;
	private String selectedMaxDistance;
//	private String maxDistance;
	private String messageContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_preferences);
		Toast.makeText(getBaseContext(), "Set all options to change the preferences",
				Toast.LENGTH_SHORT).show();
		setInterfalPreference();
		setMaxDistancePreference();
		setBatteryLimitPreference();
		setMessageContentPreferences();
		setTravelTimePreference();
		showPreferencesValues();
	}

	private void setInterfalPreference() {
		intervalValues = getResources().getStringArray(
				R.array.notification_values);
		intervalSpinner = (Spinner) findViewById(R.id.interval_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.notification_interfal,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		intervalSpinner.setAdapter(adapter);
		intervalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int selectedItem, long arg3) {
				if (!(selectedItem == 0)) {
					selectedInterval = intervalValues[selectedItem];
					// mpv.setNotificationInterval(Integer.parseInt(selectedInterval.toString()));
					Toast.makeText(getBaseContext(),
							"Report Interval: " + selectedInterval+" Minutes",
							Toast.LENGTH_SHORT).show();
				} else
					selectedInterval = "default";
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setBatteryLimitPreference() {
		batteryLimitValues = getResources().getStringArray(
				R.array.battery_notification_values);
		batteryLimitSpinner = (Spinner) findViewById(R.id.battery_limit_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.notify_battery,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		batteryLimitSpinner.setAdapter(adapter);
		batteryLimitSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int selectedItem, long arg3) {
						if (!(selectedItem == 0)) {
							selectedBatteryLimit = batteryLimitValues[selectedItem];
							Toast.makeText(
									getBaseContext(),
									"Battery Limit: " + selectedBatteryLimit
											+ "%", Toast.LENGTH_SHORT).show();
						} else
							selectedBatteryLimit = "default";
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void setTravelTimePreference() {
		travelTimeValues = getResources().getStringArray(
				R.array.traveling_time_values);
		travelTimeSpinner = (Spinner) findViewById(R.id.travel_time_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.traveling_time_limit,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		travelTimeSpinner.setAdapter(adapter);
		travelTimeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int selectedItem, long arg3) {
						if (!(selectedItem == 0)) {
							selectedTravelTime = travelTimeValues[selectedItem];
							Toast.makeText(
									getBaseContext(),
									"Time Travel: " + selectedTravelTime
											+ " Hour", Toast.LENGTH_SHORT)
									.show();
						} else
							selectedTravelTime = "default";
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	// private void setMaxDistancePreference() {
	// maxDistanceButton = (Button) findViewById(R.id.maxDistanceButton);
	// maxDistanceButton.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// buildMaxDistanceAlert();
	// }
	// });
	// }

	private void setMaxDistancePreference() {
		maxDistanceValues = getResources().getStringArray(
				R.array.traveling_max_distance_values);
		maxDistanceSpinner = (Spinner) findViewById(R.id.max_distance_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.traveling_max_distance,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		maxDistanceSpinner.setAdapter(adapter);
		maxDistanceSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int selectedItem, long arg3) {
						if (!(selectedItem == 0)) {
							selectedMaxDistance = maxDistanceValues[selectedItem];
							Toast.makeText(
									getBaseContext(),
									"Max Distance: " + selectedMaxDistance
											+ " KM", Toast.LENGTH_SHORT).show();
						} else
							selectedMaxDistance = "default";
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void setMessageContentPreferences() {
		messageContentButton = (Button) findViewById(R.id.messageContentButton);
		messageContentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buildMessageContentAlert();
			}
		});
	}

	private void buildMessageContentAlert() {
		AlertDialog.Builder maxDistanceAlert = new AlertDialog.Builder(this);
		maxDistanceAlert.setTitle("Message Content");
		final EditText inputMessageContent = new EditText(this);
		maxDistanceAlert.setView(inputMessageContent);
		maxDistanceAlert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						messageContent = inputMessageContent.getText()
								.toString();
						Toast.makeText(getBaseContext(),
								"Pesan: " + messageContent, Toast.LENGTH_LONG)
								.show();
					}
				});
		maxDistanceAlert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// Do nothing;
					}
				});
		maxDistanceAlert.show();
	}

	private void showPreferencesValues() {
		Button showPreferencesValues = (Button) findViewById(R.id.showValuesButton);
		showPreferencesValues.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setPreferences();
				createSounds("2");
				goToMainApplication();
				//goToPreferencesValues();
			}
		});
	}

	private void goToMainApplication() {
		Intent mainApp = new Intent(getBaseContext(),
				MainApplicationActivity.class);
		startActivity(mainApp);
		finish();
	}

//	private void goToPreferencesValues() {
//		Intent showValues = new Intent(getBaseContext(),
//				ShowPreferencesValues.class);
//		startActivity(showValues);
//		finish();
//	}

//	private void setPreferencesX() {
//		if (selectedInterval.equals("default")
//				|| selectedBatteryLimit.equals("default")
//				|| selectedTravelTime.equals("default")
//				|| selectedMaxDistance.equals("default")) {
//		}
//		else{
//			 MPreferencesValues mpv = new MPreferencesValues(this);
//			 mpv.setNotificationInterval(Integer.parseInt(this.selectedInterval));
//			 mpv.setBatteryLimit(Integer.parseInt(this.selectedBatteryLimit));
//			 mpv.setTravelTimeLimit(Integer.parseInt(this.selectedTravelTime));
//			 mpv.setMessageContent(this.messageContent);
//			 // mpv.setMaxDistance(Integer.parseInt(this.maxDistance));
//			 mpv.setMaxDistance(Integer.parseInt(this.selectedMaxDistance));
//		}
//	}
	
	private void setPreferences(){
		MPreferencesValues mpv = new MPreferencesValues(this);
		
		if(!this.selectedInterval.equals("default"))
			mpv.setNotificationInterval(Integer.parseInt(this.selectedInterval));
		if(!this.selectedBatteryLimit.equals("default"))
			mpv.setBatteryLimit(Integer.parseInt(this.selectedBatteryLimit));
		if(!this.selectedTravelTime.equals("default"))
			mpv.setTravelTimeLimit(Integer.parseInt(this.selectedTravelTime));
		if(!this.selectedMaxDistance.equals("default"))
			mpv.setMaxDistance(Integer.parseInt(this.selectedMaxDistance));
		
		if(this.messageContent != null)
			mpv.setMessageContent(this.messageContent);
			
	}

	private void createSounds(String id) {
		Intent createSound = new Intent(getBaseContext(), ServiceSound.class);
		createSound.putExtra("MusicID", id);
		startService(createSound);
	}
}
