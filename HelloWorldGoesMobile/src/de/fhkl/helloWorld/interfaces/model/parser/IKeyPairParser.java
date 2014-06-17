package de.fhkl.helloWorld.interfaces.model.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.fhkl.helloWorld.interfaces.model.account.key.KeyPair;
import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;

/**
 * Provides methods to parse an KeyPair from XML into the corresponding Java
 * Class (including all subclasses) and vice versa.
 */
public interface IKeyPairParser {

	/**
	 * Generates an KeyPair from a given XML document
	 * 
	 * @param keyPair
	 * @return the KeyPair
	 */
	public KeyPair parseKeyPair(Node keyPair);

	/**
	 * returns the xml node representing the publicKey for exchange
	 * 
	 * @param publicKey
	 * @param doc
	 * @return the PublicKeyNode
	 */
	public Node parsePublicKey(PublicKey publicKey, Document doc);

}