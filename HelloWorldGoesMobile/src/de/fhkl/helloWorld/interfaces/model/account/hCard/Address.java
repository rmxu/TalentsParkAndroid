package de.fhkl.helloWorld.interfaces.model.account.hCard;


import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

public class Address extends
		StructuredProfileAttribute<String, Attribute> {

	public Address() {
		super("adr");

		attributes.put("street-address", new ProfileAttributeList<SingleProfileAttribute<String>>(
				"street-address"));
		attributes.put("country-name", new SingleProfileAttribute<String>(
				"country-name"));
		attributes.put("post-office-box", new SingleProfileAttribute<String>(
				"post-office-box"));
		attributes.put("region", new SingleProfileAttribute<String>("region"));
		attributes.put("locality", new SingleProfileAttribute<String>("locality"));
		attributes.put("postal-code",
				new SingleProfileAttribute<String>("postal-code"));
		attributes.put("extended-address", new SingleProfileAttribute<String>(
				"extended-address"));
	}

	public ProfileAttributeList<SingleProfileAttribute<String>> getStreetAddress() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes.get("street-address");
	}

	public void setStreetAddress(ProfileAttributeList<SingleProfileAttribute<String>> streetAddress) {
		attributes.put("street-address", streetAddress);
	}
	
	public void addStreetAddress(SingleProfileAttribute<String> value){
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes.get("street-address")).add(value);
	}
	
	public void addStreetAddress(String value){
		SingleProfileAttribute<String> sr = new SingleProfileAttribute<String>("street-address");
		sr.setValue(value);
		addStreetAddress(sr);
	}
	
	public void setStreetAddress(String address) {
		((SingleProfileAttribute<String>) attributes.get("street-address")).setValue(address);
	}

	public SingleProfileAttribute<String> getPostOfficeBox() {
		return (SingleProfileAttribute<String>) attributes.get("post-office-box");
	}

	public void setPostOfficeBox(SingleProfileAttribute<String> postOfficeBox) {
		attributes.put("post-office-box", postOfficeBox);
	}
	
	public void setPostOfficeBox(String value) {
		((SingleProfileAttribute<String>) attributes.get("post-office-box")).setValue(value);
	}

	public SingleProfileAttribute<String> getExtendedAddress() {
		return (SingleProfileAttribute<String>) attributes.get("extended-address");
	}

	public void setExtendedAddress(SingleProfileAttribute<String> extendedAddress) {
		attributes.put("extended-address", extendedAddress);
	}
	
	public void setExtendedAddress(String value) {
		((SingleProfileAttribute<String>) attributes.get("extended-address")).setValue(value);
	}

	public SingleProfileAttribute<String> getRegion() {
		return (SingleProfileAttribute<String>) attributes.get("region");
	}

	public void setRegion(SingleProfileAttribute<String> region) {
		attributes.put("region", region);
	}
	
	public void setRegion(String value) {
		((SingleProfileAttribute<String>) attributes.get("region")).setValue(value);
	}

	public SingleProfileAttribute<String> getLocality() {
		return (SingleProfileAttribute<String>) attributes.get("region");
	}

	public void setLocality(SingleProfileAttribute<String> locality) {
		attributes.put("locality", locality);
	}
	
	public void setLocality(String value) {
		((SingleProfileAttribute<String>) attributes.get("locality")).setValue(value);
	}
	
	public SingleProfileAttribute<String> getPostalCode() {
		return (SingleProfileAttribute<String>) attributes.get("postal-code");
	}

	public void setPostalCode(SingleProfileAttribute<String> postalCode) {
		attributes.put("postal-code", postalCode);
	}
	
	public void setPostalCode(String value) {
		((SingleProfileAttribute<String>) attributes.get("postal-code")).setValue(value);
	}

	public SingleProfileAttribute<String> getCountryName() {
		return (SingleProfileAttribute<String>) attributes.get("country-name");
	}

	public void setCountryName(SingleProfileAttribute<String> countryName) {
		attributes.put("country-name", countryName);
	}
	
	public void setCountryName(String value) {
		((SingleProfileAttribute<String>) attributes.get("country-name")).setValue(value);
	}
	
	public String toString(){
		String addressString = "address: " + (getType().equals("") ? "" : "Type: "+getType())+"\n\t";
			
		if((getStreetAddress().size()) > 0){
			addressString += "street-address: ";
			for(SingleAttribute a: getStreetAddress()){
				addressString += "\n\t\t" + a.getValue();
			}
			addressString += "\n";			
		}
		addressString += "\tcountry-name: " + getCountryName().getValue()+ "\n";
		addressString += "\tpost-office-box: " + getPostOfficeBox().getValue()+ "\n";
		addressString += "\tregion: " + getRegion().getValue()+ "\n";
		addressString += "\tlocality: " + getLocality().getValue()+ "\n";
		addressString += "\tpostal-code: " + getPostalCode().getValue()+ "\n";
		addressString += "\textended-address: " + getExtendedAddress().getValue()+ "\n";
		return addressString;
								
	}

}
