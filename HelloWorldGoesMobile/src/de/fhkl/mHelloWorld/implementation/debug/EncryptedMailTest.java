package de.fhkl.mHelloWorld.implementation.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xml.sax.InputSource;

import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import de.fhkl.mHelloWorld.implementation.http.Client;
import de.fhkl.mHelloWorld.implementation.util.ParseHelper;
import android.util.Log;

public class EncryptedMailTest {
	private static final String I = "======================= [HELLO-WORLD] "
			+ "EncryptedMailTest" + ": ";

	private static String mSecretText = "Super, die Decryption hat funktioniert!";
	private static String mPublicKeyUrl = "http://neeerd.ne.funpic.de/max-pk.xml";

	public static void makeTest()
	{
		// toString liefert verfälschte ergebnisse!!
		//String pFromAccount = HelloWorldBasic.getAccount().getKeyPair().getPublicKey().getPublicKey().toString();
		PublicKey pubKeyFromAccount = HelloWorldBasic.getAccount().getKeyPair().getPublicKey();
		String pFromAccount = ParseHelper.publicKeyToXml(pubKeyFromAccount);
		InputStream pFromUrl = getPublicKey(mPublicKeyUrl);
		try {
			// pFromUrl after this method: NULL !!
			comparePublicKeys(pFromAccount, pFromUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static InputStream getPublicKey(String url) {
		Log.i(I, "getPublicKey ...");
		Client client = new Client();
		InputStream pubKey = client.getRequestAsInputStream(mPublicKeyUrl);
		return pubKey;
	}

	// pFromUrl after this method: NULL !!
	public static void comparePublicKeys(String pFromAccount, 
											InputStream pFromUrl) 
	throws IOException {
		Log.i(I, "comparePublicKeys ...");
		Log.i(I, "pFromAccount: ");
		Log.i(I, pFromAccount);
		// hilft auch nix...
//		InputStream iWillBeNull = (new InputSource(new InputStreamReader(pFromUrl))).getByteStream();
		// InputStream is null after console-log!!!
		Log.i(I, "pFromUrl: " + inputStreamToString(pFromUrl));
//		Log.i(I, "pFromUrl (double-check): " + inputStreamToString(iWillBeNull));
	}

	// DIRTY METHOD!!! InputStream is null after this method!! - just for log-check!
	public static String inputStreamToString(InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}

		br.close();
		return sb.toString();
	}

}
