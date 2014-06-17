package de.fhkl.helloWorld.interfaces.model.account.hCard;

import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;

public class HCard extends StructuredProfileAttribute<String, Attribute> {

	public HCard() {
		super("hcard");
		attributes.put("geo", new SingleProfileAttribute<String>("geo"));
		attributes.put("fn", new SingleProfileAttribute<String>("fn"));
		attributes.put("n", new Name());
		attributes.put("bday", new SingleProfileAttribute<String>("bday"));
		attributes.put("nickname",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"nickname"));
		attributes.put("photo",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"photo"));
		attributes
				.put(
						"logo",
						new ProfileAttributeList<SingleProfileAttribute<String>>(
								"logo"));
		attributes.put("adr", new ProfileAttributeList<Address>("adr"));
		attributes.put("email",
				new ProfileAttributeList<SingleProfileAttribute<String>>(
						"email"));
		attributes
				.put(
						"tel",
						new ProfileAttributeList<SingleProfileAttribute<String>>(
								"tel"));
		attributes
				.put(
						"url",
						new ProfileAttributeList<SingleProfileAttribute<String>>(
								"url"));
		attributes.put("org", new ProfileAttributeList<Organization>("org"));
		setFn("");
		setBday("");

	}

	public HCard(StructuredProfileAttribute sa) {
		super("hcard");
		attributes.put("geo", sa.getAttribute("geo") != null ? sa
				.getAttribute("geo") : new SingleProfileAttribute<String>("geo"));
		attributes.put("fn", sa.getAttribute("fn") != null ? sa
				.getAttribute("fn") : new SingleProfileAttribute<String>("fn"));
		
		attributes.put("n", sa.getAttribute("n") != null ? sa.getAttribute("n")
				: new Name());
		attributes.put("bday", sa.getAttribute("bday") != null ? sa
				.getAttribute("bday") : new SingleProfileAttribute<String>(
				"bday"));
		attributes.put("nickname", sa.getAttribute("nickname") != null ? sa
				.getAttribute("nickname")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"nickname"));
		attributes.put("photo", sa.getAttribute("photo") != null ? sa
				.getAttribute("photo")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"photo"));
		attributes.put("logo", sa.getAttribute("logo") != null ? sa
				.getAttribute("logo")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"logo"));
		attributes
				.put("adr", sa.getAttribute("adr") != null ? sa
						.getAttribute("adr")
						: new ProfileAttributeList<Address>("adr"));
		attributes.put("email", sa.getAttribute("email") != null ? sa
				.getAttribute("email")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"email"));
		attributes.put("tel", sa.getAttribute("tel") != null ? sa
				.getAttribute("tel")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"tel"));
		attributes.put("url", sa.getAttribute("url") != null ? sa.getAttribute("url")
				: new ProfileAttributeList<SingleProfileAttribute<String>>(
						"url"));
		attributes.put("org", sa.getAttribute("org") != null ? sa
				.getAttribute("org") : new ProfileAttributeList<Organization>(
				"org"));
	}

	@SuppressWarnings("unchecked")
	public SingleProfileAttribute<String> getFn() {
		return (SingleProfileAttribute<String>) attributes.get("fn");
	}
	
	@SuppressWarnings("unchecked")
	public SingleProfileAttribute<String> getGeo() {
		return (SingleProfileAttribute<String>) attributes.get("geo");
	}

	public void setGeo(SingleProfileAttribute<String> geo) {
		attributes.put("geo", geo);
	}
	
	public void setFn(SingleProfileAttribute<String> fn) {
		attributes.put("fn", fn);
	}

	public void setFn(String fn) {
		((SingleProfileAttribute<String>) attributes.get("fn")).setValue(fn);
	}

	public Name getN() {
		return (Name) attributes.get("n");
	}

	public void setN(Name n) {
		attributes.put("n", n);
	}

	@SuppressWarnings("unchecked")
	public SingleProfileAttribute<String> getBday() {
		return (SingleProfileAttribute<String>) attributes.get("bday");
	}

	public void setBday(SingleProfileAttribute<String> bday) {
		attributes.put("bday", bday);
	}

	public void setBday(String fn) {
		((SingleProfileAttribute<String>) attributes.get("bday")).setValue(fn);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getNickname() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("nickname");
	}

	public void setNickname(
			ProfileAttributeList<SingleProfileAttribute<String>> nickname) {
		attributes.put("nickname", nickname);
	}

	public void addNickname(SingleProfileAttribute<String> nickname) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("nickname")).add(nickname);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getPhoto() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("photo");
	}

	public void setPhoto(
			ProfileAttributeList<SingleProfileAttribute<String>> photo) {
		attributes.put("photo", photo);
	}

	public void addPhoto(SingleProfileAttribute<String> photo) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("photo")).add(photo);
	}

	public void addPhoto(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"photo");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("photo")).add(value2);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getLogo() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("logo");
	}

	public void setLogo(
			ProfileAttributeList<SingleProfileAttribute<String>> logo) {
		attributes.put("logo", logo);
	}

	public void addLogo(SingleProfileAttribute<String> logo) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("logo")).add(logo);
	}

	public void addLogo(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"logo");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("logo")).add(value2);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<Address> getAdr() {
		return (ProfileAttributeList<Address>) attributes.get("adr");
	}

	public void setAdr(ProfileAttributeList<Address> adr) {
		attributes.put("adr", adr);
	}

	public void addAdr(Address adr) {
		((ProfileAttributeList<Address>) attributes.get("adr")).add(adr);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getEmail() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("email");
	}

	public void setEmail(
			ProfileAttributeList<SingleProfileAttribute<String>> email) {
		attributes.put("email", email);
	}

	public void addEmail(SingleProfileAttribute<String> email) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("email")).add(email);
	}

	public void addEmail(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"email");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("email")).add(value2);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getTel() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("tel");
	}

	public void setTel(ProfileAttributeList<SingleProfileAttribute<String>> tel) {
		attributes.put("tel", tel);
	}

	public void addTel(SingleProfileAttribute<String> tel) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("tel")).add(tel);
	}

	public void addTel(String value) {
		SingleProfileAttribute<String> value2 = new SingleProfileAttribute<String>(
				"tel");
		value2.setValue(value);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("tel")).add(value2);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<SingleProfileAttribute<String>> getUrl() {
		return (ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("url");
	}

	public void setUrl(ProfileAttributeList<SingleProfileAttribute<String>> url) {
		attributes.put("url", url);
	}

	public void addUrl(SingleProfileAttribute<String> url) {
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("url")).add(url);
	}

	public void addUrl(String url) {
		SingleProfileAttribute<String> url2 = new SingleProfileAttribute<String>(
				"url");
		url2.setValue(url);
		((ProfileAttributeList<SingleProfileAttribute<String>>) attributes
				.get("url")).add(url2);
	}

	@SuppressWarnings("unchecked")
	public ProfileAttributeList<Organization> getOrg() {
		return (ProfileAttributeList<Organization>) attributes.get("org");
	}

	public void setOrg(ProfileAttributeList<Organization> org) {
		attributes.put("org", org);
	}

	public void addOrg(Organization org) {
		((ProfileAttributeList<Organization>) attributes
				.get("org")).add(org);
	}

	public String toString() {
		String hcardString = "";
		hcardString += "fn: " + getFn().getValue() + "\n";
		hcardString += "bday: " + getBday().getValue() + "\n";
		
		hcardString += getN().toString();

		if ((getNickname().size()) > 0) {
			hcardString += "nickname: ";
			for (SingleAttribute a : getNickname()) {
				hcardString += "\n\t" + a.getValue();
			}
			hcardString += "\n";
		}
		if ((getPhoto().size()) > 0) {
			hcardString += "photo: ";
			for (SingleAttribute a : getPhoto()) {
				hcardString += "\n\t" + a.getValue();
			}
			hcardString += "\n";
		}
		if ((getLogo().size()) > 0) {
			hcardString += "logo: ";
			for (SingleAttribute a : getLogo()) {
				hcardString += "\n\t" + a.getValue();
			}
			hcardString += "\n";
		}
		for (Address a : getAdr())
			hcardString += a.toString();

		if ((getEmail().size()) > 0) {
			hcardString += "email: ";
			for (SingleAttribute a : getEmail()) {
				hcardString += "\n\t" + a.getValue() + (a.getType().equals("") ? "" : " Type: "+a.getType());
			}
			hcardString += "\n";
		}
		if ((getTel().size()) > 0) {
			hcardString += "tel: ";
			for (SingleAttribute a : getTel()) {
				hcardString += a.getValue() + (a.getType().equals("") ? "" : " Type: "+a.getType());
			}
			hcardString += "\n";
		}
		if ((getUrl().size()) > 0) {
			for (SingleAttribute a : getUrl()) {
				hcardString += a.getKey() + " " + a.getValue();
			}
			hcardString += "\n";
		}
		for (Organization o : getOrg()) {
			hcardString += o.toString();
		}

		return hcardString;
	}
}
