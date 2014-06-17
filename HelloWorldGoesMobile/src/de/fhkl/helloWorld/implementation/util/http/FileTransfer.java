package de.fhkl.helloWorld.implementation.util.http;

import java.io.InputStream;

/**
 * Provides methods for up- and downloading files
 */
public class FileTransfer {

	/**
	 * @param fileIn
	 *            stream of the file to be uploaded
	 * @param url
	 * @param fileName
	 * @return false, if upload fails
	 */
	public static boolean uploadFile(InputStream fileIn, String url,
			String fileName) {

		// TODO

		return false;
	}

	/**
	 * @param url
	 *            url where the file is located
	 * @param fileName
	 * @return stream of the file to be downloaded
	 */
	public static InputStream downloadFile(String url, String fileName) {

		// TODO

		return null;
	}

}
