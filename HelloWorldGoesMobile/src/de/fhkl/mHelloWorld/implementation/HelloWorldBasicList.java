package de.fhkl.mHelloWorld.implementation;


import de.fhkl.helloWorld.interfaces.model.account.Account;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class HelloWorldBasicList extends ListActivity 
{
	private static final String I = "======================= [HELLO-WORLD] "
		+ "HelloWorldBasicList" + ": ";
	
	private static final int HOME_ID = Menu.FIRST;
    private static final int CONTACTS_ID = Menu.FIRST + 1;
    private static final int MAP_ID = Menu.FIRST + 2;
    private static final int MAIL_ID = Menu.FIRST + 4;
    private static final int SETTINGS_ID = Menu.FIRST + 5;
    private static final int LOGOUT_ID = Menu.FIRST + 6;
    
  	protected static Bundle user_data;
  	protected static Account userAccount = null;
  	
    @Override
    public void onCreate(Bundle bundle)
    {
    	super.onCreate(bundle);
    	//this.user_data=bundle;
    }
    
    public void setUserdataBundle(Bundle bundle)
    {
    	
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
        	intent = new Intent(this, ShowOwnHCard.class);
        	this.startActivity(intent);
            return true;
        case CONTACTS_ID:
        	intent = new Intent(this, Contact_Types.class);
        	this.startActivity(intent);
            return true;
        case MAP_ID:
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
            return true;   
        case MAIL_ID:
        	Log.i(I, "Mail Menu Button selected");
        	intent = new Intent(this, MailChecker.class);
        	this.startActivity(intent);
            return true;    
        case LOGOUT_ID:
        	Log.i(I, "Logout Menu Button selected");
        	HelloWorldBasic.setUsername(null);
        	HelloWorldBasic.logout();
        	intent = new Intent(this, HelloWorldLogin.class);
        	this.startActivity(intent);
            return true; 
        }
       
        return super.onMenuItemSelected(featureId, item);
    }

}
