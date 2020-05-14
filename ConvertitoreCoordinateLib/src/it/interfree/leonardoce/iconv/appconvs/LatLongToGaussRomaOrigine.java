package it.interfree.leonardoce.iconv.appconvs;

import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import android.content.Context;

public class LatLongToGaussRomaOrigine extends LatLongToGaussRoma 
{
	public LatLongToGaussRomaOrigine(Context context)
	{
		super();
		
		IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			fusoTrasformazione = db.getFusoGaussSelezionato();
		}
		finally
		{
			db.close();
		}
	}
}
