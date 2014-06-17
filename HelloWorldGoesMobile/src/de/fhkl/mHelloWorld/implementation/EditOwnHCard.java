package de.fhkl.mHelloWorld.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;

import de.fhkl.helloWorld.implementation.actions.AccountManager;
import de.fhkl.helloWorld.interfaces.model.account.hCard.Address;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.ProfileAttributeList;
import de.fhkl.helloWorld.interfaces.model.attribute.profile.SingleProfileAttribute;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditOwnHCard extends HelloWorldBasicList implements
		Button.OnClickListener
{

	private static final String I = "======================= [HELLO-WORLD] "
			+ "EditOwnHCard" + ": ";

	// button for confirming the values
	private Button mConfirmButton;
	private Button mCancelButton;

	// Temporary ArrayList for saving the attributes which is used later in the
	// save-phase
	private static ArrayList<HashMap<String, String>> attributelist = new ArrayList<HashMap<String, String>>();

	public void onCreate(Bundle bundle)
	{
		Log.i(I, "start EditOwnHCard-Screen");
		super.onCreate(bundle);
		setContentView(R.layout.hcard_edit);

		// initializing
		init();

		// call a method to fill the text-views with the values
		// and also set the value of the temporary attributelist which is used
		// later in the save-phase
		attributelist = fillData();
	}

	public void init()
	{
		// declaration of the 'confirm' - button
		mConfirmButton = (Button) this.findViewById(R.id.hcard_button_confirm);
		mConfirmButton.setOnClickListener(this);

		// declaration of the 'cancel' - button
		mCancelButton = (Button) this.findViewById(R.id.hcard_button_cancel);
		mCancelButton.setOnClickListener(this);
	}

	public ArrayList<HashMap<String, String>> fillData()
	{
		// list
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// item
		HashMap<String, String> item = new HashMap<String, String>();

		String value = "";

		// general attributes (fullname, birthday)

		// fullname
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getFn()
					.getValue();
		} catch (Exception e)
		{
			// do nothing
			Log.i(I, "No Fullname exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Fullname");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Fullname");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// birthday
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getBday()
					.getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Bday exists!");
		}

		// value = checkValue(list, item, value, "Birthday");
		if (value == null || value.equals(""))
		{
			item.put("line1", "Birthday");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Birthday");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		// nickname attributes
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getNickname()
					.get(0).getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Nickname exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Nickname");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Nickname");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// email attributes
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getEmail()
					.get(0).getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Email exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Email");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Email");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// telephone attributes
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getTel().get(0)
					.getValue();
		} catch (Exception e)
		{
			Log.i(I, "No telephone-number exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Telephone");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Telephone");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// websites
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getUrl().get(0)
					.getValue();
		} catch (Exception e)
		{
			Log.i(I, "No URL exists!");
		}
		
		if (value == null || value.equals(""))
		{
			item.put("line1", "URL");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "URL");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// photo attribute
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getPhoto()
					.get(0).getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Photo-URL exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Photo Url");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Photo Url");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// country
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getCountryName().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Countryname exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Country");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Country");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// post-office-box
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getPostOfficeBox().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Post-office-box exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Post Office Box");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Post Office Box");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// region
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getRegion().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Region exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Region");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Region");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// locality
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getLocality().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Locality exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Locality");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Locality");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// postal-code
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getPostalCode().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Postal-code exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Postal Code");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Postal Code");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// extended address
		try
		{
			value = userAccount.getPrivateProfile().getHCard().getAdr().get(0)
					.getExtendedAddress().getValue();
		} catch (Exception e)
		{
			Log.i(I, "No Extended-Address exists!");
		}

		if (value == null || value.equals(""))
		{
			item.put("line1", "Extended Address");
			item.put("line2", "");
			list.add(item);
			item = null;
			item = new HashMap<String, String>();
			value = "";
		}

		if (!value.equals(""))
		{
			item.put("line1", "Extended Address");
			item.put("line2", value);
			list.add(item);
			item = null;
			item = new HashMap<String, String>();

			value = "";
		}

		// logo attributes (logo still missing)

		OwnSimpleAdapter notes = new OwnSimpleAdapter(this, list,
				R.layout.hcard_edit_element, new String[] { "line1", "line2" },
				new int[] { R.id.text1, R.id.text2 });
		setListAdapter(notes);
		// return
		return list;
	}

	/**
	 * Adapter for implementing a feedback if an item of the list
	 * (profile-attribute) was focused by clicking on it Every time when a
	 * profile-attribute was focused or lost focus the current attribute-value
	 * is noticed
	 */

	private static class OwnSimpleAdapter extends SimpleAdapter implements
			EditText.OnFocusChangeListener
	{
		private ArrayList<HashMap<String, String>> data;

		@SuppressWarnings("unchecked")
		public OwnSimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] objects, int[] to)
		{
			super(context, data, resource, objects, to);
			this.data = (ArrayList<HashMap<String, String>>) data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout view = (LinearLayout) super.getView(position,
					convertView, parent);

			Log.i(I, "Position of current view: " + position);
			Log.i(I, "AttributeName of view at position " + position + ": "
					+ this.data.get(position).get("line1"));
			Log.i(I, "AttributeValue of view at position " + position + ": "
					+ this.data.get(position).get("line2"));

			view.setTag(this.data.get(position).get("line1"));
			EditText edit = (EditText) view.getChildAt(1);
			edit.setOnFocusChangeListener(this);

			return view;
		}

		/**
		 * This method is responsible for the focus change if an attribute was
		 * clicked on (focus was set) or if the focus was left of an attribute
		 */

		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			// TODO Auto-generated method stub

			// getting the EditText
			EditText edit = (EditText) v;
			Log.i(I, "onFocusChange auf Feld:" + edit.getText().toString());
			Log.i(I, "hasFocus von Feld:" + hasFocus);

			// getting the parent (view) of the EditText which is a LinearLayout
			LinearLayout lin = (LinearLayout) edit.getParent();
			// we use the LinearLayout to get the TextView (attribute-name)
			// which belongs to the EditText (attribute-value)
			TextView text = (TextView) lin.getChildAt(0);
			Log.i(I, "Attributname von Feld lautet:"
					+ text.getText().toString() + "!");

			// if the focus of an EditText-field was set or reset then the
			// EditText-field is read.
			// if there was any changes about the EditText-value (if the text
			// changes) then
			// it's possible to save the change of the attribute with the method
			// applyAttribute()

			// apply attribute with (attribute-name, attribute-value)
			applyAttribute(text.getText().toString(), edit.getText().toString());
		}
	}

	@Override
	public void onClick(View view)
	{
		Intent intent;

		// if you want to confirm the configuration of the hcard
		if (view.getId() == R.id.hcard_button_confirm)
		{
			Log.i(I, "confirm-button clicked");
			Log.i(I, "confirmed the configuration of the hcard");

			// This is for the case if a change of one attribute was NOT noticed
			// Then we take the attributes which are displayed on the screen and
			// make a second notice
			// ATTENTION: Not all profile-attributes are displayed on the view
			// of the screen
			// that's why we can't access all attributes here and instead have
			// to take the helper-method of the onFocusChange()

			// listview
			ListView list = this.getListView();

			// amount of items which are displayed at the moment
			int listsize = list.getChildCount();
			Log.i(I, "There are " + listsize
					+ " items displayed on the screen!");

			// get the value of the items (attributes) which are displayed at
			// the moment
			for (int i = 0; i < listsize; i++)
			{
				// linearlayout, here you get the item of the list
				LinearLayout layout = (LinearLayout) list.getChildAt(i);

				// attributevalue
				EditText attributevalue = (EditText) layout.getChildAt(1);

				// attributename
				TextView attributename = (TextView) layout.getChildAt(0);

				Log.i(I, "attributename=" + attributename.getText().toString()
						+ " and attributevalue="
						+ attributevalue.getText().toString() + "!!!");

				// apply attribute with (attribute-name, attribute-value)
				applyAttribute(attributename.getText().toString(),
						attributevalue.getText().toString());
			}

			// now let's save the hcard
			saveAttributes(attributelist);
			Log.i(I, "saved the HCard!");

			Toast.makeText(EditOwnHCard.this,
					"Your changes was successfully saved.", Toast.LENGTH_LONG)
					.show();

			// and then start activity -> switch to other view (ShowOwnHCard)
			intent = new Intent(this, ShowOwnHCard.class);
			startActivity(intent);
		}

		// if you want to cancel the configuration of the hcard
		else if (view.getId() == R.id.hcard_button_cancel)
		{
			Log.i(I, "cancel-button clicked");
			Log.i(I, "canceled the configuration of the hcard");
			Log.i(I, "did not saved the HCard!");

			Toast.makeText(EditOwnHCard.this, "Your changes was not saved.",
					Toast.LENGTH_LONG).show();

			intent = new Intent(this, ShowOwnHCard.class);
			startActivity(intent);
		}
	}

	/**
	 * Helper method which is used to check if there are already
	 * address-attributes used. If not then we will create an instance of
	 * Address which is used to set or get the address-attributes like country,
	 * postal code, location, region, etc.
	 */

	private void checkAddressAttribute()
	{
		// needed for the address-attributes
		int address_size = userAccount.getPrivateProfile().getHCard().getAdr()
				.size();
		Log.i(I, "The size of address=" + "!");
		if (address_size == 0)
		{
			// no address-node exists -> create a new one and add it to the
			// current hcard
			Address newAddress = new Address();
			userAccount.getPrivateProfile().getHCard().getAdr().add(newAddress);
		}
	}

	/**
	 * Helper method which is responsible for noticing the change of the
	 * attributes. The attributes are stored into the temporary ArrayList
	 * attributelist which will be used in the save-phase
	 */

	private static ArrayList<HashMap<String, String>> applyAttribute(
			String attr_name, String attr_value)
	{
		int size = attributelist.size();

		for (int i = 0; i < size; i++)
		{
			Log.i(I, "For-Schleife drinn!");
			if (attr_name.equals(attributelist.get(i).get("line1").toString()))
			{
				Log.i(I, "In der if-Abfrage drinn!");
				attributelist.get(i).put("line2", attr_value);
				Log.i(I, "Test:" + attributelist.get(i).get("line2").toString()
						+ "!!!");
				break;
			}
		}

		return attributelist;
	}

	/**
	 * Method for saving the current attributes with all the values
	 */

	private void saveAttributes(ArrayList<HashMap<String, String>> list)
	{
		// creating a new AccountManager
		AccountManager am = new AccountManager();

		// check if address-attributes exists
		 checkAddressAttribute();

		// every item is checked so there is no possibility that any mistakes
		// can happen
		for (HashMap<String, String> item : list)
		{
			Log.i(I, "Checking attribute: " + item.get("line1"));

			// save fullname
			if (item.get("line1").toLowerCase().equals("fullname"))
			{
				Log.i(I, "Attribute is fullname!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getFn()
							.setValue(item.get("line2").toString());
					Log.i(I, "userAccount-fullname lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getFn().getValue().toString());
					continue;
				}
			}

			// save birthday
			if (item.get("line1").toLowerCase().equals("birthday"))
			{
				Log.i(I, "Attribute is birthday!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					// checking if attribute birthday exists in the hcard or if
					// it's completely new
					if (userAccount.getPrivateProfile().getHCard().getBday()
							.getValue().toString().equals(""))
					{
						userAccount.getPrivateProfile().getHCard().setBday(item.get("line2"));
						Log.i(I, "userAccount-birthday lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getBday().getValue().toString());
					} else
					{
						userAccount.getPrivateProfile().getHCard().getBday()
								.setValue(item.get("line2").toString());
						Log.i(I, "userAccount-birthday lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getBday().getValue().toString());
					}
					continue;
				}
			}

			// save nickname
			if (item.get("line1").toLowerCase().equals("nickname"))
			{
				Log.i(I, "Attribute is birthday!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					int size = userAccount.getPrivateProfile().getHCard()
							.getNickname().size();
					Log.i(I, item.get("line1") + "s" + " vorhanden=" + size
							+ "!");

					// checking if attribute nickname is already existing in the
					// current hcard or if it's completely new
					if (size == 0)
					{
						SingleProfileAttribute<String> newNickname = new SingleProfileAttribute<String>(
								item.get("line1"));
						newNickname.setValue(item.get("line2"));
						Log.i(I, item.get("line1") + "-key="
								+ newNickname.getKey().toString() + " and "
								+ item.get("line1") + "-value="
								+ newNickname.getValue().toString());
						userAccount.getPrivateProfile().getHCard().addNickname(
								newNickname);
						Log.i(I, "userAccount-nickname0 lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getNickname().get(0).getValue()
										.toString());

					} else
					{
						userAccount.getPrivateProfile().getHCard()
								.getNickname().get(0).setValue(
										item.get("line2"));
						Log.i(I, "userAccount-nickname lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getNickname().get(0).getValue()
										.toString());
					}
					continue;
				}
			}

			// save email
			if (item.get("line1").toLowerCase().equals("email"))
			{
				Log.i(I, "Attribute is email!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					int size = userAccount.getPrivateProfile().getHCard()
							.getEmail().size();
					Log.i(I, item.get("line1") + "s" + " vorhanden=" + size
							+ "!");

					if (size == 0)
					{
						SingleProfileAttribute<String> newEmail = new SingleProfileAttribute<String>(
								item.get("line1"));
						newEmail.setValue(item.get("line2"));
						Log.i(I, item.get("line1") + "-key="
								+ newEmail.getKey().toString() + " and "
								+ item.get("line1") + "-value="
								+ newEmail.getValue().toString());
						userAccount.getPrivateProfile().getHCard().addEmail(
								newEmail);
						Log.i(I, "userAccount-nickname0 lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getEmail().get(0).getValue()
										.toString());
					} else
					{
						userAccount.getPrivateProfile().getHCard().getEmail()
								.get(0).setValue(item.get("line2").toString());
						Log.i(I, "userAccount-email lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getEmail().get(0).getValue()
										.toString());
					}
					continue;
				}
			}

			// save telephone
			if (item.get("line1").toLowerCase().equals("telephone"))
			{
				Log.i(I, "Attribute is telephone!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					int size = userAccount.getPrivateProfile().getHCard()
							.getTel().size();
					Log.i(I, item.get("line1") + "s" + " vorhanden=" + size
							+ "!");

					if (size == 0)
					{
						SingleProfileAttribute<String> newTel = new SingleProfileAttribute<String>(
								item.get("line1"));
						newTel.setValue(item.get("line2"));
						Log.i(I, item.get("line1") + "-key="
								+ newTel.getKey().toString() + " and "
								+ item.get("line1") + "-value="
								+ newTel.getValue().toString());
						userAccount.getPrivateProfile().getHCard().addTel(
								newTel);
						Log.i(I, "userAccount-nickname0 lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getTel().get(0).getValue().toString());
					} else
					{
						userAccount.getPrivateProfile().getHCard().getTel()
								.get(0).setValue(item.get("line2").toString());
						Log.i(I, "userAccount-telephone lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getTel().get(0).getValue().toString());
					}
					continue;
				}
			}

			// save website
			if (item.get("line1").toLowerCase().equals("url"))
			{
				Log.i(I, "Attribute is url!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					int size = userAccount.getPrivateProfile().getHCard()
							.getUrl().size();
					Log.i(I, item.get("line1") + "s" + " vorhanden=" + size
							+ "!");

					if (size == 0)
					{
						SingleProfileAttribute<String> newUrl = new SingleProfileAttribute<String>(
								item.get("line1"));
						newUrl.setValue(item.get("line2"));
						Log.i(I, item.get("line1") + "-key="
								+ newUrl.getKey().toString() + " and "
								+ item.get("line1") + "-value="
								+ newUrl.getValue().toString());
						userAccount.getPrivateProfile().getHCard().addUrl(
								newUrl);
						Log.i(I, "userAccount-nickname0 lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getUrl().get(0).getValue().toString());
					} else
					{
						userAccount.getPrivateProfile().getHCard().getUrl()
								.get(0).setValue(item.get("line2").toString());
						Log.i(I, "userAccount-url lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getUrl().get(0).getValue().toString());
					}
					continue;
				}
			}

			// save photo-url
			if (item.get("line1").toLowerCase().equals("photo url"))
			{
				Log.i(I, "Attribute is photo-url!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					int size = userAccount.getPrivateProfile().getHCard()
							.getPhoto().size();
					Log.i(I, item.get("line1") + "s" + " vorhanden=" + size
							+ "!");

					if (size == 0)
					{
						SingleProfileAttribute<String> newPhoto = new SingleProfileAttribute<String>(
								"photo");
						newPhoto.setValue(item.get("line2"));
						Log.i(I, item.get("line1") + "-key="
								+ newPhoto.getKey().toString() + " and "
								+ item.get("line1") + "-value="
								+ newPhoto.getValue().toString());
						userAccount.getPrivateProfile().getHCard().addPhoto(
								newPhoto);
						Log.i(I, "userAccount-nickname0 lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getPhoto().get(0).getValue()
										.toString());
					} else
					{
						userAccount.getPrivateProfile().getHCard().getPhoto()
								.get(0).setValue(item.get("line2").toString());
						
						Log.i(I, "userAccount-photourl lautet: "
								+ userAccount.getPrivateProfile().getHCard()
										.getPhoto().get(0).getValue()
										.toString());
					}
					continue;
				}

			}

			// save country
			if (item.get("line1").toLowerCase().equals("country"))
			{
				Log.i(I, "Attribute is country!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setCountryName(item.get("line2"));
					Log.i(I, "userAccount-country lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getCountryName()
									.getValue().toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getCountryName().setValue("");
				}
				continue;
			}

			// save post office box
			if (item.get("line1").toLowerCase().equals("post office box"))
			{
				Log.i(I, "Attribute is post office box!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setPostOfficeBox(item.get("line2"));
					Log.i(I, "userAccount-Postofficebox lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getPostOfficeBox()
									.getValue().toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getPostOfficeBox().setValue("");
				}
				continue;
			}

			// save region
			if (item.get("line1").toLowerCase().equals("region"))
			{
				Log.i(I, "Attribute is region!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setRegion(item.get("line2"));
					Log.i(I, "userAccount-region lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getRegion().getValue()
									.toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getRegion().setValue("");
				}
				continue;
			}

			// save locality
			if (item.get("line1").toLowerCase().equals("locality"))
			{
				Log.i(I, "Attribute is locality!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setLocality(item.get("line2"));
					Log.i(I, "userAccount-locality lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getLocality().getValue()
									.toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getLocality().setValue("");
				}
				continue;
			}

			// save postal code
			if (item.get("line1").toLowerCase().equals("postal code"))
			{
				Log.i(I, "Attribute is postal code!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setPostalCode(item.get("line2"));
					Log.i(I, "userAccount-postalcode lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getPostalCode().getValue()
									.toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getPostalCode().setValue("");
				}
				continue;
			}

			// save extended address
			if (item.get("line1").toLowerCase().equals("extended address"))
			{
				Log.i(I, "Attribute is extended address!");

				// checking if the EditText-field is empty
				if (!item.get("line2").equals(""))
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.setExtendedAddress(item.get("line2"));
					Log.i(I, "userAccount-extended address lautet: "
							+ userAccount.getPrivateProfile().getHCard()
									.getAdr().get(0).getExtendedAddress()
									.getValue().toString());

				} else
				{
					userAccount.getPrivateProfile().getHCard().getAdr().get(0)
							.getExtendedAddress().setValue("");
				}
				continue;
			}

		}

		// at the end the account has to be encrypted and saved
		am.encryptAndSaveAccount(userAccount, HelloWorldBasic.getUsername(),
				HelloWorldBasic.getPassword(), null);
	}
}
