package de.fhkl.helloWorld.interfaces.model.attribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract model for an attribute. This class is the core class for everything<br />
 * modeled in the application from the account to any user information shared to<br />
 * the web. All subclasses are instantiated dynamically to make it easy to parse
 * to and from XML.<br />
 * 
 */
public interface IAttribute extends Copyable<IAttribute>{

	/**
	 * Parses this Attribute into an DOM Element. Override in subclasses to add
	 * custom attributes.
	 * 
	 * @param dom
	 * @return Element
	 */
	public abstract Element parseToXML(Document dom);

	/**
	 * Set the members of this attribute by parsing the given Dom Element Sets
	 * the members of this Attribute by parsing the given DomObject
	 * 
	 * @param node
	 */
	public abstract void parseFromXML(Element node);
}
