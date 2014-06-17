package de.fhkl.helloWorld.interfaces.actions.messages;

public class SignatureAuthenticationFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public SignatureAuthenticationFailedException(String message) {
		super(message);
	}

	public SignatureAuthenticationFailedException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public String toString() {
		return "Failed to check the signature";
	}
}
