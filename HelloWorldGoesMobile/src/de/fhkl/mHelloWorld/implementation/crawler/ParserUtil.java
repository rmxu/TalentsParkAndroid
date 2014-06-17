package de.fhkl.mHelloWorld.implementation.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

public class ParserUtil {
	
	private static String propfile = "utilregex.properties";
	/*
	 * pageMatches sucht im Quellcode nach einem bestimmten ausdruck (regex)
	 * bsp.: <span class="nametext">horsT<br /> begin ist <span
	 * class="nametext"> und end <br /> regex ist dann [\\p{L}\\p{Punct}\\d ]+ als ergebnis
	 * erhalten wir horsT
	 */
	public static String pageMatches(String page, String begin, String end, String regex)
			throws PatternSyntaxException, IndexOutOfBoundsException, IOException
	{
		boolean matchFound = false;
		String match = "";
		int index;
		
		java.io.InputStream propFile = ParserUtil.class.getResourceAsStream(
				propfile);
		Properties props = new Properties(System.getProperties());
		props.load(propFile);

		while (!matchFound)
		{
			// findet genaue Position innerhalb des Quelltextes
			index = page.indexOf(begin);
			if (index > -1)
			{
				page = page.substring(index + begin.length());
				match = page.substring(0, page.indexOf(end));
				
				int e = Integer.parseInt(props.getProperty("pageMatches.countReplace"));
				for(int i = 1; i <= e; i++)
				{
					match = match.replaceAll(props.getProperty("pageMatches." + i + ".replace"), props.getProperty("pageMatches." + i + ".with"));
				}

				match = match.replaceAll("[ ]*\\t[ ]*", " ");
				match = match.replaceAll("[ ]*\\n[ ]*", " ");
				match = match.replaceAll("[ ]*\\r[ ]*", " ");
				match = match.trim();

				if (match.matches(regex))
				{
					matchFound = true;
					//System.out.println("MatchResult : " + match);
				} else
				{
					System.out.println("No Match Result = " + match + " " + regex);
				}
			} else
			{
				return null;
			}
		} // end while
		return match;
	}
	
	public static List<String> getAttributeList(List<String> list, String interests) throws IOException
	{
		java.io.InputStream propFile = ParserUtil.class.getResourceAsStream(
				propfile);
		Properties props = new Properties(System.getProperties());
		props.load(propFile);
		
		if (interests != null && interests.compareTo("") != 0)
		{
			interests = interests.trim();
			
			int e = Integer.parseInt(props.getProperty("attributeList.countReplace"));
			for(int i = 1; i <= e; i++)
			{
				interests = interests.replaceAll(props.getProperty("attributeList." + i + ".replace"), props.getProperty("attributeList." + i + ".with"));
			}
		
			interests = interests.replaceAll("[ ]+ ", " ");

			StringTokenizer st = new StringTokenizer(interests, "µ");
			while (st.hasMoreTokens())
			{
				list.add(st.nextToken());
			}
		}
		return list;
	}

}
