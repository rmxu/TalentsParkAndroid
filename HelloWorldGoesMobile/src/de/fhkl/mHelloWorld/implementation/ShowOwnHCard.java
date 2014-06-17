package de.fhkl.mHelloWorld.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.mHelloWorld.implementation.debug.EncryptedMailTest;
import de.fhkl.mHelloWorld.implementation.util.AccountSettingsChecker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowOwnHCard extends HelloWorldBasicList implements
		Button.OnClickListener {

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_ShowOwnHCard: ";
	// @Override

	// button-instance
	private Button mEditHCardButton;

	public void onCreate(Bundle bundle) {
		Log.i(I, "start OwnHCard-Screen");
		super.onCreate(bundle);

		// if logged out -> no account -> back to login
		if (HelloWorldBasic.getAccount() == null) {
			Intent intent = new Intent(this, HelloWorldLogin.class);
			this.startActivity(intent);
		} 
		else 
		{
			// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)

			// Log.i(I,"Timestamp: "+userAccount.getKeyPair().getPrivateKey().getTimestamp());
			// Log.i(I,"Private Key: "+userAccount.getKeyPair().getPrivateKey().getPrivateKey().toString());
			//			
			// PrivateKey test =
			// userAccount.getKeyPair().getPrivateKey().getPrivateKey();
			// Log.i(I,"Private Key, test: "+test.toString());
			// PrivateKey test2 =
			// HelloWorldBasic.getAccount().getKeyPair().getPrivateKey().getPrivateKey();
			// Log.i(I,"Private Key, test2: "+test2.toString());

			Helper help = new Helper();
			help.getHCardTest();
			setContentView(R.layout.hcard_own);
			init();
			fillScreen();
			
			//debugging:
			//EncryptedMailTest.makeTest();
		}
	}

	public void init() {
		mEditHCardButton = (Button) this.findViewById(R.id.hcard_button_edit);
		mEditHCardButton.setOnClickListener(this);
	}

	public void onResume() {
		super.onResume();
		
		Log.i(I, "--------------------------------------------------------- ");
		Log.i(I, "ShowOwnHCard resumed.");
		Log.i(I, "--------------------------------------------------------- ");
		
		// check Account Settings
		AccountSettingsChecker checker = new AccountSettingsChecker();
		
		if (checker.isCheckStatus() == false) {
			Intent intent = new Intent(this, SettingsChooser.class);
			this.startActivity(intent);
		}
		
		if (HelloWorldBasic.isShowUseLogout()) {
			Toast.makeText(ShowOwnHCard.this,
					"Want to exit? Please use Menu -> Logout",
					Toast.LENGTH_LONG).show();
			HelloWorldBasic.setShowUseLogout(false);
		} else if (HelloWorldBasic.isShowWelcome()) {
			// not really needed ...
//			Toast.makeText(ShowOwnHCard.this, "Loading successful - Welcome!",
//					Toast.LENGTH_SHORT).show();
			HelloWorldBasic.setShowWelcome(false);
		}

	}

	public ArrayList<HashMap<String, String>> fillScreen() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = new HashMap<String, String>();

		// ////////////////////////////
		// // get the User-Imgage //////
		// ////////////////////////////
		Helper help = new Helper();
		list = help.getHCardData(userAccount.getPrivateProfile().getHCard());
		ImageView iView = (ImageView) findViewById(R.id.userphoto);

		if (list.get(0).get("line1").equals("Photo")) {
			iView.setImageBitmap(Helper.getAndSetBitmapFromNet(userAccount
					.getPrivateProfile().getHCard().getPhoto().get(0).getValue()));
		} else {
			iView.setImageResource(R.drawable.standard_avatar);
		}

		Log.i(I, "bind Bitmap to ImageView successful");
		// setProgressBarIndeterminateVisibility(false);

		SimpleAdapter notes = new SimpleAdapter(this, list,
				R.layout.hcard_element, new String[] { "line1", "line2" },
				new int[] { R.id.text1, R.id.text2 });
		setListAdapter(notes);

		return list;
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		if (view.getId() == R.id.hcard_button_edit) {
			Log.v(I, "Klick auf Edit Hcard-Button registriert");
			intent = new Intent(this, EditOwnHCard.class);
			startActivity(intent);
		}
	}

}
