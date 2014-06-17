package de.fhkl.mHelloWorld.implementation;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.swing.JTree;
//import javax.swing.tree.DefaultMutableTreeNode;

//import org.jdesktop.application.Application;

//import de.fhkl.helloWorld.implementation.application.HelloWorldApp;
import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
//import de.fhkl.helloWorld.testGui.contacts.ContactTreeCellenderer;
//import de.fhkl.helloWorld.testGui.contacts.ContactPane.UpdateContacts;
//import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import de.fhkl.mHelloWorld.implementation.util.AccountSettingsChecker;
import de.fhkl.mHelloWorld.implementation.util.ExportAccount;
import de.fhkl.mHelloWorld.implementation.util.NamingHelper;

public class SettingsChooser extends HelloWorldBasic implements Button.OnClickListener{
	
	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_SettingsChooser: ";
	private Cursor mNotesCursor;
	protected static boolean sIsCompleteConfigured = false;
	
	// if saving was successful
	private static String sSuccessMessage = "";
	private static String getSuccessMessage() {
		return sSuccessMessage;
	}
	public static void setSuccessMessage(String successMessage) {
		sSuccessMessage = successMessage;
	}

	//@Override
	public void onCreate(Bundle bundle)
	{
		Log.i(I, "start SettingsChooser");
		super.onCreate(bundle);
		setContentView(R.layout.settings_chooser);
		
		//Bundle userdatabundle = getIntent().getExtras();
		//user_data = userdatabundle;
		
		Button button = (Button) findViewById(R.id.setchooser_button_incoming);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.setchooser_button_outgoing);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.setchooser_button_ftp);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.setchooser_button_pkey);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.setchooser_button_export);
        button.setOnClickListener(this);
        
        
//        if(!Helper.settingsSet())
//        {
//        	Toast.makeText(this,
//					"Please setup your ftp and mail settings",
//					Toast.LENGTH_LONG).show();
//        }
	}
	
	public void onResume()
	{
		super.onResume();
		
		Log.i(I, "--------------------------------------------------------- ");
		Log.i(I, "SettingsChooser resumed.");
		Log.i(I, "--------------------------------------------------------- ");
		
		sIsCompleteConfigured = false;
		
		if (getSuccessMessage() != "")
		{
			Toast.makeText(this, getSuccessMessage() + " settings succesfully saved!",
					Toast.LENGTH_SHORT).show();
			setSuccessMessage("");
		}
		
		AccountSettingsChecker checker = new AccountSettingsChecker();
		
        if (checker.isCheckStatus() == false)
        {
        	int emptyFields = checker.getEmptyFieldsNumber();
        	int emptyConfigs = checker.getEmptyConfigNumber();
        	boolean exception = checker.isFieldException();
        	if (emptyFields > 0 || emptyConfigs > 0)
        	{
            	Toast.makeText(this,
    					"Please fill out all Settings!\nWe detected:\n\t* "
            			+ emptyConfigs + " empty Configuration(s)\n\t* "
            			+ emptyFields + " empty Field(s)",
    					Toast.LENGTH_LONG).show();
        	}
        	else if (exception == true)
        	{
            	Toast.makeText(this,
    					"Please fill out all Settings!\nThere are still empty Fields in: \n"
            			+ checker.getExceptionCollector(), 
    					Toast.LENGTH_LONG).show();
        	}
        	else if (checker.isEmptyPublicKeyUrl() == true)
        	{
            	Toast.makeText(this,
    					"Please upload your PublicKey!", 
    					Toast.LENGTH_LONG).show();
        	}
        }
        else
        {
        	sIsCompleteConfigured = true;
        }
	}

	@Override
	public void onClick(View v) {
    	
		Intent intent = null;
		Bundle bundle = null;
		
    	switch (v.getId()) {
		case R.id.setchooser_button_incoming:
			
			Log.i(I,"clicked on button: incoming:");
			intent = new Intent(this, Settings.class);
			intent.putExtra("chooser",1);
			startActivity(intent);
			break;
		
		case R.id.setchooser_button_outgoing:
			Log.i(I,"clicked on button: outgoing:");
			intent = new Intent(this, Settings.class);
			intent.putExtra("chooser",2);
			startActivity(intent);
			break;
			
		case R.id.setchooser_button_ftp:
			Log.i(I,"clicked on button: ftp:");
			intent = new Intent(this, Settings.class);
			intent.putExtra("chooser",3);
			startActivity(intent);
			break;
			
		case R.id.setchooser_button_pkey:
			Log.i(I,"clicked on button: ftp:");
			de.fhkl.helloWorld.interfaces.model.account.key.PublicKey pKey = userAccount.getKeyPair().getPublicKey();
//			de.fhkl.helloWorld.interfaces.model.account.key.PublicKey hwPublicKey = userAccount.getKeyPair().getPublicKey();
			
			if(ExportAccount.uploadPublicKey(pKey))
			{
				Toast.makeText(SettingsChooser.this,
						"Upload Public-Key successful! (Filename: " 
						+ NamingHelper.makePublicKeyFileName() + ")", Toast.LENGTH_LONG)
						.show();
			}
			else
			{
				Toast.makeText(SettingsChooser.this,
						"Error while uploading Public-Key.\nCheck your FTP Settings!",
						Toast.LENGTH_SHORT)
						.show();
			}
			// if all Settings are made, be sure you got the normal menu 
			// not only "logout"
			this.onResume();
			break; // muharr
		case R.id.setchooser_button_export:
			if(ExportAccount.export()){
				Toast.makeText(SettingsChooser.this,
						"Upload Account successful! (Filename: " 
						+ NamingHelper.makeExportAccountFileName() + ")", Toast.LENGTH_LONG)
						.show();
			}
			else 
				Toast.makeText(SettingsChooser.this,
						"Error while uploading your Account.\nCheck your FTP Settings!",
						Toast.LENGTH_SHORT)
						.show();

			// if all Settings are made, be sure you got the normal menu 
			// not only "logout"
			this.onResume();

			break;
    	}
	}
	
	
	// if still empty setting fields - menu: ONLY logout possible!!
	// in other cases there will errors occur
	private static final int HOME_ID = Menu.FIRST;
    private static final int CONTACTS_ID = Menu.FIRST + 1;
    private static final int MAP_ID = Menu.FIRST + 2;
    private static final int MAIL_ID = Menu.FIRST + 4;
    private static final int SETTINGS_ID = Menu.FIRST + 5;
    private static final int LOGOUT_ID = Menu.FIRST + 6;
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        
        if (sIsCompleteConfigured != false)
        {
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
        }

        item = menu.add(0, LOGOUT_ID, 0, R.string.menuitem_logout);
        item.setIcon(R.drawable.exit);

        return true;
    }
}
