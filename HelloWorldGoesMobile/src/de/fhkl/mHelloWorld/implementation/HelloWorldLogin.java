package de.fhkl.mHelloWorld.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

import com.google.android.maps.GeoPoint;

import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.interfaces.actions.NoAccountException;
import de.fhkl.helloWorld.interfaces.actions.WrongLoginException;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.mHelloWorld.implementation.debug.OwnKeysTest;
import de.fhkl.mHelloWorld.implementation.util.ExportAccount;
import de.fhkl.mHelloWorld.implementation.util.LogOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Application Name: Generic Login Screen for the Android Platform (back end)
 * Description: This is a generic login screen which catches the username and
 * password values Created on: November 23, 2007 Created by: Pogz Ortile
 * Contact: pogz(at)redhat(dot)polarhome(dot)com Notes: The string values for
 * username and password are assigned to sUserName and sPassword respectively
 * You arve free to distribute, modify, and wreck for all I care. GPL ya!
 */

public class HelloWorldLogin extends Activity implements Button.OnClickListener {

	private static final String I = "======================= [HELLO-WORLD] "
			+ "HelloWorldLogin" + ": ";
	private boolean DEBUGGING = true;

	private Button mAcceptButton;
	private TextView mUsername;
	private TextView mPassword;
	private TextView mError;

	// IDs for the login-menu. Includes for example "about", "help", etc.
	private static final int CREATE_ID = Menu.FIRST;
	private static final int IMPORT_ID = Menu.FIRST + 1;
	private static final int HELP_ID = Menu.FIRST + 2;

	// for more userfriendly responses
	protected static ProgressDialog sProgressDialog;
	private static boolean sErrNoAcc = false;
	private static boolean sErrPw = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.login);
		System.setErr(new PrintStream(new LogOutputStream("System.err")));
		System.setOut(new PrintStream(new LogOutputStream("System.out")));
		init();
	}

	public void init() {
		System.out
				.println("Timestamp: " + new Date(System.currentTimeMillis()));
		// Main-Dir for Application for Basic-Class
		HelloWorldBasic.setPath(this.getFileStreamPath("").getAbsolutePath()
				+ "/../");
		Log.i(I, "current Directory: " + HelloWorldBasic.getPath());
		mAcceptButton = (Button) findViewById(R.id.login_button_login);
		mAcceptButton.setOnClickListener(this);
		mUsername = (EditText) findViewById(R.id.login_username_input);
		mPassword = (EditText) findViewById(R.id.login_password_input);
		mError = (TextView) findViewById(R.id.error_field);

		// if a user has just logged out
		if (HelloWorldBasic.isLoggedOut()) {
			mUsername.setText("");
			mPassword.setText("");
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button_login:
			Log.v(I, "Klick auf Login-Button registriert");
			mError.setText("");

			// make sure that folder "files" exists
			FileWriter.prepare(this);

			// get view text
			HelloWorldBasic.setPassword(mPassword.getText().toString());
			HelloWorldBasic.setUsername(mUsername.getText().toString());

			// check input (if empty do nothing -> tell user)
			if ((mPassword.getText().toString() == null || mPassword.getText()
					.toString().equals(""))
					|| (mUsername.getText().toString() == null || mUsername
							.getText().toString().equals(""))) {
				mError.setText(R.string.login_error_empty);
			}
			// ok input not empty, go ahead - check input...
			else {
				// Progressbar
				sProgressDialog = ProgressDialog.show(HelloWorldLogin.this,
						"Login-Check", "Try to decrypt your Account ...", true,
						false);

				// new thread (for progressbar)
				new Thread() {
					public void run() {
						try {
							AccountManager ac = new AccountManager();
							Intent intent;
							try {

								// login (may cause Exception)
								HelloWorldBasic.userAccount = ac.login(
										mUsername.getText().toString(),
										mPassword.getText().toString());

								infoHandler.sendEmptyMessage(0);

								// give account to others
								HelloWorldBasicList.userAccount = HelloWorldBasic.userAccount;
								HelloWorldBasicMap.userAccount = HelloWorldBasic.userAccount;

								// setting user-dir
								HelloWorldBasic.setUserDir(HelloWorldBasic
										.getPath()
										+ mUsername.getText() + "/");
								HelloWorldBasic.setUserTempDir(HelloWorldBasic
										.getPath()
										+ mUsername.getText() + "/");
								HelloWorldBasic.setUsername(mUsername.getText()
										.toString());
								Log.i(I, "User-Dir: "
										+ HelloWorldBasic.getUserDir());

								// set LoggedOut to false (default)
								if (HelloWorldBasic.isLoggedOut()) {
									HelloWorldBasic.setLoggedOut(false);
								}
								// register login for back-button etc.
								HelloWorldBasic.setLoggedIn(true);

								if (DEBUGGING) {
									new OwnKeysTest();
								}

								if(Helper.settingsSet())
								{
								intent = new Intent(HelloWorldLogin.this,
										ShowOwnHCard.class);
								startActivity(intent);
								}
								if(!Helper.settingsSet())
								{
									intent = new Intent(HelloWorldLogin.this,
											SettingsChooser.class);
									startActivity(intent);
								}

							} catch (WrongLoginException e) {
								sErrPw = true;
								e.printStackTrace();
							} catch (NoAccountException e) {
								sErrNoAcc = true;
								e.printStackTrace();
							}
						} catch (Exception e) {
						}
						// important for progressbar (dismiss)
						handler.sendEmptyMessage(0);
					}
				}.start();

			}

		}
	}

	// handler needed to be able inside the thread to
	// modify text of the view on the run
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// Dismiss the Dialog
			sProgressDialog.dismiss();
			// Error-Response for user (need to be in that way because of the
			// thread)
			if (sErrPw) {
				mError.setText(R.string.login_error_pw);
				sErrPw = false;
			} else if (sErrNoAcc) {
				mError.setText(R.string.login_error_noaccount);
				sErrNoAcc = false;
			}
			if (HelloWorldBasic.isLoggedIn()) {
				HelloWorldBasic.setShowWelcome(true);
			}
		}
	};

	// just for info
	private Handler infoHandler = new Handler() {
		public void handleMessage(Message msg) {
			mError.setText("Ok. Loading your Account ...");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		Bundle bundle = intent.getExtras();

		TextView error_field = (TextView) findViewById(R.id.error_field);
		error_field.setText(bundle.getString("error_value"));

		if (HelloWorldBasic.isLoggedOut()) {
			mUsername.setText("");
			mPassword.setText("");
			HelloWorldBasic.setLoggedOut(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuItem item;

		item = menu.add(0, CREATE_ID, 0, R.string.menuitem_create);
		item.setIcon(R.drawable.new_profile);

		item = menu.add(0, IMPORT_ID, 0, R.string.menuitem_import);
		item.setIcon(R.drawable.import_profile);

		item = menu.add(0, HELP_ID, 0, R.string.menuitem_help);
		item.setIcon(R.drawable.show_info);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {

		case IMPORT_ID:
			// Bundle bundle = new Bundle();
			// bundle.putString("username", mUsername.getText().toString());
			// bundle.putString("password", mPassword.getText().toString());
			intent = new Intent(this, ImportChooser.class);
			this.startActivity(intent);
			return true;

		case CREATE_ID:
			intent = new Intent(this, CreateAccount.class);
			this.startActivity(intent);
			return true;

		case HELP_ID:
			 intent = new Intent(this, About.class);
			 this.startActivity(intent);
			 return true;
		}
		return false;
	}

	// protected void onDestroy()
	// {
	// super.onDestroy();
	// Log.i(I, "HelloWorldLogin-Activity destroyed.");
	// }

	private void checkLogState() {
		if (HelloWorldBasic.isLoggedIn()) {
			HelloWorldBasic.setShowUseLogout(true);
			// if still logged in, redirect user to HCard
			Intent intent = new Intent(this, ShowOwnHCard.class);
			startActivity(intent);
		}
	}

	// protected void onStart() {
	// super.onStart();
	// Log.i(I, "HelloWorldLogin-Activity started.");
	// checkLogState();
	// }

	protected void onResume() {
		super.onResume();
		Log.i(I, "HelloWorldLogin-Activity resumed.");
		checkLogState();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// only required if a user has just logged out
		if (keyCode == KeyEvent.KEYCODE_BACK && HelloWorldBasic.isLoggedOut()) {
			if (HelloWorldBasic.isLoggedOut()) {
				Intent intent = new Intent(this, HelloWorldLogin.class);
				this.startActivity(intent);
			}
			Toast
					.makeText(
							HelloWorldLogin.this,
							"Sorry, this function is disabled because another user has just logged out!",
							Toast.LENGTH_LONG).show();
		}
		return super.onKeyDown(keyCode, event);
	}

}