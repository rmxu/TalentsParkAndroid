package de.fhkl.helloWorld.implementation.model.parser;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;



import de.fhkl.helloWorld.implementation.model.attribute.AttributeInstantiator;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.InvalidClassException;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.IProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.parser.IAccountParser;


/**
 * This class parse the account
 */

public class AccountParser implements IAccountParser {

	/**
	 * parse the inputStream to an account
	 */
	public Account parseAccount(BufferedInputStream is) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Account account = null;

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(is);
			
			de.fhkl.mHelloWorld.implementation.parser.ProfileParserOut.showXML(dom, System.out);
			
			account = (Account) new AttributeInstantiator().getInstance(dom
					.getDocumentElement());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InvalidClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	/**
	 * parse an account to a xml document
	 */
	public Document parseAccount(Account account) {

		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		
		account.getPrivateProfile().getHCard().setWriteRelationShipTypes(true);
		
		for(SubProfile sp : account.getSubProfiles()){
			if(sp instanceof EncryptedSubProfile){
				((EncryptedSubProfile)sp).setParseToAccount(true);
			}
		}
		
		for(Contact c : account.getContacts()){
			for(SubProfile sp : c.getProfiles()){
				if(sp instanceof EncryptedSubProfile){
					((EncryptedSubProfile)sp).setParseToAccount(true);
				}
			}
		}
		
		
		
		Node root = account.parseToXML(doc);

		doc.appendChild(root);
		
		

		// TODO Auto-generated method stub
		return doc;
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
