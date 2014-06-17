package de.fhkl.helloWorld.interfaces.model.attribute.single;

import java.sql.Timestamp;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;

/**
 * Extends the single attribute for use with the Class Date
 */
public class SingleDateAttribute extends SingleAttribute<Date> {

	public SingleDateAttribute(String key) {
		super(key);
		value = new Date();
	}
	
	@Override
	public void parseFromXML(Element node) {
		setValue(new Timestamp(0));
	}

	@Override
	public Element parseToXML(Document dom) {
		Element elem = super.parseToXML(dom);
		elem.setAttribute("value", "" + getValue().getTime());
		return elem;
	}
}
