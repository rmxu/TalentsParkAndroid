package de.fhkl.mHelloWorld.implementation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.People;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.MapView.LayoutParams;

import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;

public class ContactFinderMap extends HelloWorldBasicMap implements
		ImageButton.OnClickListener
{

	// ===========================================================
	// Fields
	// ===========================================================

	private static final String MY_LOCATION_CHANGED_ACTION = new String(
			"android.intent.action.LOCATION_CHANGED");
	private static final String I = "======================= [HELLO-WORLD] "
			+ "<ContactFinderMap>" + ": ";
	/**
	 * Minimum distance in meters for a friend to be recognize as a Friend to be
	 * drawn
	 */
	protected static final int NEARFRIEND_MAX_DISTANCE = 10000000; // 10.000km
	private static int sIdx;

	final Handler mHandler = new Handler();
	protected AccountManager am = new AccountManager();

	private ImageButton zoomIn;
	private ImageButton zoomOut;
	private ImageButton center;
	private ImageButton toggle;

	protected boolean doUpdates = true;
	protected MapView myMapView = null;
	protected MapController myMapController = null;
	// protected OverlayController myOverlayController = null;
	protected List<Overlay> overlays = null;
	protected boolean firstrun = true;
	protected LocationManager myLocationManager = null;
	protected Location myLocation = null;
	protected MyIntentReceiver myIntentReceiver = null;
	protected final IntentFilter myIntentFilter = new IntentFilter(
			MY_LOCATION_CHANGED_ACTION);
	protected static final int SUB_ACTIVTY_REQUEST_CODE = 1314;
	/** List of friends in */
	protected ArrayList<Friend> nearFriends = new ArrayList<Friend>();

	// boolean-variable for indicating if it's set to street-view or
	// satellite-view
	protected boolean satelliteviewIsSet = true;
	private GeoPoint mRandomPoint = new GeoPoint(5309691, 8851933);

	// ===========================================================
	// Extra-Classes
	// ===========================================================

	/**
	 * This tiny IntentReceiver updates our stuff as we receive the intents
	 * (LOCATION_CHANGED_ACTION) we told the myLocationManager to send to us.
	 */
	class MyIntentReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (ContactFinderMap.this.doUpdates)
				ContactFinderMap.this.updateView();
		}

	}

	class myLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location)
		{
			if (ContactFinderMap.this.doUpdates)
				ContactFinderMap.this.updateView();

		}

		@Override
		public void onProviderDisabled(String provider)
		{

		}

		@Override
		public void onProviderEnabled(String provider)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			// TODO Auto-generated method stub

		}

	}

	private class myMapOverlay extends ItemizedOverlay<OverlayItem>
	{
		private List<OverlayItem> mItemList = new ArrayList<OverlayItem>();
		private Double lat, lng;
		Paint paint = new Paint();

		Drawable greenMarker = null;

		public myMapOverlay(Drawable greenMarker)
		{
			super(greenMarker);
			this.greenMarker = greenMarker;
			Log.i(I, "myMapOverlay");
			// Add one sample item.

			lat = ContactFinderMap.this.myLocation.getLatitude() * 1E6;
			lng = ContactFinderMap.this.myLocation.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

			mItemList.add(new OverlayItem(point, "Title", "Snippet"));
			populate();
		}

		@Override
		protected OverlayItem createItem(int gIndex)
		{
			Log.i(I, "createItem MyMapOverlay");
			return mItemList.get(gIndex);
		}

		/**
		 * This is where tap events can be handled.
		 */
		@Override
		protected boolean onTap(int gIndex)
		{
			Log.i(I, "onTap");

			Toast.makeText(ContactFinderMap.this, "Yes, thats me",
					Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow)
		{

			super.draw(canvas, mapView, shadow);
			boundCenterBottom(greenMarker);

		}

		@Override
		public int size()
		{

			return mItemList.size();
		}

		@Override
		protected void finalize() throws Throwable
		{
			try
			{
				// ...
			}
			finally
			{
				super.finalize();
			}
		}
	}

	private class MapOverlay extends ItemizedOverlay<OverlayItem>
	{
		private List<OverlayItem> mItemList = new ArrayList<OverlayItem>();
		private Double lat, lng;
		Paint paint = new Paint();
		Friend aFriend;
		ArrayList<String> distance = new ArrayList<String>();
		GeoPoint point;
		int i = 0;
		ArrayList<Long> id = new ArrayList<Long>();
		Drawable redmapMarker = null;

		public MapOverlay(Drawable redmapMarker)
		{
			super(redmapMarker);
			this.redmapMarker = redmapMarker;
			Log.i(I, "MapOverlay");
			// Add one sample item.

			remove(ContactFinderMap.this.nearFriends);

			myLocation = myLocationManager.getLastKnownLocation("gps");
			for (Friend aFriend : ContactFinderMap.this.nearFriends)
			{
				Log.i(I, "For");

				lat = aFriend.itsLocation.getLatitude() * 1E6;
				lng = aFriend.itsLocation.getLongitude() * 1E6;
				point = new GeoPoint(lat.intValue(), lng.intValue());
				id.add(aFriend.itsId);

				if (myLocation.distanceTo(aFriend.itsLocation) < NEARFRIEND_MAX_DISTANCE)
				{
					if (aFriend.itsLocation != null)
					{
						final DecimalFormat df = new DecimalFormat("####0.000");
						String formattedDistance = df.format(myLocation
								.distanceTo(aFriend.itsLocation) / 1000);
						Log.i(I, "formattedDistance" + formattedDistance);
						distance.add(formattedDistance);

					}
				}
				mItemList
						.add(new OverlayItem(point, aFriend.itsName, "Snippet"));
				Log.i(I, "mItemList MapOverlay: " + mItemList.size());

				// mItemList.add(new OverlayItem(point, "Title", "Snippet"));

			}

			populate();
			Drawable greenmapMarker = getResources().getDrawable(
					R.drawable.personal_red);
			// You HAVE to specify the bounds! It seems like the markers are
			// drawn
			// through Drawable.draw(Canvas) and therefore must have its bounds
			// set
			// before drawing.
			greenmapMarker.setBounds(0, 0, greenmapMarker.getIntrinsicWidth(),
					greenmapMarker.getIntrinsicHeight());
			myMapView.getOverlays().add(new myMapOverlay(greenmapMarker));

		}

		protected void remove(ArrayList<Friend> nearFriends)
		{
			Log.i(I, "remove");
			for (Friend aFriend : nearFriends)
			{
				myMapView.getOverlays().remove(aFriend.itsLocation);
			}
		}

		@Override
		protected OverlayItem createItem(int pIndex)
		{
			Log.i(I, "createItem");
			return mItemList.get(pIndex);
		}

		/**
		 * This is where tap events can be handled.
		 */
		@Override
		protected boolean onTap(int pIndex)
		{
			Log.i(I, "onTap");

			String name = mItemList.get(pIndex).getTitle();
			String[] info = { "Auf dem Weg zur CeBit", "Gerade zu Hause",
					"Im Urlaub", "Bei der Arbeit", "Nichts Besonderes" };
			String dis = distance.get(pIndex);
			if (sIdx >= info.length)
				sIdx = 0;
			showInfo(id.get(pIndex), name, info[sIdx++], dis);
			// Toast.makeText(ContactFinderMap.this,
			// "My Friend: " +mItemList.get(pIndex).getTitle()
			// + "\nInfo: Gerade bei Oma." +
			// "\nDistance: "+distance.get(pIndex)+"km",
			// Toast.LENGTH_SHORT).show();

			return true;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow)
		{

			super.draw(canvas, mapView, shadow);
			boundCenterBottom(redmapMarker);

		}

		@Override
		public int size()
		{

			return mItemList.size();
		}

	}

	/**
	 * This method is so huge, because it does a lot of FANCY painting. We could
	 * shorten this method to a few lines. But as users like eye-candy apps ;)
	 * ...
	 */
	protected class MyLocationOverlay extends com.google.android.maps.Overlay
	{

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when)
		{
			super.draw(canvas, mapView, shadow);
			// Setup our "brush"/"pencil"/ whatever...
			Paint paint = new Paint();
			paint.setTextSize(14);

			// Create a Point that represents our GPS-Location
			Double lat = ContactFinderMap.this.myLocation.getLatitude() * 1E6;
			Double lng = ContactFinderMap.this.myLocation.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(point, myScreenCoords);

			// Draw a circle for our location
			RectF oval = new RectF(myScreenCoords.x - 7, myScreenCoords.y + 7,
					myScreenCoords.x + 7, myScreenCoords.y - 7);

			// Setup a color for our location
			// paint.setStyle(Style.FILL);
			// paint.setARGB(255, 190, 55, 55); // Nice strong Android-Green
			// Draw our name
			// canvas.drawText(getString(R.string.map_overlay_own_name),
			// myScreenCoords.x +9, myScreenCoords.y-12, paint);

			// Change the paint to a 'Lookthrough' Android-Green
			// paint.setARGB(80, 193, 115, 110);

			// paint.setStrokeWidth(2);

			// draw an oval around our location
			// canvas.drawOval(oval, paint);

			// With a black stroke around the oval we drew before.
			// paint.setARGB(255,0,0,0);
			// paint.setStyle(Style.STROKE);
			// canvas.drawCircle(myScreenCoords.x, myScreenCoords.y, 7, paint);

			Point friendScreenCoords = new Point();
			// Draw each friend with a line pointing to our own location.
			for (Friend aFriend : ContactFinderMap.this.nearFriends)
			{
				lat = aFriend.itsLocation.getLatitude() * 1E6;
				lng = aFriend.itsLocation.getLongitude() * 1E6;
				point = new GeoPoint(lat.intValue(), lng.intValue());

				// Converts lat/lng-Point to coordinates on the screen.
				mapView.getProjection().toPixels(point, friendScreenCoords);

				if (Math.abs(friendScreenCoords.x) < 2000
						&& Math.abs(friendScreenCoords.y) < 2000)
				{
					// Draw a circle for this friend and his name
					// oval = new RectF(friendScreenCoords.x - 4,
					// friendScreenCoords.y + 4,
					// friendScreenCoords.x + 4, friendScreenCoords.y - 4);

					// Setup a color for all friends

					paint.setStyle(Style.FILL);
					paint.setFakeBoldText(true);
					paint.setAntiAlias(true);

					paint.setARGB(255, 0, 0, 0);
					canvas.drawText(aFriend.itsName, friendScreenCoords.x + 8,
							friendScreenCoords.y - 11, paint);
					canvas.drawText(aFriend.itsName, friendScreenCoords.x + 10,
							friendScreenCoords.y - 13, paint);
					canvas.drawText(aFriend.itsName, friendScreenCoords.x + 10,
							friendScreenCoords.y - 11, paint);
					canvas.drawText(aFriend.itsName, friendScreenCoords.x + 8,
							friendScreenCoords.y - 13, paint);
					paint.setFakeBoldText(false);

					paint.setARGB(255, 255, 255, 255); // Nice blue
					canvas.drawText(aFriend.itsName, friendScreenCoords.x + 9,
							friendScreenCoords.y - 12, paint);

					// Draw a line connecting us to the current Friend
					paint.setARGB(255, 193, 115, 110); // Nice red, more look
														// through...

					paint.setStrokeWidth(2);
					canvas.drawLine(myScreenCoords.x, myScreenCoords.y,
							friendScreenCoords.x, friendScreenCoords.y, paint);
					paint.setStrokeWidth(1);
					// draw an oval around our friends location
					// canvas.drawOval(oval, paint);

					// With a black stroke around the oval we drew before.
					// paint.setARGB(255,0,0,0);
					// paint.setStyle(Style.STROKE);
					// canvas.drawCircle(friendScreenCoords.x,
					// friendScreenCoords.y, 4, paint);
				}
			}
			return true;
		}
	}

	protected void gotoHcard(long id)
	{
		Intent intent = new Intent(this, ShowHCard.class);
		intent.putExtra("row", id);
		this.startActivity(intent);

	}

	private void showInfo(final long id, String name, String info, String dist)
	{
		Intent inten;
		new AlertDialog.Builder(this)

		.setTitle(name)

		.setMessage("Info: " + info + "\nDistance: " + dist + " km")

		// .setIcon(R.drawable.question)

				.setNegativeButton("Go to hCard",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int whichButton)
							{

								gotoHcard(id);
							}

						})

				.setPositiveButton("back",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int whichButton)
							{

								setResult(RESULT_OK);

								// finish();

							}

						})

				.show();
		// final Builder winAlert;
		// Dialog winDialog;
		//
		// OnClickListener okListener = new OnClickListener() {
		// // @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// return;
		//
		// }
		// };
		//
		// winAlert = new AlertDialog.Builder(this).setIcon(R.drawable.icon)
		// .setTitle("My title").setPositiveButton(
		// "Ok", okListener);
		//        
		// winDialog = winAlert.create();
		//
		// // winAlert.setCanceledOnTouchOutside(true);
		// // winDialog.setContentView(R.layout.about);
		// // winDialog.addContentView(findViewById(R.layout.about),
		// // new LayoutParams(LayoutParams.FILL_PARENT,
		// // LayoutParams.FILL_PARENT));
		// winDialog.show();

	}

	// ===========================================================
	// """Constructor""" (or the Entry-Point of this class)
	// ===========================================================
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		Log.i(I, "onCreate");

		// myMapView.setClickable(true);
		setContentView(R.layout.contact_finder);
		// setContentView(myMapView);

		init();
		this.overlays = this.myMapView.getOverlays();

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
		this.overlays.add(myLocationOverlay);

		/*
		 * MapController is capable of zooming and animating and stuff like that
		 */
		myMapController = this.myMapView.getController();
		myMapController.setZoom(3);

		// Initialize the LocationManager
		this.myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		this.updateView();

		/*
		 * Prepare the things, that will give us the ability, to receive
		 * Information about our GPS-Position.
		 */
		this.setupForGPSAutoRefreshing();

		/*
		 * Update the list of our friends once on the start, as they are
		 * not(yet) moving, no updates to them are necessary
		 */
		this.refreshFriendsList(NEARFRIEND_MAX_DISTANCE);

		if (firstrun)
		{
			Drawable redmapMarker = getResources().getDrawable(
					R.drawable.personal_blue);
			// You HAVE to specify the bounds! It seems like the markers are
			// drawn
			// through Drawable.draw(Canvas) and therefore must have its bounds
			// set
			// before drawing.
			redmapMarker.setBounds(0, 0, redmapMarker.getIntrinsicWidth(),
					redmapMarker.getIntrinsicHeight());

			myMapView.getOverlays().add(new MapOverlay(redmapMarker));
			firstrun = false;
		}
		// double lat = this.myLocation.getLatitude() *1E6;
		// Log.i(I,"lat ausgabe oncrate"+lat);
		// myMapController.zoomIn();

		Double lat = ContactFinderMap.this.myLocation.getLatitude() * 1E6;
		Double lng = ContactFinderMap.this.myLocation.getLongitude() * 1E6;
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

		this.myMapController.setCenter(point);

	}

	public void init()
	{
		// myMapView = (MapView) findViewById(R.id.simpleGM_map) ; // Get map
		// from XML
		myMapView = (MapView) findViewById(R.id.contact_finder_map);
		zoomIn = (ImageButton) findViewById(R.id.contact_finder_button_zoomIn); // Get
																				// button
																				// from
																				// xml
		zoomIn.setOnClickListener(this);
		zoomIn.setBackgroundDrawable(null);
		// zoomIn.setVisibility(1);
		zoomOut = (ImageButton) findViewById(R.id.contact_finder_button_zoomOut); // Get
																					// button
																					// from
																					// xml
		zoomOut.setOnClickListener(this);
		zoomOut.setBackgroundDrawable(null);
		// zoomOut.setVisibility(View.INVISIBLE );
		center = (ImageButton) findViewById(R.id.contact_finder_button_center); // Get
																				// button
																				// from
																				// xml
		center.setOnClickListener(this);
		center.setBackgroundDrawable(null);
		toggle = (ImageButton) findViewById(R.id.contact_finder_button_toggleView); // Get
																					// button
																					// from
																					// xml
		toggle.setOnClickListener(this);
		toggle.setBackgroundDrawable(null);
	}

	public void onClick(View v)
	{
		zoomOut.setOnClickListener(this);
		zoomOut.setVisibility(View.VISIBLE);
		// for the center point
		Double lat = ContactFinderMap.this.myLocation.getLatitude() * 1E6;
		Double lng = ContactFinderMap.this.myLocation.getLongitude() * 1E6;
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
		switch (v.getId())
		{
		case R.id.contact_finder_button_zoomIn:
			// Zoom not closer than possible
			this.myMapController.zoomIn();
			// this.myMapController.setCenter(point);

			break;
		case R.id.contact_finder_button_zoomOut:
			// Zoom not closer than possible
			this.myMapController.zoomOut();
			// this.myMapController.setCenter(point);

			break;
		case R.id.contact_finder_button_toggleView:
			myMapView.setSatellite(satelliteviewIsSet);
			satelliteviewIsSet = !satelliteviewIsSet;

			break;
		case R.id.contact_finder_button_center:
			// Zoom not closer than possible
			// this.myMapController.zoomIn();
			this.myMapController.setCenter(point);

			break;
		}
	}

	/**
	 * Restart the receiving, when we are back on line.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		this.doUpdates = true;

		/*
		 * As we only want to react on the LOCATION_CHANGED intents we made the
		 * OS send out, we have to register it along with a filter, that will
		 * only "pass through" on LOCATION_CHANGED-Intents.
		 */
		registerReceiver(this.myIntentReceiver, this.myIntentFilter);

	}

	/**
	 * Make sure to stop the animation when we're no longer on screen, failing
	 * to do so will cause a lot of unnecessary cpu-usage!
	 */
	// @Override
	// public void onFreeze(Bundle icicle) {
	// this.doUpdates = false;
	// unregisterReceiver(this.myIntentReceiver);
	// super.onFreeze(icicle);
	// }
	// ===========================================================
	// Overridden methods
	// ===========================================================
	// Called only the first time the options menu is displayed.
	// Create the menu entries.
	// Menu adds items in the order shown.

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_I)
		{
			// Zoom not closer than possible
			this.myMapController.zoomIn();
			// this.myMapController.zoomInFixing(Math.min(21,
			// this.myMapView.getZoomLevel() + 1));
			return true;
		}
		else
			if (keyCode == KeyEvent.KEYCODE_O)
			{
				// Zoom not farer than possible
				this.myMapController.zoomOut();
				// this.myMapController.zoomInFixing(Math.max(1,
				// this.myMapView.getZoomLevel() - 1),0);
				return true;
			}
			else
				if (keyCode == KeyEvent.KEYCODE_T)
				{
					// Switch to satellite view
					myMapView.setSatellite(satelliteviewIsSet);
					satelliteviewIsSet = !satelliteviewIsSet;
					return true;
				}
		// Override Superclass, backbutton works
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void setupForGPSAutoRefreshing()
	{
		/*
		 * Register with out LocationManager to send us an intent (whos
		 * Action-String we defined above) when an intent to the location
		 * manager, that we want to get informed on changes to our own position.
		 * This is one of the hottest features in Android.
		 */
		final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 25; // in Meters
		final long MINIMUM_TIME_BETWEEN_UPDATE = 5000; // in Milliseconds

		// Get the first provider available
		List<String> providers = this.myLocationManager.getAllProviders();
		String strProvider = providers.get(0);

		LocationListener listener = new myLocationListener();
		this.myLocationManager.requestLocationUpdates(strProvider,
				MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				listener);

		/*
		 * Create an IntentReceiver, that will react on the Intents we said to
		 * our LocationManager to send to us.
		 */
		this.myIntentReceiver = new MyIntentReceiver();

	}

	private void updateView()
	{
		if (!firstrun)
		{

			myMapView.getOverlays().clear();
			myMapView.invalidate();

			this.overlays = this.myMapView.getOverlays();

			MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
			this.overlays.add(myLocationOverlay);

			Drawable redmapMarker = getResources().getDrawable(
					R.drawable.personal_blue);
			// You HAVE to specify the bounds! It seems like the markers are
			// drawn
			// through Drawable.draw(Canvas) and therefore must have its bounds
			// set
			// before drawing.
			redmapMarker.setBounds(0, 0, redmapMarker.getIntrinsicWidth(),
					redmapMarker.getIntrinsicHeight());
			Log.i(I, "dsdfdfd");
			myMapView.getOverlays().add(new MapOverlay(redmapMarker));
			firstrun = false;
		}
		// Refresh our gps-location
		// this.myLocation = myLocationManager.getCurrentLocation("gps");
		this.myLocation = myLocationManager.getLastKnownLocation("gps");

		double lat = this.myLocation.getLatitude();
		double lon = this.myLocation.getLongitude();
		String geo = lat + "," + lon;
		userAccount.getPrivateProfile().getHCard().getGeo().setValue(geo);
		am.encryptAndSaveAccount(userAccount, HelloWorldBasic.getUsername(),
				HelloWorldBasic.getPassword(), null);

		/*
		 * Redraws the mapViee, which also makes our OverlayController redraw
		 * our Circles and Lines
		 */
		this.myMapView.invalidate();

		/*
		 * As the location of our Friends is static and for performance-reasons,
		 * we do not call this
		 */
		// this.refreshFriendsList(NEARFRIEND_MAX_DISTANCE);
	}

	private void refreshFriendsList(long maxDistanceInMeter)
	{

		Location friendLocation = null;
		String geo;
		// Both Floats only for testing
		float x = 2;
		float y = 12.9f;
		AttributeList<Contact> contacts = userAccount.getContacts();
		// Contact c = contacts.get((int)row_id);
		Log.i(I, "net Contacts ");
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try
		{

			String urlPath = null;
			String cName = null;
			String address = null;
			int cId = 0;
			// for (Contact c : contacts) {
			for (int i = 0; i < contacts.size(); i++)
			{
				try
				{
					Contact c = contacts.get(i);
					Log.i(I, "friend nr. " + i + ": " + c.getFullName());
					EncryptedSubProfile friendProfile = (EncryptedSubProfile) c
							.getProfiles().get(0);

					ProfileRequester requester = new ProfileRequester(
							friendProfile);
					friendProfile = requester.call();
					Log.i(I, "requestCall");

					// for testing

					String sx = new Float(x).toString();
					String sy = new Float(y).toString();
					geo = sx.concat(",").concat(sy);
					Log.i(I, "geodata: " + geo);
					friendProfile.getHCard().getGeo().setValue(geo);
					x += 25.33;
					y += 13.75;
					// testing end
					// Log.i(I,"asffffffffffffff"+friendProfile.getHCard().getAdr().get(0).getLocality().getValue());

					if (friendProfile.getHCard().getGeo().getValue() != null)
					{
						geo = friendProfile.getHCard().getGeo().getValue();
						cName = friendProfile.getHCard().getFn().getValue();
						// urlPath =
						// friendProfile.getHCard().getGeo().getValue();

						StringTokenizer loi = new StringTokenizer(geo, ",");

						String latid = loi.nextToken();
						String longit = loi.nextToken();

						Log.i(I, "longit " + longit);
						Log.i(I, "latid  " + latid);

						friendLocation = new Location("gps");
						friendLocation.setLongitude(Float.parseFloat(longit));
						friendLocation.setLatitude(Float.parseFloat(latid));

					}

					if (friendLocation != null
							&& this.myLocation.distanceTo(friendLocation) < maxDistanceInMeter)
					{
						nearFriends.add(new Friend(friendLocation, cName, cId));
					}
					cId++;

				}
				catch (Exception e)
				{
					Log.e(I, "Exception while trying to get subprofile-data ...");
				}
			} // end: for
		}
		catch (Exception e)
		{
			Log.i(I, " Exception");
			e.printStackTrace();
		}

		/*
		 * 
		 * 
		 * 
		 * 
		 * Cursor c = getContentResolver().query(People.CONTENT_URI, null, null,
		 * null, People.NAME + " ASC"); startManagingCursor(c); c.moveToFirst();
		 * 
		 * int notesColumn = c.getColumnIndex(People.NOTES); int nameColumn =
		 * c.getColumnIndex(People.NAME);
		 * 
		 * // Moves the cursor to the first row // and returns true if there is
		 * sth. to get
		 * 
		 * if (c.isFirst()) { do { String notesString =
		 * c.getString(notesColumn);
		 * 
		 * Location friendLocation = null; if (notesString != null) { // Pattern
		 * for extracting geo-ContentURIs from the notes. final String
		 * geoPattern =
		 * "(geo:[\\-]?[0-9]{1,3}\\.[0-9]{1,6}\\,[\\-]?[0-9]{1,3}\\.[0-9]{1,6}\\#)"
		 * ; // Compile and use regular expression Pattern pattern =
		 * Pattern.compile(geoPattern);
		 * 
		 * CharSequence inputStr = notesString; Matcher matcher =
		 * pattern.matcher(inputStr);
		 * 
		 * boolean matchFound = matcher.find(); if (matchFound) { // We take the
		 * first match available String groupStr = matcher.group(0); // And
		 * parse the Lat/Long-GeoPos-Values from it friendLocation = new
		 * Location(geoPattern); String latid =
		 * groupStr.substring(groupStr.indexOf(":") + 1, groupStr.indexOf(","));
		 * String longit = groupStr.substring(groupStr.indexOf(",") + 1,
		 * groupStr.indexOf("#"));
		 * friendLocation.setLongitude(Float.parseFloat(longit));
		 * friendLocation.setLatitude(Float.parseFloat(latid)); } }
		 * if(friendLocation != null &&
		 * this.myLocation.distanceTo(friendLocation) < maxDistanceInMeter){
		 * String friendName = c.getString(nameColumn); nearFriends.add(new
		 * Friend(friendLocation, friendName)); } } while (c.moveToNext()); }
		 */
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
