package de.fhkl.helloWorld.interfaces.model.account.profile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

public class Contact extends StructuredProfileAttribute<String, Attribute> {

	private boolean ignored;

	private String openID = "";
	private String publicKeyURL = "";
	private String fullName= "";

	public String getPublicKeyURL() {
		return publicKeyURL;
	}

	public void setPublicKeyURL(String publicKeyURL) {
		this.publicKeyURL = publicKeyURL;
	}

	public Contact() {
		super("contact");
		attributes.put("relation", new AttributeList<RelationShipType>(
				"relation"));
		attributes.put("profiles", new AttributeList<SubProfile>("profiles"));
		
	}

	@SuppressWarnings("unchecked")
	public AttributeList<RelationShipType> getRelation() {
		return (AttributeList<RelationShipType>) getAttribute("relation");
	}

	public void setRelation(AttributeList<RelationShipType> relation) {
		setAttribute(relation);
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	@SuppressWarnings("unchecked")
	public AttributeList<SubProfile> getProfiles() {
		return (AttributeList<SubProfile>) getAttribute("profiles");
	}

	public void setProfiles(AttributeList<SubProfile> profiles) {
		setAttribute(profiles);
	}


	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}
	
	public String getFullName(){
		return fullName;
	}
	
	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		setOpenID(node.getAttribute("openID"));
		setFullName(node.getAttribute("fn"));
		setIgnored(Boolean.parseBoolean(node.getAttributes().getNamedItem(
				"ignored").getNodeValue()));
		setPublicKeyURL(node.getAttribute("publicKeyURL"));

	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("ignored", "" + isIgnored());
		elem.setAttribute("openID", "" + openID);
		elem.setAttribute("fn", "" + fullName);
		elem.setAttribute("publicKeyURL", publicKeyURL);
		return elem;
	}

}
