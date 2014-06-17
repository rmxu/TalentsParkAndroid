package de.fhkl.mHelloWorld.implementation;

import java.util.ArrayList;
import org.w3c.dom.Element;
import java.util.HashMap;

import de.fhkl.helloWorld.interfaces.model.account.Account;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Contacts extends HelloWorldBasicList implements Button.OnClickListener{
	
		
		private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_Contacts: ";
		private int mGroupIndex;
		private String mGroupName;
		
		//@Override
		public void onCreate(Bundle bundle)
		{
			Log.i(I, "start Contacts-Screen");
			super.onCreate(bundle);
			setContentView(R.layout.contacts);
			
			Bundle userdatabundle = getIntent().getExtras();
			long row_id = userdatabundle.getLong("row");
			mGroupIndex = (int) row_id;
			mGroupName = userdatabundle.getString("groupName");
			Log.i(I, "Contact-Id (row_id of the last screen): " + row_id);
			Log.i(I, "GroupName (row_id of the last screen): " + mGroupName);
			listContacts(row_id);
			
			Button button = (Button) findViewById(R.id.contacts_button_invite);
			button.setOnClickListener(this);
			button = (Button) findViewById(R.id.contacts_button_editgroup);
			button.setOnClickListener(this);
			TextView title = (TextView) findViewById(R.id.contacts_groupname);
			title.setText("Contacts: " + mGroupName);
		}

		private void listContacts(long row_id) {
			
			//ProfileManager pm = new ProfileManager();
			//Account a = pm.getAccount(user_data, null);
			
			Log.i(I,"Clicked Type: "+userAccount.getRelationShipTypes().get((int)row_id).getName());
			
			String typeName = userAccount.getRelationShipTypes().get((int)row_id).getName();
			String fullName;
			AttributeList<Contact> contacts = userAccount.getContacts();
			
			AttributeList<Contact> contactsFromType = null;
			
			ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> item = new HashMap<String,String>();
			
			for(Contact c: contacts)
			{
				
				//Log.i(I,"Contacts (FullName) : "+ c.getFullName());
				//Log.i(I,"Contacts (URL) : "+ c.getPublicKeyURL());
				//Log.i(I,"typeName : "+ typeName);
				//Log.i(I,"c.getType() : "+ c.getRelation().get(0).getName());
				
				if(typeName.equals(c.getRelation().get(0).getName()))
				{
					
					
					fullName = c.getFullName();
					//contactsFromType.add(c);
					HCard hCard = c.getProfiles().get(0).getHCard();
					Log.i(I,"Contacts (FullName) : "+fullName);
					
					if(fullName.equals(""))
					{
						Log.i(I,"im IF-Block");
						item.put("line1", c.getPublicKeyURL());
					}
					else
					{
						Log.i(I,"im ELSE-Block");
						item.put( "line1",fullName);
					}
					list.add(item);
					item = null;
					item = new HashMap<String,String>();
				}
			}
			
			SimpleAdapter notes = new SimpleAdapter( 
					this, 
					list,
					R.layout.contact,
					new String[] { "line1"},
					new int[] { R.id.text1}  );
	       setListAdapter( notes );
		}

	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);

	        Log.i(I,"onListItemClick ID:"+id);

	        Intent i = new Intent(this, ShowHCard.class);
	        i.putExtra("row", id);
	       /* i.putExtra(NotesDbAdapter.KEY_ROWID, id);
	        i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
	                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
	        i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
	                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));*/
	        startActivity(i);
	    }
	    
		@Override
		public void onClick(View v) {
	    	
	    	switch (v.getId()) {
			case R.id.contacts_button_invite:
				Log.i(I,"clicked on button: invite friend:");
				Intent intent = new Intent(this, InviteFriend.class);
				intent.putExtra("groupIndex", mGroupIndex);
				intent.putExtra("groupName", mGroupName);
				startActivity(intent);
				break;
	    	
		case R.id.contacts_button_editgroup:
			Log.i(I,"clicked on button: invite friend:");
			intent = new Intent(this, CreateSubprofile.class);
			int request = 1;
			intent.putExtra("request", request);
			intent.putExtra("groupIndex", mGroupIndex);
			intent.putExtra("groupName", mGroupName);
			startActivity(intent);
			break;
    	}
		}
}
