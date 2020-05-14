package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.core.convs.LatLongToWebMercator;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import android.content.Context;

/**
 * Questa classe permette di avere delle conversioni piu' agevoli
 * incapsulando i vari algoritmi di conversione a partire dalle
 * coordinate LatLong WGS84.
 */
public class ConversioniUtils {
	private Context context;
	private RisultatoConversione risultato;
	private Punto3D orig;

	public ConversioniUtils(Context context, RisultatoConversione risultato) {
		this.context = context;
		this.risultato = risultato;
		this.orig = dammiPuntoOrigine();
	}
	
	public void conversioneInLatLongED50() throws Exception {
		
		ICoordinateConversion conv = new LatLongToED50();
		Punto3D destED50 = conv.convert(orig);
		
		risultato.lat_ed50 = destED50.y;
		risultato.longi_ed50 = destED50.x;
	}

	public void conversioneInGauss() throws Exception {
		ICoordinateConversion conv = new LatLongToGaussRomaOrigine(context);
		Punto3D destGauss = conv.convert(orig);
		
		risultato.nord = destGauss.y;
		risultato.est = destGauss.x;
	}

	public void conversioneInCassini() throws Exception {
		ICoordinateConversion proiezione = new LatLongToCassiniOrigine(context);
		Punto3D destCassini = proiezione.convert(orig);
		
		risultato.x = destCassini.y;
		risultato.y = destCassini.x;
	}

	public void conversioneInUTM() throws Exception {
		ICoordinateConversion conversioneUTM = new LatLongToUTMZona(context);
		PuntoUTM punto3D = (PuntoUTM)conversioneUTM.convert(orig);
		risultato.utm_est = punto3D.x;
		risultato.utm_nord = punto3D.y;
		risultato.zonaUtm = punto3D.getZona();
	}

	public void conversioneInMGRS() throws Exception {
		try 
		{
			ICoordinateConversion conversioneMGRS = new LatLongToMGRS();
			Punto3D risultatoMgrs = conversioneMGRS.convert(orig);
			risultato.puntoMgrs = risultatoMgrs.toString();
		}
		catch(Exception exc)
		{
			risultato.puntoMgrs = "<KO>";
			throw exc;
		}
	}
	
	public void conversioneInWebMercator() throws Exception {
		try 
		{
			ICoordinateConversion conversioneWebMercator = new LatLongToWebMercator();
			Punto3D risultatoWebMercator = conversioneWebMercator.convert(orig);
			risultato.webmercator_x = risultatoWebMercator.x;
			risultato.webmercator_y = risultatoWebMercator.y;
		}
		catch(Exception exc)
		{
			risultato.webmercator_x = 0;
			risultato.webmercator_y = 0;
			throw exc;
		}
	}

	private Punto3D dammiPuntoOrigine() {
		return new Punto3D( risultato.longi, risultato.lat, 0 );
	}
}
