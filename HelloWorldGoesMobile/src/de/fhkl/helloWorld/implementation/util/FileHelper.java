package de.fhkl.helloWorld.implementation.util;

import java.io.FileOutputStream;
import java.io.InputStream;

import android.util.Log;

public class FileHelper {

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_FileHelper: ";

	public static boolean writeFile(String pfad, InputStream in) {
		FileOutputStream fos;
		Log.i(I,"Pfad: "+pfad);
		try {
			fos = new FileOutputStream(pfad);
			byte[] b = new byte[1024];
			int i = in.read(b);
			while (i != -1) {
				fos.write(b, 0, i);
				i = in.read(b);
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}
}
