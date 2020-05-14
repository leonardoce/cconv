package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.components.LatLongView;
import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class InputLatLongActivity extends AppCompatActivity implements LocationListener
{
	private Button btConvertiLatLong;
	private ToggleButton btDaGPS;
	private LatLongView input;
	private EditText editLatLongDescrizione;
	
	private boolean coordinataDaGps = false;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.input_latlong);
		
		// -------------------
		// Prende i componenti
		// -------------------
		
		btConvertiLatLong = (Button) findViewById(R.id.btConvertiLatLong);
		btDaGPS = (ToggleButton) findViewById(R.id.btDaGPS);
		input = (LatLongView) findViewById(R.id.inputLongLongConvertire);
		editLatLongDescrizione = (EditText) findViewById(R.id.editLatLongDescrizione);
		
		// -------------------------------
		// Inizializzazione dei componenti
		// -------------------------------
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		btDaGPS.setEnabled(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
		
		// ------------------------
		// Ascoltatore degli eventi
		// ------------------------
		
		btConvertiLatLong.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				onConverti();
			}
		});
		
		btDaGPS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPrendiDaGPS();
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		input.reset();
		btDaGPS.setChecked(false);
		coordinataDaGps = false;
		input.requestFocus();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (coordinataDaGps)
		{
			locationManager.removeUpdates(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (coordinataDaGps)
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this);
		}
	}

	private void onPrendiDaGPS()
	{
		boolean oldStato = coordinataDaGps; 
		coordinataDaGps = btDaGPS.isChecked();
		
		if (oldStato==false && coordinataDaGps)
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this);
		}
		
		if (oldStato==true && !coordinataDaGps)
		{
			locationManager.removeUpdates(this);
		}
	}
	
	// -----------------------------
	// Questi gestiscano la location
	// -----------------------------

	@Override
	public void onLocationChanged(Location location) 
	{
		if (!coordinataDaGps)
		{
			return;
		}
		
		double lat = location.getLatitude();
		double longitude = location.getLongitude();
		
		Punto3D punto = new Punto3D(longitude, lat, 0);
		input.setPunto(punto);
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	}
	
	// -------------------------------------------
	// Gestione della conversione delle coordinate
	// -------------------------------------------
	
	private void onConverti()
	{

		if ( !input.isCorretto() ) {
		    new AlertDialog.Builder(this)
			    .setMessage(getString(R.string.msg_coordinata_non_congruente))
			    .setTitle(getString(R.string.app_name))
			    .setPositiveButton(getString(R.string.ok), null)
			    .show();
		    return;
		}


		// Proietto a Gauss Roma		
		RisultatoConversione risultato = new RisultatoConversione();
		
		Punto3D orig = input.getPunto();
		risultato.lat = orig.y;
		risultato.longi = orig.x;
		risultato.descrizione_punto = editLatLongDescrizione.getText().toString();
		
		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
			utils.conversioneInLatLongED50();
			utils.conversioneInGauss();
			utils.conversioneInCassini();
			utils.conversioneInUTM();
			utils.conversioneInMGRS();
			utils.conversioneInWebMercator();
		} catch(Exception e) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();		
			return;
		}

		// Tipo di conversione
		risultato.tipo_ingresso = "Da Latitudine e Longitudine ";
		if (coordinataDaGps)
		{
			risultato.tipo_ingresso += "(GPS)";
		}
		risultato.initPropertiesFromContext(this);
		
		// Faccio vedere il risultato
		Intent passaggioRisultato = new Intent(this, RisultatoConversioneActivity.class);
		passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE, risultato);
		startActivity(passaggioRisultato);
	}		
}
