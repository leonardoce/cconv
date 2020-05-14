package it.interfree.leonardoce.iconv;

import it.interfree.leonardoce.convertitorecoordinatelib.ConvertitoreCoordinateActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Intent i = new Intent(this, ConvertitoreCoordinateActivity.class);
        startActivity(i);
        finish();
	}
	 
}
