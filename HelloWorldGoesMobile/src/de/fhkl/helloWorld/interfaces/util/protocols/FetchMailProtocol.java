package de.fhkl.helloWorld.interfaces.util.protocols;

/**
 * This enum lists the supported protocols for fetching mails. Adding a protocol
 * here means that it has to be added to the class that actually fetches the
 * mails
 */
public enum FetchMailProtocol {

	POP3(110, "pop3"), POP3S(995, "pop3s"), IMAP(143, "imap"), IMAPS(993,
			"imaps"), NNTP(119, "nntp"), NNTPS(563, "nntps");

	private final int standardPort;
	private final String lowerCaseName;

	FetchMailProtocol(int sp, String lcn) {
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
