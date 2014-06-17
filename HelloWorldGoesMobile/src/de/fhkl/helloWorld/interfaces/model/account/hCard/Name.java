package de.fhkl.helloWorld.interfaces.model.account.hCard;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

public class Name extends
		StructuredProfileAttribute<String, Attribute> {

	public Name() {
		super("n");
		attributes.put("honorific-prefix",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"honorific-prefix"));
		attributes.put("given-name",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"given-name"));
		attributes.put("additional-name",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"additional-name"));
		attributes.put("family-name",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"family-name"));
		attributes.put("honorific-suffix",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"honorific-suffix"));
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getHonorificPrefix() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-prefix");
	}

	public void setHonorificPrefix(
			ProfileAttributeList<SingleProfileAttribute<String>> honorificPrefix) {
		attributes.put("honorific-prefix", honorificPrefix);
	}

	public void addHonorificPrefix(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-prefix")).add(value);
	}

	public void addHonorificPrefix(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"honorific-prefix");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-prefix")).add(value2);
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getGivenName() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("given-name");
	}

	public void setGivenName(SingleProfileAttribute<String> givenName) {
		attributes.put("given-name", givenName);
	}

	public void addGivenNamePrefix(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("given-name")).add(value);
	}

	public void addGivenNamePrefix(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"given-name");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("given-name")).add(value2);
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getAdditionalName() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("additional-name");
	}

	public void setAdditionalName(SingleProfileAttribute<String> additionalName) {
		attributes.put("additional-name", additionalName);
	}

	public void addAdditionalName(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("additional-name")).add(value);
	}

	public void addAdditionalName(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"additional-name");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("additional-name")).add(value2);
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getFamilyName() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("family-name");
	}

	public void setFamilyName(SingleProfileAttribute<String> familyName) {
		attributes.put("family-name", familyName);
	}

	public void addFamilyName(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("family-name")).add(value);
	}

	public void addFamilyName(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"family-name");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("family-name")).add(value2);
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getHonorificSuffix() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-suffix");
	}

	public void setHonorificSuffix(
			SingleProfileAttribute<String> honorificSuffix) {
		attributes.put("honorific-suffix", honorificSuffix);
	}

	public void addHonorificSuffix(SingleProfileAttribute<String> value) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-suffix")).add(value);
	}

	public void addHonorificSuffix(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"honorific-suffix");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("honorific-suffix")).add(value2);
	}

	public String toString() {
		String nameString = "";
		for (Attribute s : attributes.values()) {
			AttributeList<SingleAttribute<String>> al = (AttributeList<SingleAttribute<String>>) s;
			if (al.size() > 0) {
				nameString += "\t"+al.getKey() + ": ";
				for (SingleAttribute<String> att : al) {
					nameString += att.getValue() + " ";
				}
				nameString += "\n";
			}
		}
		if (!nameString.equals("")) return "n: \n"+nameString;
		else return "";
	}
}
