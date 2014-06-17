package de.fhkl.helloWorld.interfaces.model.messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public class MessageHead extends
		StructuredAttribute<String, SingleAttribute<String>> {

	public MessageType messageType;

	public MessageHead() {
		super("messageHead");
		attributes.put("messageType",
				new SingleAttribute<String>("messageType"));
		attributes.put("sender", new SingleAttribute<String>("sender"));
		attributes.put("publicKeyURL", new SingleAttribute<String>("publicKeyURL"));
	}
	
	public String getPublicKeyURL() {
		return (String) getAttribute("publicKeyURL").getValue();
	}

	public void setPublicKeyURL(String url) {
		((SingleAttribute<String>) getAttribute("publicKeyURL")).setValue(url);
	}

	public MessageHead(MessageType mt) {
		this();
		setMessageType(mt);
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getSender() {
		return (String) getAttribute("sender").getValue();
	}

	public void setSender(String sender) {
		((SingleAttribute<String>) getAttribute("sender")).setValue(sender);
	}

	public void setMessageType(MessageType mt) {
		messageType = mt;
	}

	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		setMessageType(MessageType.values()[Integer.parseInt(getAttribute(
				"messageType").getValue())]);
	}

	public Element parseToXML(Document dom) {
		attributes.get("messageType").setValue(""+messageType.ordinal());
		Element elem = super.parseToXML(dom);
		return elem;
	}

}
