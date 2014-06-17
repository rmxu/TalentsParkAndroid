package de.fhkl.mHelloWorld.implementation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FileWriter {
	private static final String I = "======================= [HELLO-WORLD] "
			+ "FileWriter" + ": ";

	public static boolean sIsSuccess = false;

	// to be sure, that the folder .../files/ exists
	public static void prepare(Context ct)
	{
		Log.i(I, "prepare ... ");
		String writePath = ct.getFileStreamPath("").getAbsolutePath();
		File file = new File(writePath);
		if (! file.exists() )
		{
			file.mkdirs();
		}
		else
		{
			Log.i(I, "folder /files/.. exists!");
		}
	}
	
	/** Called when the activity is first created. */
	public void getAndWriteAccountFromUrl(String username, String url, Context ct) {
		// if an account is already successful written before
		// we have to make sure that the variable is false again at the start
		sIsSuccess = false;

		InputStream in = getProfileFromUrl(url);
		// InputStream in =
		// getProfileFromUrl("http://neeerd.ne.funpic.de/max-subprofil-freunde.xml");
		// String str
		// ="gibt es eine Möglichkeit aus einem String einen InputStream zu erzeugen.";
		// InputStream in = new ByteArrayInputStream(str.getBytes());
		try {
			writeFile(in, "account.xml", username, ct);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected InputStream getProfileFromUrl(String url) {
		Log.i(I, "try to download url: " + url);
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(url);

		Log.i(I, "start download");
		HttpResponse response;
		InputStream in = null;
		try {
			Log.i(I, "try to execute ...");
			response = client.execute(getMethod);
			Log.i(I, "download executed. Getting the Response ...");

			in = response.getEntity().getContent();
			Log.i(I, "opened");
		} catch (Exception e) {
			Log.i(I, "Exception bei execute oder getContent");
		}
		return in;
	}

	private static void writeFile(InputStream is, String filename,
			String username, Context ct) throws IOException {
		FileWriter.prepare(ct);
		String dataDir = ct.getFileStreamPath("").getAbsolutePath() + "/../"
				+ username + "/";
		byte[] buffer = new byte[1024];
		int read;
		File file;
		String directory;

		filename = dataDir + filename;
		Log.i(I, "filename: " + filename);
		if (filename.lastIndexOf('/') != filename.length() - 1) {
			directory = filename.substring(0, filename.lastIndexOf('/'));
			file = new File(directory);
			if (!file.exists())
				file.mkdirs();
		} else {
			Log
					.i(
							I,
							"TRIFFT NICHT ZU: if (filename.lastIndexOf('/') != filename.length() - 1) ---> ELSE aufgerufen - return.");
			return;
		}
		OutputStream os = null;
		os = new FileOutputStream(filename);
		Log.i(I, "try to write file ...");
		while ((read = is.read(buffer)) > 0) {
			os.write(buffer, 0, read);
		}
		os.close();
		is.close();
		if (file.exists()) {
			sIsSuccess = true;
			Log.i(I, "file successful! written in: " + file.getAbsolutePath());
		}
	}

	public static void writeTempFile(InputStream is, String filename) 
	throws IOException {
		String dataDir = HelloWorldBasic.getUserTempDir();
		byte[] buffer = new byte[1024];
		int read;
		File file;
		String directory;

		filename = dataDir + filename;
		Log.i(I, "filename: " + filename);
		if (filename.lastIndexOf('/') != filename.length() - 1) {
			directory = filename.substring(0, filename.lastIndexOf('/'));
			file = new File(directory);
			// create Dirs, if not exsist
			if (!file.exists())
				file.mkdirs();
		} else {
			Log
					.i(
							I,
							"TRIFFT NICHT ZU: if (filename.lastIndexOf('/') != filename.length() - 1) ---> ELSE aufgerufen - return.");
			return;
		}
		OutputStream os = null;
		os = new FileOutputStream(filename);
		Log.i(I, "try to write file ...");
		while ((read = is.read(buffer)) > 0) {
			os.write(buffer, 0, read);
		}
		os.close();
		is.close();
		if (file.exists()) {
			Log.i(I, "file successful! written in: " + file.getAbsolutePath());
		}
	}

}
