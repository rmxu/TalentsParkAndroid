package de.fhkl.mHelloWorld.implementation.util;

import android.util.Log;

public class StringUtil {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "StringUtil" + ": ";
	
	/**
	 * Encodes a string valid for a URL
	 * @param input
	 * @return
	 */
	public static String makeUrlValid(String input) {
		Log.i(I, "makeUrlValid -> input: " + input);
		input = input.toLowerCase().trim();
		input = input.replaceAll("[\'\"&%$§!)(`´'/#;,\\*+^°=?<>]", ""); 
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == 'ä') {
				filtered.append("ae");
			} else if (c == 'ö') {
				filtered.append("oe");
			} else if (c == 'ü') {
				filtered.append("ue");
			} else if (c == 'ß') {
				filtered.append("ss");
			} else if (c == ' ') {
				filtered.append("_");
			} else if (c == '	') {
				filtered.append("_");
			} else {
				filtered.append(c);
			}
		}
		Log.i(I, "makeUrlValid -> output: " + filtered.toString());
		return (filtered.toString());
	}
}
