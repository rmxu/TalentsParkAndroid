package de.fhkl.helloWorld.interfaces.model.attribute.profile;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.attribute.IAttribute;

public interface IProfileAttribute extends IAttribute {

	public static final String RELATIONSHIPTYPEDELIMITER = "#DEL#";

	/**
	 * Reads the relationship types from the given DomObject into this attribute
	 * 
	 * @param element
	 */
	public void readRelationShipTypes(Element element);

	/**
	 * Writes the relationship types from this attribute into the given
	 * DomObject
	 * 
	 * @param element
	 */
	public void writeRelationShipTypes(Element element);

	/**
	 * @return ArrayList
	 */
	public ArrayList<RelationShipType> getRelationShipTypes();

	/**
	 * @param relationShipTypes
	 */
	public void setRelationShipTypes(
			ArrayList<RelationShipType> relationShipTypes);

	/**
	 * Parses this Attribute into an DOM Element. Override in subclasses to add
	 * custom attributes.
	 * 
	 * @param dom
	 * @param isSubProfile
	 * @return Element
	 */
	public abstract Element parseToXML(Document dom);
	
	public void setWriteRelationShipTypes(boolean b);

}
