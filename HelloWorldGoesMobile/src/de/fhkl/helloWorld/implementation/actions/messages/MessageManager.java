package de.fhkl.helloWorld.implementation.actions.messages;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import android.util.Log;


import de.fhkl.helloWorld.HelloWorldProperties;
import de.fhkl.helloWorld.implementation.model.message.SessionKey;
import de.fhkl.helloWorld.implementation.model.parser.GlobalParser;
import de.fhkl.helloWorld.implementation.model.security.CryptoAES256;
import de.fhkl.helloWorld.implementation.model.security.CryptoRSA2048;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import de.fhkl.mHelloWorld.implementation.http.Client;
import de.fhkl.mHelloWorld.implementation.mail.MailClient;
import de.fhkl.mHelloWorld.implementation.mail.MessageReceiver;
import de.fhkl.mHelloWorld.implementation.mail.MessageSender;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageListener;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.SignatureAuthenticationFailedException;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FetchMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.messages.Message;
import de.fhkl.helloWorld.interfaces.model.messages.MessageBody;
import de.fhkl.helloWorld.interfaces.model.messages.MessageWrapper;
import de.fhkl.helloWorld.interfaces.model.messages.body.RelationShipRequestBody;
import de.fhkl.helloWorld.interfaces.model.messages.body.SimpleTextMessageBody;
import de.fhkl.helloWorld.interfaces.util.protocols.FetchMailProtocol;
import de.fhkl.mHelloWorld.implementation.mail.IMailClient;
import de.fhkl.mHelloWorld.implementation.mail.MessageBean;
//import de.fhkl.helloWorld.tests.parser.ProfileParserOut;
import de.fhkl.mHelloWorld.implementation.parser.ProfileParserOut;

/**
 * The class MessageManger contains methods for creating and opening a message.
 * 
 * @author helloworld
 */
public class MessageManager implements IMessageManager {

	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "MessageManager" + ": ";	
	 
	 private static boolean isInfoEnabled = true;

	private final IMessageListener messageListener;

	public MessageManager(IMessageListener messageListener) {
		this.messageListener = messageListener;
	}
	
	/*
	 * debugging: did not exist in desktop-verison !!
	 */
	public MessageManager() {
		this.messageListener = null;
	}

	@SuppressWarnings("unchecked")
	public String checkForEmailMessages()
			throws SignatureAuthenticationFailedException {

		if (isInfoEnabled)
			Log.i(I, "checkForEmailMessages");

		Account account = HelloWorldBasic.getAccount();

		Collection<Message> messages = new LinkedList<Message>();

		FetchMailConnection conn = 
			account.getConfiguration().getFetchMailConncetions().get(0);
		
		String host = conn.getHost().getValue();
		String username = conn.getUsername().getValue();
		String password = conn.getPassword().getValue();
		FetchMailProtocol protocol = conn.getProtocol();
		MessageReceiver mReceiver = 
			new MessageReceiver(
					host, 
					username, 
					password,
					protocol);
		
		MailClient mailClient = new MailClient(null, mReceiver);

		Collection<MessageBean> messageBeans = mailClient.receiveMessages();
		if (isInfoEnabled)
			Log.i(I, "checkForEmailMessages - received "+messageBeans.size()+" messages");
		for (MessageBean mb : messageBeans) 
		{
			// check if its an request message ...
			if(mb.getSubject().toString().equals(IMailClient.IDENTIFIER))
			{
				HashMap<String, InputStream> attachements = mb.getAttachements();
				for (String s : attachements.keySet()) {

					try {
						InputStream in = attachements.get(s);
						Document doc = (new GlobalParser()).parseXML(in);

						in.close();

						MessageWrapper m = new MessageWrapper();
						m.parseFromXML(doc.getDocumentElement());

						Message message = openMessageWrapper(m);
						messages.add(message);

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// only text message ...
			else
			{
				Log.i(I, "TEXT-Message-Content: " + mb.getText());
				return mb.getText();
			}

		}
		if (messages.size() > 0) interpretMessages(messages);
		else {
			if (isInfoEnabled) Log.i(I, "No new messages!");
			return "";
		}
		return "";
	}

	public void sendRelationShipRequest(AttributeList<SubProfile> subProfiles,
			String publicKeyUrl, String address, String openID, String content) {
		if (isInfoEnabled)
			Log.i(I, "sendRelationShipRequest");

		Log.i(I, "MessageWrapper mw = createRelationShipReques(...)");
		MessageWrapper mw = createRelationShipRequest(subProfiles, content,
				publicKeyUrl);
		
		
		Log.i(I, "sendMessage("+mw+","+address+")");
		sendMessage(mw, address);

	}

	public void sendMessage(MessageWrapper m, String address) {

		if (isInfoEnabled)
			Log.i(I, "---------------------------- sendMessage - to: " + address);

		Account account = HelloWorldBasic.getAccount();

		Log.i(I, "Parameter: MessageWrapper m: " + m.getData());  ////////////// here: everything ok - data exist
		
		// Parsing the message and adding it to the MessageBean
		if (isInfoEnabled)
			Log.i(I, "sendMessage - creating message bean...");
		Document dom = new DocumentImpl();
		dom.appendChild(m.parseToXML(dom));

		Log.i(I, "dom.getNodeName(): " + dom.getNodeName()); ////////////// here: ERROR - everything is null
		Log.i(I, "dom.getLocalName(): " + dom.getLocalName());
		Log.i(I, "dom.getNamespaceURI(): " + dom.getNamespaceURI());
		Log.i(I, "dom.getNodeValue(): " + dom.getNodeValue());
		Log.i(I, "dom.getPrefix(): " + dom.getPrefix());
		Log.i(I, "dom.getFirstChild().getNodeValue(): " + dom.getFirstChild().getNodeValue());
		// null-pointer-exception:
//		Log.i(I, "dom.getPrefix(): " + dom.getNextSibling().getNodeValue());
//		Log.i(I, "dom.getPrefix(): " + dom.getNextSibling().getNodeValue());
//		Log.i(I, "dom.getPrefix(): " + dom.getNextSibling().getNodeValue());
		//Log.i(I, "dom.getAttributes().item(0).getNodeValue(): " + dom.getAttributes().item(0).getNodeValue());
		//Log.i(I, "dom.getAttributes().item(1).getNodeValue(): " + dom.getAttributes().item(1).getNodeValue());
		//Log.i(I, "dom.getImplementation().toString(): " + dom.getImplementation().toString());

		StringWriter stringOut = new StringWriter();
		// serial schreibt den inhalt von dom in  stringOut
		XMLSerializer serial = new XMLSerializer(stringOut, new OutputFormat(
				dom));
		Log.i(I, "serial: " + serial);

		ByteArrayInputStream in = null;

		try {
			serial.serialize(dom);

			in = new ByteArrayInputStream(stringOut.toString()
					.getBytes("UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Neu drin
	String es = stringOut.toString();
		
		Log.i(I, "UND JETZT DAS GANZE ZUSAMMEN" + es);
		
		
//		try {
//			Log.i(I, "in: " + Helper.toString(in)); ////////////// here: ERROR - just data-sceleton exist
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		MessageBean msg = new MessageBean();
		msg.setRecipientsAddress(address);
		msg.setSenderAddress(account.getConfiguration().getSendMailConncetions().get(0).getSenderAddress().getValue());
		msg.setSubject(IMailClient.IDENTIFIER);
		
		msg.addAttachement(HelloWorldProperties.getString("MailAttachmentSuffix"), in);
		Log.i(I, "msg.getHeaders().values(): " + msg.getHeaders().values());

		// Getting the mail client and sending the request
		if (isInfoEnabled)
			Log.i(I, "sendMessage - starting mail client...");
		SendMailConnection conn = account.getConfiguration()
				.getSendMailConncetions().get(0);

		MessageSender mSender = 
			new MessageSender(
					conn.getHost().getValue(),//"host", 
					conn.getUsername().getValue(),//"username", 
					conn.getPassword().getValue(),//"password", 
					Integer.parseInt(conn.getPort().getValue()),//25, 
					conn.getProtocol()
					);

		MailClient mailClient = new MailClient(mSender, null);

		if (isInfoEnabled)
			Log.i(I, "sendMessage - sending message...");
		mailClient.sendMessage(msg);
	}

	@SuppressWarnings("unchecked")
	public void interpretMessages(Collection<Message> messages) {

		if (isInfoEnabled)
			Log.i(I, "interpretMessages: total: "+messages.size());

		HashMap<Integer, LinkedList<Message>> sortedMessages = new HashMap<Integer, LinkedList<Message>>();

		sortedMessages.put(0, new LinkedList<Message>());
		sortedMessages.put(1, new LinkedList<Message>());
		sortedMessages.put(2, new LinkedList<Message>());
		sortedMessages.put(3, new LinkedList<Message>());
		sortedMessages.put(4, new LinkedList<Message>());
		sortedMessages.put(5, new LinkedList<Message>());
		
		String messageTypes = "";
		
		for (Message m : messages) {
			messageTypes += " "+m.getHead().getMessageType().ordinal();
			sortedMessages.get(m.getHead().getMessageType().ordinal()).add(m);
		}
		Log.i(I, "messageTypes: "+messageTypes);

		if (sortedMessages.get(0).size() > 0) {
		}

		// PRIVATE_MESSAGE
		if (sortedMessages.get(1).size() > 0) {
			if (isInfoEnabled)
				Log.i(I, "interpretMessages - found "
						+ sortedMessages.get(1).size() + " private messages");
			Collection<Message<SimpleTextMessageBody>> ms = new LinkedList<Message<SimpleTextMessageBody>>();
			for (Message m : sortedMessages.get(3)) {
				ms.add(m);
			}
			messageListener.simpleTextMessageReceived(ms);
		}
		if (sortedMessages.get(2).size() > 0) {
		}

		// FRIENDSHIP_REQUEST
		if (sortedMessages.get(3).size() > 0) {
			if (isInfoEnabled)
				Log.i(I, "interpretMessages - found "
								+ sortedMessages.get(3).size()
								+ " friendship requests");
			Collection<Message<RelationShipRequestBody>> ms = new LinkedList<Message<RelationShipRequestBody>>();
			for (Message m : sortedMessages.get(3)) {
				ms.add(m);
			}
			messageListener.friendShipRequestReceived(ms);
		}
		if (sortedMessages.get(4).size() > 0) {
		}
		if (sortedMessages.get(5).size() > 0) {
		}
	}

	public Message openMessageWrapper(MessageWrapper messageWrapper)
			throws SignatureAuthenticationFailedException {

		if (isInfoEnabled)
			Log.i(I, "----------- Entered: openMessageWrapper ---------------");

		Account account = HelloWorldBasic.getAccount();

		CryptoRSA2048 rsaCrypto = new CryptoRSA2048();
		CryptoAES256 aesCrypto = new CryptoAES256();

		try {
			Log.i(I, "--- Interpret MessageWrapper-Content ---");
			Log.i(I, "messageWrapper.getData(): " + messageWrapper.getData());
			// bricht hier sonst schon ab.. gibt wohl keine message
//			Log.i(I, "messageWrapper.getMessage().getBody(): " + messageWrapper.getMessage().getBody());
//			Log.i(I, "messageWrapper.getMessage().getContent(): " + messageWrapper.getMessage().getContent());
			
			Log.i(I, "Try to get SessionKey ...");
			// get the encrypted session key and its signature

			String encryptedSessionKey = messageWrapper.getSessionKey();
			Log.i(I, "SessionKey: " + encryptedSessionKey);
			Log.i(I, "Try to get SignatureSessionKey ...");
			String signatureSessionKey = messageWrapper
					.getSignatureSessionKey();
			Log.i(I, "signatureSessionKey: " + signatureSessionKey);

			Log.i(I, "Own PrivateKey: " + account.getKeyPair().getPrivateKey().getPrivateKey());
			
			Log.i(I, "Try to decrypt SecretKey ...");
			// decrypt session key with own private key
			Log.i(I, "account.getKeyPair() ..."+ account.getKeyPair());
			Log.i(I, "account.getKeyPair().getPrivateKey() ..."+ account.getKeyPair().getPrivateKey());
			String decryptedSecretKey = rsaCrypto.decrypt(encryptedSessionKey,
					account.getKeyPair().getPrivateKey().getPrivateKey());
			//Log.i(I, "SecretKey: " + decryptedSecretKey);
			Log.i(I, "SecretKey: -deactivated, maybe bad for logging because of special chars-");

			// TODO algorithm musttest be specified in XML File

			// generate the session key
			Log.i(I, "Try to create new SecretKeySpec");
			SecretKeySpec seckey = new SecretKeySpec(decryptedSecretKey
					.getBytes(), "AES");
			Log.i(I, "seckey.getAlgorithm(): " + seckey.getAlgorithm());
			Log.i(I, "seckey.getFormat(): " + seckey.getFormat());
			Log.i(I, "seckey.getEncoded().toString(): " + seckey.getEncoded().toString());

			// decrypt the message
			Log.i(I, "Try to decrypt messageWrapper with the SecretKeySpec");
			String decryptedMessage = aesCrypto.decrypt(messageWrapper
					.getData(), seckey);
			Log.i(I, "decrypt messageWrapper successfully!!");
			
			Message message = new Message();
			Log.i(I, "Try to parse the decrypted XML-Message");
			Document doc = (new GlobalParser()).parseXML(decryptedMessage);
			Log.i(I, "parsed decrypted XML-Message successfully!!");
			
			ProfileParserOut.exportXML(doc, System.out);
			
			Log.i(I, "Try to parse from XML to Object");
			message.parseFromXML(doc.getDocumentElement());
			
			Log.i(I, "Body: "+message.getBody());
			
			Log.i(I, "PK: "+message.getHead()
					.getPublicKeyURL());
			
			// get the senders public Key
			java.security.PublicKey publicKey = getPublicKey(message.getHead()
					.getPublicKeyURL());

			// check signature
			boolean sessionKeySignatureValid = rsaCrypto.checkSignature(
					publicKey, decryptedSecretKey, signatureSessionKey);

			if (isInfoEnabled)
				Log.i(I, "check signature of sessionKey: "
						+ sessionKeySignatureValid);

			// check signature
			boolean dataSignatureValid = rsaCrypto.checkSignature(publicKey,
					decryptedMessage, messageWrapper.getSignatureData());
			if (isInfoEnabled)
				Log.i(I, "check signature of data: " + dataSignatureValid);

			// if encryption successful, return decrypted message
			if (sessionKeySignatureValid && dataSignatureValid) {
				if (isInfoEnabled)
					Log.i(I, "successfuly openend valid messageWrapper");

				if (isInfoEnabled)
					Log.i(I, "----------- End of: openMessageWrapper ---------------");
				
				return message;
			}

		} catch (Exception e) {
			Log.e(I, "failed to open messageWrapper");
			e.printStackTrace();
		}

		throw new SignatureAuthenticationFailedException(
				"Failed to check the signature");
	}

	public MessageWrapper createRelationShipRequest(
			AttributeList<SubProfile> subProfiles, String text,
			String publicKeyUrl) {

		Account account = HelloWorldBasic.getAccount();

		// generate new relationship request
		RelationShipRequestBody relationShipRequest = new RelationShipRequestBody();

		relationShipRequest.setPublicKey(account.getKeyPair().getPublicKey()
				.getUrl());
		relationShipRequest.setSubProfiles(subProfiles);
		
		relationShipRequest.setContent(text);

		Message<RelationShipRequestBody> message = new Message<RelationShipRequestBody>(
				account.getPrivateProfile().getOpenID().getUrl(),
				relationShipRequest);
		
		message.getHead().setPublicKeyURL(account.getKeyPair().getPublicKey().getUrl());

		if (isInfoEnabled)
			Log.i(I, "RelationShipRequest successfully created");

		return createMessage(message, publicKeyUrl, account.getKeyPair()
				.getPrivateKey().getPrivateKey());
	}

	public MessageWrapper createSimpleTextMessage(String title, String text) {

		if (isInfoEnabled)
			Log.i(I, "createSimpleTextMessage");

		Account account = HelloWorldBasic.getAccount();

		// Generate new simple text message
		SimpleTextMessageBody stmb = new SimpleTextMessageBody();
		stmb.setContent(text);
		stmb.setTitel(title);

		Message<SimpleTextMessageBody> message = new Message<SimpleTextMessageBody>(
				account.getPrivateProfile().getOpenID().getUrl(), stmb);

		return null; // createMessage(message, account.getContacts().get(0),
						// account
		// .getKeyPair().getPrivateKey().getPrivateKey());
	}

	// jules: einfache Nachrichten per public-key (empfaenger) und signatur -> privatekey (sender) schicken
	// status: do not work, yet
	public MessageWrapper createTextMessage(String text,
			String publicKeyUrl, PrivateKey senderPrivateKey) 
	{
		MessageWrapper messageWrapper = new MessageWrapper();
		
		// get the receiver's public key from the contact
		java.security.PublicKey publicKey = getPublicKey(publicKeyUrl);
		Log.i(I, "publicKey: " + publicKey);
		
		// generate the session key with its signature
		SessionKey sessionKey = getEncryptedSessionKey(publicKey,
				senderPrivateKey);
		Log.i(I, "sessionKey: " + sessionKey);
		// set the sessionKey and its signature
		messageWrapper.setSessionKey(sessionKey.getEncryptedSecretKey());
		messageWrapper.setSignatureSessionKey(sessionKey.getSignature());

		// sign the data and set it to the messageWrapper
		CryptoRSA2048 rsaCrypto = new CryptoRSA2048();
		String signature = rsaCrypto.signMessage(senderPrivateKey, text);
		messageWrapper.setSignatureData(signature);
		Log.i(I, "signature: " + signature);
		
		// encrypt the data and set it to the messageWrapper
		CryptoAES256 aesCrypto = new CryptoAES256();
		String encrypted = aesCrypto.encrypt(text, sessionKey.getSecretKey());
		messageWrapper.setData(encrypted);
		Log.i(I, "encrypted: " + encrypted);
		
		return messageWrapper;
	}
	
	public MessageWrapper createMessage(Message<? extends MessageBody> message,
			String publicKeyUrl, PrivateKey senderPrivateKey) {

		Log.i(I, "------------------------ ENTERED createMessage -----------------");
		Document dom = new DocumentImpl();
		Element elem = message.parseToXML(dom);
		dom.appendChild(elem);
		
		Log.i(I, "dom.getFirstChild().getNodeValue(): " + dom.getFirstChild().getNodeValue());

		StringWriter stringOut = new StringWriter();
		XMLSerializer serial = new XMLSerializer(stringOut, new OutputFormat(
				dom,"UTF-8",false));
		try {
			serial.serialize(dom);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String data = stringOut.toString();
//		
//		StringTokenizer token = new StringTokenizer(data);
//		String token1 = token.nextToken("\n");
//		String token2 = token.nextToken();
//		data = "";
//		data = token1+token2;
//		data.trim();
//		Log.i(I, "data: " + data);

		if (isInfoEnabled)
			Log.i(I, "createMessage");
		MessageWrapper messageWrapper = new MessageWrapper();

		// get the receiver's public key from the contact
		java.security.PublicKey publicKey = getPublicKey(publicKeyUrl);
		Log.i(I, "publicKey: " + publicKey);

		// generate the session key with its signature
		////////////////////////////////////////////////////////////////
		/////// getEncryptedSessionKey is a very complex methode ///////
		////////////////////////////////////////////////////////////////
		SessionKey sessionKey = getEncryptedSessionKey(publicKey,
				senderPrivateKey);
		Log.i(I, "sessionKey: " + sessionKey);
		// set the sessionKey and its signature
		messageWrapper.setSessionKey(sessionKey.getEncryptedSecretKey());
		messageWrapper.setSignatureSessionKey(sessionKey.getSignature());

		// sign the data and set it to the messageWrapper
		CryptoRSA2048 rsaCrypto = new CryptoRSA2048();
		String signature = rsaCrypto.signMessage(senderPrivateKey, data);
		messageWrapper.setSignatureData(signature);
		Log.i(I, "signature: " + signature);

		// encrypt the data and set it to the messageWrapper
		CryptoAES256 aesCrypto = new CryptoAES256();
		SecretKey sKey = sessionKey.getSecretKey();
		Log.i(I,"Secret Key: "+sKey.toString());
		String encrypted = aesCrypto.encrypt(data, sKey);
		messageWrapper.setData(encrypted);
		Log.i(I, "encrypted: " + encrypted);
		
		if (isInfoEnabled)
			Log.i(I, "Message successfully created");
		Log.i(I, "------------------------ END OF createMessage -----------------");
		return messageWrapper;
	}

	public java.security.PublicKey getPublicKey(String url) {

		if (isInfoEnabled)
			Log.i(I, "getPublicKey");

		Client client = new Client();

		// get the contacts public key
		if (!(url.contains("http://"))) {
			url = "http://"+url;
		}
		
		InputStream input = client.getRequestAsInputStream(url);

		// parse public key
		Document xml = GlobalParser.parseXML(input);
		PublicKey pb = new PublicKey();
		pb
				.parseFromXML((Element) xml.getElementsByTagName("publicKey")
						.item(0));
		java.security.PublicKey publicKey = pb.getPublicKey();

		if (publicKey == null)
			Log.e(I, " failed to download java.security.PublicKey: " + url);
		else if (isInfoEnabled)
			Log.i(I, "succesfully downloaded java.security.PublicKey: "
					+ url);

		return publicKey;
	}

	public SessionKey getEncryptedSessionKey(java.security.PublicKey publicKey,
			PrivateKey privateKey) {

		if (isInfoEnabled)
			Log.i(I, "getEncryptedSessionKey");

		// create secretKey
		SecretKey secretKey = (new CryptoAES256()).generateKey();
		String secretKeyString = new String(secretKey.getEncoded());

		Log.i(I,"SecretKey: "+secretKeyString);
		
		// create Instance of SessionKey and initialize
		SessionKey sessionKey = new SessionKey();
		sessionKey.setSecretKey(secretKey);

		// encrypt secretKey
		String encryptedSecretKey = null;
		CryptoRSA2048 crypto = new CryptoRSA2048();

		// sign the message
//		doesnt work because of encoding
//		String signature = crypto.signMessage(privateKey, secretKeyString);
//		sessionKey.setSignature(signature);

		
		String signature = crypto.signMessage2(privateKey, secretKey);
		sessionKey.setSignature(signature);
		
		if (isInfoEnabled)
			Log.i(I, "try to encrypt generated sessionKey");
		try {
//			doesnt work because of encoding
//			encryptedSecretKey = crypto.encrypt(secretKeyString, publicKey);
			encryptedSecretKey = crypto.encrypt2(secretKey, publicKey);

		} catch (Exception e) {
			Log.e(I, "failed to encrypt generated sessionkey");
			e.printStackTrace();
			return null;
		}

		sessionKey.setEncryptedSecretKey(encryptedSecretKey);
		if (isInfoEnabled)
			Log.i(I, "sessionKey succesfully encrypted and signed");
		return sessionKey;
	}
}
