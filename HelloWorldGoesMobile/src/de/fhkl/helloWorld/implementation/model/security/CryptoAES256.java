/**
 * 
 */
package de.fhkl.helloWorld.implementation.model.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;

import android.util.Log;



/**
 * This class offers the symmetric encryption with a 256bit long
 * javax.crypto.SecretKey. The algorithm is AES.
 * 
 * Generated Keys from this class are used for the encryption of subprofiles and
 * messages.
 * 
 * @author HelloWorld
 */
public class CryptoAES256   {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "CryptoAES256" + ": ";

	// key length
	private int symmetricKeyLength = 256;
	// key algorithm
	private String symmetricAlgorithm = "AES";

	public InputStream encrypt(InputStream in, SecretKey key)
			throws InvalidKeyException {

		// generate a SecretKeySpec of the given key
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), key
				.getAlgorithm());

		/*
		 * ByteArrayOutputStream saves the encrypted stream. We had to do this,
		 * because if we returned the CipherInputStream, there were some
		 * problems in other methods because we had to reset the
		 * CipherInputStream at one point, and this didn't work
		 */
		ByteArrayOutputStream fos = new ByteArrayOutputStream();

		Cipher cipher;
		try {
			// get algorithm from key to initialize the cipher
			cipher = Cipher.getInstance(key.getAlgorithm());
			// set encrypt-mode and the given key to the cipher
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			// ENCRYPTION: encrypt the given InputStream
			CipherInputStream cis = new CipherInputStream(in, cipher);

			// write encrypted InputStream in OutputStream
			byte[] b = new byte[1024];
			int i = cis.read(b);
			while (i != -1) {
				fos.write(b, 0, i);
				i = cis.read(b);
			}

			cis.close();
			in.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert OutputStream to InputStream and return
		return CryptoUtil.outToInputStream(fos);
	}

	public InputStream decrypt(InputStream in, SecretKey key)
			throws IOException {
		// generate cipher objects for decryption
		Cipher cipher;

		/*
		 * ByteArrayOutputStream saves the encrypted stream. We had to do this,
		 * because if we returned the CipherInputStream, there were some
		 * problems in other methods because we had to reset the
		 * CipherInputStream at one point, and this didn't work
		 */
		ByteArrayOutputStream fos = new ByteArrayOutputStream();

		try {
			// get algorithm from key to initialize the cipher
			cipher = Cipher.getInstance(key.getAlgorithm());
			// set decrypt-mode and the given key to the cipher
			cipher.init(Cipher.DECRYPT_MODE, key);

			// DECRYPTION: decrypt the given InputStream
			CipherInputStream cis = new CipherInputStream(in, cipher);

			// write decrypted InputStream in OutputStream
			byte[] b = new byte[1024];
			int i = cis.read(b);
			while (i != -1) {
				fos.write(b, 0, i);
				i = cis.read(b);
			}

			cis.close();
			in.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		// Convert OutputStream to InputStream and return
		return CryptoUtil.outToInputStream(fos);
	}

	/**
	 * Symmetric encryption of a String.
	 * 
	 * @param data
	 *            the data which should be encrypted
	 * @param secretkey
	 *            the secretkey
	 * 
	 * @return the encrypted string
	 */
	public String encrypt(String data, SecretKey secretkey) {

		Cipher cipher;
		String encryptedData = null;

		try {
			cipher = Cipher.getInstance(secretkey.getAlgorithm());

			cipher.init(Cipher.ENCRYPT_MODE, secretkey);

			encryptedData = new String(org.apache.commons.codec.binary.Hex
					.encodeHex(cipher.doFinal(data.getBytes())));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return encryptedData;
	}

	/**
	 * Symmetric decryption of a String.
	 * 
	 * @param data
	 *            the encrypted data
	 * @param secretkey
	 *            the secretkey
	 * 
	 * @return the string
	 */
	public String decrypt(String data, SecretKey secretkey) {
		Log.i(I, "---------------------- decrypt: AES256 --------------------");
		Log.i(I, "--- 1. param data: " + data);
		Log.i(I, "--- 2. param secretkey.getFormat(): " + secretkey.getFormat());
		Cipher cipher;
		String decrypted = null;
		try {
			cipher = Cipher.getInstance(secretkey.getAlgorithm());
			Log.i(I, "secretkey.getAlgorithm(): " + secretkey.getAlgorithm());
			
			Log.i(I, "Try to initialize");
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			
			Log.i(I, "Try to create new String from cipher");
			decrypted = new String(cipher
					.doFinal(org.apache.commons.codec.binary.Hex.decodeHex(data
							.toCharArray())));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		Log.i(I, "---------------------- end of decrypt: AES256 --------------------");
		return decrypted;
	}

	/**
	 * generate secretKey
	 * @return
	 */
	public SecretKey generateKey() {
		SecretKey secretKey = null;
		try {
			// generate a secretKey with the KeyGenerator and the given
			// algorithm
			KeyGenerator kgen = KeyGenerator.getInstance(symmetricAlgorithm);
			// set length from the key
			kgen.init(symmetricKeyLength);

			// generate secretKey
			secretKey = kgen.generateKey();

		} catch (Exception e) {
			System.out.println("USE A STATIC LOGGER FOR LOGGING!!!");
		}
		return secretKey;
	}
}
