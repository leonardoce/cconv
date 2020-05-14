package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.GeocentricException;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto3D;


public class LatLongToGaussRoma extends CoordinateConversionBase 
{
	protected FusoGauss fusoTrasformazione;
	
	public LatLongToGaussRoma()
	{
		fusoTrasformazione = FusoGauss.FUSO_OVEST;
	}
	
	public LatLongToGaussRoma(FusoGauss pFuso)
	{
		fusoTrasformazione = pFuso;
	}

	@Override
	public String getDescription() {
		String description = "Conversione da coordinate " +
				"Latitudine-Longitudine a coordinate Gauss-Boaga con origine a Roma (Monte Mario). " +
				"Cambio di datum" +
				"fra ellissoide internazionale e ellissoide WGS84";
		return description;
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_X_Y;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		
		try
		{
			// Prima porto in radianti le coordinate
			// -------------------------------------
			Punto3D orig_radians = GeodesicUtils.degreeToRadians(orig);
			
			// Porto le coordinate geodetiche in coordinate geocentriche
			// utilizzando l'ellissoide WGS84
			// ---------------------------------------------------------
			
			GeocentricInfo geo = new GeocentricInfo();
			Ellipsoid wgs = Ellipsoid.WGS_1984;
			geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);

			Punto3D orig_geocentric = geo.geodeticToGeocentric(orig_radians.y, orig_radians.x, 0);
			
			// Inverto il cambio di datum
			// --------------------------
			//
			//orig_geocentric.x -= -225;
			//orig_geocentric.y -= -65;
			//orig_geocentric.z -= 9;


            // Nota bene.
            // I dati per il cambio di datum vengono da qua
            // http://www.rigacci.org/wiki/doku.php/tecnica/gps_cartografia_gis/gauss_boaga_wgs84
            //
            // Questi parametri, come si vede nel PDF in Mercurial, hanno una precisione,
            // all'incirca, di 4 metri.

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

            /**
             * In Friuli Venezia Giulia vanno particolarmente
             * bene i parametri che seguono.
             * Li ho presi da qua:
             * http://gvsig.gva.es/download/events/giornate-italiane/2010/Trasformazioni_di_coordinate_rgb.pdf
             *
             * Il PDF si trova anche su Mercurial
             */
            /*double Dx_BF = -128.6633;
            double Dy_BF = -30.2694;
            double Dz_BF = -6.12;
            double Rx_BF = -1.05572  * SEC_TO_RAD;
            double Ry_BF = -2.6951 * SEC_TO_RAD;
            double Rz_BF = -2.28808 * SEC_TO_RAD;
            double M_BF = (-16.9352/1000000.0)+1;
            */

            double x_tmp = (orig_geocentric.x - Dx_BF) / M_BF;
            double y_tmp = (orig_geocentric.y - Dy_BF) / M_BF;
            double z_tmp = (orig_geocentric.z - Dz_BF) / M_BF;

            orig_geocentric.x =        x_tmp + Rz_BF*y_tmp - Ry_BF*z_tmp;
            orig_geocentric.y = -Rz_BF*x_tmp +       y_tmp + Rx_BF*z_tmp;
            orig_geocentric.z =  Ry_BF*x_tmp - Rx_BF*y_tmp +       z_tmp;

			// Rimetto tutto in coordanate geodetiche
			// utilizzando l'ellissoide internazionale
			// --------------------------------------

			Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
			geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);
			Punto3D b_radians = geo.geocentricToGeodetic(orig_geocentric.x, orig_geocentric.y, orig_geocentric.z);
			
			// Sottrango il primo meridiano
			// (roma)
			// ----------------------------
			
			Punto3D b_degree = GeodesicUtils.radiansToDegree(b_radians);
			b_degree.x -= GeodesicUtils.degreeToDecimal(12, 27, 8.4);
			
			// Proietto tutto con la
			// trasversa di Mercatore
			// ----------------------
			
			String proiezioneGauss;
			
			if (fusoTrasformazione==FusoGauss.FUSO_OVEST)
			{
				proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs";
			}
			else
			{
				proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=2.54766666666666 +k=0.999600 +x_0=2520000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs";
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
			
			Punto3D out_degree = new Punto3D();
			gauss.transform(b_degree, out_degree);
	
			return out_degree;
		} catch (GeocentricException ge)
		{
			throw new ConversionException("Dati non coerenti", ge);
		}
	}


}
