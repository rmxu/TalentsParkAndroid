package de.fhkl.helloWorld.interfaces.model.messages;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public class Message<T extends MessageBody> extends StructuredAttribute<String, Attribute>{

	public Message() {
		super("message");
		attributes.put("messageHead", new MessageHead());
		attributes.put("messageBody", null);
	}

	public Message(MessageType messageType) {
		this();
		getHead().setMessageType(messageType);
	}
	
	public Message(MessageType messageType, String sender) {
		this(messageType);
		getHead().setSender(sender);
	}
	
	public Message(String sender, T body) {
		this(body.MESSAGE_TYPE, sender);
		setBody(body);
	}

	public MessageHead getHead() {
		return (MessageHead) getAttribute("messageHead");
	}

	public void setHead(MessageHead head) {
		setAttribute(head);
	}
	
	public String getContent() {
		return ((SingleAttribute<String>) getAttribute("content")).getValue();
	}

	public void setContent(String content) {
		((SingleAttribute<String>) getAttribute("content")).setValue(content);
	}

	public T getBody() {
		return (T) getAttribute("messageBody");
	}

	public void setBody(T body) {
		attributes.put("messageBody", body);
	}
}
