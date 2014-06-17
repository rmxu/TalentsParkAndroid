package de.fhkl.helloWorld.interfaces.model.messages;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public abstract class MessageBody extends StructuredAttribute<String, Attribute> {
	
	public final MessageType MESSAGE_TYPE;
	
	public MessageBody(MessageType type) {
		super("messageBody");
		MESSAGE_TYPE = type;
		attributes.put("content", new SingleAttribute<String>("content"));
	}
	
	public String getContent() {
		return ((SingleAttribute<String>) getAttribute("content")).getValue();
	}

	public void setContent(String content) {
		((SingleAttribute<String>) getAttribute("content")).setValue(content);
	}

}
