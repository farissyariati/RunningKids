package com.mousepad.runningkids;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainPreferencesActivity extends PreferenceActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preferences);
		Toast.makeText(getBaseContext(), "Press menu button",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Next").setIcon(R.drawable.next);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent goToSecondPreferences = new Intent(getBaseContext(),
					SecondPreferencesActivity.class);
			startActivity(goToSecondPreferences);
			finish();
			break;
		default:
			break;
		}
		return true;
	}
}