package de.fhkl.mHelloWorld.implementation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



/**
 * Application Name: Generic Login Screen for the Android Platform (back end)
 * Description: This is a generic login screen which catches the username and
 * password values Created on: November 23, 2007 Created by: Pogz Ortile
 * Contact: pogz(at)redhat(dot)polarhome(dot)com Notes: The string values for
 * username and password are assigned to sUserName and sPassword respectively
 * You arve free to distribute, modify, and wreck for all I care. GPL ya!
 */

public class About extends Activity {

	private static final String I = "======================= [HELLO-WORLD] "
			+ "About" + ": ";

	private TextView mVersion;

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.about);
	}

}