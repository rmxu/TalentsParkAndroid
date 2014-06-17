package de.fhkl.mHelloWorld.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.StringTokenizer;

import de.fhkl.helloWorld.HelloWorldProperties;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.helloWorld.implementation.actions.messages.MessageManager;
import de.fhkl.helloWorld.implementation.model.security.CryptoRSA2048;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageListener;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
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
import android.app.Dialog;
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

public class MessageWriter extends HelloWorldBasic implements Runnable
{
	// Debugging?
	// private static boolean DEBUGGING = false;
	// Logging-TAG
	private static final String I = "======================= [HELLO-WORLD] "
			+ "MailWriter" + ": ";

	// Main Components
	private SendMailConnection mSendConf;
	private MessageSender mSender;
	private MessageBean mMessageBean;
	private Bundle mUserBundle;
	private String mCompleteBody;
	private CryptoRSA2048 mRsaCrypto;
	private PublicKey mPublicKey;
	// GUI
	private String mHost;
	private String mPort;
	private String mUser;
	private String mPassword;
	private String mFrom;
	private EditText mTo;
	private EditText mSubject;
	private EditText mBody;
	private Button mSend;
	private TextView mResult;
	private static String sPublicKeyUrl;
	// Protocols
	private SendMailProtocol mProtocol;
	// Progress-Dialog
	private ProgressDialog mProgress;
	// Successfully sent?
	private boolean mIsSent = false;

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		// mail_send.xml
		setContentView(R.layout.write_message);

		// get User-ID
		mUserBundle = getIntent().getExtras();

		// fill view
		mTo = (EditText) this.findViewById(R.id.mail_send_to);
		mSubject = (EditText) this.findViewById(R.id.mail_send_subject);
		mBody = (EditText) this.findViewById(R.id.mail_send_body);
		mSend = (Button) this.findViewById(R.id.mail_send_button_send);
		mResult = (TextView) this.findViewById(R.id.mail_send_result_field);

		// get SendMail-Configurations
		mSendConf = HelloWorldBasic.getAccount().getConfiguration()
				.getSendMailConncetions().get(0);

		// Try to set Contact-Receiver-Data
		setReceiverData();

		// add button-listener: send-button
		mSend.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				mIsSent = false;
				mResult.setText("");
				try
				{
					mProtocol = mSendConf.getProtocol();
					mHost = mSendConf.getHost().getValue();
					mPort = mSendConf.getPort().getValue();
					mUser = mSendConf.getUsername().getValue();
					mPassword = mSendConf.getPassword().getValue();
					mFrom = mSendConf.getSenderAddress().getValue();
				}
				catch (Exception e)
				{
					Log.i(I, "Exception in setting up Sender-Data!");
				}

				mCompleteBody = mFrom + "\n" + mSubject.getText() + "\n"
						+ mBody.getText();

				if (mCompleteBody.length() > 256)
				{
					// content-size too big
					Toast.makeText(
							MessageWriter.this,
							"Sorry! The RSA-2048-Bit Encryption can maximal encrypt" +
							" 256 Characters. Please send in two or more steps!\n" +
							"Now you have " + mCompleteBody.length() + " Characters.", 
							Toast.LENGTH_LONG).show();
				}
				else
				{
					// showDialog(0); // progress-dialog
					mProgress = ProgressDialog.show(MessageWriter.this,
							"Sending Email in Progress", "To: "
									+ mTo.getText().toString(), true, false);

					// new Thread
					Log.i(I, "Starting a new Thread");
					Thread thread = new Thread(MessageWriter.this);
					thread.start(); // run() is called
				}
			}
		});
	}

	// is called, when thread starts
	public void run()
	{
		try
		{
			Looper.prepare();
			Log.i(I, "Try to sendMail();");
			MessageManager mm = new MessageManager();
			// get PubliKey (content)
			mPublicKey = mm.getPublicKey(sPublicKeyUrl);
			// encrypt Body-Content
			mRsaCrypto = new CryptoRSA2048();
			
//			String encrypted = "";
//			String[] parts = make250CharStringArray(mCompleteBody);
//			for (int i = 0; i < parts.length; i++)
//			{
//				encrypted += mRsaCrypto.encrypt(parts[i], mPublicKey);
//				Log.i(I, "encrypted a[" + i + "]=" + parts[i]);
//				Log.i(I, "encrypted: \n" + encrypted);
//				mIsSent = sendMail(encrypted);
//			}
			
			mCompleteBody = encryptContent(mCompleteBody);
			// send message
			mIsSent = sendMail(mCompleteBody);
			Log.i(I, "mIsSent = " + mIsSent);
		}
		catch (Exception e)
		{
			Log.e("sendMail-Exception", e.getMessage(), e);
		}
		// after all, tell handler to close the window
		finally
		{
			handler.sendEmptyMessage(0);
		}
	}

	private String encryptContent(String content)
	{
		Log.i(I, "Message -> Length: " + content.length());
		return mRsaCrypto.encrypt(content, mPublicKey);
	}


	public static String[] make250CharStringArray(String str)
	{
		int length = str.length();
		int fieldNum;
		if (length <= 256)
		{
			fieldNum = 1;
		}
		else if (length % 250 == 0)
		{
			fieldNum = length / 250;
		}
		else
		{
			fieldNum = length / 64 + 1;
		}
		Log.i(I, fieldNum + " Fields required for length of " + length);
		String[] a = new String[fieldNum];
		int start = 0;
		int ende = 64;
		for (int i = 0; i < a.length; i++)
		{
			if (ende > str.length())
			{
				// if too long
				ende = length;
				a[i] = str.substring(start, ende);
			}
			else
			{
				a[i] = str.substring(start, ende) + " (...)";
			}

			Log.i(I, "a[" + i + "]=" + a[i]);
			start += 250;
			ende += 250;
			if (ende > str.length())
			{
				// if too long
				ende = length;
			}
		}
		return a;
	}

	// a handler for the progress-window
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			giveResponseToUser();
		}
	};

	private void giveResponseToUser()
	{
		if (mIsSent)
		{
			// einblenden
			Toast.makeText(
					MessageWriter.this,
					"Mail successfully sent to: " + mTo.getText().toString()
							+ "!", Toast.LENGTH_SHORT).show();
			mResult.setText("Mail successfully sent!");
		}
		else
		{
			mResult.setText("Uups, that did not work. Please check your"
					+ " data.");
		}
	}

	private boolean sendMail(String content)
	{
		Log.i(I, "Create new MessageSender with Data from View");
		mSender = new MessageSender(mHost, // host
				mUser, // user
				mPassword, // password
				Integer.valueOf(mPort), // port
				mProtocol); // protocol

		Log.i(I, "Create new MessageBean with Data from View");
		mMessageBean = new MessageBean();
		mMessageBean.setRecipientsAddress(mTo.getText().toString());
		mMessageBean.setSenderAddress(mFrom);
		mMessageBean.setSubject(HelloWorldProperties
				.getString("MailSubjectTextMessageIdentifier"));
		mMessageBean.setText(content);

		return mSender.sendMessage(mMessageBean);
	}

	// private void fillDebugData()
	// {
	// Log.i(I, "Filling View with debugging-Content");
	// mHost.setText("smtp.googlemail.com");
	// mPort.setText("465");
	// mUser.setText("neeerdy@googlemail.com");
	// mFrom.setText("neeerdy@googlemail.com");
	// mTo.setText("neeerdy@googlemail.com");
	// mSubject.setText("Mail from Android-HelloWorld-Client!");
	// mBody.setText("Hell yeah, dude - it works!!!");
	// // test of global-account-access
	// String accountPart =
	// HelloWorldBasic.getAccount().getConfiguration().getSendMailConncetions().get(0).getUsername().getValue();
	// mResult.setText(accountPart);
	// }

	private boolean setReceiverData()
	{
		// getting Email from Bundle
		try
		{
			mTo.setText(mUserBundle.getString("mailAddress"));
			Log.i(I, "To: " + mTo.getText());
		}
		catch (Exception e)
		{
			mTo.setText("No Email-Address available!");
			return false;
		}

		try
		{
			sPublicKeyUrl = mUserBundle.getString("pubKeyUrl");
			Log.i(I, "PublicKey-Url: " + sPublicKeyUrl);
		}
		catch (Exception e)
		{
			Toast.makeText(MessageWriter.this, "No PublicKey-Url was found!:",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;

	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Please wait sending Email ...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}

}
