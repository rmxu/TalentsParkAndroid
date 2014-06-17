package de.fhkl.helloWorld.interfaces.model.parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import de.fhkl.helloWorld.interfaces.model.account.configuration.Configuration;

/**
 * Provides methods to parse an Configuration from XML into the corresponding
 * Java Class (including all subclasses) and vice versa. Uses an IProfileParser
 * to parse profiles contained in the Configuration
 */
public interface IConfigurationParser {

	/**
	 * Generates an Configuration from a given XML document
	 * 
	 * @param bis
	 * @return the Configuration
	 */
	public Configuration parseConfiguration(BufferedInputStream bis);

	/**
	 * Parses an Configuration into XML
	 * 
	 * @param ac
	 * @return the document
	 */
	public BufferedOutputStream parseConfiguration(Configuration ac);

}
