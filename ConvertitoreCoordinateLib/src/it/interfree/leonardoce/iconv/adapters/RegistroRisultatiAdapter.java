package it.interfree.leonardoce.iconv.adapters;

import it.interfree.leonardoce.convertitorecoordinatelib.ActivityGlobals;
import it.interfree.leonardoce.convertitorecoordinatelib.R;
import it.interfree.leonardoce.convertitorecoordinatelib.RisultatoConversioneActivity;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegistroRisultatiAdapter extends BaseAdapter
{
	private List<RisultatoConversione> listaLog; 
	private Context context;
	private DateFormat dateFormat;
	private DateFormat timeFormat;
	
	public RegistroRisultatiAdapter(Context pContext)
	{
		context = pContext;
		listaLog = new LinkedList<RisultatoConversione>();
		dateFormat = android.text.format.DateFormat.getDateFormat(pContext.getApplicationContext());
		timeFormat = android.text.format.DateFormat.getTimeFormat(pContext.getApplicationContext());
	}
	
	public void refresh()
	{
		IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			listaLog.clear();
			listaLog.addAll(db.getLog());
		}
		finally
		{
			db.close();
		}
		
		this.notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return listaLog.size();
	}

	@Override
	public Object getItem(int position) {
		return listaLog.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View result;
		
		if (convertView!=null)
		{
			result = convertView;
		}
		else
		{
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = vi.inflate(R.layout.sublayout_riga_risultato, null); 
		}
		
		// aggiorno la vista
		TextView rigaDataPunto = (TextView) result.findViewById(R.id.rigaDataPunto);
		TextView rigaDescrizione = (TextView) result.findViewById(R.id.rigaDescrizione);
		ImageButton btVediRisultato = (ImageButton) result.findViewById(R.id.rigaScegliRisultato);
				
		final RisultatoConversione ris = listaLog.get(position);		
		btVediRisultato.setTag(ris);
		
		if (convertView==null)
		{
			btVediRisultato.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					visualizzaRisultato((RisultatoConversione)v.getTag());
				}
			});
		}
		
		rigaDataPunto.setText(
			dateFormat.format(new Date(ris.time_last)) +
			" " +
			timeFormat.format(new Date(ris.time_last)));
		rigaDescrizione.setText(ris.descrizione_punto);
		
		return result;
	}
		
	public void visualizzaRisultato(RisultatoConversione ris)
	{
		Intent passaggioRisultato = new Intent(context, RisultatoConversioneActivity.class);
		passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE, ris);
		context.startActivity(passaggioRisultato);	
	}
}
