package de.fhkl.helloWorld.interfaces.model.account.key;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

/**
 * DONE
 */
public class KeyPair extends StructuredAttribute<String, Attribute> {

	public KeyPair() {
		super("keyPair");
		attributes.put("privateKey", new PrivateKey());
		attributes.put("publicKey", new PublicKey());
	}

	public PrivateKey getPrivateKey() {
		return (PrivateKey) getAttribute("privateKey");
	}

	public void setPrivateKey(PrivateKey key) {
		setAttribute(key);
	}

	public PublicKey getPublicKey() {
		return (PublicKey) getAttribute("publicKey");
	}

	public void setPublicKey(PublicKey key) {
		setAttribute(key);
	}
}
