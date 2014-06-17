package de.fhkl.helloWorld.interfaces.model.account.profile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.account.key.KeyWrapper;

public class EncryptedProfile extends Profile {

	private String algorithm;

	public EncryptedProfile() {
		super("encryptedProfile");
		attributes.put("keyWrapper", new KeyWrapper());
		algorithm = "";
	}

	public KeyWrapper getKeyWrapper() {
		return (KeyWrapper) getAttribute("keyWrapper");
	}

	public void setKeyWrapper(KeyWrapper keyWrapper) {
		setAttribute(keyWrapper);
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		this.algorithm = node.getAttribute("algorithm");
	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		this.algorithm = elem.getAttribute("algorithm");
		return elem;
	}

}
