package de.fhkl.mHelloWorld.implementation.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import android.util.Log;
import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;

public class ParseHelper {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "SubProfileHelper" + ": ";

	public static InputStream subProfileToXmlToInputStream(AttributeList<SubProfile> sbs)
	{
		Document dom = new DocumentImpl();
		Log.i(I, "parse Subprofile to XML ...");
		dom.appendChild(sbs.parseToXML(dom));
		
		StringWriter stringOut = new StringWriter();
		// serial schreibt den inhalt von dom in  stringOut
		Log.i(I, "new XMLSerializer ...");
		XMLSerializer serial = new XMLSerializer(stringOut, new OutputFormat(
				dom));
		ByteArrayInputStream in = null;

		try {
			Log.i(I, "Try to serialize dom...");
			serial.serialize(dom);
			Log.i(I, "Generate InputStream (UTF-8) ...");
			in = new ByteArrayInputStream(stringOut.toString()
					.getBytes("UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	// HW-PublicKey
	public static String publicKeyToXml(PublicKey pubKey)
	{
		Document dom = new DocumentImpl();
		Log.i(I, "parse PublicKey to XML ...");
		dom.appendChild(pubKey.parseToXML(dom));
		
		StringWriter stringOut = new StringWriter();
		// serial schreibt den inhalt von dom in  stringOut
		Log.i(I, "new XMLSerializer ...");
		XMLSerializer serial = new XMLSerializer(stringOut, new OutputFormat(
				dom));
		String in = null;

		try {
			Log.i(I, "Try to serialize dom...");
			serial.serialize(dom);
			Log.i(I, "Generate InputStream (UTF-8) ...");
			in = new String(stringOut.toString()
					.getBytes("UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
}
