package de.fhkl.helloWorld.interfaces.model.account.profile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.IProfileAttribute;

public class SubProfile extends Profile implements IProfileAttribute{

	private RelationShipType relation;
	
	public SubProfile() {
		super("subProfile");
		attributes.put("contacts", new AttributeList<Contact>("contacts"));
	}

	protected SubProfile(String key) {
		super(key);
		attributes.put("contacts", new AttributeList<Contact>("contacts"));
	}

	public SubProfile(Profile profile) {
		this();
		setProfile(profile);
		attributes.put("contacts", new AttributeList<Contact>("contacts"));
	}
	
	public void setRelation(RelationShipType r){
		this.relation = r;
	}
	
	public RelationShipType getRelation(){
		return relation;
	}

	public void setProfile(Profile profile) {
		setHCard(profile.getHCard());
		setAttributes(profile.getAttributes());
		setOpenID(profile.getOpenID());
	}

	@SuppressWarnings("unchecked")
	public AttributeList<Contact> getContacts() {
		return (AttributeList<Contact>) getAttribute("contacts");
	}

	public void setContacts(AttributeList<Contact> contacts) {
		setAttribute(contacts);
	}
	
	
	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		RelationShipType r = new RelationShipType();
		r.setName(node.getAttribute("relation"));
		this.relation = r;

	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("relation", relation.getName());
		return elem;
	}


}
