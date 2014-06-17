package de.fhkl.helloWorld.interfaces.model.account.profile;

import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;

/**
 * DONE
 */
public class RelationShipType extends SingleAttribute<String> {

	public RelationShipType() {
		super("relationShipType");
	}

	public String getName() {
		return value;
	}

	public void setName(String rtype) {
		value = rtype;
	}

}
