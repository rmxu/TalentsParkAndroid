package de.fhkl.helloWorld.interfaces.model.account.key;

import javax.crypto.spec.SecretKeySpec;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;

public class SecretKey extends SingleAttribute<SecretKeySpec> {

	SecretKeySpec secKey;

	public SecretKey() {
		super("secretKey");
	}

}
