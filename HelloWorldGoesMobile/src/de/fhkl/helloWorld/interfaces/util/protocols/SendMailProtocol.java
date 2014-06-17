package de.fhkl.helloWorld.interfaces.util.protocols;

/**
 * This enum lists the supported protocols for sending mails. Adding a protocol
 * here means that it has to be added to the class that actually fetches the
 * mails
 */
public enum SendMailProtocol {

	SMTP(25, "smtp"), SMTPS(465, "smtps");

	private final int standardPort;
	private final String lowerCaseName;

	SendMailProtocol(int sp, String lcn) {
		this.standardPort = sp;
		this.lowerCaseName = lcn;
	}

	public int standardPort() {
		return standardPort;
	}

	public String lowerCaseName() {
		return lowerCaseName;
	}
}
