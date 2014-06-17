package de.fhkl.mHelloWorld.implementation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ImportProfileFromNetworkChooser extends Activity implements
		Button.OnClickListener
{
	// Button
	private Button mImportButton;

	// EditText for the URL
	private EditText mUrl;
	
	private static final String I = 
		 "======================= [HELLO-WORLD] " +
		 "ImportProfileFromNetworkChooser" + ": ";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);

		Log.v(I, "test");
		setContentView(R.layout.import_profile_chooser);

		init();
	}
	
	public void init()
	{
		// declaration of the 'Import' button
		mImportButton = (Button) this.findViewById(R.id.import_profile_from_network_button);
		mImportButton.setOnClickListener(this);
		
		// pre-definition of the EditText - field
		mUrl = (EditText) this.findViewById(R.id.import_from_url);
		
		mUrl.setText("http://profile.myspace.com/index.cfm?fuseaction=user.viewprofile&friendid=431545369");
	}

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		
		Intent intent;
		
		if ( view.getId() == R.id.import_profile_from_network_button)
		{
			String url_txt = mUrl.getText().toString();
			intent = new Intent(this, ImportProfileFromNetwork.class);
			intent.putExtra("url", url_txt);
			startActivity(intent);			
		}

	}

}
