package de.fhkl.helloWorld.interfaces.model.attribute;

import org.w3c.dom.Element;

import de.fhkl.helloWorld.HelloWorldProperties;

/**
 * This class provides two methods to parse Objects of the type Attribute to and<br />
 * from XML.
 */
public interface IAttributeInstantiator {

	/**
	 * the name of the attribute in each XML node that contains the name of the corresponding java class 
	 */
	public final static String CLASS_IDENTIFIER =  "javaclass";
		
		//HelloWorldProperties.getString("ClassIdentifier");
	
	/**
	 * garantees that dynamically instantiated classes are located inside the package named by this String 
	 */
	public final static String PACKAGE_CONSTRAINT =  "de.fhkl.helloWorld";

	/**
	 * Uses the attribute with the name defined by the CLASS_IDENTIFIER of the
	 * given<br />
	 * node to instantiate an Object of the type Attribute. This method is used
	 * recursively<br />
	 * to read complex structures from XML.
	 * 
	 * @param node
	 * @return Attribute
	 * @throws InvalidClassException
	 */
	public Attribute getInstance(Element node) throws InvalidClassException;
}
