package de.fhkl.helloWorld.interfaces.model.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

/**
 * A structured attribute containes attributes mapped by their key. The
 * attributes are stored in a hash map.<br />
 * Try to parameterize as good as possible to gain type constraints.
 * 
 * If you want to define a structured attribute, you must instantiate the
 * HashMap within<br />
 * 
 * Defining additional getters and setters makes it easy to access attributes
 * contained in the hash.
 */
public class StructuredAttribute<T extends Object, V extends Attribute> extends
		Attribute implements Iterable<T> {

	

	protected final HashMap<T, V> attributes;

	public StructuredAttribute(String key) {
		super(key);
		attributes = new HashMap<T, V>();
	}

	public StructuredAttribute(Element elem) {
		this(elem.getAttribute("key"));
		parseFromXML(elem);
	}

	public boolean containsKey(T key) {
		return attributes.containsKey(key);
	}

	public boolean containsValue(V value) {
		return attributes.containsValue(value);
	}

	public Set<T> keySet() {
		return attributes.keySet();
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(V a) {
		attributes.put((T) a.getKey(), a);
	}

	public V getAttribute(T k) {
		return attributes.get(k);
	}

	public Element parseToXML(Document dom) {

		String className = this.getClass().getName();

		Element node = dom.createElement(key);
		node.setAttribute("type", type);
		node.setAttribute(IAttributeInstantiator.CLASS_IDENTIFIER, className);

		for (Object a : attributes.keySet()) {
			
			Element n = attributes.get(a).parseToXML(dom);
			if (n != null)
				node.appendChild(n);
		}

		return node;
	}

	@SuppressWarnings("unchecked")
	public void parseFromXML(Element node) {
		type = node.getAttribute("type");
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node element = nodeList.item(i);
			if (containsKey((T) element.getNodeName()))
				try {
					setAttribute((V) attributeInstantiator
							.getInstance((Element) element));
				} catch (InvalidClassException e) {

					e.printStackTrace();
				}
		}
	}

	public Iterator<T> iterator() {
		return (Iterator<T>) attributes.values().iterator();
	}
	
	@SuppressWarnings("unchecked")
	public StructuredAttribute<T, V> clone() {
		StructuredProfileAttribute<T, V> s;
		try {
			s = (StructuredProfileAttribute<T, V>) attributeInstantiator.getInstance(this.getClass().getName(), key);
			s.setType(this.getType());
			for (V a : attributes.values()){
				s.setAttribute((V) a.clone());
			}
			return s;
		} catch (InvalidClassException e) {
			e.printStackTrace();
			return null;
		}
	}
}
