package de.fhkl.helloWorld.interfaces.model.account.profile;

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.single.SingleDateAttribute;

/**
 * DONE
 */
public class Profile extends StructuredProfileAttribute<String, Attribute> {

	private String url;

	public Profile() {
		super("profile");
		attributes.put("timestamp", new SingleDateAttribute("timestamp"));
		attributes.put("hcard", new HCard());
		attributes.put("openID", new OpenID());
		attributes
				.put("attributes", new AttributeList<Attribute>("attributes"));
		url = "";
	}

	protected Profile(String key) {
		super(key);
		attributes.put("timestamp", new SingleDateAttribute("timestamp"));
		attributes.put("hcard", new HCard());
		attributes.put("openID", new OpenID());
		attributes
				.put("attributes", new AttributeList<Attribute>("attributes"));
		url = "";
	}

	public Date getTimestamp() {
		return ((SingleDateAttribute) attributes.get("timestamp")).getValue();
	}

	public void setTimestamp(Date timestamp) {
		((SingleDateAttribute) attributes.get("timestamp")).setValue(timestamp);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public OpenID getOpenID() {
		return (OpenID) getAttribute("openID");
	}

	public void setOpenID(OpenID openID) {
		setAttribute(openID);
	}

	public HCard getHCard() {
		return (HCard) getAttribute("hcard");
	}

	public void setHCard(HCard card) {
		setAttribute(card);
	}

	@SuppressWarnings("unchecked")
	public AttributeList<Attribute> getAttributes() {
		return (AttributeList<Attribute>) getAttribute("attributes");
	}

	public void setAttributes(AttributeList<Attribute> attributes) {
		setAttribute(attributes);
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		url = node.getAttributes().getNamedItem("url").getNodeValue();

	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("url", getUrl());
		return elem;
	}
	
}
