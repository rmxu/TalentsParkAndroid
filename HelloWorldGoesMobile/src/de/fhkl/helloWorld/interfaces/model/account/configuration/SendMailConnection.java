package de.fhkl.helloWorld.interfaces.model.account.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;

public class SendMailConnection extends StructuredAttribute<String, Attribute> {

	/**
	 * choosen transfer protocol
	 */
	private SendMailProtocol protocol;

	public SendMailConnection() {

		super("sendMailConnection");
		attributes.put("host", new SingleAttribute<String>("host"));
		attributes.put("port", new SingleAttribute<String>("port"));
		attributes.put("username", new SingleAttribute<String>("username"));
		attributes.put("password", new SingleAttribute<String>("password"));
		attributes.put("senderAddress", new SingleAttribute<String>("senderAddress"));
		protocol =  null;
	}

	public SendMailProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(SendMailProtocol protocol) {
		this.protocol = protocol;
	}
	
	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getSenderAddress() {
		return (SingleAttribute<String>) attributes.get("senderAddress");
	}

	public void setSenderAddress(SingleAttribute<String> senderAddress) {
		attributes.put("senderAddress", senderAddress);
	}

	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getHost() {
		return (SingleAttribute<String>) attributes.get("host");
	}

	public void setHost(SingleAttribute<String> host) {
		attributes.put("host", host);
	}

	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getPort() {
		return (SingleAttribute<String>) attributes.get("port");
	}

	public void setPort(SingleAttribute<String> port) {
		attributes.put("port", port);
	}

	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getUsername() {
		return (SingleAttribute<String>) attributes.get("username");
	}

	public void setUsername(SingleAttribute<String> username) {
		attributes.put("username", username);
	}

	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getPassword() {
		return (SingleAttribute<String>) attributes.get("password");
	}

	public void setPassword(SingleAttribute<String> password) {
		attributes.put("password", password);
	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		if(protocol != null)
			elem.setAttribute("protocol", protocol.ordinal() + "");
		else
			elem.setAttribute("protocol",1 +"");
		return elem;
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		protocol = SendMailProtocol.values()[Integer.parseInt(node
				.getAttribute("protocol"))];
	}
}
