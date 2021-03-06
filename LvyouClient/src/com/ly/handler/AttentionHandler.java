package com.ly.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AttentionHandler extends DefaultHandler {

	private String val = "";
	private String[] loginfo;
	private ArrayList<String[]> list= new ArrayList<String[]>();
	private String error;

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("aid")) {
			loginfo = new String[5];
		}
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("aid")) {
			loginfo[0] = val;
			//System.out.println("aid: " + loginfo[0]);

		}
		if (localName.equals("hid")) {
			loginfo[1] = val;
			//System.out.println("hid: " + loginfo[1]);

		}

		if (localName.equals("hname")) {
			loginfo[2] = val;
			//System.out.println("hname: " + loginfo[2]);
		}
		if (localName.equals("oid")) {
			loginfo[3] = val;
			//System.out.println("oid: " + loginfo[3]);

		}
		if (localName.equals("oname")) {
			loginfo[4] = val;
			//System.out.println("oname: " + loginfo[4]);
		}
		if (localName.equals("error")) {
			this.error = val;
		}
		if (localName.equals("oname")) {
			list.add(loginfo);
		}

		val = "";
		super.endElement(uri, localName, qName);
	}

	public String[] getLoginfo() {
		return loginfo;
	}

	public void setLoginfo(String[] loginfo) {
		this.loginfo = loginfo;
	}

	public ArrayList<String[]> getList() {
		return list;
	}

	public void setList(ArrayList<String[]> list) {
		this.list = list;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

		val += new String(ch, start, length);
		super.characters(ch, start, length);

	}
}
