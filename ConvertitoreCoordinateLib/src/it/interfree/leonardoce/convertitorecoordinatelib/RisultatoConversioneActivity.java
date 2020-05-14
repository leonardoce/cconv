package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.utils.ResUtils;
import it.interfree.leonardoce.iconv.utils.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RisultatoConversioneActivity extends AppCompatActivity
{
    private TextView risultato_lat, risultato_long;
    private TextView risultato_lat_decimale, risultato_long_decimale;
    private TextView risultato_minuti_lat_decimale, risultato_minuti_long_decimale;
    private TextView risultato_lat_ed50, risultato_long_ed50;
    private TextView risultato_lat_decimale_ed50, risultato_long_decimale_ed50;
    private TextView risultato_minuti_lat_decimale_ed50, risultato_minuti_long_decimale_ed50;
    private TextView risultato_nord, risultato_est;
    private TextView risultato_x, risultato_y;
    private TextView risultato_webmercator_x, risultato_webmercator_y;
    private TextView risultato_utm_x, risultato_utm_y, risultato_utm_zona;
    private TextView risultato_mgrs;
    private EditText editRisultatoDescrizione;
    private Button btAltraConversione, btHome, btCancellaPunto;

    private RisultatoConversione ris;
    private boolean salvato;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.risultato_conversione);

    	risultato_lat = (TextView) findViewById(R.id.risultato_lat);
    	risultato_long = (TextView) findViewById(R.id.risultato_long);
    	risultato_nord = (TextView) findViewById(R.id.risultato_nord);
    	risultato_est = (TextView) findViewById(R.id.risultato_est);
    	risultato_x = (TextView) findViewById(R.id.risultato_x);
    	risultato_y = (TextView) findViewById(R.id.risultato_y);
    	risultato_webmercator_x = (TextView) findViewById(R.id.risultato_webmercator_x);
    	risultato_webmercator_y = (TextView) findViewById(R.id.risultato_webmercator_y);
        risultato_utm_x = (TextView) findViewById(R.id.risultato_utm_x);
        risultato_utm_y = (TextView) findViewById(R.id.risultato_utm_y);
        risultato_utm_zona = (TextView) findViewById(R.id.risultato_utm_zona);

        btAltraConversione = (Button) findViewById(R.id.btAltraConversione);
    	btHome = (Button) findViewById(R.id.btHome);
    	btCancellaPunto = (Button) findViewById(R.id.btCancellaPunto);
    	editRisultatoDescrizione = (EditText)findViewById(R.id.editRisultatoDescrizione);
    	risultato_mgrs = (TextView) findViewById(R.id.risultato_mgrs);
    	risultato_lat_decimale = (TextView) findViewById(R.id.risultato_lat_decimale);
    	risultato_long_decimale = (TextView) findViewById(R.id.risultato_long_decimale);
	
	risultato_minuti_lat_decimale = (TextView) findViewById(R.id.risultato_minuti_lat_decimale);
	risultato_minuti_long_decimale = (TextView) findViewById(R.id.risultato_minuti_long_decimale);
	risultato_minuti_lat_decimale_ed50 = (TextView) findViewById(R.id.risultato_minuti_lat_decimale_ed50);
	risultato_minuti_long_decimale_ed50 = (TextView) findViewById(R.id.risultato_minuti_long_decimale_ed50);

    	risultato_lat_ed50 = (TextView) findViewById(R.id.risultato_lat_ed50);
    	risultato_long_ed50 = (TextView) findViewById(R.id.risultato_long_ed50);
    	risultato_lat_decimale_ed50 = (TextView) findViewById(R.id.risultato_lat_decimale_ed50);
    	risultato_long_decimale_ed50 = (TextView) findViewById(R.id.risultato_long_decimale_ed50);

    	Intent i = getIntent();
    	if (i.hasExtra(ActivityGlobals.RISULTATO_CONVERSIONE)
            && i.getExtras().get(ActivityGlobals.RISULTATO_CONVERSIONE) instanceof RisultatoConversione)
            {
    		ris = (RisultatoConversione) i.getExtras().get(ActivityGlobals.RISULTATO_CONVERSIONE);
    		caricaRisultatoConversione();
            }

    	btAltraConversione.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

    	btHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    altraConversione();
                }
            });

    	btCancellaPunto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancellaQuestoRisultato();
                }
            });

    	risultato_lat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vaiGoogleMaps();
                }
            });

    	risultato_long.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vaiGoogleMaps();
                }
            });
    }

    @Override
    protected void onPause()
    {
    	super.onPause();

    	if (!salvato)
            {
    		salvato = true;
    		IOriginiStorage db = new OriginiCassiniManager(this);
    		try
                    {
    			ris.descrizione_punto = editRisultatoDescrizione.getText().toString();
    			if (ris.inserito)
                            {
    				db.updateLogConversioni(ris);
                            }
    			else
                            {
    				db.addToLogConversioni(ris);
                            }
                    }
    		finally
                    {
    			db.close();
                    }
            }
    }

    private void caricaRisultatoConversione()
    {
    	if (ris==null)
            {
    		return;
            }

    	DecimalFormat dec = new DecimalFormat("###,##0.00");
    	DecimalFormat decGradiDecimali = new DecimalFormat("###,##0.000000");

    	risultato_x.setText(dec.format(ris.x));
    	risultato_y.setText(dec.format(ris.y));
    	
    	risultato_webmercator_x.setText(dec.format(ris.webmercator_x));
    	risultato_webmercator_y.setText(dec.format(ris.webmercator_y));    	

        risultato_utm_x.setText(dec.format(ris.utm_est));
        risultato_utm_y.setText(dec.format(ris.utm_nord));
        risultato_utm_zona.setText("" + ris.zonaUtm);

        risultato_nord.setText(dec.format(ris.nord));
    	risultato_est.setText(dec.format(ris.est));

    	// Qui enuncia sia i gradi in formato sessadecimale
    	// che quelli in formato decimale

    	SpannableString span = new SpannableString(ResUtils.formatLatitude(this, ris.lat));
    	span.setSpan(new UnderlineSpan(), 0, span.length(), 0);
    	risultato_lat.setText(span);
    	risultato_lat_decimale.setText(decGradiDecimali.format(ris.lat) + " (" + GeodesicUtils.formatQTH(ris.lat, ris.longi) + ")");
    	risultato_long_decimale.setText(decGradiDecimali.format(ris.longi));
        risultato_minuti_lat_decimale.setText(ResUtils.formatMinutesLatitude(this, ris.lat));
        risultato_minuti_long_decimale.setText(ResUtils.formatMinutesLongitude(this, ris.longi));

    	span = new SpannableString(ResUtils.formatLongitude(this, ris.longi));
    	span.setSpan(new UnderlineSpan(), 0, span.length(), 0);
    	risultato_long.setText(span);

    	// Adesso metto anche i risultati ED50
    	risultato_lat_ed50.setText(ResUtils.formatLatitude(this, ris.lat_ed50));
    	risultato_long_ed50.setText(ResUtils.formatLongitude(this, ris.longi_ed50));
    	risultato_lat_decimale_ed50.setText(decGradiDecimali.format(ris.lat_ed50) + " (" + GeodesicUtils.formatQTH(ris.lat_ed50, ris.longi_ed50) + ")");
    	risultato_long_decimale_ed50.setText(decGradiDecimali.format(ris.longi_ed50));
    	risultato_minuti_lat_decimale_ed50.setText(ResUtils.formatMinutesLatitude(this, ris.lat_ed50));
    	risultato_minuti_long_decimale_ed50.setText(ResUtils.formatMinutesLongitude(this, ris.longi_ed50));

    	editRisultatoDescrizione.setText(StringUtils.null2string(ris.descrizione_punto));

    	if (ris.inserito)
            {
    		btAltraConversione.setText(getString(R.string.risultato_indietro));
            }
    	else
            {
    		btAltraConversione.setText(getString(R.string.risultato_inserisci_nuovo));
            }

    	risultato_mgrs.setText(ris.puntoMgrs);

    	salvato = false;
    }

    private void altraConversione()
    {
    	Intent i = new Intent(this, ConvertitoreCoordinateActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
    }

    private void vaiGoogleMaps()
    {
    	if (ris==null)
            {
    		return;
            }

    	try
            {
	    	Intent browserIntent = ris.vaiGoogleMaps();
	    	if (browserIntent!=null)
                    {
		    	startActivity(browserIntent);
                    }
            } catch(Exception ex)
            {
    		// Log.e(RisultatoConversioneActivity.class.getName(), "Open Maps", ex);
            }
    }

    private void cancellaQuestoRisultato()
    {
    	salvato = true;
    	IOriginiStorage db = new OriginiCassiniManager(this);
        try
            {
                if (ris.inserito)
                    {
                        db.deleteRigaLog(ris.id);
                        salvato = true;
                    }
                finish();
            }
        finally
            {
                db.close();
            }
    }

    private void condividiRisultato() {
        ReportRegistro report = new ReportRegistro(this);

        StringBuilder messaggio = new StringBuilder();
	report.aggiungiRisultatoInReportTesto(messaggio, ris);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.format(this.getString(R.string.stato_gps_sharing_title), DateFormat.getDateTimeInstance().format(new Date())));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, messaggio.toString());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.stato_gps_send)));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.result_menu_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_share) {
			condividiRisultato();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
