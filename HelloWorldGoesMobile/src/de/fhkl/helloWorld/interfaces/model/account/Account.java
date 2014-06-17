package de.fhkl.helloWorld.interfaces.model.account;

import java.sql.Timestamp;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;


import de.fhkl.helloWorld.interfaces.model.account.configuration.Configuration;
import de.fhkl.helloWorld.interfaces.model.account.key.KeyPair;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public class Account extends StructuredAttribute<String, Attribute> {

	private static final String I = 
		"======================= [HELLO-WORLD] " +
		 "Account" + ": ";
	
	private Date timestamp;

	private String url;

	public Account() {
		super("account");
		attributes
				.put("subProfiles", new AttributeList<SubProfile>("subProfiles"));
		attributes.put("profile", new Profile());
		attributes.put("keyPair", new KeyPair());
		attributes.put("contacts", new AttributeList<Contact>("contacts"));
		attributes.put("relationShipTypes",
				new AttributeList<RelationShipType>("relationShipTypes"));
		
		attributes.put("configuration", new Configuration());
		
		this.timestamp = new Date();
		this.url = "";
	}

	public Date getTimestamp() {
		return new Date();
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	public AttributeList<SubProfile> getSubProfiles() {
		Log.i(I, "getSubProfiles: " + getAttribute("subProfiles").toString());
		return (AttributeList<SubProfile>) getAttribute("subProfiles");
	}

	public void setSubProfiles(AttributeList<SubProfile> subProfiles) {
		setAttribute(subProfiles);
	}
	
	public Configuration getConfiguration(){
		return (Configuration) getAttribute("configuration");
	}
	
	public void setConfiguration(Configuration conf){
		setAttribute(conf);
	}

	public Profile getPrivateProfile() {
		return (Profile) getAttribute("profile");
	}

	public void setPrivateProfile(Profile privateProfile) {
		setAttribute(privateProfile);
	}

	@SuppressWarnings("unchecked")
	public AttributeList<Contact> getContacts() {
		return (AttributeList<Contact>) getAttribute("contacts");
	}

	public void setContacts(AttributeList<Contact> contacts) {
		setAttribute(contacts);
	}

	public KeyPair getKeyPair() {
		return (KeyPair) getAttribute("keyPair");
	}

	public void setKeyPair(KeyPair keyPair) {
		setAttribute(keyPair);
	}

	@SuppressWarnings("unchecked")
	public AttributeList<RelationShipType> getRelationShipTypes() {
		return (AttributeList<RelationShipType>) getAttribute("relationShipTypes");
	}

	public void setRelationShipTypes(
			AttributeList<RelationShipType> relationShipTypes) {
		setAttribute(relationShipTypes);
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);

		setTimestamp(new Timestamp(0));
		url = node.getAttributes().getNamedItem("url").getNodeValue();

	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("timestamp", "" + getTimestamp().getTime());
		elem.setAttribute("url", getUrl());
		return elem;
	}

}
