package de.fhkl.helloWorld.interfaces.model.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;

/**
 * Provides methods to parse an Profile from XML into the corresponding Java
 * Class (including all subclasses) and vice versa.
 */
public interface IProfileParser {

	/**
	 * Generates an Profile from a given XML document
	 * 
	 * @param profile
	 * @return the profile
	 */
	public Profile parseProfile(Node profile);

	/**
	 * Parses an Profile into XML
	 * 
	 * @param pro - the Profile
	 * @param doc
	 * @return the document
	 */
	public Node parseProfile(Profile pro, Document doc);

}
