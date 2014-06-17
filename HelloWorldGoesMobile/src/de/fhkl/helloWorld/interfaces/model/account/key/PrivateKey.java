package de.fhkl.helloWorld.interfaces.model.account.key;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.sql.Timestamp;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;


import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

/**
 * DONE parser
 */
public class PrivateKey extends
		StructuredAttribute<String, SingleAttribute<String>> {
	
	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "PrivateKey" + ": ";	

	private String url;
	private Date timestamp;
	private java.security.PrivateKey privateKey;

	public PrivateKey() {
		super("privateKey");
		attributes.put("modulus", new SingleAttribute<String>("modulus"));
		attributes.put("public_exponent", new SingleAttribute<String>(
				"public_exponent"));
		attributes.put("private_exponent", new SingleAttribute<String>(
				"private_exponent"));
		attributes.put("prime_p", new SingleAttribute<String>("prime_p"));
		attributes.put("prime_q", new SingleAttribute<String>("prime_q"));
		attributes.put("prime_exponent_p", new SingleAttribute<String>(
				"prime_exponent_p"));
		attributes.put("prime_exponent_q", new SingleAttribute<String>(
				"prime_exponent_q"));
		attributes.put("crt_coefficient", new SingleAttribute<String>(
				"crt_coefficient"));
		url = "";
		timestamp = new Date();
	}

	public PrivateKey(java.security.PrivateKey key) {
		this();
		privateKey = key;
	}

	public java.security.PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(java.security.PrivateKey key) {
		privateKey = key;
		
		Log.i(I,"setPrivateKey Public_Exponent:"+privateKey.toString());
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

		setTimestamp(new Timestamp(0));
		
		// android-debugging: - overwrite timestamp - 
		setTimestamp(new Date());

		RSAPrivateCrtKeySpec rsaKey = new RSAPrivateCrtKeySpec(new BigInteger(
				getAttribute("modulus").getValue()), new BigInteger(
				getAttribute("public_exponent").getValue()), new BigInteger(
				getAttribute("private_exponent").getValue()), new BigInteger(
				getAttribute("prime_p").getValue()), new BigInteger(
				getAttribute("prime_q").getValue()), new BigInteger(
				getAttribute("prime_exponent_p").getValue()), new BigInteger(
				getAttribute("prime_exponent_q").getValue()), new BigInteger(
				getAttribute("crt_coefficient").getValue()));

		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			setPrivateKey(keyFactory.generatePrivate(rsaKey));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Element parseToXML(Document dom) {

		RSAPrivateCrtKey spec = (RSAPrivateCrtKey) getPrivateKey();

		getAttribute("modulus").setValue(spec.getModulus().toString());
		getAttribute("public_exponent").setValue(
				spec.getPublicExponent().toString());
		getAttribute("private_exponent").setValue(
				spec.getPrivateExponent().toString());
		getAttribute("prime_p").setValue(spec.getPrimeP().toString());
		getAttribute("prime_q").setValue(spec.getPrimeQ().toString());
		getAttribute("prime_exponent_p").setValue(
				spec.getPrimeExponentP().toString());
		getAttribute("prime_exponent_q").setValue(
				spec.getPrimeExponentQ().toString());
		getAttribute("crt_coefficient").setValue(
				spec.getCrtCoefficient().toString());

		Element elem = super.parseToXML(dom);

		elem.setAttribute("timestamp", "" + getTimestamp().getTime());

		return elem;
	}

}
