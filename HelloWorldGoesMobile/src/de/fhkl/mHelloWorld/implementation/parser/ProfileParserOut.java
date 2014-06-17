package de.fhkl.mHelloWorld.implementation.parser;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;

import android.util.Log;

public class ProfileParserOut {

	private static final String I = "=======[HELLO-WORLD]" + "ProfileParserOut"
			+ ": ";

	/*
	 * Methode um das Dokument zu exportieren
	 */
	public static void exportXML(Document doc, OutputStream out) {
		
		showXML(doc, out);

//		try {
//			Log.i(I, "C1");
//			FileOutputStream fos = new FileOutputStream(HelloWorldBasic
//					.getPath()
//					+ "test/account2.xml");
//			OutputFormat format = new OutputFormat(doc);
//			Log.i(I, "C2");
//			format.setLineWidth(65);
//			Log.i(I, "C3");
//			format.setIndenting(true);
//			Log.i(I, "C4");
//			format.setIndent(2);
//			Log.i(I, "C5");
//			XMLSerializer serializer = new XMLSerializer(fos, format);
//			Log.i(I, "C6");
//			serializer.serialize(doc.getDocumentElement());
//			Log.i(I, "C7");
//			fos.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	/*
	 * Shows a Document as XML in the Console
	 */
	public static void showXML(Document doc, OutputStream out) {

		try {

			// FileOutputStream fos = new
			// FileOutputStream("xml/profileTest.xml");
			OutputFormat format = new OutputFormat(doc);
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(doc.getDocumentElement());
			// fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
