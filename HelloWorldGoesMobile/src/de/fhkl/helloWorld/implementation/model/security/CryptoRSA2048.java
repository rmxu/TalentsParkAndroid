package de.fhkl.helloWorld.implementation.model.security;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;

import android.util.Log;



/**
 * The Class CryptoRSA2048. This class offers the asymmetric encryption with a
 * 2048bit long java.security.KeyPair. The algorithm is RSA.
 * 
 * Generated key pairs from this class are used for the exchange of session keys
 * and to generate and verify digital signatures.
 * 
 * @author HelloWorld
 * 
 */
public class CryptoRSA2048  {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "CryptoRSA2048" + ": ";

	// key length
	private int asymmetricKeyLength = 2048;
	// key algorithm
	private String asymmetricAlgorithm = "RSA";
	// the signature hash algorithm
	private String signatureHash = "MD5withRSA";

	public boolean checkSignature(PublicKey publicKey, String message,
			String sign) {

		boolean verifySign = false;

		// generate signature
		Signature myVerifySign;

		try {
			// initialize the algorithm of the signature
			myVerifySign = Signature.getInstance(signatureHash);
			// initialize the publicKey of the sender
			myVerifySign.initVerify(publicKey);

			// generate the signature from the given message with the publicKey
			// of the sender
			byte[] byteMessage = message.getBytes();
			myVerifySign.update(byteMessage);

			// convert the given signature to byte[]
			byte[] signature = org.apache.commons.codec.binary.Hex
					.decodeHex(sign.toCharArray());

			// check if the given signature is identical to the generated
			verifySign = myVerifySign.verify(signature);

			System.out.println("USE A STATIC LOGGER FOR LOGGING!!!");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}

		if (verifySign == true)
			return true;
		return false;
	}

	public String signMessage(PrivateKey privateKey, String message) {
		try {
			// generate signature with own private key from the given message
			Signature mySign = Signature.getInstance(signatureHash);
			mySign.initSign(privateKey);
			
			byte[] test = message.getBytes();
			
			mySign.update(message.getBytes());

			// the signature
			byte[] byteSignedData = mySign.sign();

			// encode signature to hexa-decimal format and save this in a string
			// and return
			return new String(org.apache.commons.codec.binary.Hex
					.encodeHex(byteSignedData));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public String signMessage2(PrivateKey privateKey, SecretKey message) {
		try {
			// generate signature with own private key from the given message
			Signature mySign = Signature.getInstance(signatureHash);
			mySign.initSign(privateKey);
			
			byte[] test = message.getEncoded();
			
			mySign.update(test);

			// the signature
			byte[] byteSignedData = mySign.sign();

			// encode signature to hexa-decimal format and save this in a string
			// and return
			return new String(org.apache.commons.codec.binary.Hex
					.encodeHex(byteSignedData));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String encrypt(String in, PublicKey key) {

		// generate cipher object
		Cipher cipher;
		String encrytpedString = null;
		try {
			// set algorithm and encrypt-mode to the cipher
			cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// ENCRYPTION: encrypt the given String with given PublicKey
			encrytpedString = new String(org.apache.commons.codec.binary.Hex
					.encodeHex(cipher.doFinal(in.getBytes())));

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

		return encrytpedString;

	}
	
	public String encrypt2(SecretKey in, PublicKey key) {

		// generate cipher object
		Cipher cipher;
		String encrytpedString = null;
		try {
			// set algorithm and encrypt-mode to the cipher
			cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// ENCRYPTION: encrypt the given String with given PublicKey
			encrytpedString = new String(org.apache.commons.codec.binary.Hex
					.encodeHex(cipher.doFinal(in.getEncoded())));

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

		return encrytpedString;

	}

	public String decrypt(String encrypted, PrivateKey key) {

		Log.i(I, "---------------------- decrypt: RSA2048 --------------------");
		if (encrypted == null)
			Log.i(I, "Function will fail, because parameter encrypted is NULL !!");
		// generate cipher object
		Cipher cipher;
		String decrypted = null;
		try {
			// set algorithm and decrypt-mode to the cipher
			cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);

			Log.i(I, "Alogrithm successfully set.");
			// DECRYPTION: decrypt the given String with given PrivateKey
			Log.i(I, "Try to decrypt ...");
			// jules: start
			Log.i(I, "Try to get chars ...");
			
			byte[] test = encrypted.getBytes();
			
			char[] encryptedChars = encrypted.toCharArray();
			Log.i(I, "Try to get bytes ...");
			byte[] encryptedBytes = 
				org.apache.commons.codec.binary.Hex.decodeHex(encryptedChars);
			Log.i(I, "Try to create String from Bytes with cipher.doFinal(encryptedBytes) ...");
			decrypted = new String(cipher.doFinal(encryptedBytes));
			Log.i(I, "decrypted: " + decrypted);
			// jules: end
			
			/* old:
			decrypted = new String(cipher
					.doFinal(org.apache.commons.codec.binary.Hex
							.decodeHex(encrypted.toCharArray())));
							*/
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
		Log.i(I, "---------------------- end of decrypt: RSA2048 --------------------");
		return decrypted;
	}

	public KeyPair generateKeyPair() {
		KeyPair keyPair = null;
		try {
			// set asymmetric algorithm and length
			KeyPairGenerator keyPairGen = KeyPairGenerator
					.getInstance(asymmetricAlgorithm);
			keyPairGen.initialize(asymmetricKeyLength);

			// generate key pair
			keyPair = keyPairGen.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}

}
