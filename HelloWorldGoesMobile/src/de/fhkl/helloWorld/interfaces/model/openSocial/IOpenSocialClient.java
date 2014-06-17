package de.fhkl.helloWorld.interfaces.model.openSocial;

import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;

/**
 * This interface provides methods to import data from social networks by
 * implementing the Open Social API
 */
public interface IOpenSocialClient {

	public final int STUDIVZ = 1;
	public final int XING = 2;
	public final int MYSPACE = 3;
	public final int MEINVZ = 4;
	public final int ORKUT = 5;
	public final int FRIENDSTER = 6;
	public final int LOKALISTEN = 7;

	/**
	 * Imports Data from the specified social network and generates a SubProbile
	 * 
	 * @param socialNetwork
	 * @param username
	 * @param password
	 * @return Subprofile
	 */
	public SubProfile getData(int socialNetwork, String username,
			String password);

}
