package de.fhkl.helloWorld.implementation.model.session;

import de.fhkl.helloWorld.interfaces.model.account.Account;

/**
 * basic info of the HelloWorld Session
 * @author Naruto
 * @date 2014-6-25
 */
public class HelloWorldSession {

	private String username;
	private Account account;

	public HelloWorldSession() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
