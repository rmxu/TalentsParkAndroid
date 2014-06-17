package de.fhkl.mHelloWorld.implementation.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Security;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import de.fhkl.helloWorld.HelloWorldProperties;
import de.fhkl.helloWorld.interfaces.util.protocols.FetchMailProtocol;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;

import android.util.Log;

public class MessageReceiver
{

	private static final String I = "======================= [HELLO-WORLD] "
			+ "MessageReceiver" + ": ";

	private static boolean DEBUGGING = true;

	private String host;
	private String username;
	private String password;
	private String mailbox;

	private String tempDir;

	private int port;
	private FetchMailProtocol protocol;

	private String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	private final Properties props;

	public MessageReceiver(String host, String username, String password,
			String mailbox, int port, FetchMailProtocol protocol)
	{

		this.host = host;
		this.username = username;
		this.password = password;
		this.mailbox = mailbox;

		this.port = port;
		this.protocol = protocol;

		this.props = System.getProperties();
		setUpProtocol();

		// TODO temporary email directory
		this.tempDir = HelloWorldBasic.getUserTempDir();
	}

	public MessageReceiver(String host, String username, String password,
			String mailbox)
	{
		this(host, username, password, mailbox, -1, FetchMailProtocol.POP3);
	}

	public MessageReceiver(String host, String username, String password)
	{
		this(host, username, password, "INBOX", -1, FetchMailProtocol.POP3);
	}

	public MessageReceiver(String host, String username, String password,
			FetchMailProtocol protocol)
	{
		this(host, username, password, "INBOX", -1, protocol);
	}

	private void setUpProtocol()
	{

		if (port == -1)
			port = protocol.standardPort();

		switch (protocol)
		{
		case IMAP:
			setUpIMAP();
		case IMAPS:
			setUpIMAPS();
		case NNTP:
			setUpNNTP();
		case NNTPS:
			setUpNNTPS();
		case POP3S:
			setUpPOP3S();
		case POP3:
		default:
			setUpPOP3();
		}
	}

	private void setUpPOP3()
	{
		props.put("mail.pop3.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	private void setUpIMAP()
	{
		props.put("mail.imap.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	private void setUpNNTP()
	{
		props.put("mail.nntp.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	private void setUpPOP3S()
	{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.put("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.put("mail.pop3.socketFactory.fallback", "false");
		props.put("mail.pop3.port", port + "");
		props.put("mail.pop3.socketFactory.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	private void setUpIMAPS()
	{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.put("mail.imap.socketFactory.class", SSL_FACTORY);
		props.put("mail.imap.socketFactory.fallback", "false");
		props.put("mail.imap.port", port + "");
		props.put("mail.imap.socketFactory.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	private void setUpNNTPS()
	{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.put("mail.nntp.socketFactory.class", SSL_FACTORY);
		props.put("mail.nntp.socketFactory.fallback", "false");
		props.put("mail.nntp.port", port + "");
		props.put("mail.nntp.socketFactory.port", port + "");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
	}

	/**
	 * Checks the predefined mailbox for HelloWorld messages. Sorry for the long
	 * method body. I am going to split it into some smaller parts soon.
	 * 
	 * @return Collection
	 */
	public Collection<MessageBean> receiveMessages()
	{

		Collection<MessageBean> messages = new LinkedList<MessageBean>();

		Session session = Session.getInstance(props);

		if (DEBUGGING)
		{
			Log.i(I, "Checking for messages ...");
			session.setDebug(true);
		}

		Store store;
		try
		{

			store = session.getStore(protocol.lowerCaseName());
			store.connect(host, port, username, password);

			Folder folder = store.getFolder(mailbox);

			// Open folder with etite access to delete checked messages
			folder.open(Folder.READ_WRITE);

			int totalMessages = folder.getMessageCount();

			if (totalMessages == 0)
			{
				Log.i(I, "No messages in mailbox " + mailbox
						+ ", returning ...");
				return messages;
			}

			/*
			 * Was always 0 in every test, doesn't indicate the number of new
			 * messages ... int newMessages = folder.getNewMessageCount();
			 */

			Log.i(I, "Opening mailbox ...\n\tFound " + totalMessages
					+ " messages in mailbox " + mailbox);

			// Search for HelloWorld messages in all messages
			// TODO need to check all messages?
			Message[] msgs = folder.getMessages();

			// Get suitable fetch profile
			/*
			 * FetchProfile fp = new FetchProfile();
			 * fp.add(FetchProfile.Item.ENVELOPE);
			 * fp.add(FetchProfile.Item.FLAGS); fp.add("X-Mailer");
			 * folder.fetch(msgs, fp);
			 */

			int messageCounter = 0;

			String textMessageSubject = HelloWorldProperties
    		.getString(HelloWorldProperties.MAIL_SUBJECT_TEXT_MESSAGE_IDENTIFIER);
			
			Log.i(I, "Starting to check new messages, please be patient!");
			Log.i(I, "Searching for HelloWorldSubjects like :");
			Log.i(I, "\t" + textMessageSubject);
			Log.i(I, "\t" + IMailClient.IDENTIFIER);

			for (int i = 0; i < msgs.length; i++)
			{

				if ((i + 1) % 10 == 0)
				{
					Log.i(I, (i + 1) + " messages checked ...");
				}

				// check if only Text Message
				boolean isTextMessage = false;
				if (msgs[i].getSubject().equals(textMessageSubject))
				{
					Log.i(I, "Found a HelloWorld Text Message");
					isTextMessage = true;
				}
				
				Log.i(I, "Check Subject: " + msgs[i].getSubject());
				
				// Filter HelloWorld message: Request or Text Message
				if (msgs[i].getSubject().equals(IMailClient.IDENTIFIER))
				{

					Log.i(I, "\tFound HelloWorld message nr. "
							+ ++messageCounter);

					Message helloWorldMessage = msgs[i];

					try
					{
						Multipart multipart = (Multipart) helloWorldMessage
								.getContent();

						Log.i(I, "\tFound " + multipart.getCount()
								+ " attachement/s in HelloWorld message");

						if (multipart.getCount() == 0)
						{
							Log
									.i(I,
											"\tNo attachements, continuing with next message!");
							continue;
						}

						MessageBean msgBean = new MessageBean();
						{
							msgBean.setRecipientsAddress(helloWorldMessage
									.getRecipients(Message.RecipientType.TO)[0]
									.toString());
							msgBean.setSenderAddress(helloWorldMessage
									.getFrom()[0].toString());
							msgBean.setSubject(IMailClient.IDENTIFIER);
						}

						for (int j = 0; j < multipart.getCount(); j++)
						{
							BodyPart part = multipart.getBodyPart(j);

							/*
							 * Could be helpful later ... MimeBodyPart pp =
							 * (MimeBodyPart) multipart .getBodyPart(j);
							 * InputStream is = multipart.getBodyPart(j)
							 * .getInputStream();
							 */

							String disposition = part.getDisposition();
							if ((disposition != null)
									&& (disposition.equals(Part.ATTACHMENT) || (disposition
											.equals(Part.INLINE))))
							{

								/*
								 * Generate temporary filename from current
								 * time, message number and the attachement
								 * number. Do multiple attachments make sense?
								 * Who knows ...
								 */

								File file = null;

								String tempFileName = System
										.currentTimeMillis()
										+ "_" + j + ".xml";

								String filename = tempDir + "" + tempFileName;

								Log.i(I, "\tSaving attachement nr. " + (j + 1)
										+ " to temporary file: " + filename);

								file = new File(filename);
								InputStream in = part.getInputStream();

								FileOutputStream fos = new FileOutputStream(
										file);
								byte[] buf = new byte[in.available()];
								int c = 0;
								while ((c = in.read(buf)) != -1)
								{
									fos.write(buf, 0, c);
								}
								in.close();
								fos.close();

								File file2 = new File(filename);

								msgBean.addAttachement(filename,
										new FileInputStream(file2));

							}
						}

						messages.add(msgBean);

					}
					catch (Exception e)
					{

						Log.e(I, "Failed to fetch HelloWorld message nr.: "
								+ messageCounter);
						e.printStackTrace();
					}

					// The mail must only be set to deleted when everything
					// worked fine!
					helloWorldMessage.setFlag(Flags.Flag.DELETED, true);

				} // filter HelloWorld message
				// if text message
				else if (isTextMessage)
				{

					Log.i(I, "\tFound HelloWorld Text-message nr. "
							+ ++messageCounter);

					Message helloWorldMessage = msgs[i];

					try
					{
						MimeMultipart mimeMultipart = (MimeMultipart) helloWorldMessage.getContent();
						
						MessageBean msgBean = new MessageBean();
						{
							String theText = mimeMultipart.getBodyPart(0).getContent().toString();
							Log.i(I, "Message-Text: " + theText);
							
							msgBean.setRecipientsAddress(helloWorldMessage
									.getRecipients(Message.RecipientType.TO)[0]
									.toString());
							msgBean.setSenderAddress(helloWorldMessage
									.getFrom()[0].toString());
							msgBean.setText(theText);
							msgBean.setSubject(HelloWorldProperties
									.getString(HelloWorldProperties.MAIL_SUBJECT_TEXT_MESSAGE_IDENTIFIER));
						}
						messages.add(msgBean);
						// we just can display 1 message -> break after first textMessage!!
						
						// The mail must only be set to deleted when everything
						// worked fine!
						if (!helloWorldMessage.isSet(Flags.Flag.DELETED))
							helloWorldMessage.setFlag(Flags.Flag.DELETED, true);
						if (folder.isOpen())
							folder.close(true);
						break;

					}
					catch (Exception e)
					{

						Log.e(I, "Failed to fetch HelloWorld Text-message nr.: "
								+ messageCounter);
						e.printStackTrace();
					}

					// The mail must only be set to deleted when everything
					// worked fine!
					if (!helloWorldMessage.isSet(Flags.Flag.DELETED))
						helloWorldMessage.setFlag(Flags.Flag.DELETED, true);

				}

			} // end check messages in folder

			if (folder.isOpen())
				folder.close(true);

			Log.i(I, "All messages checked!\n\tTotal messages: " + msgs.length
					+ "\n\tHelloWorld messages: " + messageCounter);

		}
		catch (NoSuchProviderException e)
		{

			e.printStackTrace();
		}
		catch (MessagingException e)
		{

			e.printStackTrace();
		}

		return messages;
	}

	public void setProtocol(FetchMailProtocol protocol)
	{
		this.protocol = protocol;
		setUpProtocol();
	}

}
