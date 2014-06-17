package de.fhkl.helloWorld.interfaces.model.attribute;

import de.fhkl.helloWorld.implementation.model.attribute.AttributeInstantiator;

public abstract class Attribute implements IAttribute {

	public final static AttributeInstantiator attributeInstantiator = new AttributeInstantiator();

	/**
	 * An optional field to flag attributes
	 */
	protected String type;

	/**
	 * The name of the attribute
	 */
	protected final String key;

	public Attribute(String key, String type) {
		this.key = key;
		this.type = type;
	}

	public Attribute(String key) {
		this(key, "");
	}

	public String getKey() {
		return key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public abstract Attribute clone();
}
