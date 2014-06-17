package de.fhkl.mHelloWorld.implementation.debug;

import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.key.PrivateKey;
import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import android.util.Log;

public class OwnKeysTest {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "OwnKeysTest" + ": ";
	private static final String END = " -OK.";
	private static Account account;
	
	private void init()
	{
		account = HelloWorldBasic.getAccount();
	}
	
	public OwnKeysTest()
	{
		init();
		checkPrivateKey();
		checkPublicKey();
	}
	
	private void checkPrivateKey()
	{
		logS("HW-PrivateKey Check");
		PrivateKey hwPrivateKey = account.getKeyPair().getPrivateKey();
		log("Key: " + hwPrivateKey.getKey());
		log("Type: " + hwPrivateKey.getType());
		log("Url: " + hwPrivateKey.getUrl());
		log("Timestamp: " + hwPrivateKey.getTimestamp());
		log("toString: " + hwPrivateKey.toString());
		logE("HW-PrivateKey Check");
		
		logL();
		
		logS("PrivateKey Check");
		java.security.PrivateKey privateKey = account.getKeyPair().getPrivateKey().getPrivateKey();
		log("Algorithm: " + privateKey.getAlgorithm());
		log("Format: " + privateKey.getFormat());
		log("toString: " + privateKey.toString());
		logE("PrivateKey Check");
	}
	
	private void checkPublicKey()
	{
		logS("HW-PublicKey Check");
		PublicKey hwPublicKey = account.getKeyPair().getPublicKey();
		log("Key: " + hwPublicKey.getKey());
		log("Type: " + hwPublicKey.getType());
		log("Url: " + hwPublicKey.getUrl());
		log("Timestamp: " + hwPublicKey.getTimestamp());
		log("Modulus: " + hwPublicKey.getModulus());
		log("PublicExponent: " + hwPublicKey.getPublicExponent());
		log("toString: " + hwPublicKey.toString());
		logE("HW-PublicKey Check");
		
		logL();
		
		logS("PublicKey Check");
		java.security.PublicKey publicKey = account.getKeyPair().getPublicKey().getPublicKey();
		log("Algorithm: " + publicKey.getAlgorithm());
		log("Format: " + publicKey.getFormat());
		log("toString: " + publicKey.toString());
		logE("PublicKey Check");
	}
	
	// END to make sure, that the Logging-String is printed completely
	private void log(String msg)
	{
		Log.i(I, msg + END);
	}
	
	private void logS(String msg)
	{
		Log.i(I, "");
		Log.i(I, "------------------------------------ START: " +  msg + "------------------------------------");
	}
	
	private void logE(String msg)
	{
		Log.i(I, "------------------------------------ END: " +  msg + "------------------------------------");
		Log.i(I, "");
	}
	
	private void logL()
	{
		Log.i(I, "");
		Log.i(I, "");
	}
}
