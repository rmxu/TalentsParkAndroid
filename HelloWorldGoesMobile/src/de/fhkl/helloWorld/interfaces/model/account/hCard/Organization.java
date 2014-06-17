package de.fhkl.helloWorld.interfaces.model.account.hCard;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

public class Organization extends StructuredProfileAttribute<String, Attribute> {

	public Organization() {
		super("org");
		attributes
				.put("organization-unit",
						new ProfileAttributeList<SingleProfileAttribute<String>>(
								"organization-unit"));
		attributes.put("organization-name", new SingleProfileAttribute<String>(
				"organization-name"));
		
		setOrganizationName("");
	}

	@SuppressWarnings("unchecked")
	public SingleProfileAttribute<String> getOrganizationName() {
		return (SingleProfileAttribute<String>) attributes.get("organization-name");
	}

	public void setOrganizationName(SingleProfileAttribute<String> organizationName) {
		attributes.put("organization-name", organizationName);
	}
	
	public void setOrganizationName(String name) {
		((SingleProfileAttribute<String>) attributes.get("organization-name")).setValue(name);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getOrganizationUnit() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("organization-unit");
	}

	public void setOrganizationUnit(
			ProfileAttributeList<SingleProfileAttribute<String>> organizationUnit) {
		attributes.put("organization-unit", organizationUnit);
	}
	
	public void addOrganizationUnit(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes.get("organization-unit")).add(value);
	}
	
	public void addOrganizationUnit(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>("organization-unit");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes.get("organization-unit")).add(value2);
	}
	
	public String toString(){
		String orgaString = "org: \n\t"+"organization-name: "+getOrganizationName().getValue();
		orgaString += "\n\tunits:\n";
		for (SingleProfileAttribute<String> s : getOrganizationUnit()) {
			orgaString += "\t\t"+s.getValue()+"\n";
		}
		
		return orgaString;
	}
}
