package it.interfree.leonardoce.iconv.db;

import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.utils.GoogleMapsUtility;

import java.io.Serializable;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class RisultatoConversione implements Serializable
{
    private static final long serialVersionUID = 7053592850570811669L;
    public double lat,longi;
    public double x, y;
    public double webmercator_x, webmercator_y;
    public double nord, est;
    public String tipo_ingresso;
    public long time_last;
    public String descrizione_punto;

    public double utm_est, utm_nord;
    public int zonaUtm;
    public String utm_datum;

    public double lat_ed50, longi_ed50;

    public FusoGauss fuso;
    public String idOrigineCassini;
    public int id;
    public boolean inserito;

    public String puntoMgrs;

    public RisultatoConversione()
    {
        time_last = System.currentTimeMillis();
    }

    public void initPropertiesFromContext(Context context)
    {
        IOriginiStorage db = new OriginiCassiniManager(context);

        try
            {
                fuso = db.getFusoGaussSelezionato();
                idOrigineCassini = db.getIdOrigineSelezionata();
                utm_datum = db.getDatumUTMSelezionato();
            }
        finally
            {
                db.close();
            }
    }

    public String getUrlGoogleMaps()
    {
        return GoogleMapsUtility.getGoogleMapsUrl(this.lat, this.longi);
    }

    public Intent vaiGoogleMaps()
    {
    	Uri uri = Uri.parse(getUrlGoogleMaps());
    	return new Intent(Intent.ACTION_VIEW, uri);
    }

}
