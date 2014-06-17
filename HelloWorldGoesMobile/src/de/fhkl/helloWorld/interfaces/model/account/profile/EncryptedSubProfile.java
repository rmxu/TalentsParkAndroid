package de.fhkl.helloWorld.interfaces.model.account.profile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.fhkl.helloWorld.interfaces.model.account.key.KeyWrapper;
import de.fhkl.helloWorld.interfaces.model.attribute.IAttributeInstantiator;

public class EncryptedSubProfile extends SubProfile {

	private boolean parseToAccount = false;
	
	public EncryptedSubProfile() {
		super("encryptedSubProfile");
		attributes.put("keyWrapper", new KeyWrapper());
	}

	public KeyWrapper getKeyWrapper() {
		return (KeyWrapper) getAttribute("keyWrapper");
	}

	public void setKeyWrapper(KeyWrapper keyWrapper) {
		setAttribute(keyWrapper);
	}


	@Override
	public void parseFromXML(Element node) {
		if(node.getChildNodes().getLength() == 1){
			parseFromXML(node,true);
		}
		else	
			super.parseFromXML(node);
	}

	@Override
	public Element parseToXML(Document dom) {
		if(isParseToAccount())
			return parseToXML(dom, true);
		else {
			Element elem = super.parseToXML(dom);
			return elem;
		}
	}
	
	
	/*
	 * this methods are overloaded so that the account won«t save redundant data 
	 */
	public void parseFromXML(Element node,boolean toAccount) {

		setUrl(node.getAttribute("url"));
		
		RelationShipType r = new RelationShipType();
		r.setName(node.getAttribute("relation"));
		setRelation(r);
		NodeList subNodes =  node.getChildNodes();
		for(int i = 0; i< subNodes.getLength(); i++){
			Element n = (Element) subNodes.item(i);
			if(n.getNodeName().equals("keyWrapper")){
				getKeyWrapper().parseFromXML(n);
			}
		}
	}
	
	public Element parseToXML(Document dom,boolean toAccount) {
		String className = this.getClass().getName();
		Element root = dom.createElement("encryptedSubProfile");
		root.setAttribute(IAttributeInstantiator.CLASS_IDENTIFIER, className);
		root.setAttribute("relation", getRelation().getName());
		root.setAttribute("url", getUrl());
		root.appendChild(getKeyWrapper().parseToXML(dom));
		return root;
	}

	public boolean isParseToAccount() {
		return parseToAccount;
	}

	public void setParseToAccount(boolean parseToAccount) {
		this.parseToAccount = parseToAccount;
	}

}
