package de.fhkl.mHelloWorld.implementation.http;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


public class Client
{
	 private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "Client" + ": ";
	 
	 public static boolean sIsSuccess = false;

	public InputStream getRequestAsInputStream(String url)
	{
		Log.i(I, "try to download url: " + url);
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(url);

		Log.i(I, "start download");
		HttpResponse response;
		InputStream in = null;
		try
		{
			Log.i(I, "try to execute ...");
			response = client.execute(getMethod);
			Log.i(I, "download executed. Getting the Response ...");

			in = response.getEntity().getContent();
			Log.i(I, "opened");
		}
		catch (Exception e)
		{
			Log.i(I, "Exception bei execute oder getContent");
		}
		return in;
	}

}
