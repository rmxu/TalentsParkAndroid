package de.fhkl.helloWorld.interfaces.model.messages;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import android.util.Log;
import java.io.*;



import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

/**
 * This class represents the message "envelope". All messages are send within
 * this "envelope" to guarantee the integrity and authenticity.
 */

//////// IMPORTANT: DOES NOT WORK: setNodeValue // WORKS: appendChild(dom.createTextNode(..))
//////// IMPORTANT: DOES NOT WORK: item(0).getNodeValue() // WORKS: item(0).getFirstChild().getNodeValue()

public class MessageWrapper extends
		StructuredAttribute<String, SingleAttribute<String>> {
	
	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "MessageWrapper" + ": ";	

	/**
	 * message will not be parsed, instead message will be parsed to XML,
	 * encrypted and then saved in the attribdata field data.<br />
	 * The other way data will be decrypted, parsed to XML and saved in the
	 * message field.
	 * 
	 */
	private Message message;

	public MessageWrapper() {
		super("messageWrapper");
		attributes.put("sessionKey", new SingleAttribute<String>("sessionKey"));
		attributes.put("signatureSessionKey", new SingleAttribute<String>(
				"signatureSessionKey"));
		attributes.put("signatureData", new SingleAttribute<String>(
				"signatureData"));

		// The encapsulated message
		attributes.put("data", new SingleAttribute<String>("data"));
	}

	public String getSessionKey() {
		return attributes.get("sessionKey").getValue();
	}

	public void setSessionKey(String sessionKey) {
		attributes.get("sessionKey").setValue(sessionKey);
	}

	public String getSignatureSessionKey() {
		return attributes.get("signatureSessionKey").getValue();
	}

	public void setSignatureSessionKey(String signatureSessionKey) {
		attributes.get("signatureSessionKey").setValue(signatureSessionKey);
	}

	public String getData() {
		return attributes.get("data").getValue();
	}

	public void setData(String data) {
		attributes.get("data").setValue(data);
	}

	public String getSignatureData() {
		return attributes.get("signatureData").getValue();
	}

	public void setSignatureData(String signatureData) {
		attributes.get("signatureData").setValue(signatureData);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Element parseToXML(Document dom) {
		Log.i(I, "------------------------ ENTERED parseToXML -----------------");
		Element node = dom.createElement(key);
		node.setAttribute("type", type);
		
		
		Log.i(I, "dom.createElement(key)");
		Log.i(I, "dom--"+ dom);
		Log.i(I, "type--"+ type);
		Log.i(I, "key--"+ key);
		Log.i(I, "getData()--"+ getData());
		Log.i(I, "getSignatureSessionKey()--"+ getSignatureSessionKey());
		Log.i(I, "getSessionKey()--"+ getSessionKey());
		
		
		
		Element sessionKey = dom.createElement("sessionKey");
		sessionKey.appendChild(dom.createTextNode(getSessionKey()));
		node.appendChild(sessionKey);
		
		
		//////// IMPORTANT: DO NOT USE setNodeValue - USE INSTEAD: appendChild(dom.createTextNode(..))!!!
		//sessionKey.setNodeValue("blubbdiblubb" + getSessionKey());
	
		
		Element data = dom.createElement("data");
		data.appendChild(dom.createTextNode(getData()));
		node.appendChild(data);
		
		Element signatureData = dom.createElement("signatureData");
		signatureData.appendChild(dom.createTextNode(getSignatureData()));
		node.appendChild(signatureData);
		
		
		
		Element signatureSessionKey = dom.createElement("signatureSessionKey");
		signatureSessionKey.appendChild(dom.createTextNode(getSignatureSessionKey()));
		node.appendChild(signatureSessionKey);

		Log.i(I, "------------------------ END OF parseToXML -----------------");
		return node;
	}

	//////// IMPORTANT: DO NOT USE getNodeValue - USE INSTEAD: getFirstChild().getNodeValue()!!!
	
	@SuppressWarnings("unchecked")
	public void parseFromXML(Element node) {
		Log.i(I, "--------------- Entered: parseFromXML ----------------");
		type = node.getAttribute("type");
		Log.i(I, "Setting Values from XML in attributes to MassageWrapper extends StructuredAttribute ...");
		
		setSessionKey(node.getElementsByTagName("sessionKey").item(0).getFirstChild().getNodeValue());
		setSignatureData(node.getElementsByTagName("signatureData").item(0).getFirstChild().getNodeValue());
		setData(node.getElementsByTagName("data").item(0).getFirstChild().getNodeValue());
		setSignatureSessionKey(node.getElementsByTagName("signatureSessionKey").item(0).getFirstChild().getNodeValue());
		
		Log.i(I, "--------------- End of: parseFromXML ----------------");
		
		// old version:
//		setSessionKey(node.getElementsByTagName("sessionKey").item(0).getNodeValue());
//		setSignatureData(node.getElementsByTagName("signatureData").item(0).getNodeValue());
//		setData(node.getElementsByTagName("data").item(0).getNodeValue());
//		setSignatureSessionKey(node.getElementsByTagName("signatureSessionKey").item(0).getNodeValue());
	}

}
