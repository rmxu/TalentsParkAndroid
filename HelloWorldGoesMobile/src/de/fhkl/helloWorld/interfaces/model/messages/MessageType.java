package de.fhkl.helloWorld.interfaces.model.messages;

/**
 * This interface defines the different message types. It's only important for
 * parsing
 */
public enum MessageType {

	NOTIFICATION,
	PRIVATE_MESSAGE,
	PUBLIC_MESSAGE,
	FRIENDSHIP_REQUEST,
	FRIENDSHIP_RESPONSE_ACCEPT,
	FRIENDSHIP_RESPONSE_DECLINE;
	
	
}
