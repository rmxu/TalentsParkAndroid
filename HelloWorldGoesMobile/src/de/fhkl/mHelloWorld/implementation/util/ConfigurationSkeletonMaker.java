package de.fhkl.mHelloWorld.implementation.util;

import android.util.Log;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.Configuration;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FetchMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.util.protocols.FetchMailProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.FileTransferProtocol;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;

public class ConfigurationSkeletonMaker 
{
	private static final String I = "======================= [HELLO-WORLD] "
		+ "ConfigurationSkeletonMaker" + ": ";
	
	private Account account;
	
	public ConfigurationSkeletonMaker()
	{
		account = HelloWorldBasic.getAccount();
		createConfSkeleton();
	}
	
	// empty-default value of xml nodes
	private static final SingleAttribute<String> EMPTY = new SingleAttribute<String>("");
	
	// create root Configuration Skeleton
	public void createConfSkeleton()
	{
		Configuration conf = null;
		try
		{
			conf = account.getConfiguration();
		}
		catch (Exception e)
		{
			Log.i(I, "create Configuration Skeleton ...");
			conf = new Configuration();
			account.setConfiguration(conf);
		}
	}
	
	// create Incoming Skeleton
	public void createIncomingSkeleton()
	{
		Log.i(I, "create Mail Incoming Skeleton ...");
		FetchMailConnection conn = null;
		try
		{
			conn = account.getConfiguration()
			.getFetchMailConncetions().get(0);
		}
		catch (Exception e)
		{
			conn = new FetchMailConnection();
			conn.setHost(EMPTY);
			conn.setPassword(EMPTY);
			conn.setPort(EMPTY);
			conn.setProtocol(FetchMailProtocol.POP3);
			conn.setSenderAddress(EMPTY);
			conn.setUsername(EMPTY);
			AttributeList<FetchMailConnection> connections 
				= new AttributeList<FetchMailConnection>("fetchMailConnections");
			connections.add(conn);
			account.getConfiguration().setFetchMailConnections(connections);
		}
	}
	
	// create Outgoing Skeleton
	public void createOutgoingSkeleton()
	{
		Log.i(I, "create Mail Outgoing Skeleton ...");
		SendMailConnection conn = null;
		try
		{
			conn = account.getConfiguration()
			.getSendMailConncetions().get(0);
		}
		catch (Exception e)
		{
			conn = new SendMailConnection();
			conn.setHost(EMPTY);
			conn.setPassword(EMPTY);
			conn.setPort(EMPTY);
			conn.setProtocol(SendMailProtocol.SMTP);
			conn.setSenderAddress(EMPTY);
			conn.setUsername(EMPTY);
			AttributeList<SendMailConnection> connections 
				= new AttributeList<SendMailConnection>("sendMailConnections");
			connections.add(conn);
			account.getConfiguration().setSendMailConnections(connections);
		}
	}
	
	// create FTP Skeleton
	public void createFtpSkeleton()
	{
		Log.i(I, "create Ftp Skeleton ...");
		FileTransferConnection conn = null;
		try
		{
			conn = account.getConfiguration()
			.getFileTransferConncetions().get(0);
		}
		catch (Exception e)
		{
			conn = new FileTransferConnection();
			conn.setHost(EMPTY);
			conn.setHttpUrl(EMPTY);
			conn.setPassword(EMPTY);
			conn.setPort(EMPTY);
			conn.setProtocol(FileTransferProtocol.FTP);
			conn.setUsername(EMPTY);
			AttributeList<FileTransferConnection> connections 
				= new AttributeList<FileTransferConnection>("fileTransferConnections");
			connections.add(conn);
			account.getConfiguration().setFileTransferConnections(connections);
		}
	}


}
