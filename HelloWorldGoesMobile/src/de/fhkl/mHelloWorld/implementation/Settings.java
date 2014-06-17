package de.fhkl.mHelloWorld.implementation;

import java.util.ArrayList;

import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.Configuration;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FetchMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.util.protocols.FetchMailProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.FileTransferProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol; //import de.fhkl.helloWorld.testGui.contacts.ContactTreeCellenderer;
//import de.fhkl.helloWorld.testGui.contacts.ContactPane.UpdateContacts;
//import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import de.fhkl.mHelloWorld.implementation.util.AccountSettingsChecker;
import de.fhkl.mHelloWorld.implementation.util.NamingHelper;
import de.fhkl.mHelloWorld.implementation.util.StringUtil;

public class Settings extends HelloWorldBasic implements Button.OnClickListener
{

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_Settings: ";
	private int chooser;

	private Button incoming_button;
	private Button outgoing_button;
	private Button ftp_button;
	private RadioButton pop3;
	private RadioButton pop3s;
	private RadioButton smtp;
	private RadioButton smtps;
	private RadioButton ftp;
	private RadioButton sftp;
	private EditText host;
	private EditText port;
	private EditText adress;
	private EditText username;
	private EditText password;
	private EditText httpUrl;

	// @Override
	public void onCreate(Bundle bundle)
	{
		Log.i(I, "start Settings");
		super.onCreate(bundle);
		Bundle userdatabundle = getIntent().getExtras();
		chooser = userdatabundle.getInt("chooser");

		// VERY IMPORTANT: checks the settings and if the skeleton is not
		// available let it create by ConfigurationSkeletonMaker
		new AccountSettingsChecker();

		switch (chooser)
		{
		case 1:
			setContentView(R.layout.settings_incoming);
			incoming_button = (Button) findViewById(R.id.settings_incoming_button_save);
			incoming_button.setOnClickListener(this);
			initFields();
			initIncoming();
			break;
		case 2:
			setContentView(R.layout.settings_outgoing);
			outgoing_button = (Button) findViewById(R.id.settings_outgoing_button_save);
			outgoing_button.setOnClickListener(this);
			initFields();
			initOutgoing();
			break;
		case 3:
			setContentView(R.layout.settings_ftp);
			ftp_button = (Button) findViewById(R.id.settings_ftp_button_save);
			httpUrl = (EditText) findViewById(R.id.settings_ftp_httpurl);
			ftp_button.setOnClickListener(this);
			initFields();
			initftp();
			// continuously update edit-url-field
			host.setOnFocusChangeListener(new View.OnFocusChangeListener()
			{
				@Override
				public void onFocusChange(View arg0, boolean arg1)
				{
					updateHttpUrlField();
				}
			});
			break;
		}

		// Bundle userdatabundle = getIntent().getExtras();
		// user_data = userdatabundle;

		// Button outgoing_button = (Button)
		// findViewById(R.id.settings_outgoing_button_save);
		// outgoing_button.setOnClickListener(this);
		// Button ftp_button = (Button)
		// findViewById(R.id.settings_ftp_button_save);
		// ftp_button.setOnClickListener(this);
	}

	private void updateHttpUrlField()
	{
		Log.i(I, "update (ftp) http-url ...");
		String ftpUrl = host.getText().toString();
		httpUrl.setText(NamingHelper.generateHttpUrlFromFtpUrl(ftpUrl));
	}

	private void initftp()
	{

		sftp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on SFTP");
				port.setText(FileTransferProtocol.SFTP.standardPort() + "");
			}
		});

		ftp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on FTP");
				port.setText(FileTransferProtocol.FTP.standardPort() + "");
			}
		});

		FileTransferConnection conn = userAccount.getConfiguration()
				.getFileTransferConncetions().get(0);

		String value = "";

		value = conn.getHost().getValue();
		if (value != "")
		{
			host.setText(value);
			value = "";
		}
		value = conn.getPassword().getValue();
		if (value != "")
		{
			password.setText(value);
			value = "";
		}
		value = conn.getUsername().getValue();
		if (value != "")
		{
			username.setText(value);
			value = "";
		}
		value = conn.getPort().getValue();
		if (value != "")
		{
			port.setText(value);
		}
		value = conn.getProtocol().lowerCaseName();
		if (value != "")
		{
			String name = conn.getProtocol().lowerCaseName();
			if (name == "ftp")
			{
				ftp.setChecked(true);
				name = "";
			}
			name = conn.getProtocol().lowerCaseName();
			if (name == "sftp")
			{
				sftp.setChecked(true);
			}
		}
		// try to get HttpUrl-Field (only in android-accounts available)
		try
		{
			value = conn.getHttpUrl().getValue();
			if (value != "")
			{
				httpUrl.setText(value);
			}
			else
				if (conn.getHost().getValue() != "")
				{
					httpUrl.setText(NamingHelper
							.generateHttpUrlFromFtpSettings());
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void initOutgoing()
	{
		
		smtps.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on SMTPS");
				port.setText(SendMailProtocol.SMTPS.standardPort() + "");
			}
		});
		
		smtp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on SMTP");
				port.setText(SendMailProtocol.SMTP.standardPort() + "");
			}
		});

		SendMailConnection conn = userAccount.getConfiguration()
				.getSendMailConncetions().get(0);

		String value = "";

		value = conn.getSenderAddress().getValue();
		if (value != "")
		{
			adress.setText(value);
			value = "";
		}
		value = conn.getHost().getValue();
		if (value != "")
		{
			host.setText(value);
			value = "";
		}
		value = conn.getPassword().getValue();
		if (value != "")
		{
			password.setText(value);
			value = "";
		}
		value = conn.getUsername().getValue();
		if (value != "")
		{
			username.setText(value);
			value = "";
		}
		value = conn.getPort().getValue();
		if (value != "")
		{
			port.setText(value);
		}
		value = conn.getProtocol().lowerCaseName();
		if (value != "")
		{
			String name = conn.getProtocol().lowerCaseName();
			if (name == "smtp")
			{
				smtp.setChecked(true);
				name = "";
			}
			name = conn.getProtocol().lowerCaseName();
			if (name == "smtps")
			{
				smtps.setChecked(true);
			}
		}
	}

	private void initFields()
	{

		pop3 = (RadioButton) findViewById(R.id.mail_send_pop3);
		pop3s = (RadioButton) findViewById(R.id.mail_send_pop3s);
		smtp = (RadioButton) findViewById(R.id.mail_send_smtp);
		smtps = (RadioButton) findViewById(R.id.mail_send_smtps);
		ftp = (RadioButton) findViewById(R.id.mail_send_ftp);
		sftp = (RadioButton) findViewById(R.id.mail_send_ftps);
		host = (EditText) findViewById(R.id.mail_send_host);
		port = (EditText) findViewById(R.id.mail_send_port);
		adress = (EditText) findViewById(R.id.settings_edittext_adress);
		username = (EditText) findViewById(R.id.mail_send_user);
		password = (EditText) findViewById(R.id.mail_send_password);
		httpUrl = (EditText) findViewById(R.id.settings_ftp_httpurl);
	}

	private void initIncoming()
	{
		
		pop3s.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on POP3S");
				port.setText(FetchMailProtocol.POP3S.standardPort() + "");
			}
		});
		
		pop3.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Log.i(I, "clicked on POP3");
				port.setText(FetchMailProtocol.POP3.standardPort() + "");
			}
		});

		FetchMailConnection conn = userAccount.getConfiguration()
				.getFetchMailConncetions().get(0);

		String value = conn.getSenderAddress().getValue();
		if (value != "")
		{
			adress.setText(value);
			value = "";
		}
		value = conn.getHost().getValue();
		if (value != "")
		{
			host.setText(value);
			value = "";
		}
		value = conn.getPassword().getValue();
		if (value != "")
		{
			password.setText(value);
			value = "";
		}
		value = conn.getUsername().getValue();
		if (value != "")
		{
			username.setText(value);
			value = "";
		}
		value = conn.getPort().getValue();
		if (value != "")
		{
			port.setText(value);
		}
		value = conn.getProtocol().lowerCaseName();
		if (value != "")
		{
			String name = conn.getProtocol().lowerCaseName();
			if (name == "pop3")
			{
				pop3.setChecked(true);
				name = "";
			}
			name = conn.getProtocol().lowerCaseName();
			if (name == "pop3s")
			{
				pop3s.setChecked(true);
			}
		}
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.settings_incoming_button_save:
			Log.i(I, "clicked on button: incoming:");
			saveFetchMailSettings(v);
			break;

		case R.id.settings_outgoing_button_save:
			Log.i(I, "clicked on button: outgoing:");
			saveSendMailSettings(v);
			break;

		case R.id.settings_ftp_button_save:
			Log.i(I, "clicked on button: ftp:");
			saveFTPSettings(v);
			break;
		}
	}

	private void saveFTPSettings(View v)
	{
		Configuration conf = userAccount.getConfiguration();
		AttributeList<FileTransferConnection> conList = conf
				.getFileTransferConncetions();
		FileTransferConnection con = conList.get(0);

		con.setHost(new SingleAttribute<String>("host", host.getText()
				.toString()));
		con.setPort(new SingleAttribute<String>("port", port.getText()
				.toString()));
		con.setUsername(new SingleAttribute<String>("username", username
				.getText().toString()));
		con.setPassword(new SingleAttribute<String>("password", password
				.getText().toString()));
		con.setHttpUrl(new SingleAttribute<String>("httpurl", httpUrl.getText()
				.toString()));

		if (ftp.isChecked())
		{
			FileTransferProtocol ftp = FileTransferProtocol.FTP;
			con.setProtocol(ftp);
		}
		if (sftp.isChecked())
		{
			FileTransferProtocol sftp = FileTransferProtocol.SFTP;
			con.setProtocol(sftp);
		}
		ArrayList<FileTransferConnection> attributes = new ArrayList<FileTransferConnection>();
		attributes.add(con);
		conList.setAttributes(attributes);
		conf.setFileTransferConnections(conList);
		userAccount.setConfiguration(conf);

		try
		{
			AccountManager am = new AccountManager();
			am.encryptAndSaveAccount(userAccount,
					HelloWorldBasic.getUsername(), HelloWorldBasic
							.getPassword(), null);
			SettingsChooser.setSuccessMessage("FTP");
			goBackToSettingsChooser();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Sorry, settings couldn't be saved",
					Toast.LENGTH_LONG).show();
		}
	}

	private void saveSendMailSettings(View v)
	{
		Configuration conf = userAccount.getConfiguration();
		AttributeList<SendMailConnection> conList = conf
				.getSendMailConncetions();
		SendMailConnection con = conList.get(0);

		con.setHost(new SingleAttribute<String>("host", host.getText()
				.toString()));
		con.setPort(new SingleAttribute<String>("port", port.getText()
				.toString()));
		Log.i(I, "adress test: ** " + adress.getText().toString());
		con.setSenderAddress(new SingleAttribute<String>("senderAddress",
				adress.getText().toString()));
		con.setUsername(new SingleAttribute<String>("username", username
				.getText().toString()));
		con.setPassword(new SingleAttribute<String>("password", password
				.getText().toString()));

		if (smtp.isChecked())
		{
			SendMailProtocol smtp = SendMailProtocol.SMTP;
			con.setProtocol(smtp);
		}
		if (smtps.isChecked())
		{
			SendMailProtocol smtps = SendMailProtocol.SMTPS;
			con.setProtocol(smtps);
		}
		ArrayList<SendMailConnection> attributes = new ArrayList<SendMailConnection>();
		attributes.add(con);
		conList.setAttributes(attributes);
		conf.setSendMailConnections(conList);
		userAccount.setConfiguration(conf);
		try
		{
			AccountManager am = new AccountManager();
			am.encryptAndSaveAccount(userAccount,
					HelloWorldBasic.getUsername(), HelloWorldBasic
							.getPassword(), null);
			SettingsChooser.setSuccessMessage("Outgoing Mail");
			goBackToSettingsChooser();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Sorry, settings couldn't be saved",
					Toast.LENGTH_LONG).show();
		}

	}

	private void saveFetchMailSettings(View v)
	{
		Log.i(I, "saved SenderAdress: "
				+ userAccount.getConfiguration().getFetchMailConncetions().get(
						0).getSenderAddress().getValue());
		Configuration conf = userAccount.getConfiguration();
		AttributeList<FetchMailConnection> conList = conf
				.getFetchMailConncetions();
		FetchMailConnection fetchCon = conList.get(0);

		fetchCon.setHost(new SingleAttribute<String>("host", host.getText()
				.toString()));
		fetchCon.setPort(new SingleAttribute<String>("port", port.getText()
				.toString()));
		Log.i(I, "adress test: ** " + adress.getText().toString());
		fetchCon.setSenderAddress(new SingleAttribute<String>("senderAddress",
				adress.getText().toString()));
		fetchCon.setUsername(new SingleAttribute<String>("username", username
				.getText().toString()));
		fetchCon.setPassword(new SingleAttribute<String>("password", password
				.getText().toString()));

		if (pop3.isChecked())
		{
			FetchMailProtocol pop3 = FetchMailProtocol.POP3;
			fetchCon.setProtocol(pop3);
		}
		if (pop3s.isChecked())
		{
			FetchMailProtocol pop3s = FetchMailProtocol.POP3S;
			fetchCon.setProtocol(pop3s);
		}
		ArrayList<FetchMailConnection> attributes = new ArrayList<FetchMailConnection>();
		attributes.add(fetchCon);
		conList.setAttributes(attributes);
		conf.setFetchMailConnections(conList);
		userAccount.setConfiguration(conf);
		try
		{
			AccountManager am = new AccountManager();
			am.encryptAndSaveAccount(userAccount,
					HelloWorldBasic.getUsername(), HelloWorldBasic
							.getPassword(), null);

			Log.i(I, "saved SenderAdress: "
					+ userAccount.getConfiguration().getFetchMailConncetions()
							.get(0).getSenderAddress().getValue());

			SettingsChooser.setSuccessMessage("Incoming Mail");
			goBackToSettingsChooser();

		}
		catch (Exception e)
		{
			Toast.makeText(this, "Sorry, settings couldn't be saved",
					Toast.LENGTH_LONG).show();
		}
	}

	private void goBackToSettingsChooser()
	{
		Intent intent = new Intent(this, SettingsChooser.class);
		startActivity(intent);
	}

	// if still empty setting fields - menu: ONLY logout possible!!
	// in other cases there will errors occur
	private static final int HOME_ID = Menu.FIRST;
	private static final int CONTACTS_ID = Menu.FIRST + 1;
	private static final int MAP_ID = Menu.FIRST + 2;
	private static final int MAIL_ID = Menu.FIRST + 4;
	private static final int SETTINGS_ID = Menu.FIRST + 5;
	private static final int LOGOUT_ID = Menu.FIRST + 6;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item;

		if (SettingsChooser.sIsCompleteConfigured != false)
		{
			item = menu.add(0, HOME_ID, 0, R.string.menuitem_home);
			item.setIcon(R.drawable.home);

			item = menu.add(0, CONTACTS_ID, 0, R.string.menuitem_contacts);
			item.setIcon(R.drawable.contacts);

			item = menu.add(0, MAP_ID, 0, R.string.menuitem_map);
			item.setIcon(R.drawable.map);

			item = menu.add(0, SETTINGS_ID, 0, R.string.menuitem_settings);
			item.setIcon(R.drawable.settings);

			item = menu.add(0, MAIL_ID, 0, R.string.menuitem_mail);
			item.setIcon(R.drawable.mail);
		}

		item = menu.add(0, LOGOUT_ID, 0, R.string.menuitem_logout);
		item.setIcon(R.drawable.exit);

		return true;
	}

}
