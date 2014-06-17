package de.fhkl.helloWorld.implementation.model.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

/**
 * The Class CryptoUtil. This class offers some useful methods for the de- /
 * encryption of HelloWorld. Some methods are only for debugging.
 */
public class CryptoUtil {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "CryptoUtil" + ": ";

	/**
	 * Converts a ByteArrayOutputStream to a ByteArrayInputStream
	 * 
	 * @param out
	 *            the ByteArrayOutputStream
	 * 
	 * @return ByteArrayInputStream
	 */
	public static ByteArrayInputStream outToInputStream(
			ByteArrayOutputStream out) {
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * Converts a ByteArrayInputStream to a ByteArrayOutputStream
	 * 
	 * @param in
	 *            the ByteArrayInputStream
	 * 
	 * @return ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream inToOutputStream(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			int currentByte;
			while ((currentByte = in.read()) != -1) {
				out.write(currentByte);
			}
		} catch (Exception e) {
			System.out.println("USE A STATIC LOGGER FOR LOGGING!!!");
		}
		return out;
	}

	/**
	 * Converts an InputStream to a String
	 * 
	 * @param in
	 *            the InputStream
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String inputStreamToString(InputStream in) {

		StringBuffer buffer = new StringBuffer();
		try {

			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if InputStream is encrypted. If the first five signs are similar
	 * to a normal xml-header (<?xml) the InputStream isn't encrypted
	 * 
	 * @param in
	 *            the in
	 * 
	 * @return true, if is encrypted
	 */
	public static boolean isEncrypted(InputStream in) {
		// byteArray for the five signs
		byte[] b = new byte[5];

		Log.i(I, "param InputStream in (should not be null): " + in);
		
		// set marker for reset InputStream later
		in.mark(5);

		try {
			// read the first 5 signs from the InputStream
			in.read(b, 0, 5);
		} catch (Exception e) {
			System.out.println("USE A STATIC LOGGER FOR LOGGING!!!");
		}

		if ((new String(b)).equals("<?xml")) {
			try {
				// reset the InputStream
				in.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	public static boolean isEncrypted(String in) {
	
		if(in.matches("<?xml")) 
			return true;
		return false;
	}

}
