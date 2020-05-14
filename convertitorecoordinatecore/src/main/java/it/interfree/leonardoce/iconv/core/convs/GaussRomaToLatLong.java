package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.GeocentricException;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;


public class GaussRomaToLatLong extends CoordinateConversionBase 
{
	protected FusoGauss fusoTrasformazione;

	public GaussRomaToLatLong(FusoGauss pFuso)
	{
		fusoTrasformazione = pFuso;
	}
	
	public GaussRomaToLatLong()
	{
		fusoTrasformazione = FusoGauss.FUSO_OVEST;
	}
	
	@Override
	public String getDescription() {
		String description = "Conversione da coordinate " +
				"Gauss-Boaga con origine a Roma (Monte Mario) in " +
				"coordinate Latitudine-Longitudine. Cambio di datum" +
				"fra ellissoide internazionale e ellissoide WGS84";
		return description;
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_X_Y;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		
		/*
		# Monte Mario (Rome) / Italy zone 1 (fuso ovest)
		<26591> +proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.9996 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs  <>
		# Monte Mario (Rome) / Italy zone 2 (fuso est)
		<26592> +proj=tmerc +lat_0=0 +lon_0=2.54766666666666 +k=0.9996 +x_0=2520000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs  <>
		*/
		try
		{
			String proiezioneGauss;
			
			if (fusoTrasformazione==FusoGauss.FUSO_OVEST)
			{
				proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9";
			}
			else
			{
				proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=2.54766666666666 +k=0.999600 +x_0=2520000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9";
			}

            // Nota bene: i parametri lon_0 hanno come primo meridiano quello di Montemario Roma
            // che rispetto a Greenwich e' a 12d 27p 08.40s
            // ovvero 12.45233333333333337d
            //
            // Il primo caso e' corrisponde quindi a 9d ovvero:
            // -3.45233333333333 = 9 - 12.45233333333333337
            //
            // Il secondo e' invece 15d ovvero:
            // 2.54766666666666 = 15 - 12.45233333333333337

			Projection gauss = ProjectionFactory.fromPROJ4Specification(proiezioneGauss.split(" "));
					
			double gauss_nord = orig.y;
			double gauss_est = orig.x;
			
			Punto2D gausspt = new Punto2D(gauss_est, gauss_nord);
			Punto2D latlong = new Punto2D();
			gauss.inverseTransform(gausspt, latlong);
	
			// Questa conversione e' senza i dati del primo meridiano
			// e senza cambio di ellissoide
			
			// Adesso aggiungo il primo meridiano (Roma)		
			latlong.x += GeodesicUtils.degreeToDecimal(12, 27, 8.4);
			
			// Porto tutto in geocentriche
			Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
			GeocentricInfo geo = new GeocentricInfo();
			geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);
			
			Punto2D latlong_radians = GeodesicUtils.degreeToRadians(latlong);
			Punto3D punto = geo.geodeticToGeocentric(latlong_radians.y, latlong_radians.x, 0);

            // Nota bene.
            // I dati per il cambio di datum vengono da qua
            // http://www.rigacci.org/wiki/doku.php/tecnica/gps_cartografia_gis/gauss_boaga_wgs84

            // +towgs84=-104.1,-49.1,-9.9,0.971,-2.917,0.714,-11.68
            // ----------------------------------------------------

            double SEC_TO_RAD = 4.84813681109535993589914102357e-6;

            double Dx_BF = -104.1;
            double Dy_BF = -49.1;
            double Dz_BF = -9.9;
            double Rx_BF = 0.971 * SEC_TO_RAD;
            double Ry_BF = -2.917 * SEC_TO_RAD;
            double Rz_BF = 0.714 * SEC_TO_RAD;
            double M_BF = (-11.68/1000000.0)+1;

            double x_out = M_BF*(       punto.x - Rz_BF*punto.y + Ry_BF*punto.z) + Dx_BF;
            double y_out = M_BF*( Rz_BF*punto.x +       punto.y - Rx_BF*punto.z) + Dy_BF;
            double z_out = M_BF*(-Ry_BF*punto.x + Rx_BF*punto.y +       punto.z) + Dz_BF;

            punto.x = x_out;
            punto.y = y_out;
            punto.z = z_out;

			// +towgs84=-225,-65,9 (cambio di Datum)
			// -------------------------------------
	
			//punto.x += -225;
			//punto.y += -65;
			//punto.z += 9;
			
			// Riporto in geodesiche
			// ---------------------				
			Ellipsoid wgs = Ellipsoid.WGS_1984;
			geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);
			Punto3D latlong_dest_radians = geo.geocentricToGeodetic(punto.x, punto.y, punto.z);
			
			Punto3D latlong_dest = GeodesicUtils.radiansToDegree(latlong_dest_radians);
			// La Z si misura comunque in radianti
			latlong_dest.z = latlong_dest_radians.z;
	
			return latlong_dest;
		} catch (GeocentricException ge)
		{
			throw new ConversionException("Dati non coerenti", ge);
		}
	}

}
