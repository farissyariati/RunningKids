package com.mousepad.runningkids.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MPreferencesValues {
	// tag collection preferencesI
	static final String TAG_CHILD_NAME = "child_name";
	static final String TAG_ENABLE_SMS = "enable_sms";
	static final String TAG_ENABLE_EMAIL = "enable_email";
	static final String TAG_PHONE_NUMBER_PRIME = "phone_main";
	static final String TAG_PHONE_NUMBER_SECONDARY = "phone_secondary";
	static final String TAG_RECEPIENT_EMAIL_ADDRESS = "recepient_email";
	static final String TAG_SENDER_EMAIL_ADDRESS = "sender_email";
	static final String TAG_SENDER_EMAIL_PASSWORD = "sender_email_password";

	// tag collection preferencesII

	static final String TAG_NOTIFICATION_INTERVAL = "notification_interval";
	static final String TAG_TRAVEL_TIME_LIMIT = "time_limit";
	static final String TAG_MAX_DISTANCE = "max_distance";
	static final String TAG_MESSAGE_CONTENT = "message_content";
	static final String TAG_BATTERY_LIMIT = "battery_limit";
	static final String MESSAGE_CONTENT_DEFAULT = "here's my location: ";

	// tag on first install
	static final String TAG_ON_FIRST_INSTALL = "on_first_install";

	// tag Location
	static final String TAG_LAST_LATITUDE = "last_latitude";
	static final String TAG_LAST_LONGITUDE = "last_longitude";

	// global variable, prefs value
	private String childName;
	private boolean enableSMS;
	private boolean enableEmail;
	private String mainPhoneNumber;
	private String secondaryPhoneNumber;
	private String recepientEmailAddress;
	private String senderEmailAddress;
	private String senderEmailAddressPassword;

	// context & sharedPreferences
	private Context context;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	// constructor
	public MPreferencesValues(Context ctx) {
		this.context = ctx;
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		setDefaultSharedPreferences();
		editor = sharedPreferences.edit();
	}

	private void setDefaultSharedPreferences() {
		this.childName = sharedPreferences.getString(TAG_CHILD_NAME, "derp");
		this.enableSMS = sharedPreferences.getBoolean(TAG_ENABLE_SMS, true);
		this.enableEmail = sharedPreferences
				.getBoolean(TAG_ENABLE_EMAIL, false);
		this.mainPhoneNumber = sharedPreferences.getString(
				TAG_PHONE_NUMBER_PRIME, "");
		this.secondaryPhoneNumber = sharedPreferences.getString(
				TAG_PHONE_NUMBER_SECONDARY, "");
		this.recepientEmailAddress = sharedPreferences.getString(
				TAG_RECEPIENT_EMAIL_ADDRESS, "@gmail.com");
		this.senderEmailAddress = sharedPreferences.getString(
				TAG_SENDER_EMAIL_ADDRESS, "@gmail.com");
		this.senderEmailAddressPassword = sharedPreferences.getString(
				TAG_SENDER_EMAIL_PASSWORD, "");
	}

	// method collection for getting a value in shared preferences;

	public String getChildName() {
		return this.childName;
	}

	public boolean getSMSPermission() {
		return this.enableSMS;
	}

	public boolean getEmailPermission() {
		return this.enableEmail;
	}

	public String getRecepientEmailAddress() {
		return this.recepientEmailAddress;
	}

	public String getSenderEmailAddress() {
		return this.senderEmailAddress;
	}

	public String getSenderEmailAddressPassword() {
		return this.senderEmailAddressPassword;
	}

	public String getMainPhoneNumber() {
		return this.mainPhoneNumber;
	}

	public String getSecondaryPhoneNumber() {
		return this.secondaryPhoneNumber;
	}

	// getPreferences for Manual SharedPreferences

	public int getNotificationInterval() {
		return sharedPreferences.getInt(TAG_NOTIFICATION_INTERVAL, 60);// in
																		// minute
	}

	public int getTravelTimeLimit() {
		return sharedPreferences.getInt(TAG_TRAVEL_TIME_LIMIT, 6); // in hour
	}

	public int getMaxDistance() {
		return sharedPreferences.getInt(TAG_MAX_DISTANCE, 20); // in km
	}

	public int getBatteryLimit() {
		return sharedPreferences.getInt(TAG_BATTERY_LIMIT, 20); // in %
	}

	public String getMessageContent() {
		return sharedPreferences.getString(TAG_MESSAGE_CONTENT,
				MESSAGE_CONTENT_DEFAULT);
	}

	// Manually Set SharedPreferences
	public void setNotificationInterval(int notificationIntervalValue) {
		editor.putInt(TAG_NOTIFICATION_INTERVAL, notificationIntervalValue);
		editor.commit();
	}

	public void setTravelTimeLimit(int timeLimit) {
		editor.putInt(TAG_TRAVEL_TIME_LIMIT, timeLimit);
		editor.commit();
	}

	public void setMaxDistance(int maxDistance) {
		editor.putInt(TAG_MAX_DISTANCE, maxDistance);
		editor.commit();
	}

	public void setMessageContent(String messageContent) {
		editor.putString(TAG_MESSAGE_CONTENT, messageContent);
		editor.commit();
	}

	public void setBatteryLimit(int batteryLimit) {
		editor.putInt(TAG_BATTERY_LIMIT, batteryLimit);
		editor.commit();
	}

	// on FirstInstalation

	public void changeFlagOnFirstInstall(boolean onInstall) {
		editor.putBoolean(TAG_ON_FIRST_INSTALL, onInstall);
		editor.commit();
	}

	public boolean getConditionOnFirstInstall() {
		return sharedPreferences.getBoolean(TAG_ON_FIRST_INSTALL, true);
	}

	// location
	// dalam string, karena floating point lokasi sangat significant
	public void changeLastLocation(double latitude, double longitude) {
		editor.putString(TAG_LAST_LATITUDE, latitude+"");
		editor.putString(TAG_LAST_LONGITUDE, longitude+"");
		editor.commit();
	}

	public double getLastLatitude() {
		return Double.parseDouble(sharedPreferences.getString(
				TAG_LAST_LATITUDE, "0.0"));
	}

	public double getLastLongitude() {
		return Double.parseDouble(sharedPreferences.getString(
				TAG_LAST_LONGITUDE, "0.0"));
	}

}
