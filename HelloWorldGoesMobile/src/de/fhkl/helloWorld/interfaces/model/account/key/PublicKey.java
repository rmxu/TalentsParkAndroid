package de.fhkl.helloWorld.interfaces.model.account.key;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

/**
 * DONE
 */
public class PublicKey extends
		StructuredAttribute<String, SingleAttribute<String>> {
	
	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "PublicKey" + ": ";	

	private String url;
	private Date timestamp;
	private java.security.PublicKey publicKey;

	public PublicKey() {
		super("publicKey");
		attributes.put("modulus", new SingleAttribute<String>("modulus"));
		attributes.put("public_exponent", new SingleAttribute<String>(
				"public_exponent"));
		url = "";
		timestamp = new Date();
	}

	public PublicKey(java.security.PublicKey key) {
		this();
		publicKey = key;
	}

	public void setModulus(String modulus) {
		getAttribute("modulus").setValue(modulus);
	}

	public String getModulus() {
		return getAttribute("modulus").getValue();
	}

	public void setPublicExponent(String exponent) {
		getAttribute("public_exponent").setValue(exponent);
	}

	public String getPublicExponent() {
		return getAttribute("public_exponent").getValue();
	}

	public java.security.PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(java.security.PublicKey key) {
		publicKey = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		Log.i(I, "------------------------ setTimestamp: "+ timestamp +" -----------------");
		this.timestamp = timestamp;
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);

		url = node.getAttributes().getNamedItem("url").getNodeValue();
		timestamp.setTime(new Long(node.getAttributes().getNamedItem(
				"timestamp").getNodeValue()));



		RSAPublicKeySpec rsaKey = new RSAPublicKeySpec(new BigInteger(
				getModulus()), new BigInteger(getPublicExponent()));

		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			setPublicKey(keyFactory.generatePublic(rsaKey));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Element parseToXML(Document dom) {

		RSAPublicKey spec = (RSAPublicKey) getPublicKey();
		setModulus(spec.getModulus().toString());
		setPublicExponent(spec.getPublicExponent().toString());

		Element elem = super.parseToXML(dom);

		elem.setAttribute("timestamp", "" + getTimestamp().getTime());
		elem.setAttribute("url", getUrl());

		return elem;
	}

}
