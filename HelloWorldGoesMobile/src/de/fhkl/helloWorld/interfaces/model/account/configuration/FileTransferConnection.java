package de.fhkl.helloWorld.interfaces.model.account.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;
import de.fhkl.helloWorld.interfaces.util.protocols.FileTransferProtocol;

public class FileTransferConnection extends
		StructuredAttribute<String, Attribute> {

	/**
	 * choosen filetransfer protocol
	 */
	private FileTransferProtocol protocol;

	public FileTransferConnection() {

		super("fileTransferConnection");
		attributes.put("host", new SingleAttribute<String>("host"));
		attributes.put("port", new SingleAttribute<String>("port"));
		attributes.put("username", new SingleAttribute<String>("username"));
		attributes.put("password", new SingleAttribute<String>("password"));
		attributes.put("httpurl", new SingleAttribute<String>("httpurl"));
	}

	public FileTransferProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(FileTransferProtocol protocol) {
		this.protocol = protocol;
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
	
	@SuppressWarnings("unchecked")
	public SingleAttribute<String> getHttpUrl() {
		return (SingleAttribute<String>) attributes.get("httpurl");
	}
	
	public void setHttpUrl(SingleAttribute<String> httpUrl) {
		attributes.put("httpurl", httpUrl);
	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		if(protocol != null)
			elem.setAttribute("protocol", protocol.ordinal() + "");
		else elem.setAttribute("protocol", 1+ "");
		return elem;
	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		protocol = FileTransferProtocol.values()[Integer.parseInt(node
				.getAttribute("protocol"))];
	}
}
