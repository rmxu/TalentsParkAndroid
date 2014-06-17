package de.fhkl.helloWorld.interfaces.model.account.configuration;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public class Configuration extends StructuredAttribute<String, Attribute> {

	public Configuration() {

		super("configuration");
		attributes.put("fetchMailConnections", new AttributeList<FetchMailConnection>("fetchMailConnections"));
		attributes.put("sendMailConnections", new AttributeList<SendMailConnection>("sendMailConnections"));
		attributes.put("fileTransferConnections", new AttributeList<FileTransferConnection>("fileTransferConnections"));
	}

	public AttributeList<FetchMailConnection> getFetchMailConncetions() {
		return (AttributeList<FetchMailConnection>) attributes.get("fetchMailConnections");
	}

	public void setFetchMailConnections(AttributeList<FetchMailConnection> fmc) {
		attributes.put("fetchMailConnections", fmc);
	}

	public AttributeList<SendMailConnection> getSendMailConncetions() {
		return (AttributeList<SendMailConnection>) attributes.get("sendMailConnections");
	}

	public void setSendMailConnections(AttributeList<SendMailConnection> smc) {
		attributes.put("sendMailConnections", smc);
	}

	public AttributeList<FileTransferConnection> getFileTransferConncetions() {
		return (AttributeList<FileTransferConnection>) attributes
				.get("fileTransferConnections");
	}

	public void setFileTransferConnections(AttributeList<FileTransferConnection> ftc) {
		attributes.put("fileTransferConnections", ftc);
	}
}
