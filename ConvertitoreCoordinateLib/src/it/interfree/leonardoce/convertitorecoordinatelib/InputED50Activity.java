package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.components.LatLongView;
import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.ED50ToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InputED50Activity extends AppCompatActivity {

	private Button btConvertiLatLong;
	private LatLongView input;
	private EditText editLatLongDescrizione;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.input_ed50);

		// -------------------
		// Prende i componenti
		// -------------------
		btConvertiLatLong = (Button) findViewById(R.id.btConvertiLatLong);
		input = (LatLongView) findViewById(R.id.inputLongLongConvertire);
		editLatLongDescrizione = (EditText) findViewById(R.id.editLatLongDescrizione);

		// ------------------------
		// Ascoltatore degli eventi
		// ------------------------
		btConvertiLatLong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onConverti();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		input.reset();
		input.requestFocus();
	}

	// -------------------------------------------
	// Gestione della conversione delle coordinate
	// -------------------------------------------
	private void onConverti() {
		RisultatoConversione risultato = new RisultatoConversione();

		risultato.lat_ed50 = input.getPunto().y;
		risultato.longi_ed50 = input.getPunto().x;
		risultato.descrizione_punto = editLatLongDescrizione.getText().toString();
		risultato.tipo_ingresso = "Da Lat/Long ED50 ";
		risultato.initPropertiesFromContext(this);

		if (!input.isCorretto()) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		Punto3D orig = input.getPunto();
		Punto3D latlong;

		// Prima converto in latlong
		// -------------------------
		ICoordinateConversion toLatLong = new ED50ToLatLong();
		try {
			latlong = toLatLong.convert(orig);
		} catch (ConversionException exc) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		risultato.lat = latlong.y;
		risultato.longi = latlong.x;

		// Adesso converto a cassini
		// -------------------------
		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
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

		// Faccio vedere il risultato
		// --------------------------
		Intent passaggioRisultato = new Intent(this, RisultatoConversioneActivity.class);
		passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE, risultato);
		startActivity(passaggioRisultato);
	}
}
