package de.fhkl.helloWorld.implementation.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import android.util.Log;

import de.fhkl.helloWorld.implementation.model.security.CryptoAES256;
import de.fhkl.helloWorld.implementation.model.security.CryptoUtil;
//import de.fhkl.helloWorld.implementation.util.http.Client;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;

/**
 * The Class ProfileRequester.
 */
public class ProfileRequester implements Callable<SubProfile> {

	//private static Logger logger = Logger.getLogger(ProfileRequester.class);

	//private Client client;
	HttpClient client = new DefaultHttpClient();
	
	private final EncryptedSubProfile friendProfile;

	/**
	 * Instantiates a new profile requester.
	 * 
	 * @param friendProfile
	 *            the friend profile
	 */
	public ProfileRequester(EncryptedSubProfile friendProfile) {
		client = new DefaultHttpClient();
		this.friendProfile = friendProfile;
	}

	public EncryptedSubProfile call() throws InterruptedException {

		String I = 
			 "=======[HELLO-WORLD]" +
			 "ProfileRequester" + ": ";
		
		String decryptedString = null;
		String url = friendProfile.getUrl();
		Log.i(I,"call: Thread started for URL: "
					+ url);
		if (!(url.contains("http://"))) {
			url = "http://"+url;
		}
		/*// request friend profile
		StringBuffer friendProfileIS = client
				.getRequestAsStringBuffer(url);*/
		Log.i(I, "1");
		HttpGet getMethod = new HttpGet(url);
		Log.i(I, "2");
		HttpResponse response;
		Log.i(I, "3");
		InputStream friendProfileIS = null;
		Log.i(I, "4");

		try
		{
			Log.i(I, "4");
		response = client.execute(getMethod);
		Log.i(I, "4");
		Log.i(I, "download executed");

		friendProfileIS = response.getEntity().getContent();
		}
		catch(Exception e)
		{
			Log.i(I, "Exception1");
			e.printStackTrace();
		}

		EncryptedSubProfile encp = (EncryptedSubProfile) friendProfile;

		// get the ProfileKey
		SecretKey key = (SecretKey) encp.getKeyWrapper().getSecretKey();
		
		// decrypt it
		String decrypted = null;
		try {

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(friendProfileIS));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
			}
			
			bufferedReader.close();
			
			//Log.i(I, "opened: "+stringBuilder.toString());
			Log.i(I,"key: "+key);
			Log.i(I,"decrypted Subprofile: "+stringBuilder.toString());
			decryptedString = stringBuilder.toString();
			
			if(decryptedString.charAt(0) == '<')
			{
				Log.i(I,"requested Subprofile not encrypted");
				return null;
			}
				
			
			CryptoAES256 crypto = new CryptoAES256();
			decrypted = crypto.decrypt(stringBuilder.toString(), key);
			
			if (CryptoUtil.isEncrypted(decrypted)) {
				/*if (logger.isInfoEnabled())
					logger.info("the subprofile: " + friendProfile.getUrl()
							+ " could not be decrypted!!!");*/
				return null;
			}

		} catch (Exception e) {
			Log.i(I,"Exception2");
			e.printStackTrace();
			return null;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Using factory get an instance of document builder
		DocumentBuilder db;
		Document dom = null ;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse(
					new ByteArrayInputStream(decrypted.getBytes("UTF-8")
					));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

		// parse using builder to get DOM representation of the XML file
		encp.parseFromXML(dom.getDocumentElement());
		encp.setUrl(url);
		
		Log.i(I,"requestContactProfiles: Thread succesful for URL: "
					+ encp.getUrl());
		return encp;
	}
}