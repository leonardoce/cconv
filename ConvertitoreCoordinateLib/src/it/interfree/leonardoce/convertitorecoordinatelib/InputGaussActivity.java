package it.interfree.leonardoce.convertitorecoordinatelib;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import it.interfree.leonardoce.iconv.appconvs.GaussRomaToLatLongOrigine;
import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.utils.StringUtils;

public class InputGaussActivity extends AppCompatActivity
{
	private EditText editTextNord;
	private EditText editTextEst;
	private EditText editGaussDescrizione;
	private Button buttonConverti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.input_gauss);
		editTextNord = (EditText)findViewById(R.id.editTextNord);
		editTextEst = (EditText)findViewById(R.id.editTextEst);
		editGaussDescrizione = (EditText)findViewById(R.id.editGaussDescrizione);
		buttonConverti = (Button)findViewById(R.id.buttonConverti);
		
		editTextNord.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editTextEst.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		buttonConverti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onButtonConverti();
			}
		});
	}
	
    @Override
    public void onStart()
    {
    	super.onStart();
    	editTextEst.setText("");
    	editTextNord.setText("");
    }
	
	private void onButtonConverti()
	{
		String editNord = editTextNord.getText().toString();
		String editEst = editTextEst.getText().toString();
		
		double nord = StringUtils.string2real(editNord);
		double est = StringUtils.string2real(editEst);
		
		if (nord==0 || est==0)
		{
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_modo_sbagliato))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();		
			return;
		}
		
		RisultatoConversione risultato = new RisultatoConversione();
		risultato.nord = nord;
		risultato.est = est;
		risultato.descrizione_punto = editGaussDescrizione.getText().toString();
		risultato.initPropertiesFromContext(this);
		risultato.tipo_ingresso = "Da Gauss/Boaga";
		
		Punto3D inseritoDaUtente = new Punto3D(est, nord, 0);
		Punto3D latlong;
		
		// Prima converto in latlong
		// -------------------------
		
		ICoordinateConversion toLatLong = new GaussRomaToLatLongOrigine(this);
		try
		{
			latlong = toLatLong.convert(inseritoDaUtente);
		} catch(ConversionException exc)
		{
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_non_congruente))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();		
			return;
		}
		
		risultato.lat = latlong.y;
		risultato.longi = latlong.x;

		// Qua innestare tutte le altre conversioni
		// ----------------------------------------
		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
			utils.conversioneInLatLongED50();
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
