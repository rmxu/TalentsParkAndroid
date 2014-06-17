package de.fhkl.helloWorld.interfaces.model.account.key;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;

/**
 * TODO
 */
public class KeyWrapper extends SingleAttribute<Key> {
	
	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "KeyWrapper" + ": ";	

	private Date timestamp;
	private String algorithm;

	public KeyWrapper() {
		super("keyWrapper");
		timestamp = new Date();
		algorithm = "";
	}

	public KeyWrapper(Key securityKey, Date timestamp) {
		super("keyWrapper");
		this.value = securityKey;
		this.timestamp = timestamp;
	}

	public Key getSecretKey() {
		return value;
	}

	public void setSecretKey(Key securityKey) {
		this.value = securityKey;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		Log.i(I, "------------------------ setTimestamp: "+ timestamp +" -----------------");
		this.timestamp = timestamp;
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
		timestamp.setTime(
				new Long(
						node.getAttribute("timestamp")));
		algorithm = node.getAttribute("algorithm");
		String key = node.getAttribute("value");
		SecretKeySpec seckey;
		try {
			seckey = new SecretKeySpec(org.apache.commons.codec.binary.Hex
					.decodeHex(key.toCharArray()), algorithm);

			setSecretKey(seckey);

		} catch (Exception e) {
			// Oh, oh!
			e.printStackTrace();
		}
	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("timestamp", "" + getTimestamp().getTime());
		elem.setAttribute("algorithm", algorithm);
		elem.setAttribute("value",
				new String(Hex.encodeHex(value.getEncoded())));
		return elem;
	}
}
