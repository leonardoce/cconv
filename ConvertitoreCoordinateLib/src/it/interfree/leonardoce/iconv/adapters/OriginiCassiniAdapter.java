package it.interfree.leonardoce.iconv.adapters;

import it.interfree.leonardoce.convertitorecoordinatelib.R;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OrigineCassini;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class OriginiCassiniAdapter extends BaseAdapter 
{
	private Context context;
	private List<OrigineCassini> listaOrigini;
	private String origineSelezionata;
    private Runnable postCambiataOrigine;

    public Runnable getPostCambiataOrigine() {
        return postCambiataOrigine;
    }

    public void setPostCambiataOrigine(Runnable postCambiataOrigine) {
        this.postCambiataOrigine = postCambiataOrigine;
    }

    public OriginiCassiniAdapter(Context pContext)
	{
		super();
		context = pContext;

		refresh();
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		
		return super.getDropDownView(position, convertView, parent);
	}

	public void refresh()
	{
		IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			listaOrigini = db.getOrigini();
			origineSelezionata = db.getIdOrigineSelezionata();
		}
		finally
		{
			db.close();
		}
		
		this.notifyDataSetInvalidated();
	}

	@Override
	public int getCount() 
	{
		return listaOrigini.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return listaOrigini.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
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
			result = vi.inflate(R.layout.sublayout_riga_origine, null); 
		}
		
		// aggiorno la vista
		TextView rigaNomeOrigine = (TextView) result.findViewById(R.id.rigaNomeOrigine);
		TextView rigaLatitudine = (TextView) result.findViewById(R.id.rigaLatitudine);
		TextView rigaLongitudine = (TextView) result.findViewById(R.id.rigaLongitudine);
		RadioButton rigaSelezionata = (RadioButton) result.findViewById(R.id.rigaSelezionata);
		ImageButton rigaRimuoviOrigine = (ImageButton) result.findViewById(R.id.rigaRimuoviOrigine);
		
		final OrigineCassini origine = listaOrigini.get(position);
		rigaNomeOrigine.setText(origine.getDescrizioneString());
		rigaLatitudine.setText(
				GeodesicUtils.formatDegree(origine.getFix().y, "N", "S"));
		rigaLongitudine.setText(
				GeodesicUtils.formatDegree(origine.getFix().x, "E", "O"));
		rigaSelezionata.setTag(origine.getDescrizioneString());
		rigaRimuoviOrigine.setTag(origine.getDescrizioneString());
		
		if (origine.getDescrizioneString().equals(origineSelezionata))
		{
			rigaSelezionata.setChecked(true);
		}
		else
		{
			rigaSelezionata.setChecked(false);
		}
		
		if (convertView==null)
		{
			rigaSelezionata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						impostaOrigineSelezionata(buttonView.getTag().toString());						
					}
				}
			});
			
			rigaRimuoviOrigine.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					chiediRimuoviOrigine(v.getTag().toString());
				}
			});
		}
		
		return result;
	}
	
	private void impostaOrigineSelezionata(String id)
	{
		if (origineSelezionata.equals(id))
		{
			return;
		}
		
		origineSelezionata = id;		
		IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			db.setIdOrigineSelezionata(id);
		}
		finally
		{
			db.close();
		}
		
		refresh();

        if ( postCambiataOrigine!=null ) {
            postCambiataOrigine.run();
        }
	}
	
	private void chiediRimuoviOrigine(final String id)
	{
		final IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			if (db.isPredefinita(id))
			{
				new AlertDialog.Builder(context)
					.setTitle(context.getString(R.string.app_name))
					.setMessage(context.getString(R.string.msg_origine_predefinita))
					.setPositiveButton(context.getString(R.string.ok), null)
					.show();
				return;
			}
			
			// Chiede conferma
			String messaggio = String.format(
					context.getString(R.string.msg_cancella_origine),
					id
				);
			new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.app_name))
				.setMessage(messaggio)
				.setPositiveButton(
					context.getString(R.string.si), 
					new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							rimuoviOrigine(id);
						}
					}
				)
				.setNegativeButton(context.getString(R.string.no), null)
				.show();
		}
		finally
		{
			db.close();
		}		
	}
	
	private void rimuoviOrigine(String id)
	{
		final IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			db.deleteOrigineCassini(id);
		}
		finally
		{
			db.close();
		}
		
		refresh();
	}


}
