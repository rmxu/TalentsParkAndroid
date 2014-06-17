package de.fhkl.helloWorld.interfaces.actions;

import java.io.FileNotFoundException;

import javax.crypto.spec.PBEParameterSpec;

import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.messages.Message;
import de.fhkl.helloWorld.interfaces.model.messages.body.RelationShipRequestBody;

public interface IAccountManager {

	public Account login(String username, String password)
			throws WrongLoginException, NoAccountException;

	/**
	 * Creates a new account subfolder for the given username
	 * 
	 * @param username
	 * @return true if the dir was created, false if the dir already exists or
	 *         creation failed
	 */
	public boolean createUserAccountDir(String username);

	/**
	 * Returns the path of a specific user
	 * 
	 * @param username
	 * @return the HelloWorld-Path
	 */
	public String getUserAccountDir(String username);

	/**
	 * checks if the user directory already exists, this is important when
	 * creating a new user account
	 * 
	 * @param userName
	 *            desired userName
	 * 
	 * @return true if user doesn't exist on local system
	 */
	public boolean isUserNameAvailable(String userName);

	/**
	 * Enycrypts an exisiting Account xml file with Java Password Based
	 * Encryption (PBE) and saves it on the user's local machine PBE need the
	 * user's password, and two values which are generated by the user's local
	 * machine. The "iteration count" is an integer, the "salt" is an 8 byte
	 * long array.
	 * 
	 * @param account
	 *            the account to be saved
	 * @param username
	 *            the desired username
	 * @param password
	 *            the password
	 * @param pbeparams
	 *            salt and iterationcount for the
	 * @return boolean - true if account is saved
	 */
	public boolean encryptAndSaveAccount(Account account, String username,
			String password, PBEParameterSpec pbeparams);

	/**
	 * Loads the account file specified by given username and tries to decrypt
	 * it with given password
	 * 
	 * @param username
	 * @param password
	 *            the user's password
	 * @return the Account or null if not able to decrypt with given username /
	 *         password combination
	 * @throws FileNotFoundException
	 */
	public Account decryptAndParseAccount(String username, String password)
			throws FileNotFoundException;

	/**
	 * Downloads and decrypts all the subpofiles of the given contacts and
	 * updates the account
	 * 
	 * @param contacts
	 */
	public void requestContactProfiles(AttributeList<Contact> contacts, IProfileRequestListener listener);

	/**
	 * Loads and parses the user account using the global user name
	 * 
	 * @return false, if loading fails
	 */
	public boolean loadAccount();

	/**
	 * Saves the user account back to hard disk using the global user name
	 * 
	 * @return false, if saving failse
	 */
	public boolean saveAccount();

	/**
	 * Saves an backup of the user account with the given name
	 * 
	 * @param fileName
	 *            directory + file name
	 * @param encrypt
	 *            determines whether the backup of the profile will be encrypted
	 * @return false, if backup failse
	 */
	public boolean backupAccount(String fileName, boolean encrypt);

	/**
	 * Upload encrypted account to a server
	 * 
	 * @return false, if upload fails
	 */
	public boolean uploadAccount();

	/**
	 * Download encrypted account from a server and create local folder
	 * 
	 * @param url
	 *            where the account is located
	 * @param username
	 *            used to create a local folder for the account
	 * 
	 * @return false, if download fails
	 */
	public boolean downloadAccount(String url, String username);

	/**
	 * Creates a sub profile containing all attributes from the users profile
	 * that match the relation ship type
	 * 
	 * @param rType
	 * @return a new generated Subprofile
	 */
	public SubProfile createSubProfile(RelationShipType rType, Profile profile);

	/**
	 * generates a new Account
	 * 
	 * @return theAcount
	 */
	public Account createNewAccount();
	
	/**
	 * if user accepts a friendship request the new contact has to added to account
	 */
	public boolean addContact(Message<RelationShipRequestBody> contact, RelationShipType relation);
}
