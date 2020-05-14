package it.interfree.leonardoce.convertitorecoordinatelib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.UTMToLatLongZona;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.StringUtils;

public class InputUtmActivity extends AppCompatActivity {

	private EditText editX;
	private EditText editY;
	private EditText editUTMDescrizione;
	private EditText editTextUtmZona;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.input_utm);
		Button buttonConverti = (Button) findViewById(R.id.buttonConverti);
		buttonConverti.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonConverti();
			}
		});

		editX = (EditText) findViewById(R.id.editTextX);
		editY = (EditText) findViewById(R.id.editTextY);
		editUTMDescrizione = (EditText) findViewById(R.id.editUtmDescrizione);
		editTextUtmZona = (EditText) findViewById(R.id.editTextUtmZona);

		editX.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
		editY.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
	}

	@Override
	public void onStart() {
		super.onStart();

		editX.setText("");
		editY.setText("");
	}

	private void onButtonConverti() {
		String stringX = editX.getText().toString();
		String stringY = editY.getText().toString();
		String stringZonaUtm = editTextUtmZona.getText().toString();

		double x = StringUtils.string2real(stringX);
		double y = StringUtils.string2real(stringY);
		int zonaUtm = (int) StringUtils.string2real(stringZonaUtm);

		if (x == 0 || y == 0 || zonaUtm == 0) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_modo_sbagliato))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		// Converto!
		ICoordinateConversion proiezione = new UTMToLatLongZona(this);
		Punto3D origine = new PuntoUTM(x, y, 0, zonaUtm);

		Punto3D destinazione = null;

		try {
			destinazione = proiezione.convert(origine);
		} catch (Exception exc) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		RisultatoConversione risultato = new RisultatoConversione();

		risultato.utm_est = x;
		risultato.utm_nord = y;
		risultato.zonaUtm = zonaUtm;
		risultato.lat = destinazione.y;
		risultato.longi = destinazione.x;
		risultato.descrizione_punto = editUTMDescrizione.getText().toString();
		risultato.initPropertiesFromContext(this);

		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
			utils.conversioneInLatLongED50();
			utils.conversioneInGauss();
			utils.conversioneInCassini();
			utils.conversioneInMGRS();
			utils.conversioneInWebMercator();
		} catch (Exception e) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}
		
		// Passaggio alla visualizzazione del risultato
		Intent passaggioRisultato = new Intent(this, RisultatoConversioneActivity.class);
		passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE, risultato);
		startActivity(passaggioRisultato);
	}
}
