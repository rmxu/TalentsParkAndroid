package de.fhkl.helloWorld.interfaces.util.protocols;

/**
 * This enum lists the supported protocols for file transfers.
 */
public enum FileTransferProtocol {

	FTP(21, "ftp"), SFTP(22, "sftp");

	private final int standardPort;
	private final String lowerCaseName;

	FileTransferProtocol(int sp, String lcn) {
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