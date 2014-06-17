package de.fhkl.mHelloWorld.implementation.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.mHelloWorld.implementation.FileWriter;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import de.fhkl.mHelloWorld.implementation.Helper;

import android.content.Context;
import android.util.Log;

public class MessageSender
{

	private String tempDir;

	private static final String I = "======================= [HELLO-WORLD] "
			+ "MessageReceiver" + ": ";

	private static boolean DEBUGGING = true;

	private String host;
	private String username;
	private String password;

	private int port;
	private String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private final Properties props;

	private SendMailProtocol protocol;

	public MessageSender(String host, String username, String password,
			int port, SendMailProtocol protocol)
	{

		this.tempDir = HelloWorldBasic.getUserTempDir();

		this.host = host;
		this.username = username;
		this.password = password;
		this.props = System.getProperties();
		this.port = port;
		this.protocol = protocol;

		setUpProtocol();
	}

	/**
	 * Set the port to -1 if none is set by the user
	 * 
	 * @param username
	 * @param password
	 */
	public MessageSender(String host, String username, String password)
	{
		this(host, username, password, -1, SendMailProtocol.SMTP);
	}

	private void setUpProtocol()
	{

		if (port == -1)
			port = protocol.standardPort();

		switch (protocol)
		{
		case SMTPS:
			setUpSMTPS();
		case SMTP:
		default:
			setUpSMTP();
		}
	}

	private void setUpSMTP()
	{
		Log.i(I, "Setting up SMTP configuration...");
		props.put("mail.transport.protocol", protocol.lowerCaseName());
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
	}

	private void setUpSMTPS()
	{
		Log.i(I, "Setting up SMTPS configuration...");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.put("mail.transport.protocol", protocol.lowerCaseName());
		props.put("mail.smtps.host", host);
		props.put("mail.smtps.port", port);
		props.put("mail.smtps.starttls.enable", "true");
		props.put("mail.smtps.auth", "true");

		// neccesary ?
		props.put("mail.smtps.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtps.socketFactory.port", port);
	}

	private class XMLDataSource implements DataSource
	{

		private OutputStream out;
		private InputStream is;
		private String name;
		private String contentType;

		public XMLDataSource(InputStream is, String name)
		{
			this.is = is;
			this.name = name;
			contentType = "text/richtext";
		}

		public String getContentType()
		{
			return contentType;
		}

		public InputStream getInputStream() throws IOException
		{
			return is;
		}

		public String getName()
		{
			return name;
		}

		public OutputStream getOutputStream() throws IOException
		{
			return out;
		}

		public InputStream getIs()
		{
			return is;
		}

		public void setIs(InputStream is)
		{
			this.is = is;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public void setContentType(String contentType)
		{
			this.contentType = contentType;
		}

	}

	public boolean sendMessage(MessageBean messageBean)
	{

		Session session = Session.getInstance(props);
		if (DEBUGGING)
		{
			Log.i(I, "Sending message ...");
			session.setDebug(true);
		}

		try
		{
			Transport tr = session.getTransport(protocol.lowerCaseName());
			File file = null; // need to be here declared, because it has to be removed after sending

			Message msg = new MimeMessage(session);
			{
				msg
						.setFrom(new InternetAddress(messageBean
								.getSenderAddress()));
				Log.i(I, "msg.getFrom(): " + msg.getFrom());
				msg.setRecipients(Message.RecipientType.TO, InternetAddress
						.parse(messageBean.getRecipientsAddress(), false));
				Log.i(I, "msg.getAllRecipients(): " + msg.getAllRecipients());
				msg.setSubject(messageBean.getSubject());
				Log.i(I, "msg.getSubject(): " + msg.getSubject());

				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText(messageBean.getText());

				Multipart mp = new MimeMultipart();
				mp.addBodyPart(textPart);

				HashMap<String, InputStream> attachements = messageBean
						.getAttachements();

				for (String s : attachements.keySet())
				{

					/*
					 * Workaround: saving temporary file
					 */
					InputStream in = attachements.get(s);
//					if (DEBUGGING)
//						Log.i(I, "(attachment) in: " + Helper.toString(in));

					// somthing like: 1228267966034 + helloWorld.xml
					String tempFileName = System.currentTimeMillis() + s;

					String filename = tempDir + "" + tempFileName;
					file = new File(filename); // need it to now the name of the file,
												// so we can delete it after sending

//					file = new File(filename);
//					
//					// create temp-folder if needed
//					if (!file.exists())
//						file.mkdirs();
//
//					FileOutputStream fos = new FileOutputStream(file);
//					byte[] buf = new byte[in.available()];
//					int c = 0;
//					while ((c = in.read(buf)) != -1)
//					{
//						fos.write(buf, 0, c);
//					}
//					in.close();
//					fos.close();
					
					Log.i(I,"****************:  "+in);
					
					FileWriter.writeTempFile(in, tempFileName);
					
					/*
					 * // Workaround: saving temporary file
					 */

//					String filename = 
//						HelloWorldBasic.getPath() 
//						+ "max" + "/" + "account.xml";
					
					MimeBodyPart xmlPart = new MimeBodyPart();
					// XMLDataSource xd = new XMLDataSource(attachements.get(s),
					// s);
					// xmlPart.setDataHandler(new DataHanldernew
					// FileDataSource(new File(filename)));
					xmlPart.setFileName(s);
					xmlPart.attachFile(new File(filename));
					mp.addBodyPart(xmlPart);

					// TODO delte temporary files
				}

				// xmlPart.setContent(messageBean.getAttachement(), "text/xml");
				msg.setContent(mp);

				for (String attrib : messageBean.getHeaders().keySet())
				{
					msg.addHeader(attrib, messageBean.getHeaders().get(attrib));
				}
				msg.setSentDate(new Date(System.currentTimeMillis()));
			}

			tr.connect(host, port, username, password);
			tr.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			
			try
			{
				// delete temp-file
				file.delete();
			}
			catch (Exception e)
			{
				Log.i(I, "Could not delete Tempfile. Maybe there is no one, that would be ok.");
			}

			Log.i(I, "Message has been sent!");

		}
		catch (Exception e)
		{
			Log.e(I, "Error sending message: " + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setHost(String host)
	{
		this.host = host;
		setUpProtocol();
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setPort(int port)
	{
		this.port = port;
		setUpProtocol();
	}
}
