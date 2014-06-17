package de.fhkl.helloWorld.interfaces.actions.messages;

import java.util.Collection;

import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.messages.Message;

/**
 * This class manages all message transfers e.g. relationship requests
 */

public interface IMessageManager {
	
	/**
	 * Checks in the users mailbox for new messages
	 * @param account
	 * @return
	 * @throws SignatureAuthenticationFailedException 
	 */
	public String checkForEmailMessages() throws SignatureAuthenticationFailedException;
	
	/**
	 * Used by methods that receive messages, calls handlers
	 * @param message
	 */
	public void interpretMessages(Collection<Message> messages);
	
	
	/**
	 * Sends a relationship request
	 * @param url
	 * @param address
	 * @param openID
	 * @param content
	 */
	public void sendRelationShipRequest(AttributeList<SubProfile> subProfiles, String url, String address, String openID, String content);

}
