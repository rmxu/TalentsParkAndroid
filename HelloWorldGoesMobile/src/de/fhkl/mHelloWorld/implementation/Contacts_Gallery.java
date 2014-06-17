package de.fhkl.mHelloWorld.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class Contacts_Gallery extends HelloWorldBasic {
	/** Called when the activity is first created. */

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_Contacts_Gallery: ";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.contacts_gallery);

		/*
		 * Find the gallery defined in the main.xml Apply a new (custom)
		 * ImageAdapter to it.
		 */

		((Gallery) findViewById(R.id.gallery))
				.setAdapter(new ImageAdapter(this));

	}

	public class ImageAdapter extends BaseAdapter {
		private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_Contacts_Gallery: ";

		/** The parent context */
		private Context myContext;

		/** Simple Constructor saving the 'parent' context. */
		public ImageAdapter(Context c) {
			this.myContext = c;
			myRemoteImages = Helper.getImageUrls();
		}

		/** Returns the amount of images we have defined. */
		// public int getCount() { return this.myImageIds.length; }
		/* Use the array-Positions as unique IDs */
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * Returns a new ImageView to be displayed, depending on the position
		 * passed.
		 */

		/** URL-Strings to some remote images. */
		// private String[] myRemoteImages = {
		// "http://tbn3.google.com/images?q=tbn:U-Ofk5k4_iyqZM:http://www.team-wintermuehle-koeln.de/assets/images/passbild.jpg",
		// "http://tbn0.google.com/images?q=tbn:VEzgk9QxP4xeYM:http://www.timoosmanski.de/passbild.jpg",
		// "http://tbn0.google.com/images?q=tbn:KxCoL5JC_HzsAM:http://www.urologenweb.de/osieka/img/Passbild_Website_Rolf.jpg",
		// "http://tbn2.google.com/images?q=tbn:oE_CSekXSDLCIM:http://gallery.bujutsu-schwerte.de/d/1757-2/passbild%2B1985.jpg",
		// "http://tbn0.google.com/images?q=tbn:SKXsOPMwquv8RM:http://www.gasteighof.at/images/fam-georg_passbild_200411_500.jpg",
		// "http://tbn0.google.com/images?q=tbn:npBobQa3l3tmMM:http://www.andreas-janze.de/mediac/400_0/media/DIR_45001/Scan%2420Passbild.Signiert.JPG",
		// "http://tbn0.google.com/images?q=tbn:HHsABv4dPjXdVM:http://www.zml.uni-flensburg.de/mitarbeiter/m2/content/passbild/passbild.jpg"
		// };
		private String[] myRemoteImages;

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(this.myContext);

			Log.i(I, "getView :" + myRemoteImages[position]);
			
			if (myRemoteImages[position].equals("")) {
				i.setImageResource(R.drawable.standard_avatar);
			} else {
				/*
				 * Open a new URL and get the InputStream to load data from
				 * it.
				 */
				Bitmap bm = Helper.getAndSetBitmapFromNet(myRemoteImages[position]);
				i.setImageBitmap(bm);
			}
			/* Image should be scaled as width/height are set. */
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			/* Set the Width/Height of the ImageView. */
			i.setLayoutParams(new Gallery.LayoutParams(180, 150));
			return i;
		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

		@Override
		public int getCount() {
			return myRemoteImages.length;
		}
	}
}
