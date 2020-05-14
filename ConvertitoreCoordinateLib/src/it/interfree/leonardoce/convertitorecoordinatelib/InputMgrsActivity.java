package it.interfree.leonardoce.convertitorecoordinatelib;

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
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.CostantiMGRS;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.MGRSToLatLong;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoMGRS;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.StringUtils;

public class InputMgrsActivity extends AppCompatActivity {

	private EditText editTextNord;
	private EditText editTextEst;
	private EditText editDescrizione;
	private EditText editTextZona, editTextBanda, editTextQuadrante;
	private Button buttonConverti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.input_mgrs);
		editTextNord = (EditText) findViewById(R.id.editTextMgrsNord);
		editTextEst = (EditText) findViewById(R.id.editTextMgrsEst);
		editDescrizione = (EditText) findViewById(R.id.editTextMgrsDescrizione);
		editTextZona = (EditText) findViewById(R.id.editTextMgrsZona);
		editTextBanda = (EditText) findViewById(R.id.editTextMgrsBanda);
		editTextQuadrante = (EditText) findViewById(R.id.editTextMgrsQuadrante);

		buttonConverti = (Button) findViewById(R.id.buttonConverti);

		editTextNord.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		editTextEst.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		buttonConverti.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onButtonConverti();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		editTextEst.setText("");
		editTextNord.setText("");
		editTextZona.setText("");
		editTextBanda.setText("");
		editTextQuadrante.setText("");
	}

	private void onButtonConverti() {

		String editNord = editTextNord.getText().toString();
		String editEst = editTextEst.getText().toString();

		double nord = StringUtils.string2real(editNord);
		double est = StringUtils.string2real(editEst);

		if (nord == 0 || est == 0) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_modo_sbagliato))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		int zona = (int) StringUtils.string2real(editTextZona.getText().toString());
		if (zona == 0) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_mgrs_zona_non_valida))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		char banda = (editTextBanda.getText().toString() + " ").charAt(0);
		if (StringUtils.indiceInVettore(CostantiMGRS.LETTERE_BANDA, banda) == -1) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_mgrs_banda_non_valida))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		String quadrante = (editTextQuadrante.getText().toString() + "  ").substring(0, 2);
		if (StringUtils.indiceInVettore(CostantiMGRS.LETTERE_GRIGLIA_ORIZZONTALI, quadrante.charAt(0)) == -1
			|| StringUtils.indiceInVettore(CostantiMGRS.LETTERE_GRIGLIA_ORIZZONTALI, quadrante.charAt(1)) == -1) {
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_mgrs_quadrante_no_valido))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}

		// Dati inseriti
		// -------------
		PuntoMGRS inseritoDaUtente = new PuntoMGRS();
		inseritoDaUtente.x = est;
		inseritoDaUtente.y = nord;
		inseritoDaUtente.setZonaUtm(zona);
		inseritoDaUtente.setBanda(banda);
		inseritoDaUtente.setQuadrante(quadrante);

		RisultatoConversione risultato = new RisultatoConversione();
		risultato.initPropertiesFromContext(this);
		risultato.descrizione_punto = editDescrizione.getText().toString();
		risultato.puntoMgrs = inseritoDaUtente.toString();
		risultato.tipo_ingresso = "Da MGRS";
		Punto3D latlong;

		// Prima converto in latlong
		// -------------------------
		ICoordinateConversion toLatLong = new MGRSToLatLong();
		try {
			latlong = toLatLong.convert(inseritoDaUtente);
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

		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
			utils.conversioneInLatLongED50();
			utils.conversioneInGauss();
			utils.conversioneInCassini();
			utils.conversioneInUTM();
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
