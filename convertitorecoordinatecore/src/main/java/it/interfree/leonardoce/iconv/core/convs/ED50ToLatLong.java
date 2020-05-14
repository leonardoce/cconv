package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.math.GeocentricException;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Questa classe implementa la conversione da LatLong/ED50 a LatLong/WGS84
 * 
 * @author leonardo
 */
public class ED50ToLatLong extends CoordinateConversionBase {

	@Override
	public String getDescription() {
		return "Conversione da LatLong/ED50 a LatLong/WGS84";
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
			// Porto tutto in geocentriche
			Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0,
					"International 1909 (Hayford)");
			GeocentricInfo geo = new GeocentricInfo();
			geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);

			Punto2D latlong_radians = GeodesicUtils.degreeToRadians(orig);
			Punto3D punto = geo.geodeticToGeocentric(latlong_radians.y,
					latlong_radians.x, 0);

			// cambio di Datum
			// ---------------

			// new Datum("European Datum 1950", Ellipsoid.INTERNATIONAL_1967, dx
			// -87, dy -98, dz -121),

			punto.x += -87;
			punto.y += -98;
			punto.z += -121;

			// Riporto in geodesiche
			// ---------------------
			Ellipsoid wgs = Ellipsoid.WGS_1984;
			geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);
			Punto3D latlong_dest_radians = geo.geocentricToGeodetic(punto.x,
					punto.y, punto.z);

			Punto3D latlong_dest = GeodesicUtils
					.radiansToDegree(latlong_dest_radians);
			// La Z si misura comunque in radianti
			latlong_dest.z = latlong_dest_radians.z;

			return latlong_dest;
		} catch (GeocentricException ge) {
			throw new ConversionException("Dati non coerenti");
		}
	}

}
