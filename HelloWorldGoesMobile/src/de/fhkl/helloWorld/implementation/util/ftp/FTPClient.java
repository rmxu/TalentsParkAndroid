package de.fhkl.helloWorld.implementation.util.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;

//import sun.security.krb5.Credentials;
//import de.fhkl.helloWorld.implementation.util.sftp.PrintCommandListener;
import de.fhkl.helloWorld.interfaces.util.ftp.IFTPClient;

public class FTPClient extends org.apache.commons.net.ftp.FTPClient implements
		IFTPClient {

//	private static Logger logger = Logger.getLogger(FTPClient.class);
//
	private String url;
	private String username;
	private String password;

	public FTPClient(String url, String username, String password) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public void disconnect() {
		
		if (isConnected())
			try {
				super.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public boolean uploadSingleFile(InputStream source, String target) {
		if (isConnected()) {
			if (uploadFile(source, target)) {
				disconnect();
				return true;
			}
		}
		return false;
	}

	public boolean uploadFile(Object source, Object target) {
		uploadFile((InputStream) source, (String) target);
		return false;
	}

	public boolean uploadFile(InputStream source, String target) {
		if (!isConnected()) {
			return false;
		}
		
		try {
			setRemoteVerificationEnabled(false); // android-version
			setFileType(FTP.ASCII_FILE_TYPE);
			enterLocalPassiveMode();
			storeFile(target, source);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean connect(Object url, Object username, Object password) {
		return connect((String) url, (String) username, (String) password);
	}
	
	public boolean connect(){
		return connect(url,username,password);
	}

	public boolean connect(String url, String username, String password) {

		this.url = url;
		this.username = username;
		this.password = password;

		int reply;

		try {
			connect(url);
			reply = getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				
				disconnect();
				return false;
			}
			if (!login(username, password)) {
				
				return false;
			}
			
			return true;
		} catch (SocketException e) {
			
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
	}

//	public boolean connect(Credentials credentials) {
		// TODO
//		return false;
//	}

	public boolean downloadFile(Object source) {
		// TODO
		return false;
	}
}
