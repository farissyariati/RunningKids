package com.mousepad.runningkids;

import java.util.Timer;
import java.util.TimerTask;

import com.mousepad.runningkids.report.GMailSender;
import com.mousepad.runningkids.report.SMSService;
import com.mousepad.runningkids.util.AppUtil;
import com.mousepad.runningkids.util.MPreferencesValues;
import com.mousepad.runningkids.util.MyLocationManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainApplicationActivity extends Activity implements
		OnClickListener {
	// boolean var
	private boolean startApp = false;
	boolean onLoading = true;
	private boolean onSendLowBattery = false;
	private boolean onSendTooFarDistance = false;

	// preferences values variable
	private String childsName;
	private int maxTravelTime;
	private boolean enableSMS;
	private String SMSMessage;
	private boolean enableEmail;
	private String sender;
	private String senderPassword;
	private String destination;
	private int batteryLimit;
	private int maxDistance;
	private String parentsPhoneNumber;

	private String emailSubject = "Running Kids Report: ";

	private int interval;
	static final String TAG_MESSAGE_CONTENT = "message_content";
	static final String APPLICATION_VERSION = "V.5 wildkids";
	private final String ADDITIONAL_MESSAGE_FOOTER = "Sent from Running Kids "
			+ APPLICATION_VERSION;

	// components
	private View startButton;
	private View settingButton;
	private View emergencyMessageButton;
	private View emergencyCallButton;
	private View usageStatistic;
	private ProgressDialog locationProgress;
	private TextView time;

	// temp components
	private TextView locationText;
	private TextView batteryInfo;

	// spatial data
	private double childLatitude = 0.0;
	private double childLongitude = 0.0;
	private String childlLocation = "";
	private double firstLatitude = 0.0;
	private double firstLongitude = 0.0;

	// util
	private MyLocationManager mlm;
	private MPreferencesValues mpv;
	private Timer timerLocationUpdater;
	private Timer timerNotificationByInterval;
	private AppUtil util;
	/* try */
	private Timer timerUpdateBattery;
	private Timer timerUpdateDistance;
	private CountDownTimer countDown;
	private int batteryLevel;
	//private int servcieTimeCounter;

	// thread
	private Thread backgroundLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_app);
		initComponents();
		getPreferencesValue();
		loading();
	}

	// location method
	private void onLocationChanged() {
		mlm = new MyLocationManager(getBaseContext());
		mlm.onLocationUpdate();
		activateLocationUpdater(10000);
	}

	private void onInformationLocationUpdate() {
		this.childLatitude = mlm.getMyLatitude();
		this.childLongitude = mlm.getMyLongitude();
		this.childlLocation = mlm.getLocation();
		// constructLocationHandler();
	}

	private void activateLocationUpdater(int updateLocationInterval) {
		timerLocationUpdater = new Timer();
		timerLocationUpdater.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Location Updater");
				onInformationLocationUpdate();
			}
		}, 0, updateLocationInterval);
	}

	// private void constructLocationHandler() {
	// handlerLocationInfo.sendMessage(handlerLocationInfo.obtainMessage());
	// }

	// private Handler handlerLocationInfo = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Lat: " + childLatitude + "\n");
	// sb.append("Lon: " + childLongitude + "\n");
	// sb.append("Loc: " + childlLocation + "\n");
	// sb.append("Old Lat & Lon :\n" + oldLatitude + "\n" + oldLongitude);
	// locationText.setText(sb.toString());
	// }
	// };

	private void loadingLocationForFirstTime() {
		locationProgress = new ProgressDialog(this);
		locationProgress.setMessage("Searching Location");
		locationProgress.setCancelable(true);
		locationProgress.show();
	}

	private void initFirstLocation() {
		this.firstLatitude = this.childLatitude;
		this.firstLongitude = this.childLongitude;
	}

	private void checkRadiusLocation() {
		Location nowLocation = new Location("now_location");
		nowLocation.setLatitude(this.childLatitude);
		nowLocation.setLongitude(this.childLongitude);

		Location firstLocation = new Location("first_location");
		firstLocation.setLatitude(this.firstLatitude);
		firstLocation.setLongitude(this.firstLongitude);

		int distance = (int) firstLocation.distanceTo(nowLocation) / 1000;
		if (distance > maxDistance && onSendTooFarDistance == false) {
			onTooFarDistanceSendSMS();
			onSendTooFarDistance = true;

		} else {
			// put your jetpack
			System.out.println("No SMS Sent due the distance");
			System.out.println("Now Distance: " + distance + "/ MaxDistance: "
					+ maxDistance);
		}

	}

	private void loading() {
		loadingLocationForFirstTime();
		backgroundLoading = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
						loadingIndicatorHandler
								.sendMessage(loadingIndicatorHandler
										.obtainMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		backgroundLoading.start();
		mlm = new MyLocationManager(getBaseContext());
		mlm.onLocationUpdate();
		childLatitude = mlm.getMyLatitude();
		childLongitude = mlm.getMyLongitude();
		childlLocation = mlm.getLocation();
	}

//	private void waitingCounter() {
//		final Thread waiting = new Thread() {
//			@Override
//			public void run() {
//				while (true) {
//					servcieTimeCounter++;
//
//					try {
//						Thread.sleep(1000);
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}
//			}
//		};
//	}

	private Handler loadingIndicatorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (childLatitude != 0.0 && childLongitude != 0.0) {
				locationProgress.dismiss();
				onLoading = false;
			}
		}
	};

	// notification method
	private void activateNotificationByInterval(int notificationInterval) {
		timerNotificationByInterval = new Timer();
		timerNotificationByInterval.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// coba
				// if (oldLatitude != childLatitude
				// && oldLongitude != childLongitude) {
				if ((enableSMS == true)) {
					sendSMS();
				}

				if ((enableEmail == true)) {
					GMailSender email = new GMailSender(sender, senderPassword);
					try {
						email.sendMail(emailSubject + childsName, SMSMessage
								+ " " + childlLocation + "\nLatitude: "
								+ childLatitude + "\nLongitude: "
								+ childLongitude, sender, destination);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// buat method untuk menangkap lokasi pertama
				// } else {
				// System.out.println("Lokasi Lama Berpengaruh");
				// }

			}
		}, 0, notificationInterval);
	}

	// try battery
	private void activateBatteryListener(int batteryListenerInterval) {
		timerUpdateBattery = new Timer();
		timerUpdateBattery.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Test Listen");
				util = new AppUtil(getBaseContext());
				batteryLevel = util.getBatteryLevel();
				batteryHandler.sendMessage(batteryHandler.obtainMessage());
				if ((batteryLevel <= batteryLimit)
						&& (onSendLowBattery == false)) {
					System.out.println("Battery Limit: " + batteryLimit
							+ " Battery Level: " + batteryLevel);
					onBatteryLowSendSMS();
					onSendLowBattery = true;
				} else {
					System.out.println("Battery Limit: " + batteryLimit
							+ " Battery Level: " + batteryLevel);
					System.out.println("Low Battery SMS not Sent");
				}

			}
		}, 0, batteryListenerInterval);
	}

	private Handler batteryHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			batteryInfo.setText(batteryLevel + "%");
		}
	};

	private void activateUpdateDistance(int distanceListenerInterval) {
		timerUpdateDistance = new Timer();
		timerUpdateDistance.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Distance Listener");
				checkRadiusLocation();
			}
		}, 0, distanceListenerInterval);
	}

	private void getPreferencesValue() {
		mpv = new MPreferencesValues(this);

		enableSMS = mpv.getSMSPermission();
		SMSMessage = mpv.getMessageContent();

		// email
		enableEmail = mpv.getEmailPermission();
		sender = mpv.getSenderEmailAddress();
		senderPassword = mpv.getSenderEmailAddressPassword();
		destination = mpv.getRecepientEmailAddress();

		interval = mpv.getNotificationInterval() * 60000;
		childsName = mpv.getChildName();

		// old latitude & longitude
		maxTravelTime = mpv.getTravelTimeLimit();

		// battery
		batteryLimit = mpv.getBatteryLimit();

		// maxdistance
		maxDistance = mpv.getMaxDistance();

		// phoneNUmber;
		parentsPhoneNumber = mpv.getMainPhoneNumber();
	}

	private void sendSMS() {
		Intent sendMessage = new Intent(this, SMSService.class);
		sendMessage.putExtra(TAG_MESSAGE_CONTENT, SMSMessage + " "
				+ this.childlLocation + "\nLatitude: " + this.childLatitude
				+ "\nLongitude: " + this.childLongitude + "\n\n"
				+ this.ADDITIONAL_MESSAGE_FOOTER);
		startService(sendMessage);
	}

	private void sendSMSDebug() {
		String message = SMSMessage + " " + this.childlLocation + "\nLat: "
				+ this.childLatitude + "\nLong: " + this.childLongitude
				+ "\n\n" + ADDITIONAL_MESSAGE_FOOTER;
		Intent sendMessageOnDebug = new Intent(this, SMSService.class);
		sendMessageOnDebug.putExtra(TAG_MESSAGE_CONTENT, message);
		startService(sendMessageOnDebug);
	}

	private void constructIntentCall() {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + parentsPhoneNumber));
		startActivity(callIntent);
	}

	private void sendEmergencyMessage() {
		// String message =
		// "Your child send you an urgent message, please call him/her back\n\n"
		// + this.ADDITIONAL_MESSAGE_FOOTER;
		// Intent emergencyMessage = new Intent(this, SMSService.class);
		// emergencyMessage.putExtra(TAG_MESSAGE_CONTENT, message);
		// startService(emergencyMessage);
		sendSMSDebug();
	}

	private void onBatteryLowSendSMS() {
		String message = "Your child's phone battery is low. Please remind him/her\n\n"
				+ this.ADDITIONAL_MESSAGE_FOOTER;
		Intent lowBattery = new Intent(this, SMSService.class);
		lowBattery.putExtra(TAG_MESSAGE_CONTENT, message);
		startService(lowBattery);
	}

	private void onTooFarDistanceSendSMS() {
		String message = "Your child travel too far\nLatitude: "
				+ this.childLatitude + "\nLongitude: " + this.childLongitude
				+ "Please remind him/her\n\n" + this.ADDITIONAL_MESSAGE_FOOTER;
		Intent distanceSMS = new Intent(this, SMSService.class);
		distanceSMS.putExtra(TAG_MESSAGE_CONTENT, message);
		startService(distanceSMS);
	}

	// components

	private void initComponents() {
		// temp
		locationText = (TextView) findViewById(R.id.locationInfo);
		locationText.setTypeface(Typeface.createFromAsset(getAssets(),
				"DS-DIGIB.TTF"));
		batteryInfo = (TextView) findViewById(R.id.tempBatteryInfo);
		batteryInfo.setTypeface(Typeface.createFromAsset(getAssets(),
				"DS-DIGIB.TTF"));
		//
		time = (TextView) findViewById(R.id.time);
		startButton = (View) findViewById(R.id.start);
		startButton.setOnClickListener(this);
		settingButton = (View) findViewById(R.id.setting);
		settingButton.setOnClickListener(this);
		emergencyMessageButton = (View) findViewById(R.id.message);
		emergencyMessageButton.setOnClickListener(this);
		emergencyCallButton = (View) findViewById(R.id.call);
		emergencyCallButton.setOnClickListener(this);
		usageStatistic = (View) findViewById(R.id.usage);
		usageStatistic.setOnClickListener(this);
	}

	private void startRunningKidsService() {
		startApp = !startApp;
		if (startApp == true) {
			this.enableSMS = mpv.getSMSPermission();
			// locationHandler.handleMessage(locationHandler.obtainMessage());
			startButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.stop_app));
			locationText.setText("Service is Running..\n" + "Interval: "
					+ interval / 60000 + " minutes");
			countDown(this.maxTravelTime);
			onLocationChanged();
			initFirstLocation();
			activateBatteryListener(5000);
			activateUpdateDistance(4000);
			activateNotificationByInterval(interval);

		} else {
			enableSMS = false;
			stop();
			countDown.cancel();
			time.setText("Timer");
			locationText.setText("Service Off");
			batteryInfo.setText("Battery %");
			Toast.makeText(getBaseContext(), "Location Listener Disabled",
					Toast.LENGTH_SHORT).show();
			startButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.start_app));
		}
	}

	private void stop() {
		if (mlm != null)
			mlm.stopLocationListenerService();
		if (timerLocationUpdater != null)
			timerNotificationByInterval.cancel();
		if (timerLocationUpdater != null)
			timerLocationUpdater.cancel();
		if (timerUpdateBattery != null)
			timerUpdateBattery.cancel();
		if (timerUpdateDistance != null)
			timerUpdateDistance.cancel();
	}

	private void constructExitAlert() {
		String message = "Exit Application ?";
		AlertDialog.Builder exitAlert = new AlertDialog.Builder(this);
		exitAlert.setMessage(message);
		exitAlert.setPositiveButton("Exit",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						AppUtil clearCache = new AppUtil(getBaseContext());
						clearCache.deleteCache();
						stop();
						finish();
						Toast.makeText(getBaseContext(), "Exit Running Kids",
								Toast.LENGTH_SHORT).show();
					}
				});

		exitAlert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		exitAlert.show();
	}

	private void constructEmergencyMessageAlert() {
		String message = "Send Emergency SMS ?";
		AlertDialog.Builder emergencyMessageBuilder = new AlertDialog.Builder(
				this);
		emergencyMessageBuilder.setMessage(message);
		emergencyMessageBuilder.setPositiveButton("Send",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						sendEmergencyMessage();
					}
				});
		emergencyMessageBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// put your jetpack
					}
				});
		emergencyMessageBuilder.show();
	}

	private void constructEmergencyCall() {
		String message = "Call Your Parents ??";
		AlertDialog.Builder emergencyCallBuilder = new AlertDialog.Builder(this);
		emergencyCallBuilder.setMessage(message);
		emergencyCallBuilder.setPositiveButton("Call",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						constructIntentCall();
					}
				});
		emergencyCallBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// put your jetpack
					}
				});
		emergencyCallBuilder.show();
	}

	// private void constructNonActivatedFeatureAlert() {
	// String message = "This feature has not been activated yet.";
	// AlertDialog.Builder nonActivatedBuilder = new AlertDialog.Builder(this);
	// nonActivatedBuilder.setIcon(R.drawable.warning);
	// nonActivatedBuilder.setTitle("Feature Disabled");
	// nonActivatedBuilder.setMessage(message);
	// nonActivatedBuilder.show();
	// }

	private void constructAboutAlert() {
		String message = "RunningKids\n2012 MousePad Development";
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
		aboutDialog.setIcon(R.drawable.warning);
		aboutDialog.setTitle("About RunningKids");
		aboutDialog.setMessage(message);
		aboutDialog.show();
	}

	private void countDown(int maxTravelTime) {
		maxTravelTime *= 3600000;
		countDown = new CountDownTimer(maxTravelTime, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				time.setText(millisUntilFinished / 3600000 + " : "
						+ (millisUntilFinished / 60000) % 60 + " : "
						+ (millisUntilFinished / 1000) % 60);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				stop();
				constructExitAlert();
			}
		};
		countDown.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			constructExitAlert();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start:
			startRunningKidsService();
			break;
		case R.id.setting:
			stop();
			Intent setting = new Intent(getBaseContext(),
					MainPreferencesActivity.class);
			startActivity(setting);
			finish();
			break;
		case R.id.message:
			constructEmergencyMessageAlert();
			break;
		case R.id.call:
			constructEmergencyCall();
			break;
		case R.id.usage:
			// constructNonActivatedFeatureAlert();
			constructAboutAlert();
			break;
		default:
			break;
		}
	}

}
