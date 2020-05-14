package it.interfree.leonardoce.iconv.appconvs;

import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OrigineCassini;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import android.content.Context;

public class CassiniToLatLongOrigine extends CassiniToLatLong 
{
	public CassiniToLatLongOrigine(Context context)
	{
		super(0,0);
		
		IOriginiStorage db = new OriginiCassiniManager(context);
		
		try
		{
			OrigineCassini orig = db.getOrigine(db.getIdOrigineSelezionata());
			if (orig!=null)
			{
				lat_0 = orig.getFix().y;
				lon_0 = orig.getFix().x;
			}
		}
		finally
		{
			db.close();
		}
		
	}
}
