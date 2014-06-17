package de.fhkl.helloWorld.interfaces.model.parser;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;

/**
 * Provides methods to parse an Contact from XML into the corresponding Java
 * Class (including all subclasses) and vice versa. Uses an IProfileParser to
 * parse profiles contained in the Contact
 */
public interface IContactParser {

	/**
	 * Generates an Contact from a given XML document
	 * 
	 * @param contacts
	 * @return the Contact
	 */
	public ArrayList<Contact> parseContact(Node contacts);

	/**
	 * Parses an Contact into XML
	 * 
	 * @param ac
	 * @return the document
	 */
	public Node parseContact(AttributeList<Contact> ac, Document dom);

}
