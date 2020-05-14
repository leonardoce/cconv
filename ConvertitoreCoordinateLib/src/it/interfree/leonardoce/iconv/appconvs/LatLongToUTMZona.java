package it.interfree.leonardoce.iconv.appconvs;

import android.content.Context;
import it.interfree.leonardoce.iconv.core.convs.LatLongToUTM;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;

public class LatLongToUTMZona extends LatLongToUTM {

    public LatLongToUTMZona(Context context) {
        super(IConvDatumNames.ED50);

        IOriginiStorage db = new OriginiCassiniManager(context);

        try
        {
            if (IConvDatumNames.ED50.name().equals(db.getDatumUTMSelezionato()))
            {
                datum = IConvDatumNames.ED50;
            }
            else
            {
                datum = IConvDatumNames.WGS84;
            }
        }
        finally
        {
            db.close();
        }

    }
}
