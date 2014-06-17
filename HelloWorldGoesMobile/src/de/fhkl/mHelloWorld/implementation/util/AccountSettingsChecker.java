package de.fhkl.mHelloWorld.implementation.util;

import android.util.Log;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.Configuration;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FetchMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.util.protocols.FetchMailProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.FileTransferProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import de.fhkl.mHelloWorld.implementation.ShowOwnHCard;

/*
 * Checks the Account Settings. No Field is allowed to be empty
 */
public class AccountSettingsChecker 
{
	private static final String I = "======================= [HELLO-WORLD] "
		+ "AccountSettingsChecker" + ": ";
	
	private static final boolean DEBUGGING = true;			// set true for more details
	private static final String ERR = "=== [ERROR] -> ";	// Error-Descriptor
	private static final String SUB = "\t";					// indent log
	
	private String mExceptionCollector = "";		// collects Exception Messages
	private boolean mCheckStatus = false;			// total result of the check
	private boolean mFieldException = false;		// proof throws exception?
	private int mEmptyFieldsNumber = 0;				// number of empty fields
	private int mEmptyConfigNumber = 0;				// number of empty configs
	private boolean mEmptyPublicKeyUrl = false;		// publikeyurl empty?
	private Account mAccount = null;
	private Configuration mConf = null;
	
	// to create skeletons if configuration do not exist
	private ConfigurationSkeletonMaker maker = new ConfigurationSkeletonMaker(); 

	// Constructor
	public AccountSettingsChecker()
	{
		Log.i(I, "--------------------------------- Begin Settings Check ...");
		
		// initialize basic fields
		mAccount = HelloWorldBasic.getAccount();
		mConf = mAccount.getConfiguration();
		
		// check all Settings
		checkIncomingMailSettings();
		checkOutgoingMailSettings();
		checkFtpSettings();
		checkPublicKeySettings();
		
		// analyse results
		if (mEmptyFieldsNumber > 0 
				|| mEmptyConfigNumber > 0 
				|| mEmptyPublicKeyUrl == true
				|| mFieldException == true)
		{
			mCheckStatus = false;
		}
		else
		{
			mCheckStatus = true;
		}
		Log.i(I, "--------------------------------- Ended Settings Check: ");
		Log.i(I, "Check-Results: [" + mCheckStatus + "]"
				+ "\n\t" + mEmptyFieldsNumber + " empty Fields"
				+ "\n\t" + mEmptyConfigNumber + " empty Configuration"
				+ "\n\t" + "Throwed FieldException: " + mFieldException
				+ "\n\t" + "PublicKey Problem: " + mEmptyPublicKeyUrl);
		Log.i(I, "---------------------------------");
	}

	// Getters 
	public boolean isCheckStatus() {
		return mCheckStatus;
	}
	public String getExceptionCollector() {
		return mExceptionCollector;
	}
	public int getEmptyFieldsNumber() {
		return mEmptyFieldsNumber;
	}
	public boolean isEmptyPublicKeyUrl() {
		return mEmptyPublicKeyUrl;
	}
	public boolean isFieldException() {
		return mFieldException;
	}
	public int getEmptyConfigNumber() {
		return mEmptyConfigNumber;
	}
	
	private void addException(String exc)
	{
		mExceptionCollector += "\t* " + exc + "\n"; 
	}

	// proofs single field to not be empty
	private void proof(SingleAttribute<String> a)
	{
		String str = a.getValue().trim();
		if (str == "" || str == null || str.length() == 0)
		{
			Log.i(I, ERR + a.getKey() + " is empty!");
			mEmptyFieldsNumber++;
		}
		else
		{
			if (DEBUGGING)
				Log.i(I, SUB + a.getKey() + ": " + a.getValue());
		}
	}
	// proofs single field to not be empty
	private void proof(FetchMailProtocol a)
	{
		if (a == null)
		{
			Log.i(I, ERR + "FetchMailProtocol is null!");
			mEmptyFieldsNumber++;
		}
		else
		{
			if (DEBUGGING)
				Log.i(I, SUB + "FetchMailProtocol: " + a);
		}
	}
	// proofs single field to not be empty
	private void proof(SendMailProtocol a)
	{
		if (a == null)
		{
			Log.i(I, ERR + "SendMailProtocol is null!");
			mEmptyFieldsNumber++;
		}
		else
		{
			if (DEBUGGING)
				Log.i(I, SUB + "SendMailProtocol: " + a);
		}
	}
	// proofs single field to not be empty
	private void proof(FileTransferProtocol a)
	{
		if (a == null)
		{
			Log.i(I, ERR + "FileTransferProtocol is null!");
			mEmptyFieldsNumber++;
		}
		else
		{
			if (DEBUGGING)
				Log.i(I, SUB + "FileTransferProtocol: " + a);
		}
	}
	// proofs single field to not be empty
	private boolean proof(String a)
	{
		if (a == "" || a == null || a.length() == 0)
		{
			Log.i(I, ERR + "String is empty!");
			return false;
		}
		else
		{
			if (DEBUGGING)
				Log.i(I, SUB + "String: " + a);
		}
		return true;
	}
	
	// checks Fields of Incoming Mail Settings
	private boolean checkIncomingMailSettings()
	{
		Log.i(I, "IncomingMail-Settings:");
		AttributeList<FetchMailConnection> connections = mConf.getFetchMailConncetions();
		FetchMailConnection conn = null;
		try
		{
			conn = connections.get(0);
		}
		catch (Exception e)
		{
			Log.i(I, ERR + "No IncomingMailSettings found!");
			maker.createIncomingSkeleton();
			mEmptyConfigNumber++;
			return false;
		}
		try
		{
			proof(conn.getHost());
			proof(conn.getPassword());
			proof(conn.getPort());
			proof(conn.getSenderAddress());
			proof(conn.getUsername());
			proof(conn.getProtocol());
		}
		catch (Exception e)
		{
			mFieldException = true;
			Log.i(I, ERR + "Empty Field(s) in: IncomingMailSettings!");
			addException("Incoming Mail");
			return false;
		}
		return true;
	}
	
	// checks Fields of Outgoing Mail Settings
	private boolean checkOutgoingMailSettings()
	{
		Log.i(I, "OutgoingMail-Settings:");
		AttributeList<SendMailConnection> connections = mConf.getSendMailConncetions();
		SendMailConnection conn = null;
		try
		{
			conn = connections.get(0);
		}
		catch (Exception e)
		{
			Log.i(I, ERR + "No OutgoingMailSettings found!");
			maker.createOutgoingSkeleton();
			mEmptyConfigNumber++;
			return false;
		}
		try
		{
			proof(conn.getHost());
			proof(conn.getPassword());
			proof(conn.getPort());
			proof(conn.getSenderAddress());
			proof(conn.getUsername());
			proof(conn.getProtocol());
		}
		catch (Exception e)
		{
			mFieldException = true;
			Log.i(I, ERR + "Empty Field(s) in: OutgoingMailSettings!");
			addException("Outgoing Mail");
			return false;
		}
		return true;
	}
	
	// checks Fields of Ftp Settings
	private boolean checkFtpSettings()
	{
		Log.i(I, "FTP-Settings:");
		AttributeList<FileTransferConnection> connections = mConf.getFileTransferConncetions();
		FileTransferConnection conn = null;
		try
		{
			conn = connections.get(0);
		}
		catch (Exception e)
		{
			Log.i(I, ERR + "No Ftp Settings found!");
			maker.createFtpSkeleton();
			mEmptyConfigNumber++;
			return false;
		}
		try
		{
			proof(conn.getHost());
			proof(conn.getPassword());
			proof(conn.getPort());
			proof(conn.getHttpUrl());
			proof(conn.getUsername());
			proof(conn.getProtocol());
		}
		catch (Exception e)
		{
			mFieldException = true;
			Log.i(I, ERR + "Empty Field(s) in: Ftp Settings!");
			addException("FTP");
			return false;
		}
		return true;
	}
	
	// checks Url-field of the PublicKey
	private boolean checkPublicKeySettings()
	{
		Log.i(I, "PublicKey-Settings:");
		if (!proof(mAccount.getKeyPair().getPublicKey().getUrl()))
		{
			// PublicKeyUrl is empty ...
			mEmptyPublicKeyUrl = true;
			return false;
		}
		else
		{
			mEmptyPublicKeyUrl = false;
			return true;	
		}
	}
	
	
	
}
