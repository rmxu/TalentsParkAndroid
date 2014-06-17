package de.fhkl.helloWorld.interfaces.model.parser;

import java.io.BufferedInputStream;

import org.w3c.dom.Document;

import de.fhkl.helloWorld.interfaces.model.account.Account;

/**
 * Provides methods to parse an Account from XML into the corresponding Java
 * Class<br>
 * (including all subclasses) and vice versa.<br>
 * <br>
 * Uses an IProfileParser to parse profiles contained in the account
 */
public interface IAccountParser {

	/**
	 * Generates an Account from a given XML document
	 * 
	 * @param is
	 * @return the account
	 */
	public Account parseAccount(BufferedInputStream is);

	/**
	 * Parses an Account into XML
	 * 
	 * @param ac
	 * @return the document
	 */
	public Document parseAccount(Account ac);

}
