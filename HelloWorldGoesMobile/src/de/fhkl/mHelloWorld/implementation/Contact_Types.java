package de.fhkl.mHelloWorld.implementation;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class Contact_Types extends HelloWorldBasicList implements Button.OnClickListener{
	
	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_ContactTypes: ";
	private Cursor mNotesCursor;
	private ArrayList<String> mGroupNames = new ArrayList<String>();
	//@Override
	public void onCreate(Bundle bundle)
	{
		Log.i(I, "start Contact_Types-Screen");
		super.onCreate(bundle);
		setContentView(R.layout.contact_types);
		
		//Bundle userdatabundle = getIntent().getExtras();
		//user_data = userdatabundle;
		
		Button button = (Button) findViewById(R.id.contacttypes_button_new);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.contacttypes_button_gallery);
        button.setOnClickListener(this);
/*
		final Button button = (Button) findViewById(R.id.contacttypes_button_new);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Log.i(I,"clicked on button:");
    			Intent intent = new Intent(new Contact_Types(), CreateSubprofile.class);
    			startActivity(intent);
            }
        });*/

		
		listRelationShipTypes();
	}

	private void listRelationShipTypes() {
		// TODO Auto-generated method stub
		
		//ProfileManager pm = new ProfileManager();
		//Account a = pm.getAccount(user_data, null);

		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item = new HashMap<String,String>();
		
		int i = 0; // for mGroupNames
		for (RelationShipType r : userAccount.getRelationShipTypes())
		{
			//Log.i(I, "get Data from r: " + r.getName());
			//Log.i(I, "String index: " + i);
			item.put( "line1",r.getName());
			mGroupNames.add("" + r.getName());
			list.add(item);
			item = null;
			item = new HashMap<String,String>();
			i++;
		}
		
		// Now create a simple cursor adapter and set it to display
		// String[]: Create an array to specify the fields we want to display in the list (only line1)
		// and an array of the fields we want to bind those fields to (in this case just text1)
		SimpleAdapter notes = new SimpleAdapter( 
				this, 
				list,
				R.layout.contact_type,
				new String[] { "line1"},
				new int[] { R.id.text1}  );
       setListAdapter( notes );
		
/*		// and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> item = new HashMap<String,String>();
        
        String test;
        int i =0;
        
		for (RelationShipType r : a.getRelationShipTypes())
		{
			Log.i("*_*_*_*_*Rype*_*_*_*_*", r.getName());
			
			test = r.getName();
			
			//item.
			//from[0] = r.getKey();
			
			item.put("type"+i,r.getName());
			
			list.add(item);
			
			Log.i("*_*_*_*_*Rype-After*_*_*_*_*", list.toString());
			
			i++;
		}
		
		
		
		// Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[i];
		
        for(int j=0;j<i;j++)
        {
        	from[j]="type"+j;
        }
        
		SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.contact,from,to);
		
		//Cursor mNotesCursor = null;		
		//mNotesCursor = managedQuery(People.CONTENT_URI, PROJECTION, null, null);
		//startManagingCursor(mNotesCursor);
          
        // Now create a simple cursor adapter and set it to display
        //SimpleCursorAdapter notes = 
        	//    new SimpleCursorAdapter(this, R.layout.contact, mNotesCursor, from, to);
        setListAdapter(adapter);
		//list.add(r.getName());
*/
	}
	
/*    public void onClick(View v) {
        
    	Log.i(I,"clicked on new:");
    	
    	switch (v.getId()) {
		case R.id.contacttypes_button_new:
			
			Log.i(I,"clicked on button:");
			Intent intent = new Intent(this, CreateSubprofile.class);
			startActivity(intent);
			
			break;
    	}
    }*/

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.i(I,"onListItemClick ID:"+id);

        Intent i = new Intent(this, Contacts.class);
        i.putExtra("row", id);
        i.putExtra("groupName", mGroupNames.get((int)id));
       /* i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));*/
        startActivity(i);
    }

	@Override
	public void onClick(View v) {
    	
		Intent intent;
		
    	switch (v.getId()) {
		case R.id.contacttypes_button_new:
			
			Log.i(I,"clicked on button: new:");
			intent = new Intent(this, CreateSubprofile.class);
			int request = 0;
			intent.putExtra("request", request);
			startActivity(intent);			
			break;
		
		case R.id.contacttypes_button_gallery:
			Log.i(I,"clicked on button: gallery:");
			intent = new Intent(this, Contacts_Gallery.class);
			startActivity(intent);			
			break;
    	}
	}
}
