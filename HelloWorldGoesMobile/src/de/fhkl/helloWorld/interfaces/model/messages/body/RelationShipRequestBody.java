package de.fhkl.helloWorld.interfaces.model.messages.body;

import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.messages.MessageBody;
import de.fhkl.helloWorld.interfaces.model.messages.MessageType;

public class RelationShipRequestBody extends MessageBody{
		
	public RelationShipRequestBody() {
		super(MessageType.FRIENDSHIP_REQUEST);
		attributes.put("subProfiles", new AttributeList<SubProfile>("subProfiles"));
		attributes.put("publicKey", new SingleAttribute<String>("publicKey"));
	}
	
	public AttributeList<SubProfile> getSubProfiles(){
		return ((AttributeList<SubProfile>) getAttribute("subProfiles"));
	}
	
	public void setSubProfiles(AttributeList<SubProfile> subprofiles){
		setAttribute(subprofiles);
	}
	
	public void addSubProfile(SubProfile p){
		((AttributeList<SubProfile>) getAttribute("subProfiles")).add(p);
	}
	
	public String getPublicKey(){
		return ((SingleAttribute<String>) getAttribute("publicKey")).getValue();
	}
	
	public void setPublicKey(String publicKey){
		((SingleAttribute<String>) getAttribute("publicKey")).setValue(publicKey);
	}
}
