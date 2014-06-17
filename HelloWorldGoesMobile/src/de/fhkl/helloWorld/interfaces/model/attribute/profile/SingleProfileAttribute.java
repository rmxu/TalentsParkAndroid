package de.fhkl.helloWorld.interfaces.model.attribute.profile;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.attribute.Copyable;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;

public class SingleProfileAttribute<T> extends SingleAttribute<T> implements
		IProfileAttribute {

	private boolean WriteRelationShipTypes = false;

	public SingleProfileAttribute(String key) {
		super(key);

		relationShipTypes = new ArrayList<RelationShipType>();
	}

	/**
	 * List of relation ship types the attribute belongs to
	 */
	protected ArrayList<RelationShipType> relationShipTypes;

	public void readRelationShipTypes(Element element) {

		String subprofiles = element.getAttribute("subprofile");
		ArrayList<RelationShipType> list = relationShipTypes;

		if (!subprofiles.equals("")) {
			String[] subArray = subprofiles.split(RELATIONSHIPTYPEDELIMITER);
			for (String s : subArray) {
				RelationShipType rt = new RelationShipType();
				rt.setName(s);
				list.add(rt);
			}
		}
	}

	public void writeRelationShipTypes(Element element) {

		String subprofileString = "";

		if (relationShipTypes.size() > 0) {
			for (RelationShipType r : relationShipTypes) {
				subprofileString += r.getName();
				subprofileString += RELATIONSHIPTYPEDELIMITER;
			}

			subprofileString = subprofileString.substring(0, subprofileString
					.length()
					- RELATIONSHIPTYPEDELIMITER.length());
		}
		if(element != null)
			element.setAttribute("subprofile", subprofileString);
	}

	public ArrayList<RelationShipType> getRelationShipTypes() {
		return relationShipTypes;
	}

	public void setRelationShipTypes(
			ArrayList<RelationShipType> relationShipTypes) {
		this.relationShipTypes = relationShipTypes;

	}

	@Override
	public void parseFromXML(Element node) {
		super.parseFromXML(node);
		readRelationShipTypes(node);
	}

	public Element parseToXML(Document dom) {
		Element e = super.parseToXML(dom);
		if (WriteRelationShipTypes)
			writeRelationShipTypes(e);
		return e;
	}

	public void setWriteRelationShipTypes(boolean b) {
		WriteRelationShipTypes = b;

	}
	
	public SingleProfileAttribute<T> clone() {
		SingleProfileAttribute<T> s = (SingleProfileAttribute<T>) super.clone();
		s.setRelationShipTypes((ArrayList<RelationShipType>) relationShipTypes.clone());
		return s;
	}
}
