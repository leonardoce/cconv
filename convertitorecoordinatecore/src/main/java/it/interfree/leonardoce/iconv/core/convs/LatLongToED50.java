package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.math.GeocentricException;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Questa classe implementa la conversione da coordinate LatLong/WGS84 a coordinate
 * LatLong/ED50
 * @author leonardo
 */
public class LatLongToED50 extends CoordinateConversionBase {

	@Override
	public String getDescription() {
		return "Conversione da LatLong/WGS84 a LatLong/ED50";
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		try {
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
	
	        //new Datum("European Datum 1950", Ellipsoid.INTERNATIONAL_1967, dx -87, dy -98, dz -121),
	        orig_geocentric.x -= -87;
	        orig_geocentric.y -= -98;
	        orig_geocentric.z -= -121;
	
	        // Rimetto tutto in coordanate geodetiche
	        // utilizzando l'ellissoide internazionale
	        // --------------------------------------
	
	        Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
	        geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);
	        Punto3D b_radians = geo.geocentricToGeodetic(orig_geocentric.x, orig_geocentric.y, orig_geocentric.z);
	        Punto3D b_degree = GeodesicUtils.radiansToDegree(b_radians);
	
	        return b_degree;
		} catch(GeocentricException ge) {
			throw new ConversionException("Errore di congruenza dei dati");
		}
		
	}

}
