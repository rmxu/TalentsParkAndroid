package de.fhkl.helloWorld.interfaces.model.attribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

/**
 * The most simple case of an attribute containes a key and a value<br />
 * example: Name: Paul<br />
 * In most cases the value will be of the type String, but using
 * <code><T extends Object></code> provides the possiblity to use types like
 * for example Integer and Date, which can be very useful.<br />
 * Try to parameterize as often as possible to gain type constarints.
 */

public class SingleAttribute<T extends Object> extends Attribute {

	protected T value;

	public SingleAttribute(String key) {
		super(key);
		value = null;
	}

	public SingleAttribute(String key, T value) {
		this(key);
		this.value = value;
	}

	public SingleAttribute(Element elem) {
		this(elem.getAttribute("key"));
		parseFromXML(elem);
	}

	public T getValue() {
		return (T) value;
	}

	public void setValue(T value) {
		this.value = (T) value;
	}

	@SuppressWarnings("unchecked")
	public void parseFromXML(Element node) {
	
		Log.e("SINGLEATTRIBUTE:", node.getNodeName()+" : "+ node.getFirstChild().getNodeValue() );		
		
		value = (T) node.getFirstChild().getNodeValue();
		if (!node.getAttribute("type").equals(""))
			type = node.getAttribute("type");
	}

	public Element parseToXML(Document dom) {

		String className = this.getClass().getName();

		if (value != null && !value.equals("")) {
			Element e = dom.createElement(key != null ? key : "elem");
			e.setAttribute(IAttributeInstantiator.CLASS_IDENTIFIER, className);
			//setNodeValue doesn't work on Android
			//e.setNodeValue(((T) value).toString());
			e.appendChild(dom.createTextNode(((T) value).toString()));
			if (type != null)
				e.setAttribute("type", type);
			return e;
		}
		return null;
	}

	public String toSring() {
		return "key: " + key + " value: " + value;
	}
	
	@SuppressWarnings("unchecked")
	public SingleAttribute<T> clone() {
		try {
			SingleAttribute<T> a = (SingleAttribute<T>) attributeInstantiator.getInstance(this.getClass().getName(), key);
			a.setType(this.getType());
			try { // T cloneable ?
				a.setValue( (T) (((Copyable) getValue())).clone());
			} catch (Exception e) {
				// T simple data type ?
				a.setValue(this.getValue());
			}
			return a;
		} catch (InvalidClassException e1) {
			e1.printStackTrace();
			return null;
		}
	}
}