package de.fhkl.mHelloWorld.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.swing.JTree;
//import javax.swing.tree.DefaultMutableTreeNode;

//import org.jdesktop.application.Application;

//import de.fhkl.helloWorld.implementation.application.HelloWorldApp;
import de.fhkl.helloWorld.implementation.actions.messages.MessageManager;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
//import de.fhkl.helloWorld.testGui.contacts.ContactTreeCellenderer;
//import de.fhkl.helloWorld.testGui.contacts.ContactPane.UpdateContacts;
//import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.sax.Element;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.fhkl.mHelloWorld.implementation.mail.MessageBean;
import de.fhkl.mHelloWorld.implementation.mail.MessageSender;

public class InviteFriend extends HelloWorldBasic implements Runnable {
	
	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_Invite Friend: ";
	private static boolean DEBUGGING = false;
	
	// GUI
	private TextView mTitle;
	private EditText mEmail; // mail-adr of friend
	private EditText mPK;    // public-key-url
	private EditText mComment; // invite-comment
	private Button mButton;  // invite-button
	private TextView mResult; // send successful?
	// Main Components
	private MessageSender mSender;
	private MessageBean mMessageBean;
	private MessageManager mMessageManager;
	// Invisible Config-Fields
	private String mHost;
	private String mPort;
	private SendMailProtocol mProtocol;
	private String mUser;
	private String mPassword;
	private String mFrom;
	private String mTo;
	// intern
	private int mGroupIndex;

	// Progress-Dialog
	private ProgressDialog mProgress;
	// Successfully sent?
	private boolean mIsSent;
	
	public void onCreate(Bundle bundle)
	{
		Log.i(I, "start Inivte Friend-Screen");
		super.onCreate(bundle);
		setContentView(R.layout.invite_friend);
		
		// infos from last screen
		Bundle userdatabundle = getIntent().getExtras();
		mGroupIndex = userdatabundle.getInt("groupIndex");
		String groupName = userdatabundle.getString("groupName");
		Log.i(I, "Bundle-Inhalt index:" + mGroupIndex);
		Log.i(I, "Bundle-Inhalt name:" + groupName);
		
		// bind GUI
		mTitle = (TextView) findViewById(R.id.invitefriend_text_head);
		mEmail = (EditText) findViewById(R.id.invitefriend_email);
		mPK = (EditText) findViewById(R.id.invitefriend_pk);
		mComment = (EditText) findViewById(R.id.invitefriend_messagecomment);
        mButton = (Button) findViewById(R.id.invitefriend_button_invite);
        mResult = (TextView) findViewById(R.id.mail_send_result_field);
        
        mTitle.setText("Invite to: " + groupName);
        
        if (DEBUGGING)
        {
        	mEmail.setText("neeerdy@yahoo.de");
        	mPK.setText("http://neeerd.ne.funpic.de/max-pk.xml");
        }
        
		// add button-listener: invite-button
		mButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mIsSent = false;
				mResult.setText("");
				// showDialog(0); // progress-dialog
				mProgress = ProgressDialog.show(InviteFriend.this,
						"Sending Email in Progress", "To: "
								+ mEmail.getText().toString(), true, false);
				// new Thread
				Log.i(I, "Starting a new Thread");
				Thread thread = new Thread(InviteFriend.this);
				thread.start(); // run() is called
			}
		});
	}

	// is called, when thread starts
	public void run() {
		try {
			Looper.prepare();
			Log.i(I, "Try to sendMail();");
			mIsSent = sendMail();
			Log.i(I, "mIsSent = " + mIsSent);
		} catch (Exception e) {
			Log.e("sendMail-Exception", e.getMessage(), e);
		}
		// after all, tell handler to close the window
		finally {
			handler.sendEmptyMessage(0);
		}
	}

	// a handler for the progress-window
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			giveResponseToUser();
		}
	};

	private void giveResponseToUser() {
		if (mIsSent) {
			// einblenden
			Toast.makeText(
					InviteFriend.this,
					"Invitation successfully sent to: " + mEmail.getText().toString()
							+ "!", Toast.LENGTH_SHORT).show();
			mResult.setText("Invitation successfully sent!");
		} else {
			mResult.setText("Uups, that did not work. Please check your"
					+ " data or your email-outgoing configuration!");
		}
	}
	
	private boolean sendMail() {
		try {
			SendMailConnection conf = HelloWorldBasic.getAccount()
					.getConfiguration().getSendMailConncetions().get(0);
			mHost = conf.getHost().getValue();
			mPort = conf.getPort().getValue();
			if (conf.getProtocol() == SendMailProtocol.SMTP) {
				mProtocol = SendMailProtocol.SMTP;
			} else {
				mProtocol = SendMailProtocol.SMTPS;
			}
			mUser = conf.getUsername().getValue();
			mPassword = conf.getPassword().getValue();
			mFrom = conf.getSenderAddress().getValue();
		} catch (Exception e) {
			mResult.setText("No SendMailConnection found!");
		}

		Log.i(I, "Create new MessageSender with Data from View");
		mSender = new MessageSender(mHost, // host
				mUser, // user
				mPassword, // password
				Integer.valueOf(mPort), // port
				mProtocol); // protocol

		Log.i(I, "Create new MessageBean with Data from View");
		mMessageBean = new MessageBean();
		mMessageBean.setRecipientsAddress(mEmail.getText().toString());
		mMessageBean.setSenderAddress(mFrom);
		mMessageBean.setText(mComment.getText().toString());

		//return mSender.sendMessage(mMessageBean); // ---> maybe for messages between users
		sendRequest(); // test with sendRequest
		return true;
	}
	
	// was in desktop-version in: SendRelationShipRequestDialog
	public void sendRequest() {

		AttributeList<SubProfile> sbs = new AttributeList<SubProfile>(
				"subProfiles");
		RelationShipType r = HelloWorldBasic.getAccount()
				//.getRelationShipTypes().get(typesBox.getSelectedIndex()); // use select-field for chosing
				.getRelationShipTypes().get(mGroupIndex);								// the relationshiptype to send --> lover
		Log.i(I, "----------------- CHOOSEN SUBPROFILE-INDEX: " + mGroupIndex);

		for (SubProfile s : HelloWorldBasic.getAccount().getSubProfiles()) { // relationship type
			String name = ((EncryptedSubProfile) s).getRelation().getName();
			Log.i(I, "subProfile nr. " + s + "name:" +name);
			Log.i(I, "RelationShip-name:" +r.getName());
			if (((EncryptedSubProfile) s).getRelation().getName().equals(    // -> to get the right subprofile
					r.getName()))
			{
				sbs.add(s);
				Log.i(I, "----------------- DETECTED SUBPROFILE-NAME: " + r.getName());
			}
				
		}

//		this.sendRelationShipRequest(sbs, publicKey.getText(), eMailAdress
//				.getText(), openID.getText(), message.getText());
		Log.i(I, "sbs: " + sbs);
		Log.i(I, "sbs.get(0): " + sbs.get(0));
		Log.i(I, "sbs.get(0).getKey(): " + sbs.get(0).getKey());
		Log.i(I, "sbs.get(0).getUrl(): " + sbs.get(0).getUrl());
		if (sbs == null) { Log.i(I, "sbs is null!! Error will happen."); }
//		mMessageManager. ---> muss evtl. davor erstellt werden?
		/*
		 * debugging: this construcor below did not exist in desktop-verison !!
		 */
		mMessageManager = new MessageManager();
		mMessageManager.sendRelationShipRequest(sbs, mPK.getText().toString(), 
				mEmail.getText().toString(), "", "");
	}

}
