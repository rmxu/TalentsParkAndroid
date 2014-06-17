package de.fhkl.helloWorld.interfaces.model.messages.body;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.messages.MessageBody;
import de.fhkl.helloWorld.interfaces.model.messages.MessageType;

public class SimpleTextMessageBody extends MessageBody {
	
	public SimpleTextMessageBody() {
		super(MessageType.PRIVATE_MESSAGE);
		attributes.put("titel", new SingleAttribute<String>("titel"));
	}
	
	public String getTitel() {
		return ((SingleAttribute<String>) getAttribute("titel")).getValue();
	}

	public void setTitel(String titel) {
		((SingleAttribute<String>) getAttribute("titel")).setValue(titel);
	}

}
