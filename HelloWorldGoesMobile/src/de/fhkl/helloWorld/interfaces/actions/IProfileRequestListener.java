package de.fhkl.helloWorld.interfaces.actions;

import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;

public interface IProfileRequestListener {

	/*
	 * if you start a profilerequest use this method to get noticed when the thread returns
	 */
	public void profileReceived(Profile p, Contact contact);
}
