package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.WebMercatorToLatLong;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.utils.StringUtils;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InputWebMercatorActivity extends AppCompatActivity
{
    private EditText editX;
	private EditText editY;
	private EditText editDescrizione;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    	setContentView(R.layout.input_webmercator);
        Button buttonConverti = (Button) findViewById(R.id.buttonConverti);
    	buttonConverti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonConverti();
            }
        });
    	
        editX = (EditText)findViewById(R.id.editTextX);
        editY = (EditText)findViewById(R.id.editTextY);
        editDescrizione = (EditText)findViewById(R.id.editDescrizione);
        
        editX.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editY.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	
    	editX.setText("");
    	editY.setText("");
    }

    private void onButtonConverti()
    {
		String stringX = editX.getText().toString();
		String stringY = editY.getText().toString();
		
		double x = StringUtils.string2real(stringX);
		double y = StringUtils.string2real(stringY);
		
		if (x==0 || y==0)
		{
			new AlertDialog.Builder(this)
				.setMessage(getString(R.string.msg_coordinata_modo_sbagliato))
				.setTitle(getString(R.string.app_name))
				.setPositiveButton(getString(R.string.ok), null)
				.show();		
			return;
		}
    	
		// Converto!
		
		ICoordinateConversion proiezione = new WebMercatorToLatLong();
		Punto3D origine = new Punto3D(x, y,	0);
		
		Punto3D destinazione;
		
		try
		{
			destinazione = proiezione.convert( origine );
		}
		catch (Exception exc)
		{
			new AlertDialog.Builder(this)
			.setMessage(getString(R.string.msg_coordinata_non_congruente))
			.setTitle(getString(R.string.app_name))
			.setPositiveButton(getString(R.string.ok), null)
			.show();		
			return;
		}
		
		RisultatoConversione risultato = new RisultatoConversione();
		
		risultato.webmercator_x = x;
		risultato.webmercator_y = y;
		risultato.lat = destinazione.y;
		risultato.longi = destinazione.x;
		risultato.descrizione_punto = editDescrizione.getText().toString();
		risultato.tipo_ingresso = "Da Web Mercator";
		risultato.initPropertiesFromContext(this);
		
		try {
			ConversioniUtils utils = new ConversioniUtils(this, risultato);
			utils.conversioneInLatLongED50();
			utils.conversioneInGauss();
			utils.conversioneInUTM();
			utils.conversioneInMGRS();
			utils.conversioneInCassini();
		} catch(Exception e) {
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
