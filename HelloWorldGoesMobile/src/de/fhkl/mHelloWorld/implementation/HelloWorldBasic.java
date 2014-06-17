package de.fhkl.mHelloWorld.implementation;


import de.fhkl.helloWorld.interfaces.actions.IAccountManager;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.mHelloWorld.implementation.util.NamingHelper;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class HelloWorldBasic extends Activity {
	
	private static final String I = "======================= [HELLO-WORLD] "
		+ "HelloWorldBasic" + ": ";
	
	private static final int HOME_ID = Menu.FIRST;
    private static final int CONTACTS_ID = Menu.FIRST + 1;
    private static final int MAP_ID = Menu.FIRST + 2;
    private static final int MAIL_ID = Menu.FIRST + 4;
    private static final int SETTINGS_ID = Menu.FIRST + 5;
    private static final int LOGOUT_ID = Menu.FIRST + 6;
    private static final int LOGOUT_ID1 = Menu.FIRST + 7;
    private static final int LOGOUT_ID2 = Menu.FIRST + 8;
    
    private static String HELLOWORLD_DIR = "-Path not set yet-";
    private static String USER_DIR = "-Path not set yet-";
    private static String USER_TEMP_DIR = "-Path not set yet-";
    private static String USER_TEMP_DIR_NAME = "temp";

	/**
	 * The user account. It is available after a successful login.
	 * mobile: was NOT static in Desktop-version
	 */
	//private static Account account;
	protected static Account userAccount = null;
	
	// important, if a user has first logged in logged out
	// security that the new user can not go back to data of first user
	private static boolean loggedOut = false;
	
	// will be set if a user is logged in
	private static boolean loggedIn = false;
	
	private static boolean showUseLogout = false;
	private static boolean showWelcome = false;
	
	public static boolean isShowUseLogout() {
		return showUseLogout;
	}

	public static void setShowUseLogout(boolean showUseLogout) {
		HelloWorldBasic.showUseLogout = showUseLogout;
	}

	public static boolean isShowWelcome() {
		return showWelcome;
	}

	public static void setShowWelcome(boolean showWelcome) {
		HelloWorldBasic.showWelcome = showWelcome;
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static void setLoggedIn(boolean loggedIn) {
		HelloWorldBasic.loggedIn = loggedIn;
	}

	public static boolean isLoggedOut() {
		return loggedOut;
	}

	public static void setLoggedOut(boolean loggedOut) {
		HelloWorldBasic.loggedOut = loggedOut;
	}

	/**
	 * An instance of an implementation of the IAccountManager
	 * mobile: was NOT static in Desktop-version
	 */
	private static IAccountManager accountManager;

	private static String username;

	private static String password;
	
	public static String getUsername()
	{
		return username;
	}

	public static void setUsername(String uName)
	{
		username = uName;
	}

	public static String getPassword()
	{
		return password;
	}

	public static void setPassword(String pword)
	{
		password = pword;
	}
    
  	protected static Bundle user_data;
  	
  	public static void setAccount(Account a) {
  		Log.i(I, "setting userAccount");
  		userAccount = a;
  	}
 
	public static Account getAccount() {
		if (userAccount == null)
		{
			Log.e(I, "No userAccount is set, yet.");
		}
		return userAccount;
	}

	public static IAccountManager getAccountManager() {
		return accountManager;
	}
    
	public static void saveAccount() {
		accountManager.encryptAndSaveAccount(userAccount, username, password, null);
	}
	
    @Override
    public void onCreate(Bundle bundle)
    {
    	super.onCreate(bundle);
    	//this.user_data=bundle;
    }
    
    public static void setUserdataBundle(Bundle bundle)
    {
    	
    }
    
    public static void setPath(String path)
    {
    	HELLOWORLD_DIR = path;
    }
    
    public static String getPath()
    {
    	return HELLOWORLD_DIR;
    }
    
	public static String getUserDir() {
		return USER_DIR;
	}
	
	public static String getUserTempDir() {
		return USER_TEMP_DIR;
	}

	public static void setUserTempDir(String user_dir) {
		USER_TEMP_DIR = user_dir + "/" + USER_TEMP_DIR_NAME + "/";
	}
	
	public static void setUserDir(String user_dir) {
		USER_DIR = user_dir;
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item;
        
        item = menu.add(0, HOME_ID, 0, R.string.menuitem_home);
        item.setIcon(R.drawable.home);
        
        item = menu.add(0, CONTACTS_ID, 0, R.string.menuitem_contacts);
        item.setIcon(R.drawable.contacts);
        
        item = menu.add(0, MAP_ID, 0, R.string.menuitem_map);
        item.setIcon(R.drawable.map);
        
        item = menu.add(0, SETTINGS_ID, 0, R.string.menuitem_settings);
        item.setIcon(R.drawable.settings);
        
        item = menu.add(0, MAIL_ID, 0, R.string.menuitem_mail);
        item.setIcon(R.drawable.mail);
        
        item = menu.add(0, LOGOUT_ID, 0, R.string.menuitem_logout);
        item.setIcon(R.drawable.exit);

        return true;
    }
    
    //// In andere Klasse implementieren und hier entfernen
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	Intent intent;
        switch(item.getItemId()) {
        case HOME_ID:
        	Log.i(I, "My-Profile Menu Button selected");
        	intent = new Intent(this, ShowOwnHCard.class);
        	this.startActivity(intent);
            return true;  
        case CONTACTS_ID:
        	Log.i(I, "Contacts Menu Button selected");
        	if (HelloWorldBasicList.userAccount == null)
        	{
        		HelloWorldBasicList.userAccount = HelloWorldBasic.userAccount;
        	}
        	intent = new Intent(this, Contact_Types.class);
        	this.startActivity(intent);
            return true;
        case MAP_ID:
        	Log.i(I, "Map Menu Button selected");
        	if (HelloWorldBasicList.userAccount == null)
        	{
        		HelloWorldBasicList.userAccount = HelloWorldBasic.userAccount;
        	}
        	intent = new Intent(this, ContactFinderMap.class);
        	startActivity(intent);
            return true;    
        case SETTINGS_ID:
        	Log.i(I, "Settings Menu Button selected");
        	if (HelloWorldBasicList.userAccount == null)
        	{
        		HelloWorldBasicList.userAccount = HelloWorldBasic.userAccount;
        	}
        	intent = new Intent(this, SettingsChooser.class);
        	startActivity(intent);
            //getmail()
            return true;
        case MAIL_ID:
        	Log.i(I, "Mail Menu Button selected");
        	intent = new Intent(this, MailChecker.class);
        	this.startActivity(intent);
            return true;   
        case LOGOUT_ID:
        	Log.i(I, "Logout Menu Button selected");
        	HelloWorldBasic.logout();
        	intent = new Intent(this, HelloWorldLogin.class);
        	this.startActivity(intent);
            return true; 
        }
       
        return super.onMenuItemSelected(featureId, item);
    }

    public static void logout()
    {
    	// Basic
    	HelloWorldBasic.setAccount(null);
    	HelloWorldBasic.setUsername(null);
    	HelloWorldBasic.setPassword(null);
    	HelloWorldBasic.setUserdataBundle(null);
    	HelloWorldBasic.setUserDir(null);
    	HelloWorldBasic.setUserTempDir(null);
    	HelloWorldBasic.setUser_data(null);
    	HelloWorldBasic.setAccountManager(null);
    	// BasicList
    	HelloWorldBasicList.user_data = null;
    	HelloWorldBasicList.userAccount = null;
    	// BasicMap
    	HelloWorldBasicMap.user_data = null;
    	HelloWorldBasicMap.userAccount = null;
    	
    	HelloWorldBasic.setLoggedIn(false);
    	HelloWorldBasic.setLoggedOut(true);
    	
		Log.i(I, "You have been logged out successfully!");
		// Try to exit ...
    	//try { System.exit(0); } catch (Exception e) {}
    }
    
	public static Account getUserAccount() {
		return userAccount;
	}

	public static void setUserAccount(Account userAccount) {
		HelloWorldBasic.userAccount = userAccount;
	}

	public static Bundle getUser_data() {
		return user_data;
	}

	public static void setUser_data(Bundle user_data) {
		HelloWorldBasic.user_data = user_data;
	}

	public static void setAccountManager(IAccountManager accountManager) {
		HelloWorldBasic.accountManager = accountManager;
	}
    
    

}
