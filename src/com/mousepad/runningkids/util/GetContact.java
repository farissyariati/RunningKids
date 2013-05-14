//bisa dipakai sewaktu2 untuk mendapatkan kontak dari system
package com.mousepad.runningkids.util;

import com.mousepad.runningkids.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GetContact extends Activity {
	private String[] intervalValues;
	private Spinner intervalSpinner;
	private Button pickButton;
	private String selected;
	private final int PICK = 1;

	String name;
	String phoneNumber;
	String emailAddress;
	String poBox;
	String street;
	String city;
	String state;
	String postalCode;
	String country;
	String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_preferences);
		setSpinner();
		setContactPicker();
	}

	private void setSpinner() {
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
				selected = intervalValues[selectedItem];
				Toast.makeText(getBaseContext(), "Nilai Interval: " + selected,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setContactPicker() {
		//harus diaktifkan
		//pickButton = (Button) findViewById(R.id.pickContactButton);
		pickButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK);
			}
		});
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent intent) {
		if (reqCode == PICK) {
			getContactInfo(intent);
			Toast.makeText(getBaseContext(), "Contact Number: " + phoneNumber,
					Toast.LENGTH_LONG).show();
			// Your class variables now have the data, so do something with it.
		}
	}

	protected void getContactInfo(Intent intent) {
		Cursor cursor = managedQuery(intent.getData(), null, null, null, null);
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

			String hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.equalsIgnoreCase("1"))
				hasPhone = "true";
			else
				hasPhone = "false";

			if (Boolean.parseBoolean(hasPhone)) {
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				phones.close();
			}

			// Find Email Addresses
			Cursor emails = getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
							+ contactId, null, null);
			while (emails.moveToNext()) {
				emailAddress = emails
						.getString(emails
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			}
			emails.close();

			Cursor address = getContentResolver()
					.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
									+ " = " + contactId, null, null);
			while (address.moveToNext()) {
				// These are all private class variables, don't forget to create
				// them.
				poBox = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
				street = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
				city = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
				state = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
				postalCode = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
				country = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
				type = address
						.getString(address
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
			} // address.moveToNext()
		} // while (cursor.moveToNext())
		cursor.close();
	}
}
