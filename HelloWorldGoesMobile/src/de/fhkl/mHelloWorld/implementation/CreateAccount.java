package de.fhkl.mHelloWorld.implementation;

import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * This example shows how to create a list of checkboxes.
 */
public class CreateAccount extends Activity implements Button.OnClickListener {

	// for more userfriendly responses
	protected static ProgressDialog sProgressDialog;
	private static boolean sIsSuccess = false;
	private EditText mUsername;
	private EditText mPassword;
	private EditText mPasswordW;
	private EditText mFullname;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.create_account);

		Button button = (Button) findViewById(R.id.createaccount_button_create);
		mUsername = (EditText) findViewById(R.id.createaccount_text_username);
		mPassword = (EditText) findViewById(R.id.createaccount_text_password);
		mPasswordW = (EditText) findViewById(R.id.createaccount_text_password_w);
		mFullname = (EditText) findViewById(R.id.createaccount_text_fullname);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (!mPassword.getText().toString().equals(mPasswordW.getText().toString()))
		{
			Toast.makeText(this, "Password Confirmation not right! Please try again.",
					Toast.LENGTH_LONG).show();
		}
		else if (mUsername.getText().toString().equals("")
				 || mPassword.getText().toString().equals("")
			     || mFullname.getText().toString().equals(""))
		{
			Toast.makeText(this, "Please fill out all fields!",
					Toast.LENGTH_LONG).show();
		}
		else
		{
			// Progressbar
			sProgressDialog = ProgressDialog.show(CreateAccount.this,
					"Please wait", "Creating your Account ...", true, false);
			
			// new thread (for progressbar)
			new Thread() {
				public void run() {
					AccountManager am = new AccountManager();
					Account newAcc = am.createNewAccount();

					newAcc
							.getPrivateProfile()
							.getHCard()
							.setFn(
									mFullname.getText().toString());
					sIsSuccess = am
							.encryptAndSaveAccount(
									newAcc,
									mUsername.getText().toString(),
									mPassword.getText().toString(), null);
					// important for progressbar (dismiss)
					handler.sendEmptyMessage(0);
				}
			}.start();
		}
	}

	private void goToLoginView() {
		// Error-Response for user (need to be in that way because of the
		// thread)
		if (sIsSuccess) {
			Toast
					.makeText(
							this,
							"Successfully created your new Account!\nLogin with your username and passwort.",
							Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Error while encrypting and saving profile",
					Toast.LENGTH_LONG).show();
		}
		Intent intent = new Intent(this, HelloWorldLogin.class);
		this.startActivity(intent);
	}

	// handler needed to be able inside the thread to
	// modify text of the view on the run
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// Dismiss the Dialog
			sProgressDialog.dismiss();
			goToLoginView();
		}
	};
}