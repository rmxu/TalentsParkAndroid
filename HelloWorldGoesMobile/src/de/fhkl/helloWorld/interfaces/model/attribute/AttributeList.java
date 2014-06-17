package de.fhkl.helloWorld.interfaces.model.attribute;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An attribute list containes a list of attributes<br />
 * Try to parameterize as often a possible to gain type constarints.
 */
public class AttributeList<T extends Attribute> extends Attribute implements
		Iterable<T>{

	protected ArrayList<T> attributes;

	public AttributeList(String key) {
		super(key);
		attributes = new ArrayList<T>();
	}

	public AttributeList(Element elem) {
		this(elem.getAttribute("key"));
		parseFromXML(elem);
	}

	public ArrayList<T> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<T> attributes) {
		this.attributes = attributes;
	}

	public void add(T a) {
		attributes.add(a);
	}

	public T get(int index) {
		return attributes.get(index);
	}

	public int size() {
		return attributes.size();
	}

	@SuppressWarnings("unchecked")
	public void parseFromXML(Element elem) {

		type = elem.getAttribute("type");
		NodeList nodeList = elem.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node element = nodeList.item(i);
			if (element.getNodeType() != Node.TEXT_NODE)
				try {
					add((T) attributeInstantiator
							.getInstance((Element) element));
				} catch (InvalidClassException e) {
					e.printStackTrace();
				}
		}
	}

	public Element parseToXML(Document dom) {
		String className = this.getClass().getName();

		Element node = dom.createElement(key);
		node.setAttribute("type", type);
		node.setAttribute(IAttributeInstantiator.CLASS_IDENTIFIER, className);

		for (Attribute a : attributes) {
			Element n = a.parseToXML(dom);
			if (n != null)
				node.appendChild(n);
		}
		return node;
	}

	public Iterator<T> iterator() {
		return attributes.iterator();
	}
	
	public boolean contains(SingleAttribute a) {
		for (T a2: attributes){
			if (a2 instanceof SingleAttribute)
			if (((SingleAttribute)a2).getValue().equals(a.getValue())) return true;
		}
		return false;
	}
	
	public AttributeList<T> clone() {
		AttributeList<T> s;
		try {
			s = (AttributeList<T>) attributeInstantiator.getInstance(this.getClass().getName(), key);
			s.setType(this.getType());
			for (T a : attributes){
				s.add((T) a.clone());
			}
			return s;
		} catch (InvalidClassException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
