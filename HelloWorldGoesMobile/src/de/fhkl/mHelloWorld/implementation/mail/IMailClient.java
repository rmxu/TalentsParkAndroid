package de.fhkl.mHelloWorld.implementation.mail;



import java.util.Collection;

import de.fhkl.helloWorld.HelloWorldProperties;

/**
 * This class provides messages to send and receive Objects from the type
 * <code>MessageBean</code> using a mail account.
 * 
 * A <code>MessageBean</code> can contain several attachements. Those
 * attachements are ment to be from the type <code>MessageWrapper</code>.
 * 
 * By now the user has only one mail account to check for mails that contain
 * messages for HelloWorld and to send messages to other users.
 */
public interface IMailClient {

	/**
	 * This string is used to identify HelloWorld messages in the mailbox by
	 * comparing it to the subject field of every mesage;
	 */
	public static final String IDENTIFIER = HelloWorldProperties.getString("MailSubjectIdentifier");

	/**
	 * Creates a Message object from the MessageBean objects and sends it using
	 * the users predefined mail account
	 * 
	 * @param msg
	 * @return true, if everything works fine
	 */
	public boolean sendMessage(MessageBean msg);

	/**
	 * Fetches messages from the users predefined mail account into MessageBean
	 * objects. Only messages containing the <code>IDENTIFIER</code> are
	 * fetched and deleted from the server. For further processing the messages
	 * are dumped in temporary files
	 * 
	 * @return Collection
	 */
	public Collection<MessageBean> receiveMessages();

}
