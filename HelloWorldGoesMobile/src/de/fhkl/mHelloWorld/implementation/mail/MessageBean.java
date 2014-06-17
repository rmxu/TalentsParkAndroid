package de.fhkl.mHelloWorld.implementation.mail;

import java.io.InputStream;
import java.util.HashMap;

/**
 * This class helps to collect the neccesary data to generate a Message object.
 * The Message object will be generatet inside the MailClient.
 */
public class MessageBean {

	private String senderAddress;
	private String recipientsAddress;
	private String subject;
	private String text;
	private HashMap<String, String> headers;
	private HashMap<String, InputStream> attachements;

	public MessageBean() {
		headers = new HashMap<String, String>();
		attachements = new HashMap<String, InputStream>();
		senderAddress = "";
		recipientsAddress = "";
		subject = "";
		text = "";
		headers = new HashMap<String, String>();
		attachements = new HashMap<String, InputStream>();
	}

	public MessageBean(String senderAddress, String recipientsAddress,
			String subject, String text, HashMap<String, String> headers) {
		this();
		this.senderAddress = senderAddress;
		this.recipientsAddress = recipientsAddress;
		this.subject = subject;
		this.text = text;
		this.headers = headers;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getRecipientsAddress() {
		return recipientsAddress;
	}

	public void setRecipientsAddress(String recipientsAddress) {
		this.recipientsAddress = recipientsAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public void addHeader(String attrib, String value) {
		this.headers.put(attrib, value);
	}

	public HashMap<String, InputStream> getAttachements() {
		return attachements;
	}

	public void setAttachements(HashMap<String, InputStream> attachements) {
		this.attachements = attachements;
	}

	public void addAttachement(String filename, InputStream file) {
		this.attachements.put(filename, file);
	}

}
