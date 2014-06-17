package de.fhkl.mHelloWorld.implementation.mail;

import android.util.Log;
import de.fhkl.helloWorld.implementation.actions.messages.MessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.SignatureAuthenticationFailedException;

public class MailReceiverTest {

	private static final String I = "======================= [HELLO-WORLD] "
		+ "MailReceiverTest" + ": ";
	
	public static void hello()
	{
		Log.i(I, "MailReceiverTest ansprechbar.");
	}
	
	public static String checkForNewMails() 
	{
		MessageManager mm = new MessageManager();
		String textMessageContent = "";
		try {
			textMessageContent = mm.checkForEmailMessages();
		} catch (SignatureAuthenticationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (textMessageContent.length() == 0
		    || textMessageContent.equals(""))
		{
			Log.i(I, "No Text Message found!");
		}
		else
		{
			Log.i(I, "TEXT-MESSAGE DETECTED: " + textMessageContent);
		}
		return textMessageContent;

	}

}
