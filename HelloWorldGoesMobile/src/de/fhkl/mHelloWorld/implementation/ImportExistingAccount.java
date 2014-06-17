package de.fhkl.mHelloWorld.implementation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ImportExistingAccount extends Activity implements
		Button.OnClickListener {

	// GUI
	private Button mButtonDownload;
	private TextView mUrl;
	private TextView mUser;
	private TextView mErrorField;
	private static final String I = "======================= [HELLO-WORLD] "
			+ "ImportExsistingAccount" + ": ";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Log.v(I, "test");
		setContentView(R.layout.import_existing_account);

		init();
	}

	// bind GUI
	public void init() {
		mButtonDownload = (Button) findViewById(R.id.import_existing_account_button_download);
		mButtonDownload.setOnClickListener(this);
		mUrl = (EditText) findViewById(R.id.import_existing_account_url);
		mUser = (EditText) findViewById(R.id.import_existing_account_user);
		mErrorField = (TextView) findViewById(R.id.error_field);
	}

	// manage click
	public void onClick(View v) {
		// check input (if empty do nothing -> tell user)
		if ((mUrl.getText().toString() == null || mUrl.getText()
				.toString().equals(""))
				|| (mUser.getText().toString() == null || mUser
						.getText().toString().equals(""))) {
			mErrorField.setText(R.string.login_error_empty);
		} else {
			Intent intent;
			switch (v.getId()) {
			case R.id.import_existing_account_button_download:
				Log.v(I, "click registrated");
				
				int i = v.getId();
				
				// check Url-Syntax
				if (!(mUrl.getText().toString().contains("http://")))
				{
					mUrl.setText("http://" + mUrl.getText().toString());
				}
				// get Account and write it down
				new FileWriter().getAndWriteAccountFromUrl(mUser.getText()
						.toString(), mUrl.getText().toString(), this);
				if (FileWriter.sIsSuccess == true) 
				{
					Toast
					.makeText(
							ImportExistingAccount.this,
							"Account for "
							+ mUser.getText().toString()
							+ " successfully created!",
							Toast.LENGTH_LONG).show();
					
					TextView success_field = (TextView) findViewById(R.id.success_field);
					success_field.setText("Ok. Now you can go back to Login!");
				}

				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		Bundle bundle = intent.getExtras();
		Log.v("===============================", bundle
				.getString("error_value"));
		mErrorField.setText(bundle.getString("error_value"));

	}
}
