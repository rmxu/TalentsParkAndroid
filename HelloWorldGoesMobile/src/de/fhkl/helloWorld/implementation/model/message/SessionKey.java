package de.fhkl.helloWorld.implementation.model.message;

import javax.crypto.SecretKey;

/**
 * The class SessionKey contains: <br /> - the session key, for de-/encrypt
 * messages etc. <br /> - the signature of the encrypted key <br /> - the
 * encrypted session key
 * 
 */
public class SessionKey {

	private SecretKey secretKey;
	private String encryptedSecretKey;
	private String signature;

	public SecretKey getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}

	public String getEncryptedSecretKey() {
		return encryptedSecretKey;
	}

	public void setEncryptedSecretKey(String encryptedSecretKey) {
		this.encryptedSecretKey = encryptedSecretKey;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
