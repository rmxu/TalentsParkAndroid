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
import java.util.ArrayList;
import java.util.HashMap;

import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShowHCard extends HelloWorldBasicList {

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_ShowHCard: ";
	
	private Button mWriteMessageButton;
	private long mRowId;
	private Contact mContact;
	private HCard mHCard;
	
	
	// @Override

	static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[0xFFFF];

		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
	}

	public void onCreate(Bundle bundle) {
		Log.i(I, "start HCard-Screen");
		super.onCreate(bundle);
		setContentView(R.layout.hcard);

		Helper helper = new Helper();
		helper.settingsSet();
		
		Bundle userdatabundle = getIntent().getExtras();
		mRowId = userdatabundle.getLong("row");
		fillScreen(mRowId);
	}
	
	private void fillScreen(long row_id) {
		// ProfileManager pm = new ProfileManager();
		// Account
		// a = pm.getAccount(user_data, null);
		AttributeList<Contact> contacts = userAccount.getContacts();
		mContact = contacts.get((int) row_id);
		
		mWriteMessageButton = (Button) findViewById(R.id.hcard_button_message);
		mWriteMessageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.i(I, "clicked on write Message Button");
				goToWriteMessageView();
			}
		});

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = new HashMap<String, String>();

		try {
			EncryptedSubProfile friendProfile = (EncryptedSubProfile) mContact
					.getProfiles().get(0);
			ProfileRequester requester = new ProfileRequester(friendProfile);
			friendProfile = requester.call();

			mHCard = friendProfile.getHCard();
			list = Helper.getHCardData(mHCard);

			ImageView iView = (ImageView) findViewById(R.id.userphoto);
			
			if (list.get(0).get("line1").equals("Photo")) {	
				//get image from www and bind it to the view
				iView.setImageBitmap(Helper.getAndSetBitmapFromNet(friendProfile.getHCard().getPhoto().get(0).getValue()));
				Log.i(I, "bind Bitmap to ImageView successful");
			}
			else
			{
				iView.setImageResource(R.drawable.standard_avatar);
			}
			SimpleAdapter notes = new SimpleAdapter(this, list,
					R.layout.hcard_element, new String[] { "line1", "line2" },
					new int[] { R.id.text1, R.id.text2 });
			setListAdapter(notes);
		} catch (Exception e) {
			Log.i(I, "Exception");
			// textField.setText("---");
		}
	}
	
	private void goToWriteMessageView()
	{
		Intent intent = new Intent(this, MessageWriter.class);
		try
		{
			// required fields for message writer ...
			intent.putExtra("mailAddress", mHCard.getEmail().get(0).getValue().toString());
			intent.putExtra("pubKeyUrl", mContact.getPublicKeyURL());
		}
		catch (Exception e)
		{
			Log.i(I, "Exception: Problem with getting Receiver-Data");
		}
		this.startActivity(intent);
	}
	
}
