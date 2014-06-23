package de.fhkl.helloWorld;

import java.util.HashMap;

public class HelloWorldProperties {

	// a little bit unusual method of getting data, but it´s the closest way to implement
	// like in the desktop version

	private static HashMap<String, String> RESOURCE_BUNDLE = new HashMap<String, String>();
	public static final String MAIL_SUBJECT_IDENTIFIER = "MailSubjectIdentifier";
	public static final String MAIL_SUBJECT_TEXT_MESSAGE_IDENTIFIER = "MailSubjectTextMessageIdentifier";
	public static final String MAIL_ATTACHMENT_SUFFIX = "MailAttachmentSuffix";
	public static final String PACKAGE_CONSTRAINT = "PackageConstraint";
	public static final String CLASS_IDENTIFIER = "ClassIdentifier";
	

	private HelloWorldProperties() {
	}

	private static void init()
	{
		RESOURCE_BUNDLE.put(MAIL_SUBJECT_IDENTIFIER , "HelloWorld internal message");
		RESOURCE_BUNDLE.put(MAIL_SUBJECT_TEXT_MESSAGE_IDENTIFIER, "HelloWorld text message");
		RESOURCE_BUNDLE.put(MAIL_ATTACHMENT_SUFFIX, "helloWorld.xml");
		RESOURCE_BUNDLE.put(PACKAGE_CONSTRAINT, "de.fhkl.helloWorld");
		RESOURCE_BUNDLE.put(CLASS_IDENTIFIER, "javaclass");
	}
	
	public static String getString(String key) {
		init();
		try {
			return RESOURCE_BUNDLE.get(key);
		} catch (Exception e) {
			return '!' + key + '!' + e;
		}
	}
}
