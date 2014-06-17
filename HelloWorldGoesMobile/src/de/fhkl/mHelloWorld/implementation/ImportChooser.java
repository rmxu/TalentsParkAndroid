package de.fhkl.mHelloWorld.implementation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class ImportChooser extends Activity implements Button.OnClickListener {

	private Button mImportHelloWorldAccountButton;
	private Button mImportFromNetworkButton;
	
	private static final String I = "======================= [HELLO-WORLD] "
			+ "ImportChooser" + ": ";


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.import_chooser);
		init();
	}

	public void init() 
	{
		mImportHelloWorldAccountButton = (Button) this.findViewById(R.id.import_hw_button);
		mImportHelloWorldAccountButton.setOnClickListener(this);
		mImportFromNetworkButton = (Button) this.findViewById(R.id.import_network_button);
		mImportFromNetworkButton.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.import_hw_button:
			Log.v(I, "Klick auf Import HelloWorldAccount-Button registriert");
			intent = new Intent(this, ImportExistingAccount.class);
			startActivity(intent);
			break;

		case R.id.import_network_button:
			Log.v(I, "Klick auf Import Profile From Network-Button registriert");
			intent = new Intent(this, ImportProfileFromNetworkChooser.class);
			startActivity(intent);
			break;
		}
	}


}