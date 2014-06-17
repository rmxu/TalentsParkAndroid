package de.fhkl.mHelloWorld.implementation.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

import android.util.Log;
import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.Source;

/**
 * 
 * This class is for crawling the existing profile in the other social network if someone
 * wish to import his profile into the helloWorld-application on the Android mobile phone.
 *
 */

public class Crawler {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "Crawler" + ": ";
	
	// configuration-file which will be parsed to get the right attribute-names / -id's
	// for the crawler so that he knows what he has to crawl
	private static String propfile = "regex.properties";
	
	// myspace-test-account: http://profile.myspace.com/index.cfm?fuseaction=user.viewprofile&friendid=431545369
	//private static Source source;
	
	// List of "Attributes" which will be stored in Strings
	protected ArrayList<String> newAttributes = new ArrayList<String>();
	
	/**
	 * Method for crawling the HTML-file. The URL is passed with the call of the method.
	 * If succeeded then there will be a new file created.
	 */
	public ArrayList<String> crawlProfile(Source source) throws MalformedURLException, IOException{
		
		// making the source-code of HTML-file to String
		String page = source.toString();
		
		// preparing for the InputStream
		InputStream propFile = Crawler.class.getResourceAsStream(
				propfile);
		Log.i(I, "creating new Properties-object");
		Properties props = new Properties(System.getProperties());
		Log.i(I, "Loading the given properties-file");
		props.load(propFile); // hier tritt der fehler auf
		Log.i(I, "Now crawling the attributes with the help of the properties-file");
		
		// getting the attribute "name" of the person
		try {
			String name = ParserUtil.pageMatches(page, props.getProperty("name.start"), props.getProperty("name.end"), props.getProperty("name.regex"));
			newAttributes.add(name);
			Log.i(I, "NAME is = "+name);
		} catch (Exception e1) {
			Log.e(I, "Error occured in method crawlProfile(), by getting the name:" + e1);
		}
		
		// getting the basic-data from the profile (gender, age, city, ...)
		// by using the method getBasics() which is implemented beneath
		String[] basics = getBasics(source, props);

		
		// getting the attribute "gender" of the person
		String gender = basics[2];
		//if (basics[2].toLowerCase() == "m") gender = "Male";
		//else if(basics[2].toLowerCase() == "f") gender = "Female";
		// no need of checking the gender 'cause it's already done during parsing
		newAttributes.add(gender);
		Log.i(I, "Gender is = "+gender);
		
		// getting the attribute "age" of the person
		String age = basics[3];
		newAttributes.add(age);
		Log.i(I, "Age is = "+age);

		// getting the attribute "city" of the person
		String city = basics[4];
		newAttributes.add(city);
		Log.i(I, "City is = "+city);
		
		// getting the attribute "state" of the person
		String state = basics[6];
		newAttributes.add(state);
		Log.i(I, "State is = "+state);
		
		// getting the attribute "country" of the person
		String country = basics[5];
		newAttributes.add(country);
		Log.i(I, "Country is = "+country);
		
		return newAttributes;
	}
	
	
	/**
	 * Method to get the basic text. It's including the formated attributes:
	 * sex, gender, city, state, country, age and headline.
	 * If succeeded then the method returns a String-array
	 */
	
	private static String[] getBasics(Source source, Properties props) throws PatternSyntaxException, IndexOutOfBoundsException
	{
		// String-array for storing the attributes
		String[] basics = new String[7];
		String match = "";
		
		try
		{
			List basicElements = source.findAllElements(props.getProperty("getBasics.findAllElements"));
			for (Iterator it=basicElements.iterator(); it.hasNext();)
			{
				Element linkElement=(Element)it.next();
				String x=linkElement.getAttributeValue("class");
				if (x != null && x.equals("text"))
				{
					match = linkElement.getContent().toString();
					if (match.contains("Jahre alt") && match.contains("Letzter Login:"))
					{
						match = match.trim();
						match = match.replaceAll("<br[ /]*>", " µ");
						
						int end = Integer.parseInt(props.getProperty("getBasics.countReplace"));
						for(int i = 1; i <= end; i++)
						{
							match = match.replaceAll(props.getProperty("getBasics." + i + ".replace"), props.getProperty("getBasics." + i + ".with"));
						}
						
						match = match.replaceAll("[\\n\\t\\r]+", " ");
						match = match.replaceAll("[ ]* ", " ");
												
						int i = 0;
						StringTokenizer st = new StringTokenizer(match, "µ");
						while (i <= 5 && st.hasMoreTokens())
						{
							basics[i] = st.nextToken().trim();
							i++;
						}
						
						// Divide city & state 
						if (basics[4] != null && basics[4] != "")
						{
							String match2 = basics[4];
							if (match2.contains(","))
							{
								match2 = match2.replaceAll(",[ ]*", "|");
								int index2 = match2.indexOf('|');
								basics[4] = match2.substring(0, index2);
								basics[6] = match2.substring(index2 + 1, match2.length());
							} else
							{
								basics[4] = "";
								basics[6] = match2;
							}
						}
						
						// insert abbreviation for gender
						if(basics[2].equalsIgnoreCase(props.getProperty("getBasics.genderFemale")))
						{
							basics[2] = "Female";
						}
						else
						{
							basics[2] = "Male";
						}
						return basics;
					}
				}
			}
		} catch (Exception e)
		{
			//System.out.println("Error occured in method getBasics(): " + e);
			Log.e(I, "Error occured in method getBasics():" + e);
		}
		return basics;
	}

}
