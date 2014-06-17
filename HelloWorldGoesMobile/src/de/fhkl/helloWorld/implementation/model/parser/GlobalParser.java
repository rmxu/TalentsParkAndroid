package de.fhkl.helloWorld.implementation.model.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GlobalParser {

	public static Document parseXML(InputStream in) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document dom = null;
		// Using factory get an instance of document builder
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse(in);

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return dom;
	}
	
	public static Document parseXML(String in) {
		ByteArrayInputStream inStream = null;
		try {
			inStream = 
				new ByteArrayInputStream(in.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document dom = null;
		// Using factory get an instance of document builder
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse(inStream);

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return dom;
	}
}
