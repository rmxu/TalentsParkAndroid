package de.fhkl.mHelloWorld.implementation.util;

import android.util.Log;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;

public class NamingHelper 
{
	private static final String I = "======================= [HELLO-WORLD] "
		+ "NamingHelper" + ": ";
	
	// general
	public static final String HW_SUFFIX = ".xml";
	public static final String HW_DELIMITER = "-";
	public static String user;
	// SubProfile
	public static final String SP_PREFIX = "sp_";
	
	// PublicKey
	public static final String PUBKEY_PREFIX = "publickey_";

	//Account
	public static final String ACCOUNT_PREFIX ="account_";
	
	private static void init()
	{
		user = HelloWorldBasic.getUsername();
	}
	
	// Subprofile-FileName
	public static String makeSubProfileFileName(String groupName)
	{
		init();
		String name = SP_PREFIX + user + HW_DELIMITER + groupName + HW_SUFFIX;
		name = StringUtil.makeUrlValid(name);
		Log.i(I, "new SubProfile-FileName: " + name);
		return name;
	}
	
	// PublicKey-FileName
	public static String makePublicKeyFileName()
	{
		init();
		String name = PUBKEY_PREFIX + user + HW_SUFFIX;
		name = StringUtil.makeUrlValid(name);
		Log.i(I, "new PublicKey-FileName: " + name);
		return name;
	}
	
	public static String makeExportAccountFileName(){
		init();
		String name= ACCOUNT_PREFIX + user+ HW_SUFFIX;
		Log.i(I,"new Account-Filename: "+name);
		return name;
	}
	
	// make ftp to http-url
	public static String generateHttpUrlFromFtpSettings()
	{
		init();
		String ftpUrl = "";
		String httpUrl = "";
		try
		{
			ftpUrl = HelloWorldBasic.getAccount().getConfiguration()
			.getFileTransferConncetions().get(0).getHost().getValue();
			return generateHttpUrlFromFtpUrl(ftpUrl);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return httpUrl;
	}
	
	// make ftp to http-url (for ftp-settings)
	// changes the first 4 characters "ftp." of ftp.bla.de to "http://"
	public static String generateHttpUrlFromFtpUrl(String ftpUrl)
	{
		init();
		String httpUrl = "";
		try
		{
			httpUrl = "http://" + ftpUrl.substring(4, ftpUrl.length());
		}
		catch (Exception e)
		{
			Log.i(I, "Could not generate Http-Url from Ftp-Url, because Url is too short or empty.");
		}
		Log.i(I, "Generated Http-Url: " + httpUrl);
		return httpUrl;
	}
	
}
