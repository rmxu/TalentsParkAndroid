package de.fhkl.mHelloWorld.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.fhkl.helloWorld.HelloWorldProperties;
import de.fhkl.helloWorld.implementation.actions.messages.MessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageListener;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.mHelloWorld.implementation.mail.MailReceiverTest;
import de.fhkl.mHelloWorld.implementation.mail.MessageBean;
import de.fhkl.mHelloWorld.implementation.mail.MessageSender;
import de.fhkl.mHelloWorld.implementation.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/*
 * IMPORTANT: CLASS IS NOT IN USE, YET (MAYBE LATER VOR AN EXTERNAL MAIL-CLIENT)
 * CONTENT MOVED TO: InviteFriend.java and MailChecker.java
 */

public class MailApp extends HelloWorldBasic /*implements Runnable*/ {
	// Debugging?
	// private static boolean DEBUGGING = false;
	// Logging-TAG
	private static final String I = 
		"======================= [HELLO-WORLD] " +
		 "MailApp" + ": ";
	// Main Components
	private MessageSender mSender;
	private MessageBean mMessageBean;
	private MessageManager mMessageManager;
	// GUI
	private Button mDefaultData; // debugging
	private EditText mHost;
	private EditText mPort;
	private EditText mUser;
	private EditText mPassword;
	private EditText mFrom;
	private EditText mTo;
	private EditText mSubject;
	private EditText mBody;
	private Button mSend;
	private TextView mResult;
	// Protocols
	private RadioButton mSMTPS;
	private RadioButton mSMTP;
	private SendMailProtocol mChoosenProtocol;
	// Progress-Dialog
	private ProgressDialog mProgress;
	// Successfully sent?
	private boolean mIsSent;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// mail_send.xml
		setContentView(R.layout.mail_send);
		
//		MailReceiverTest.checkForNewMails();
		
	}
//		// fill view
//		mDefaultData = (Button) this
//				.findViewById(R.id.mail_send_button_defaultdata);
//		mSMTPS = (RadioButton) this.findViewById(R.id.mail_send_smtps);
//		mSMTP = (RadioButton) this.findViewById(R.id.mail_send_smtp);
//		mHost = (EditText) this.findViewById(R.id.mail_send_host);
//		mPort = (EditText) this.findViewById(R.id.mail_send_port);
//		mUser = (EditText) this.findViewById(R.id.mail_send_user);
//		mPassword = (EditText) this.findViewById(R.id.mail_send_password);
//		mFrom = (EditText) this.findViewById(R.id.mail_send_from);
//		mTo = (EditText) this.findViewById(R.id.mail_send_to);
//		mSubject = (EditText) this.findViewById(R.id.mail_send_subject);
//		mBody = (EditText) this.findViewById(R.id.mail_send_body);
//		mSend = (Button) this.findViewById(R.id.mail_send_button_send);
//		mResult = (TextView) this.findViewById(R.id.mail_send_result_field);
//
//		// add listeners for the buttons
//		
//		mDefaultData.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				Log.i(I, "Debugging active");
//				fillDebugData();
//			}
//		});
//
//		mSMTPS.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				Log.i(I, "clicked on SMTPS");
//				mPort.setText(SendMailProtocol.SMTPS.standardPort() + "");
//			}
//		});
//
//		mSMTP.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				Log.i(I, "clicked on SMTP");
//				mPort.setText(SendMailProtocol.SMTP.standardPort() + "");
//			}
//		});
//
//		// add button-listener: send-button
//		mSend.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				// showDialog(0); // progress-dialog
//				mProgress = ProgressDialog.show(MailApp.this,
//						"Sending Email in Progress", "To: "
//								+ mTo.getText().toString(), true, false);
//
//				// new Thread
//				Log.i(I, "Starting a new Thread");
//				Thread thread = new Thread(MailApp.this);
//				thread.start(); // run() is called
//			}
//		});
//	}
//
//	// is called, when thread starts
//	public void run() {
//		try {
//			Looper.prepare();
//			Log.i(I, "Try to sendMail();");
//			mIsSent = sendMail();
//			Log.i(I, "mIsSent = " + mIsSent);
//		} catch (Exception e) {
//			Log.e("sendMail-Exception", e.getMessage(), e);
//		}
//		// after all, tell handler to close the window
//		finally {
//			handler.sendEmptyMessage(0);
//		}
//	}
//
//	// a handler for the progress-window
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			mProgress.dismiss();
//			giveResponseToUser();
//		}
//	};
//
//	private void giveResponseToUser() {
//		if (mIsSent) {
//			// einblenden
//			Toast.makeText(
//					MailApp.this,
//					"Mail successfully sent to: " + mTo.getText().toString()
//							+ "!", Toast.LENGTH_SHORT).show();
//			mResult.setText("Mail successfully sent!");
//		} else {
//			mResult.setText("Uups, that did not work. Please check your"
//					+ " data.");
//		}
//	}
//
//	private boolean sendMail() {
//		// check which protocol is active
//		if (mSMTPS.isChecked()) {
//			Log.i(I, "Protocol: SMTPS (SSL/TLS) is checked.");
//			mChoosenProtocol = SendMailProtocol.SMTPS;
//		} else if (mSMTP.isChecked()) {
//			Log.i(I, "Protocol: SMTP is checked.");
//			mChoosenProtocol = SendMailProtocol.SMTP;
//		} else {
//			Log.i(I, "Protocol: NOTHING is checked. Try SMTPS ...");
//			mChoosenProtocol = SendMailProtocol.SMTPS;
//		}
//
//		Log.i(I, "Create new MessageSender with Data from View");
//		mSender = new MessageSender(mHost.getText().toString(), // host
//				mUser.getText().toString(), // user
//				mPassword.getText().toString(), // password
//				Integer.valueOf(mPort.getText().toString()), // port
//				mChoosenProtocol); // protocol
//
//		Log.i(I, "Create new MessageBean with Data from View");
//		mMessageBean = new MessageBean();
//		mMessageBean.setRecipientsAddress(mTo.getText().toString());
//		mMessageBean.setSenderAddress(mFrom.getText().toString());
//		mMessageBean.setSubject(mSubject.getText().toString());
//		mMessageBean.setText(mBody.getText().toString());
//
//		//return mSender.sendMessage(mMessageBean); // ---> maybe for messages between users
//		sendRequest(); // test with sendRequest
//		return true;
//
//	}
//
//	private void fillDebugData() {
//		Log.i(I, "Filling View with debugging-Content");
//		try {
//			SendMailConnection conf = HelloWorldBasic.getAccount()
//					.getConfiguration().getSendMailConncetions().get(0);
//			if (conf.getProtocol() == SendMailProtocol.SMTP) {
//				mSMTP.setChecked(true);
//			} else {
//				mSMTPS.setChecked(true);
//			}
//			mHost.setText(conf.getHost().getValue());
//			mPort.setText(conf.getPort().getValue());
//			mUser.setText(conf.getUsername().getValue());
//			mPassword.setText(conf.getPassword().getValue());
//			mFrom.setText(conf.getSenderAddress().getValue());
//		} catch (Exception e) {
//			mResult.setText("No SendMailConnection found!");
//		}
//		mTo.setText("neeerdy@googlemail.com");
//		mSubject.setText("Mail from Android-HelloWorld-Client!");
//		mBody.setText("Hell yeah, dude - it works!!!");
//	}
//
//	// was in desktop-version in: SendRelationShipRequestDialog
//	public void sendRequest() {
//
//		AttributeList<SubProfile> sbs = new AttributeList<SubProfile>(
//				"subProfiles");
//		RelationShipType r = HelloWorldBasic.getAccount()
//				//.getRelationShipTypes().get(typesBox.getSelectedIndex()); // use select-field for chosing
//				.getRelationShipTypes().get(3);								// the relationshiptype to send --> lover
//
//		for (SubProfile s : HelloWorldBasic.getAccount().getSubProfiles()) { // relationship type
//			String name = ((EncryptedSubProfile) s).getRelation().getName();
//			Log.i(I, "subProfile nr. " + s + "name:" +name);
//			Log.i(I, "RelationShip-name:" +r.getName());
//			if (((EncryptedSubProfile) s).getRelation().getName().equals(    // -> to get the right subprofile
//					r.getName()))
//				sbs.add(s);
//		}
//
////		this.sendRelationShipRequest(sbs, publicKey.getText(), eMailAdress
////				.getText(), openID.getText(), message.getText());
//		Log.i(I, "sbs: " + sbs);
//		Log.i(I, "sbs.get(0): " + sbs.get(0));
//		Log.i(I, "sbs.get(0).getKey(): " + sbs.get(0).getKey());
//		Log.i(I, "sbs.get(0).getUrl(): " + sbs.get(0).getUrl());
//		if (sbs == null) { Log.i(I, "sbs is null!! Error will happen."); }
////		mMessageManager. ---> muss evtl. davor erstellt werden?
//		/*
//		 * debugging: this construcor below did not exist in desktop-verison !!
//		 */
//		mMessageManager = new MessageManager();
//		mMessageManager.sendRelationShipRequest(sbs, "http://neeerd.ne.funpic.de/max-pk.xml", 
//				mTo.getText().toString(), "", "");
//	}
//
//	// private void fillDebugData()
//	// {
//	// Log.i(I, "Filling View with debugging-Content");
//	// mHost.setText("smtp.googlemail.com");
//	// mPort.setText("465");
//	// mUser.setText("neeerdy@googlemail.com");
//	// mFrom.setText("neeerdy@googlemail.com");
//	// mTo.setText("neeerdy@googlemail.com");
//	// mSubject.setText("Mail from Android-HelloWorld-Client!");
//	// mBody.setText("Hell yeah, dude - it works!!!");
//	// // test of global-account-access
//	// String accountPart =
//	// HelloWorldBasic.getAccount().getConfiguration().getSendMailConncetions().get(0).getUsername().getValue();
//	// mResult.setText(accountPart);
//	// }
//
//	// @Override
//	// protected Dialog onCreateDialog(int id)
//	// {
//	// ProgressDialog dialog = new ProgressDialog(this);
//	// dialog.setMessage("Please wait sending Email ...");
//	// dialog.setIndeterminate(true);
//	// dialog.setCancelable(true);
//	// return dialog;
//	// }

}
