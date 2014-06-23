package de.fhkl.helloWorld.implementation.actions;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.util.logging.*;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import android.util.Log;



import de.fhkl.helloWorld.implementation.model.parser.AccountParser;

import de.fhkl.helloWorld.implementation.model.security.CryptoAES256;
import de.fhkl.helloWorld.implementation.model.security.CryptoPBEMD5;
import de.fhkl.helloWorld.implementation.model.security.CryptoRSA2048;
import de.fhkl.helloWorld.implementation.model.security.CryptoUtil;
import de.fhkl.helloWorld.implementation.util.FileHelper;
import de.fhkl.helloWorld.implementation.util.ftp.FTPClient;

import de.fhkl.helloWorld.interfaces.actions.IAccountManager;
import de.fhkl.helloWorld.interfaces.actions.IProfileRequestListener;
import de.fhkl.helloWorld.interfaces.actions.NoAccountException;
import de.fhkl.helloWorld.interfaces.actions.WrongLoginException;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.key.KeyPair;
import de.fhkl.helloWorld.interfaces.model.account.key.KeyWrapper;
import de.fhkl.helloWorld.interfaces.model.account.key.PrivateKey;
import de.fhkl.helloWorld.interfaces.model.account.key.PublicKey;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.IProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.StructuredProfileAttribute;
import de.fhkl.helloWorld.interfaces.model.messages.Message;
import de.fhkl.helloWorld.interfaces.model.messages.body.RelationShipRequestBody;
import de.fhkl.mHelloWorld.implementation.HelloWorldBasic;
import de.fhkl.mHelloWorld.implementation.parser.ProfileParserOut;


/**
 * This class provides methods to handle the user's account.
 * 
 * 
 */
public class AccountManager implements IAccountManager {

	/*
	 * 
	 * 
	 * 
	 *  Mobile!
	 *  Parts of this class are already implementet in FileWriter,
	 *  but maybe we should put the part with the importProfile right in
	 *  this class
	 * 
	 * 
	 * 
	 * 
	 */
	
	 private static final String I = 
		 "=======[HELLO-WORLD]" +
		 "AccountManager" + ": ";
	 

	/** the HelloWorld directory */
    private static String helloWorldSubDir = HelloWorldBasic.getPath();
	
	//private static String helloWorldSubDir = "/data/data/de.fhkl.mHelloWorld.implementation";
	/** The account file name. */
	private static String accountFileName = "account.xml";

	// TODO this has to be replaced with userinput
	private static PBEParameterSpec pbespec = new PBEParameterSpec("jklsdfhg"
			.getBytes(), 4711);

	public Account login(String username, String password)
			throws WrongLoginException, NoAccountException {

		

		Account account = null;
		try {
			account = decryptAndParseAccount(username, password);
		} catch (FileNotFoundException e) {
			Log.i("Log", "NoAccountException wird im AccountManager geworfen");
			throw new NoAccountException();
		}
		if (account != null) {

			HelloWorldBasic.setUsername(username);
			HelloWorldBasic.setPassword(password);
			// old desktop-version:
		/*	Application.getInstance(HelloWorldApp.class).setUsername(username);
			Application.getInstance(HelloWorldApp.class).setPassword(password);*/
			
			return account;

		} else {
			
			throw new WrongLoginException();
		}
	}

	/**
	 * Creates a new account subfolder for the given username.
	 * 
	 * @param username
	 *            the username
	 * 
	 * @return true if the dir was created, false if the dir already exists or
	 *         creation failed
	 */
	public boolean createUserAccountDir(String username) {

//		String userDir = System.getProperty("user.home");
		
		String seperator = System.getProperty("file.separator");
		File helloworldDir = new File(helloWorldSubDir);
		

		String userAccountDir = helloWorldSubDir + seperator + username;
		File userAccount = new File(userAccountDir);

		if (userAccount.exists()) {
			
			return false;
		} else {
			if (userAccount.mkdir()) {

				
				// create subfolders
				File subdir = new File(userAccountDir + seperator + "contacts");
				subdir.mkdir();
				

				subdir = new File(userAccountDir + seperator + "temp");
				subdir.mkdir();
				

				subdir = new File(userAccountDir + seperator + "temp"
						+ seperator + "messages");
				subdir.mkdir();


				return true;
			}
			return false;
		}
	}

	/**
	 * Get the HelloWorld directory.
	 * 
	 * @param username
	 *            the username
	 * 
	 * @return string the directory of the HelloWorld Application
	 */
	public String getUserAccountDir(String username) {
		// old-version:
//		return System.getProperty("user.home") + helloWorldSubDir
//				+ System.getProperty("file.separator") + username;
		return helloWorldSubDir
		+ System.getProperty("file.separator") + username;
	}

	/**
	 * checks if the user directory already exists, this is important when
	 * creating a new useraccount
	 * 
	 * @param userName
	 *            desired userName
	 * 
	 * @return true if user doesnt exist on local system
	 */
	public boolean isUserNameAvailable(String userName) {
		File userAccount = new File(getUserAccountDir(userName));
		return !userAccount.exists();

	}

	/**
	 * This method encrypts and saves the user's account.
	 * 
	 * @param account
	 *            the Account input
	 * @param username
	 *            the username
	 * @param password
	 *            the choosen password
	 * @param pbeparams
	 *            the salt and iterationcount
	 * 
	 * @return true if success
	 */
	public boolean encryptAndSaveAccount(Account account, String username,
			String password, PBEParameterSpec pbeparams) {

		Log.i(I,"B1");
		// generate new user account
		createUserAccountDir(username);
		Log.i(I,"B2");
		try {
			/**
			 * encrypt the new account with the given password and the system's
			 * pbespecs. The pbespec is saved at the user's local machine and
			 * contains: - the salt: an 8 byte long bytearray - the iteration
			 * count: an integer
			 */
			
			Document doc = (new AccountParser()).parseAccount(account);

//			DOMImplementation impl = DOMImplementationImpl.getDOMImplementation();
//			// Dokumentenobjekt erzeugen
//			Document doc = impl.createDocument(null, "Linux", null);
//			// Erzeugen des Wurzelelements.
//			Element root = doc.createElement("linux-config");
//
//			// erstes Element und Kindknoten erzeugen und an die Wurzel hängen
//			Element wm = doc.createElement("window-manager");
//			Element n = doc.createElement("name");
//			Text name = doc.createTextNode("Enlightenment");
//			wm.setAttribute("default", "true");
//			n.appendChild(name);
//			wm.appendChild(n); 
//			root.appendChild(wm);
			
			Log.i(I,"B3");
			ProfileParserOut.exportXML(doc, System.out);
			Log.i(I,"B4");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Log.i(I,"B5");
			OutputFormat format = new OutputFormat(doc);
			Log.i(I,"B6");
			XMLSerializer serializer = new XMLSerializer(out, format);
			Log.i(I,"B7");
			serializer.serialize(doc.getDocumentElement());
			Log.i(I,"B8");
			InputStream enc = (new CryptoPBEMD5()).encryptPBE(CryptoUtil
					.outToInputStream(out), password, pbespec);
			Log.i(I,"B9");
//old
//			if (FileHelper.writeFile(getUserAccountDir(username)
//					+ System.getProperty("file.separator") + accountFileName,
//					enc)) {
				if (FileHelper.writeFile(getUserAccountDir(username)+"/account.xml",
						enc)) {
//					if (FileHelper.writeFile("C:"+ System.getProperty("file.separator") +"test"+ System.getProperty("file.separator") +"account.xml",
//							enc)) {
//				if (logger.isInfoEnabled())
//					logger.info("account sucessfully encrypted: "
//							+ getUserAccountDir(username)
//							+ System.getProperty("file.separator")
//							+ accountFileName);
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		Log.i(I,"B10");
		return false;

	}

	/**
	 * This method decrypts the user's account.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the user's password
	 * 
	 * @return Account - the user's account
	 * @throws FileNotFoundException
	 */
	public Account decryptAndParseAccount(String username, String password)
			throws FileNotFoundException {
		// get the HelloWorld path of the user
	    String userAccountPath = getUserAccountDir(username)
				+ System.getProperty("file.separator") + accountFileName;
	    //String userAccountPath = HelloWorldBasic.getUserDir();
	

	    Log.i("Log", "AccountPath: "+userAccountPath);
		
		FileInputStream accountIn = null;
		
		Log.i("Log", "Inputstream erzeugt");
		
		// read the user's account
		accountIn = new FileInputStream(userAccountPath);

		Log.i("Log", "Inputstream instantiiert");

		// decrypt the user's account with his password
		InputStream decrypted = (new CryptoPBEMD5()).decryptPBE(accountIn,
				password, pbespec);
		
		// check if password works
		if (decrypted == null || CryptoUtil.isEncrypted(decrypted)) {
			
			Log.i("Log", "Account wurde nicht entschl�sselt");
			return null;			
		}
		Log.i("Log", "Account wurde entschl�sselt");

		// parse the decrypted account and return him
		AccountParser parser = new AccountParser();
		Account account = parser
				.parseAccount(new BufferedInputStream(decrypted));
		
		return account;
	}
	
	public Account decryptAndParseAccount(InputStream in, String password)
	throws FileNotFoundException {
		
		// decrypt the user's account with his password
		InputStream decrypted = (new CryptoPBEMD5()).decryptPBE(in,
				password, pbespec);
		
		// check if password works
		if (CryptoUtil.isEncrypted(decrypted)) {
		
			return null;
		}
	
	
		// parse the decrypted account and return him
		AccountParser parser = new AccountParser();
		Account account = parser
				.parseAccount(new BufferedInputStream(decrypted));
		
		return account;
	}

	/**
	 * Loads and parses the friend's subprofiles.
	 */
	public void requestContactProfiles(AttributeList<Contact> attributeList,
			IProfileRequestListener listener) {
	
		/*for (Contact c : attributeList.getAttributes()) {

			
			if (c.getProfiles().size() > 0) {
				EncryptedSubProfile friendProfile = (EncryptedSubProfile) c
						.getProfiles().get(0);
				(new RequestProfileThread(friendProfile, listener,c)).start();
			}
			else{
				logger.info("contact doesnt have a subprofile published for you");
			}
		}*/
	}

	/**
	 * Loads and parses the user account using the global user name.
	 * 
	 * @return false, if loading fails
	 */
	public boolean loadAccount() {

		// TODO

		return false;
	}

	/**
	 * Saves the user account back to hard disk using the global user name.
	 * 
	 * @return false, if saving failse
	 */
	public boolean saveAccount(Account theAccount) {

		Document doc = (new AccountParser()).parseAccount(theAccount);
		ProfileParserOut.exportXML(doc, System.out);

		return true;
	}

	/**
	 * Saves an backup of the user account with the given name.
	 * 
	 * @param fileName
	 *            directory + file name
	 * @param encrypt
	 *            determines whether the backup of the profile will be encrypted
	 * 
	 * @return false, if backup fails
	 */
	public boolean backupAccount(String fileName, boolean encrypt) {

		// TODO

		return false;
	}

	/**
	 * Upload encrypted account to a server.
	 * 
	 * @return false, if upload fails
	 */
	public boolean uploadAccount() {

		// TODO

		return false;
	}

	/**
	 * Create a new asymmetric KeyPair and sets the timestamp on it
	 * 
	 */
	public KeyPair createKeyPair() {
		// generate new KeyPair
		java.security.KeyPair keyPair = (new CryptoRSA2048()).generateKeyPair();

		// generate HelloWorld KeyPair, which is extended with a timestamp and
		// the url of the PublicKey
		KeyPair hwKeyPair = new KeyPair();

		// generate new HelloWorld PrivateKey and set the current timestamp on
		// it
		PrivateKey privateKey = new PrivateKey();
		privateKey.setPrivateKey(keyPair.getPrivate());
		privateKey.setTimestamp((new Date(System.currentTimeMillis())));
		Log.i(I, "------------------------ KeyPair-PublicKey-Timestamp: "+ privateKey.getTimestamp());
		hwKeyPair.setPrivateKey(privateKey);

		// generate new HelloWorld PublicKey and set the current timestamp on it
		PublicKey publicKey = new PublicKey();
		publicKey.setPublicKey(keyPair.getPublic());
		publicKey.setTimestamp((new Date(System.currentTimeMillis())));
		Log.i(I, "------------------------ KeyPair-PublicKey-Timestamp: "+ publicKey.getTimestamp());
		hwKeyPair.setPublicKey(publicKey);
		return hwKeyPair;

	}

	/**
	 * create a new Account
	 */
	public Account createNewAccount() {
		Account account = new Account();
		account.setKeyPair(this.createKeyPair());
		account.setPrivateProfile(new Profile());

		return account;
	}

	/**
	 * Download encrypted account from a server and create local folder.
	 * 
	 * @param url
	 *            the url
	 * @param username
	 *            the username
	 * 
	 * @return false, if download fails
	 */
	public boolean downloadAccount(String url, String username) {

		// TODO

		return false;
	}

	public boolean saveAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Extracts all Attributes from a given user Profile to a SubProfile that
	 * match the given relationship type
	 * 
	 * @param type
	 * @param profile
	 * @return Subprofile
	 */
	public SubProfile createSubProfile(RelationShipType type, Profile profile) {

		SubProfile sp = new SubProfile();

		// HCard

		for (RelationShipType r : profile.getHCard().getRelationShipTypes()) {

			if (r.getName().equals(type.getName())) {
				sp.setHCard(new HCard(
						(StructuredProfileAttribute) buildInAttributes(type,
								profile.getHCard())));
				break;
			}
		}

		// Custom attributes

		// if (profile.getRelationShipTypes().contains(type))
		// Here a recursive method is needed as a complex structure can
		// be given
		// sp.getAttributes().add(
		// buildInAttributes(type, profile.getAttributes()));

		return sp;
	}

	public EncryptedSubProfile createNewSubProfile(RelationShipType r, Account userAccount) {
	
		
		EncryptedSubProfile subProfile = new EncryptedSubProfile();

		// generate a key for encryption
		CryptoAES256 crypto = new CryptoAES256();
		SecretKey key = crypto.generateKey();

		KeyWrapper wrapper = new KeyWrapper();
		wrapper.setTimestamp(new Date(System.currentTimeMillis()));
		wrapper.setSecretKey(key);
		wrapper.setAlgorithm(key.getAlgorithm());

		subProfile.setKeyWrapper(wrapper);

		subProfile.setRelation(r);
//		Application.getInstance(HelloWorldApp.class).getAccount()
//				.getSubProfiles().add(subProfile);
//		logger.info("new subprofile created for " + r.getName());
		
//		Application.getInstance(HelloWorldApp.class).getAccount()
//		.getSubProfiles().add(subProfile);
		userAccount.getSubProfiles().add(subProfile);

		return subProfile;
	}

	public boolean uploadSubprofile(SubProfile subProfile,
			FileTransferConnection conn, String path) {
		
		try {

			((EncryptedSubProfile) subProfile).setParseToAccount(false);

			Document doc = new DocumentImpl();
			Element root = subProfile.parseToXML(doc);
			doc.appendChild(root);

			// excrypt the xml docuemnt
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut,
					new OutputFormat(doc));

			serial.serialize(doc);

			CryptoAES256 crypto = new CryptoAES256();
			String encrypted = crypto.encrypt(stringOut.toString(),
					(SecretKey) ((EncryptedSubProfile) subProfile)
							.getKeyWrapper().getSecretKey());

			ByteArrayInputStream outFile = new ByteArrayInputStream(encrypted
					.getBytes());

			return uploadFile(outFile, conn, path);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public boolean uploadPublicKey(PublicKey publicKey,
			FileTransferConnection conn, String path) {

		boolean success = false;
/*
		try {

			Document doc = new DocumentImpl();
			Element root = publicKey.parseToXML(doc);
			doc.appendChild(root);

			// excrypt the xml docuemnt
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut,
					new OutputFormat(doc));

			serial.serialize(doc);

			ByteArrayInputStream outFile = new ByteArrayInputStream(stringOut
					.toString().getBytes("UTF-8"));

			return uploadFile(outFile, conn, path);
		} catch (Exception e) {

		}
*/
		return false;

	}

	public boolean uploadFile(InputStream in, FileTransferConnection conn,
			String path) {

		boolean success = false;

		try {
			// upload it to a server
//			if (conn.getProtocol().lowerCaseName().equals("sftp")) {
//				SFTPClient ftm = new SFTPClient(conn.getHost().getValue(), conn
//						.getUsername().getValue(), conn.getPassword()
//						.getValue());
//
//				if (ftm.connect())
//					ftm.uploadFile(in, path);
//				else {
//					System.out.println("USE A STATIC LOGGER FOR LOGGING!!!");
//				}
//
//				ftm.disconnect();
//				success = true;
//			} else {
				FTPClient ftm = new FTPClient(conn.getHost().getValue(), conn
						.getUsername().getValue(), conn.getPassword()
						.getValue());

				if (ftm.connect())
					ftm.uploadFile(in, path);
				else {
					Log.i(I, "Problem with the Connection.");
				}

				ftm.disconnect();
				success = true;
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Recursive method to build in attributes
	 * 
	 * @param type
	 * @param a
	 * @return
	 */
	private Attribute buildInAttributes(RelationShipType type, Attribute a) {

		
		// A SingleAttribute can be returned and breaks the recursion
		if (a instanceof SingleProfileAttribute)
			return a;

		// In case of an StrucuredAttribute the containing Attributes have
		// to be added recursively
		else if (a instanceof StructuredProfileAttribute) {

			StructuredProfileAttribute<Object, Attribute> oldAttribute = (StructuredProfileAttribute<Object, Attribute>) a;
			StructuredProfileAttribute<Object, Attribute> newAttribute = new StructuredProfileAttribute<Object, Attribute>(
					a.getKey());

			for (Object key : oldAttribute) {
			
				IProfileAttribute attr = ((IProfileAttribute) oldAttribute
						.getAttribute(((Attribute) key).getKey()));

				if (attr != null) {
					for (RelationShipType r : attr.getRelationShipTypes()) {
						
						if (r.getName().equals(type.getName())) {
						
							newAttribute.setAttribute(buildInAttributes(type,
									(Attribute) key));
							break;
						}
					}
				}

			}
			return newAttribute;

			// In case of an AttributeList the containing Attributes
			// have to be added recursively
		} else if (a instanceof ProfileAttributeList) {

			ProfileAttributeList<Attribute> oldAttribute = (ProfileAttributeList<Attribute>) a;
			ProfileAttributeList<Attribute> newAttribute = new ProfileAttributeList<Attribute>(
					oldAttribute.getKey());

			for (Attribute attr : oldAttribute.getAttributes()) {
				IProfileAttribute profileatt = (IProfileAttribute) attr;
				for (RelationShipType r : profileatt.getRelationShipTypes()) {

					if (r.getName().equals(type.getName())) {
						newAttribute.add(buildInAttributes(type, attr));
						break;
					}
				}
			}
			return newAttribute;

		}

		// This should nerver happen as this method is only called when at least
		// one
		// SingleAttribute is placed in the given structure that matches the
		// given
		// relationship type

		return null;
	}

	public boolean addContact(Message<RelationShipRequestBody> request,
			RelationShipType relation) {

		Contact newContact = new Contact();
		newContact.setOpenID(request.getHead().getSender());
		newContact.getRelation().add(relation);
		newContact.setPublicKeyURL(request.getHead().getPublicKeyURL());
		newContact.setFullName(request.getBody().getSubProfiles().get(0).getHCard().getFn().getValue());
		
		for(SubProfile s: request.getBody().getSubProfiles()){
			((EncryptedSubProfile) s).setParseToAccount(true);
			newContact.getProfiles().add(s);
			Log.e(I, "url from subprofile "+s.getUrl());
		}
		

		HelloWorldBasic.getAccount().getContacts()
				.add(newContact);
		HelloWorldBasic.saveAccount();

		return true;
	}
}
