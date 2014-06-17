package de.fhkl.mHelloWorld.implementation.mail;

import java.util.Collection;


/**
 * This class provides methods to send and receive emails.
 */
public class MailClient implements IMailClient {

	/**
	 * For sending emails
	 */
	private MessageSender messageSender;

	/**
	 * For receiving emails
	 */
	private MessageReceiver mmessageReceiver;

	/**
	 * The MessageSender and MessageReceiver are build outside this class using
	 * the users mail account data.
	 * 
	 * @param messageSender
	 * @param mmessageReceiver
	 */
	public MailClient(MessageSender messageSender,
			MessageReceiver mmessageReceiver) {
		this.messageSender = messageSender;
		this.mmessageReceiver = mmessageReceiver;
	}

	public Collection<MessageBean> receiveMessages() {
		return mmessageReceiver.receiveMessages();
	}

	public boolean sendMessage(MessageBean msg) {
		return messageSender.sendMessage(msg);
	}
}
