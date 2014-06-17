package de.fhkl.helloWorld.interfaces.model.parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import de.fhkl.helloWorld.interfaces.model.account.profile.OpenID;

/**
 * Provides methods to parse an OpenID from XML into the corresponding Java
 * Class (including all subclasses) and vice versa.
 */
public interface IOpenIDParser {

	/**
	 * Generates an OpenID from a given XML document
	 * 
	 * @param bis
	 * @return the OpenID
	 */
	public OpenID parseOpenID(BufferedInputStream bis);

	/**
	 * Parses an OpenID into XML
	 * 
	 * @param op
	 * @return the document
	 */
	public BufferedOutputStream parseOpenID(OpenID op);

}
