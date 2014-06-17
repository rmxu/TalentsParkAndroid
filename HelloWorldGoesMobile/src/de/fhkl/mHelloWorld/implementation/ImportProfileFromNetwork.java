package de.fhkl.mHelloWorld.implementation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import au.id.jericho.lib.html.Source;

import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.mHelloWorld.implementation.crawler.Crawler;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 
 * This class is for importing the profile from another social network into
 * the helloWorld-network in android. It's using the Crawler.java to crawl
 * the html-page and is handling here all the data to store it on the system
 * as a xml-file.
 *
 */

public class ImportProfileFromNetwork extends ListActivity {//HelloWorldLogin {

	private static final String I = "======================= [HELLO-WORLD] "
			+ "ImportProfileFromNetwork" + ": ";

	// HelloWorld Profile-Attributes
	// fn = String, bday = String, email = String, tel = String, url = String
	private String fn, bday, email, tel, url;

	// HelloWorld Account-Attributes (these items could appears more than once, e.g. 2
	// addresses)
	// address, country, post, region, locality, postalcode, extaddress
	private String[] address, country, post, region, locality, postalcode, extaddress;
	
	// the imported attributes from the other social network like myspace, facebook, etc.
	// e.g. name, gender, age, city, state, country
	private String importName, importGender, importAge, importCity, importState, importCountry; 
	
	// List of attributes
	private ArrayList<String> attributes = new ArrayList<String>();

	/**
	 * method for parsing the configuration-file which includes all the
	 * attributes from the existing profile on one of the social network chosen
	 * to import to helloWorld.
	 * 
	 * (how does this configuration-file look like? Is it a xml-file? It has to
	 * be defined because we have to know how to parse and get all the
	 * attributes.)
	 */
	public ArrayList<String> parsing(String url_text){
		Log.i(I, "parsing aufgerufen");
		
		// TODO
		// this method is parsing the ArrayList<String> which is getting from Crawler.crawlProfile()
		// to get the data for the attributes
		
		try{
			// get the URL dynamically)
			Log.i(I, "parsing() ##### get url");
			URL url = new URL(url_text);
			
			// getting the HTML-source of the given URL
			Log.i(I, "parsing() ##### getting the HTML-source with the given URL");
			Source source = new Source(url);
			
			// crawl profile with the given URL and HTML-source
			Log.i(I, "parsing() crawl profile with the given URL and HTML-source");
			Crawler but = new Crawler();
			attributes = but.crawlProfile(source);
			
		} catch(MalformedURLException e1){
			Log.e(I," error ocurred in onCreate()-method: "+e1);
		} catch(IOException e2){
			Log.e(I," error ocurred in onCreate()-method: "+e2);
		}
		
		// go through all the attributes
		
		// setting the full name or nickname
		if (attributes.get(0) == null || attributes.get(0) == "") {
			setImportName("---");
		}
		else {
			setImportName(attributes.get(0));
		}
		Log.i(I, "Name: "+this.importName);
		
		// setting the birthday or age
		if (attributes.get(2) == null || attributes.get(2) == "") {
			setImportAge("---");
		}
		else {
			setImportAge(attributes.get(2));
		}
		Log.i(I, "Age: "+this.importAge);
		
		// setting the gender
		if (attributes.get(1) == null || attributes.get(1) == "") {
			setImportGender("---");
		}
		else {
			setImportGender(attributes.get(1));
		}
		Log.i(I, "Gender: "+this.importGender);
		
		// setting the city
		if (attributes.get(3) == null || attributes.get(3) == "") {
			setImportCity("---");
		}
		else {
			setImportCity(attributes.get(3));
		}
		Log.i(I, "City: "+this.importCity);
		
		// setting the state
		if (attributes.get(4) == null || attributes.get(4) == "") {
			setImportState("---");
		}
		else {
			setImportState(attributes.get(4));
		}
		Log.i(I, "State: "+this.importState);
		
		// setting the country
		if (attributes.get(5) == null || attributes.get(5) == "") {
			setImportCountry("---");
		}
		else {
			setImportCountry(attributes.get(5));
		}
		Log.i(I, "Country: "+this.importCountry);
		
		// if the file is parsed correct the method returns true
		//return true;
		return attributes;
	}

	/**
	 * setter-methods to set the helloworld-desktop-attributes with values
	 */

	protected void setFn(String newFn) {
		fn = newFn;
	}

	protected void setBday(String newBday) {
		bday = newBday;
	}

	protected void setEmail(String newEmail) {
		email = newEmail;
	}

	protected void setTel(String newTel) {
		tel = newTel;
	}

	protected void setUrl(String newUrl) {
		url = newUrl;
	}

	protected void setAddress(String[] newAddress) {
		for (int i = 0; i < newAddress.length; i++) {
			address[i] = newAddress[i];
		}
	}

	protected void setCountry(String[] newCountry) {
		for (int i = 0; i < newCountry.length; i++) {
			address[i] = newCountry[i];
		}
	}

	protected void setPost(String[] newPost) {
		for (int i = 0; i < newPost.length; i++) {
			address[i] = newPost[i];
		}
	}

	protected void setRegion(String[] newRegion) {
		for (int i = 0; i < newRegion.length; i++) {
			address[i] = newRegion[i];
		}
	}

	protected void setLocality(String[] newLocality) {
		for (int i = 0; i < newLocality.length; i++) {
			address[i] = newLocality[i];
		}
	}

	protected void setPostalcode(String[] newPostalcode) {
		for (int i = 0; i < newPostalcode.length; i++) {
			address[i] = newPostalcode[i];
		}
	}

	protected void setExtaddress(String[] newExtaddress) {
		for (int i = 0; i < newExtaddress.length; i++) {
			address[i] = newExtaddress[i];
		}
	}
	
	/**
	 * setter and getter-methods to set the attributes with the imported attributes we get
	 */

	protected String getImportName() {
		return importName;
	}

	protected void setImportName(String newImportName) {
		this.importName = newImportName;
	}

	protected String getImportGender() {
		return importGender;
	}

	protected void setImportGender(String newImportGender) {
		this.importGender = newImportGender;
	}

	protected String getImportAge() {
		return importAge;
	}

	protected void setImportAge(String newImportAge) {
		this.importAge = newImportAge;
	}

	protected String getImportCity() {
		return importCity;
	}

	protected void setImportCity(String newImportCity) {
		this.importCity = newImportCity;
	}

	protected String getImportState() {
		return importState;
	}

	protected void setImportState(String newImportState) {
		this.importState = newImportState;
	}

	protected String getImportCountry() {
		return importCountry;
	}

	protected void setImportCountry(String newImportCountry) {
		this.importCountry = newImportCountry;
	}

	/**
	 * onCreate-Method - first set the content view
	 * then create a new instance of the class
	 * then go on parsing
	 * in the end you should show the attributes
	 */

	public void onCreate(Bundle bundle) {
		Log.i(I, "onCreate aufgerufen");
		super.onCreate(bundle);

		// setting the layout
		//setContentView(R.layout.import_profile);
		// using R.layout.contacts because it's the same view so instead of creating another one
		// which is actually the same view reuse it
		setContentView(R.layout.import_profile);
		Log.i(I, "onCreate() ##### setContentView() succeeded");
		
		String url = getUrl();
		
		// new instance
		//ImportProfileFromNetwork client = new ImportProfileFromNetwork();
		// getting the attributes because we need them for the method showAttributes
		ArrayList<String> newAttributes = parsing(url);
		Log.i(I, "onCreate() ##### getting the attributes from client.parsing() for" +
				"the method showAttributes ");
		
		// showing the attributes
		showAttributes(newAttributes);
		Log.i(I, "onCreate() ##### showing the attributes on display");
		
		Toast.makeText(ImportProfileFromNetwork.this,
				"You imported your profile successfully.",
				Toast.LENGTH_LONG).show();
	}
	
	public String getUrl()
	{
		Bundle tmp = this.getIntent().getExtras();
		String url_tmp = (String) tmp.get("url");
		Log.i(I, "Hallo Welt das ist ein Test: "+url_tmp);
		return url_tmp;
	}
	
	/**
	 * Method for showing the attributes which were imported to the view / display.
	 * 
	 * @param newAttributes
	 */
	private void showAttributes(ArrayList<String> newAttributes){
		
		Log.i(I, "showAttributes() aufgerufen");
	
		// creating some hashmaps for the list and items
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		Log.i(I, "created first hashmap");
		HashMap<String,String> item = new HashMap<String,String>();
		Log.i(I, "created second hashmap");

		// variable size for getting the size of the ArrayList
		// which we need for the for-loop
		int size = newAttributes.size();
		Log.i(I, "SIZE of array newAttributes= "+newAttributes.size());
		
		// new String array with the attribut-names 
		// for also listing them into the view afterwards
		String[] attributnames = {"Name", "Gender", "Age", "City", "State", "Country"};
		
		// start putting the attributenames, attributes into the list as items
		// with the help of a for-loop
		// it ends if all attributes were done
		for(int i = 0; i < size; i++) {
			
			item.put("line1", attributnames[i]);
			Log.i(I, "attributname "+attributnames[i]+" is put to item!");
			
			item.put("line2", newAttributes.get(i));
			Log.i(I, "attribut "+newAttributes.get(i)+" is put to item!");
			
			list.add(item);
			Log.i(I, "attribut "+newAttributes.get(i)+" is put to list!");
			item = null;
			item = new HashMap<String, String>();
		}
		
		Log.i(I, "you are now in showAttributes() after for-loop");
				
		// Now create a simple cursor adapter and set it to display
		// String[]: Create an array to specify the fields we want to display in the list
		// (two lines-> line 1 := attribute-name, line2 := attribute-value)
		// and an array of the fields we want to bind those fields to (in this case text1-> attribute-name, text2-> attribute-value)
		
		/** REMARK: using R.layout.hcard_element because it's the same view / layout instead of creating a new layout;
		 	because reusing is better than creating another layout which would looks totally the same way */
		
		SimpleAdapter notes = new SimpleAdapter( 
				this, 
				list,
				R.layout.hcard_element,
				new String[] { "line1","line2"},
				new int[] { R.id.text1,R.id.text2}  );Log.i(I, "creating a SimpleAdapter");
       setListAdapter( notes );
		
       Log.i(I, "you are now in showAttributes() after the creating of the SimpleAdapter");
	}

	/**
	 * Method to write all the parsed elements into a file which will be written
	 * on the system.
	 * 
	 * My first idea was to put all the data back into a XML-file which is saved
	 * on the system which has to be encrypted.
	 * Like you have different sub-profiles you get a new different sub-profile eventually.
	 */

	private static void writeFile(InputStream is, String filename,
			String username, Context ct) throws IOException {
		Log.i(I, "writeFile aufgerufen");
		// TODO

	}

}
