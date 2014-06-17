package de.fhkl.mHelloWorld.implementation.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.PublicKey;

import org.xml.sax.InputSource;

import android.util.Log;
import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.implementation.util.ftp.*;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;

public class ExportAccount {
	private static final String I = "======================= [HELLO-WORLD] "
			+ "ExportAccount" + ": ";

	private static FileTransferConnection connection;
	private static String homeDir;
	private static String userName;
	private static String host;
	private static String user;
	private static String pw;
	private static FTPClient ftp;
	private static Account userAccount = HelloWorldBasic.getAccount();
	private static InputStream in = null;
	private static boolean connected=false;
	// simple account export (ftp)
	public static boolean export() {
		Log.i(I, "export() ");
		userName = HelloWorldBasic.getUsername();
		// HelloWorldBasic.getPath()
		// /data/data/de.fhkl.mHelloWorld.implementation/files/../

		Log.i(I, "HelloWorldBasic.getPath: "
				+ HelloWorldBasic.getPath().toString());
		
		Log.i(I, "HelloWorldBasic.getPath (test): "
				+ HelloWorldBasic.getPath());
		
		

		// Looking for homedirectory
//		if (!HelloWorldBasic.getPath().toString().equals(
//				"/data/data/de.fhkl.mHelloWorld.implementation/files/../")) {
//			homeDir = HelloWorldBasic.getPath() + userName + "/";
//		} else
			homeDir = HelloWorldBasic.getPath() //"/data/data/de.fhkl.mHelloWorld.implementation/"
					+ userName + "/";

		try {
			// get Connection info
			host = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getHost().getValue();
			Log.i(I, "host " + host);
			user = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getUsername().getValue();
			Log.i(I, "user" + user);
			pw = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getPassword().getValue();
			Log.i(I, "pw" + pw);

			// Setting up Connection
			ftp = new FTPClient(host, user, pw);

			ftp.setRemoteVerificationEnabled(false);

			connected=ftp.connect();
			if (connected){
				
				Log.i(I, "File-Path: "
						+ homeDir + "account.xml");
				
				File local = new File(homeDir + "account.xml");
				Log.i(I, "File local: " + local);

				in = new FileInputStream(local);
				String filename =NamingHelper.makeExportAccountFileName(); 
				// public boolean uploadFile(InputStream source, String target) {
				ftp.uploadFile(in, filename);
				
				
				Log.i(I, "ftp.connect();" + ftp.connect());
				ftp.disconnect();
			}
			

			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return connected;
	
	
	}

	 public static void uploadSubprofile(InputStream sub, String subName) {
	 Log.i(I, "exportSubprofile() ");
	 userName = HelloWorldBasic.getUsername();
	 // HelloWorldBasic.getPath()
	 // /data/data/de.fhkl.mHelloWorld.implementation/files/../
	
	 // Log.i(I, "HelloWorldBasic.getPath: "
	 // + HelloWorldBasic.getPath().toString());
	 //
	 // // Looking for homedirectory
	 // if (!HelloWorldBasic.getPath().toString().equals(
	 // "/data/data/de.fhkl.mHelloWorld.implementation/files/../")) {
	 // homeDir = HelloWorldBasic.getPath() + "userName" + "/";
	 // } else
	 // homeDir = "/data/data/de.fhkl.mHelloWorld.implementation/"
	 // + userName + "/";
	
	 String hwUsername = "";
	 try {
	 hwUsername = HelloWorldBasic.getUsername();
	 // get Connection info
	 host = userAccount.getConfiguration().getFileTransferConncetions()
	 .get(0).getHost().getValue();
	 Log.i(I, "host " + host);
	 user = userAccount.getConfiguration().getFileTransferConncetions()
	 .get(0).getUsername().getValue();
	 Log.i(I, "user" + user);
	 pw = userAccount.getConfiguration().getFileTransferConncetions()
	 .get(0).getPassword().getValue();
	 Log.i(I, "pw" + pw);
	
	 // Setting up Connection
	 ftp = new FTPClient(host, user, pw);
	
	 ftp.setRemoteVerificationEnabled(false);
	
	 ftp.connect();
	
	 // File local = new File(homeDir + "accou);
	 // Log.i(I, "File local: " + local);
	
	 // in = new FileInputStream(local);
	
	 } catch (Exception e) {
	 // e.printStackTrace();
	 }
	 subName = StringUtil.makeUrlValid(subName);
	 hwUsername = StringUtil.makeUrlValid(hwUsername);
	 // public boolean uploadFile(InputStream source, String target) {
	 Log.i(I, "Try to upload File: " + NamingHelper.makeSubProfileFileName(subName));
	 ftp.uploadFile(sub, NamingHelper.makeSubProfileFileName(subName));
	 Log.i(I, "ftp.connect();" + ftp.connect());
	
	 }

	public static Boolean uploadPublicKey(
			de.fhkl.helloWorld.interfaces.model.account.key.PublicKey key) {
		
		userAccount = HelloWorldBasic.getAccount();

		Boolean connected = false;
		try {
			Log.i(I, "Getting FTP-Settings ...");
			// get Connection info
			host = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getHost().getValue();
			Log.i(I, "host " + host);
			user = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getUsername().getValue();
			Log.i(I, "user" + user);
			pw = userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getPassword().getValue();
			Log.i(I, "pw" + pw);

			// Setting up Connection
			Log.i(I, "Setting up new FTP-Client ...");
			ftp = new FTPClient(host, user, pw);
			ftp.setRemoteVerificationEnabled(false);
			Log.i(I, "Connect ...");
			connected = ftp.connect();

			if (connected) {
				Log.i(I, "ok - connection established.");

				// String hwKey = "RSA Public Key\nmodulus: "
				// + key.getModulus()
				// + "\npublic exponent: "
				// + key.getPublicExponent();
				Log.i(I, "Parse PublicKey to XML ...");
				String pubKey = ParseHelper.publicKeyToXml(key);

				Log.i(I, "PublicKey: " + pubKey);

				InputStream keyStream = new ByteArrayInputStream(pubKey
						.getBytes());
				Log.i(I, "Uploading file ...");
				String fileName = NamingHelper.makePublicKeyFileName();
				if (ftp.uploadFile(keyStream, fileName)) 
				{
					Log.i(I, "Setting new PubKey-Url to account ...");
					userAccount.getKeyPair().getPublicKey()
						.setUrl(NamingHelper.generateHttpUrlFromFtpSettings()
								+ "/" + fileName);
					AccountManager am = new AccountManager();
					am.encryptAndSaveAccount(userAccount, HelloWorldBasic
									.getUsername(), HelloWorldBasic
									.getPassword(), null);
					Log.i(I, "New PublicKey-Url (from account): " + 
							userAccount.getKeyPair().getPublicKey().getUrl());
				}
			}
		} catch (Exception e) {
			Log.i(I, "Upload Public-Key fails");
		}
		return connected;
	}
}
