package it.interfree.leonardoce.iconv.adapters;

import java.util.ArrayList;

import it.interfree.leonardoce.convertitorecoordinatelib.*;
import it.interfree.leonardoce.convertitorecoordinatelib.config.CurrentConfiguration;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipiConversioniAdapter extends BaseAdapter {
    private Context mContext;
    private Scelta[] tipiConversione;
    
    public TipiConversioniAdapter(Context c) {
        mContext = c;
        
        ArrayList<Scelta> scelte = new ArrayList<TipiConversioniAdapter.Scelta>();
        scelte.add(creaCambioView(c.getString(R.string.pag_input_gauss), R.drawable.mappa, InputGaussActivity.class));
        scelte.add(creaCambioView(c.getString(R.string.pag_input_cassini), R.drawable.mappa_rosso, InputCassiniActivity.class));
        scelte.add(creaCambioView(c.getString(R.string.pag_input_latlong), R.drawable.rete, InputLatLongActivity.class));
        
        scelte.add(creaCambioView(c.getString(R.string.pag_input_ed50), R.drawable.rete_2, InputED50Activity.class));
        scelte.add(creaCambioView(c.getString(R.string.pag_input_utm), R.drawable.utm, InputUtmActivity.class));

        scelte.add(creaCambioView(c.getString(R.string.pag_input_osm), R.drawable.posizione_blu, InputOSMActivity.class));
        
        scelte.add(creaCambioView(c.getString(R.string.pag_input_web_mercator), R.drawable.web_mercator, InputWebMercatorActivity.class));
        
        scelte.add(creaCambioView(c.getString(R.string.pag_input_mgrs), R.drawable.mgrs, InputMgrsActivity.class));
        scelte.add(creaCambioView(c.getString(R.string.pag_input_gps), R.drawable.antenna, StatoGpsActivity.class));

        // Questa voce e' accessibile anche dalla ActionBar ma la lascio perche' gli utenti
        // ormai sono abituati a trovarla qua
        scelte.add(creaCambioView(c.getString(R.string.pag_impostazioni), R.drawable.impostazioni, SettingsActivity.class));
        scelte.add(creaCambioView(c.getString(R.string.pag_registro), R.drawable.log, GestioneRegistroActivity.class));

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"graphmouse@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "iConv");
        
        String versionCode;
        String versionName;
        
        try {
        	versionCode = "" + c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
        	versionName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName; 
        } catch(Exception e) {
        	versionCode = "n.d.";
        	versionName = "n.d.";
        }
        
        String datiTecnici = "\n------------\nDati tecnici\n------------\nApplicazione: " + c.getPackageName() + "\nVersione: "
        		+ versionCode + " (" + versionName + ")\n------------\n\n";
        
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, datiTecnici);

        // Questa voce e' accessibile anche dalla ActionBar ma la lascio perche' gli utenti
        // ormai sono abituati a trovarla qua
        scelte.add(creaCambioView(c.getString(R.string.pag_aiuto), R.drawable.help, HelpIconvActivity.class));

        tipiConversione = new Scelta[scelte.size()];
        tipiConversione = scelte.toArray(tipiConversione);
    }

    public int getCount() {
        return tipiConversione.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public Intent getDestinazione(int position)
    {
        return tipiConversione[position].intent;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	LinearLayout lineare;
        ImageView imageView;
        TextView text;
    	
        if (convertView == null) {  // if it's not recycled, initialize some attributes        	
        	lineare = new LinearLayout(mContext);
        	lineare.setOrientation(LinearLayout.VERTICAL);
        	        	
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(4, 4, 4, 4);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            
            text = new TextView(mContext);
            //text.setTextColor(Color.rgb(23, 38, 136));
            text.setTextColor(Color.rgb(0, 0, 0));
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setGravity(Gravity.CENTER);

            // https://code.google.com/p/android/issues/detail?id=16218
            text.setLines(2);

            //text.setTypeface(Typeface.DEFAULT_BOLD);
            //text.setBackgroundColor(Color.argb(150, 226, 227, 235));
            
            lineare.addView(imageView);
            lineare.addView(text);
            
            lineare.setPadding(0, 0, 0, 4);
        } 
        else 
        {
        	lineare = (LinearLayout) convertView;
        	imageView = (ImageView) lineare.getChildAt(0);
        	text = (TextView) lineare.getChildAt(1);
        }

        imageView.setImageResource(tipiConversione[position].icona);
        text.setText(tipiConversione[position].nome);
        
        return lineare;
    }
	
	private Scelta creaCambioView(String nome, int icona, Class<? extends Activity> activityDestinazione) {
		return creaDaIntent(nome, icona, new Intent(mContext, activityDestinazione));
	}

	private Scelta creaDaIntent(String nome, int icona, Intent i) {
		Scelta result = new Scelta();
		result.nome = nome;
		result.icona = icona;
        result.intent = i;		
		return result;
	}
	
    private class Scelta
    {
    	public String nome;
    	public int icona;
    	public Intent intent;
    }
}
