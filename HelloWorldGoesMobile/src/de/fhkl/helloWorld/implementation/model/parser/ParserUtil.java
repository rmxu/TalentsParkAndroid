package de.fhkl.helloWorld.implementation.model.parser;

import java.util.Date;

public class ParserUtil {

	public static Date stringToDate(String time) {
		Date d = new Date();
		d.setTime(new Long(time));

		return d;
	}
}
