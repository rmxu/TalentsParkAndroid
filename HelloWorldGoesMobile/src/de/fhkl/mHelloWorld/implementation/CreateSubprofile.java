package de.fhkl.mHelloWorld.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.helloWorld.implementation.model.security.CryptoAES256;
import de.fhkl.helloWorld.interfaces.model.account.configuration.FileTransferConnection;
import de.fhkl.helloWorld.interfaces.model.account.hCard.Address;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.key.KeyWrapper;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.Profile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import de.fhkl.mHelloWorld.implementation.util.ExportAccount;
import de.fhkl.mHelloWorld.implementation.util.NamingHelper;
import de.fhkl.mHelloWorld.implementation.util.ParseHelper;
import de.fhkl.mHelloWorld.implementation.util.StringUtil;

public class CreateSubprofile extends HelloWorldBasicList implements
		Button.OnClickListener {

	private static final String I = "+++++++++++++++++++++++ HELLO-WORLD_CreateSubprofile: ";
	private ArrayList<HashMap<String, String>> accList;
	private CheckBoxAdapter mCheckBoxAdapter;
	private static EditText sGroupName;
	// private static EditText sUrlEdit;
	private String ftpPath;
	private static Boolean sEditingAccount = false;
	private static String subprofileUrl;
	private static EncryptedSubProfile encSubProfile;
	private static Integer groupIndex = null;
	private static String groupName = null;


	// @Override
	public void onCreate(Bundle bundle) {
		Log.i(I, "start CreateSubprofile");
		super.onCreate(bundle);
		setContentView(R.layout.new_subprofile);

		Bundle userdatabundle = getIntent().getExtras();
		
		Log.i(I,"Request-Tag: "+userdatabundle.getInt("request"));
		
		if(userdatabundle.getInt("request") == 1)
		{
			groupIndex = userdatabundle.getInt("groupIndex");
			groupName = userdatabundle.getString("groupName");
		}

		Button button = (Button) findViewById(R.id.contacttypes_button_save);
		button.setOnClickListener(this);

		sGroupName = (EditText) findViewById(R.id.contacttypes_field_new);

		// get the hcard data from the account
		Helper listHelper = new Helper();
		accList = listHelper.getHCardData(userAccount.getPrivateProfile()
				.getHCard());

		ArrayList<HashMap<String, String>> subList = new ArrayList<HashMap<String, String>>();

		if (groupIndex != null) {

			sEditingAccount = true;
			sGroupName.setText(groupName);

			encSubProfile = (EncryptedSubProfile) userAccount
					.getSubProfiles().get(groupIndex);
			Log.i(I, "Url zum SubProfile" + encSubProfile.getUrl());
			
			subprofileUrl = encSubProfile.getUrl();

			ProfileRequester requester = new ProfileRequester(encSubProfile);
			try {
				encSubProfile = requester.call();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			HCard subCard = encSubProfile.getHCard();

			// get the HCard-data from the Subprofile
			subList = Helper.getHCardData(subCard);
		}

		String[] keys = new String[accList.size()];
		CheckBoxAdapter.mCheckStates = new boolean[accList.size()];
		int i = 0;
		int j = 0;
		for (HashMap<String, String> item : accList) {
			// get the keys from the list, which later are the labels on the
			// ListView
			keys[i] = item.get("line1");
			Log.i(I, i + ". Key on accList: " + keys[i]);
			// check if the keys matches the downloaded subprofile hcard-fields.
			// If the key-field matches the subprofile hcard-field, then the
			// subprofile hcard-field is set on the downloaded subprofile.
			// only check if the groupIndex =! null. Otherwise the request came
			// for creating Subprofile and this part is useless
			if (j < subList.size() && sEditingAccount) {
				Log
						.i(I, j + ". Key on subList: "
								+ subList.get(j).get("line1"));
				if (subList.get(j).get("line1").equals(keys[i])) {
					Log.i(I, "Set mCheckStates " + i + " true");
					CheckBoxAdapter.mCheckStates[i] = true;
					j++;
				}
			}
			i++;
		}

		// create the adapter...
		mCheckBoxAdapter = new CheckBoxAdapter(this, R.layout.subprofile_item,
				keys);
		// ... and map the keys to the ListView
		setListAdapter(mCheckBoxAdapter);
	}

	// override the ArrayAdater to track the selection of the CheckBoxes
	private static class CheckBoxAdapter extends ArrayAdapter<String> implements
			CompoundButton.OnCheckedChangeListener {

		// everytime the user touch on a CheckBox the state of this CheckBox is
		// tracked to this boolean array
		private static boolean[] mCheckStates;

		public CheckBoxAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			// mCheckStates = new boolean[objects.length];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CheckBox view = (CheckBox) super.getView(position,
					convertView, parent);
			view.setTag(position);

			Log.i(I, "mCheckStates on postition" + position + " "
					+ this.getValue(position));

			view.setChecked(this.getValue(position));
			view.setOnCheckedChangeListener(this);
			view.setOnFocusChangeListener(null);
			return view;
		}

		private boolean getValue(int position) {
			return mCheckStates[position];
		}

		public int getLength() {
			return mCheckStates.length;
		}

		public void setChecked(int position, boolean isChecked) {
			mCheckStates[position] = isChecked;
			notifyDataSetChanged();
		}

		public void toggle(int position) {
			setChecked(position, !getValue(position));
		}

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			mCheckStates[position] = isChecked;
		}
	}

	private void saveSubprofile(View v) {

//		if(this.sEditingAccount)
//		{
//			userAccount.getRelationShipTypes().get(groupIndex).setName(sGroupName.getText().toString());
//		}
//		else
//		{
			// ... create the RelationShipType ...
			RelationShipType r = new RelationShipType();
			// ... with the name the user typed in in the field "Group name:"
			r.setName(sGroupName.getText().toString());
			// Subprofile which will be filled with from user checked data and
			// uploaded to the Url the user typed in
			encSubProfile = new EncryptedSubProfile();
//		}
		
			

			// Subprofile for save to the account.xml, contains the Url the user
			// typed in and the RelationShipType
			EncryptedSubProfile enc_sp;
			AccountManager am = new AccountManager();
			String fileName;
			if(this.sEditingAccount)
			{
				enc_sp = (EncryptedSubProfile) userAccount
				.getSubProfiles().get(groupIndex);
				enc_sp.setUrl(subprofileUrl);
				fileName = NamingHelper.makeSubProfileFileName(groupName);
				
				// set the group name the user typed in as RelationShipType
				encSubProfile.setRelation(r);
				
				// set the browser-reachable (http) path to the SubProfile
				encSubProfile.setUrl(userAccount.getConfiguration().getFileTransferConncetions()
						.get(0).getHttpUrl().getValue()
						+ "/" + fileName);				
				
				// set KeyWrapper to decrypt the SubProfile which will be uploaded
				SecretKey key =(SecretKey)enc_sp.getKeyWrapper().getSecretKey();
				KeyWrapper wrapper = new KeyWrapper();
				Date milli = new Date(System.currentTimeMillis());
				wrapper.setTimestamp(milli);
				wrapper.setSecretKey(key);
				wrapper.setAlgorithm(key.getAlgorithm());
				encSubProfile.setKeyWrapper(wrapper);
			}
			else
			{
			// Get the Groupname the user typed in, ....
			String typeName = ((EditText) findViewById(R.id.contacttypes_field_new))
					.getText().toString();
			// 
			userAccount.getRelationShipTypes().add(r);
			enc_sp = am.createNewSubProfile(r, userAccount);
			// set the subdirectory and the filename of the Subprofile, concatenate
			// of the group name the user typed in and the username
			String subName = sGroupName.getText().toString();
			fileName = NamingHelper.makeSubProfileFileName(subName);// "sp_"
			// make it valid
			fileName = StringUtil.makeUrlValid(fileName);
			// save the url to the user account. this is a browser-reachable url of
			// the subprofil .xml
			enc_sp.setUrl(userAccount.getConfiguration()
					.getFileTransferConncetions().get(0).getHttpUrl().getValue()
					+ "/" + fileName);

			Log.i(I, "" + HelloWorldBasic.getUsername() + " "
					+ HelloWorldBasic.getPassword());

			
			// set the group name the user typed in as RelationShipType
			encSubProfile.setRelation(r);
			
			// set the browser-reachable (http) path to the SubProfile
			encSubProfile.setUrl(userAccount.getConfiguration().getFileTransferConncetions()
					.get(0).getHttpUrl().getValue()
					+ "/" + fileName);

			
			
			// set KeyWrapper to decrypt the SubProfile which will be uploaded
			SecretKey key =(SecretKey)enc_sp.getKeyWrapper().getSecretKey();
			KeyWrapper wrapper = new KeyWrapper();
			Date milli = new Date(System.currentTimeMillis());
			wrapper.setTimestamp(milli);
			wrapper.setSecretKey(key);
			wrapper.setAlgorithm(key.getAlgorithm());
			encSubProfile.setKeyWrapper(wrapper);
			}
			
		// pro is used to map between SubProfiles saved to the account and
		// picked HCard data
		Profile pro = userAccount.getPrivateProfile();
		HCard card = encSubProfile.getHCard();
		// maps the data which the user checked from the account-hcard to the
		// subprofile-hcard
		for (int i = 0; i < mCheckBoxAdapter.getLength(); i++) {
			if (mCheckBoxAdapter.getValue(i) == true) {
				Log.i(I, "checked: " + accList.get(i).get("line1"));

				if (accList.get(i).get("line1").equals("Photo")) {
					Log.i(I, "If Photo");
					Log.i(I, "If Photo test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getPhoto().get(0).getValue());
					card.addPhoto(
							userAccount.getPrivateProfile().getHCard()
									.getPhoto().get(0).getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getPhoto().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Fullname")) {
					Log.i(I, "If Fullname");
					Log.i(I, "If Fullname test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getFn().getValue());
					card.getFn().setValue(
							userAccount.getPrivateProfile().getHCard().getFn()
									.getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getFn().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Birthday")) {
					Log.i(I, "If Birthday");
					Log.i(I, "If Birthday test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getBday().getValue());
					card.getBday().setValue(
							userAccount.getPrivateProfile().getHCard()
									.getBday().getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getBday().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Nickname")) {
					Log.i(I, "If Nickname");
					Log.i(I, "If Nickname test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getNickname().get(0).getValue());

					SingleProfileAttribute<String> nName = new SingleProfileAttribute<String>(
							"nickname");
					nName.setValue(userAccount.getPrivateProfile().getHCard()
							.getNickname().get(0).getValue());
					card.addNickname(nName);
					if(!this.sEditingAccount)
					{
					pro.getHCard().getNickname().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Email")) {
					Log.i(I, "If Email");
					Log.i(I, "If Email test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getEmail().get(0).getValue());
					card.addEmail(
							userAccount.getPrivateProfile().getHCard()
									.getEmail().get(0).getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getEmail().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Tel")) {
					Log.i(I, "If Tel");
					Log.i(I, "If Tel test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getTel().get(0).getValue());

					SingleProfileAttribute<String> tel = new SingleProfileAttribute<String>(
							"tel");
					tel.setValue(userAccount.getPrivateProfile().getHCard()
							.getTel().get(0).getValue());
					card.addTel(tel);
					if(!this.sEditingAccount)
					{
					pro.getHCard().getTel().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Url")) {
					Log.i(I, "If Url");
					Log.i(I, "If Url test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getUrl().get(0).getValue());
					card.addUrl(
							userAccount.getPrivateProfile().getHCard().getUrl()
									.get(0).getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getUrl().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Adr")) {
					Log.i(I, "If Adr");
					Log.i(I, "If Adr test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getLocality().getValue());
					Address adr = new Address();
					adr.setLocality(userAccount.getPrivateProfile().getHCard()
							.getAdr().get(0).getLocality().getValue());
					card.addAdr(adr);
					if(!this.sEditingAccount)
					{
					pro.getHCard().getAdr().getRelationShipTypes().add(r);
					}
				}

				if (accList.get(i).get("line1").equals("Country")) {
					Log.i(I, "If Country");
					Log.i(I, "If Country test:"
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getCountryName()
									.getValue());
					if(!this.sEditingAccount)
					{
					pro.getHCard().getAdr().getRelationShipTypes().add(r);
					}
					if (card.getAdr() != null) {
						Address adr = new Address();
						adr.setCountryName(userAccount.getPrivateProfile()
								.getHCard().getAdr().get(0).getCountryName()
								.getValue());
						card.addAdr(adr);
					} else {
						card.getAdr().get(0).getCountryName()
								.setValue(
										userAccount.getPrivateProfile()
												.getHCard().getAdr().get(0)
												.getCountryName().getValue());
					}

				}
			}
		}
		
		userAccount.setPrivateProfile(pro);

		// save account to disk
		am.encryptAndSaveAccount(userAccount, HelloWorldBasic.getUsername(),
				HelloWorldBasic.getPassword(), null);
		
		// start testing, upload the SubProfile decrypted

		 AttributeList<SubProfile> sbs = new AttributeList<SubProfile>(
		 "subProfiles");
				
		 sbs.add(enc_sp);
				
		 InputStream in = ParseHelper.subProfileToXmlToInputStream(sbs);
		 ExportAccount.uploadSubprofile(in,
		 "_acc_"+sGroupName.getText().toString());
				
		 sbs = null;
		 sbs = new AttributeList<SubProfile>(
		 "subProfiles");
				
		 sbs.add(encSubProfile);
		 in= null;
		 in = ParseHelper.subProfileToXmlToInputStream(sbs);
		 ExportAccount.uploadSubprofile(in, sGroupName.getText().toString());
		// end testing

		// upload the subprofile
		uploadSubprofile(encSubProfile, fileName);
	}

	private void uploadSubprofile(SubProfile sp, String path) {

		try {
			FileTransferConnection conn = HelloWorldBasic.getAccount()
					.getConfiguration().getFileTransferConncetions().get(0);
			Log.i(I, "Try to upload File: " + path);
			new AccountManager().uploadSubprofile(sp, conn, path);
			Toast.makeText(
					CreateSubprofile.this,
					"Group " + CreateSubprofile.sGroupName.getText().toString()
							+ " successfully uploaded to " + path + "!",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(
					CreateSubprofile.this,
					"Group " + CreateSubprofile.sGroupName.getText().toString()
							+ " ERROR while uploading to " + path + "!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.i(I, "onListItemClick ID:" + id);
		mCheckBoxAdapter.toggle(position);
	}

	@Override
	public void onClick(View v) {
		Log.i(I, "clicked on save");
		// check if the user entered a group name and a Url
		// if not, toast error to screen
		if (!(CreateSubprofile.sGroupName.getText().toString().equals(""))) {
			saveSubprofile(v);
			// } else if (sUrlEdit.getText().toString().equals(ftpPath)) {
			// Toast.makeText(CreateSubprofile.this,
			// "Please enter the Url-Path (Browser reachable Url)",
			// Toast.LENGTH_LONG).show();
		} else if (sGroupName.getText().toString().equals("")) {
			Toast.makeText(CreateSubprofile.this, "Please enter group name",
					Toast.LENGTH_LONG).show();
		}
	}
}