package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;



public class CassiniToLatLong extends CoordinateConversionBase 
{
	public double lat_0;
	public double lon_0;
    public double SEC_TO_RAD = 4.84813681109535993589914102357e-6;

	/**
	 * @param lat_0 Punto di emanazione in gradi
	 * @param lon_0 Punto di emanazione in gradi
	 */
	public CassiniToLatLong(double _lat_0, double _lon_0)
	{
		lat_0 = _lat_0;
		lon_0 = _lon_0;
	}

	@Override
	public String getDescription() {
		return "Retro-proiezione in coordinate Cassini con origine " +
			"latitudine: " + lat_0 + " e longitudine: " +
			lon_0;
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
        double Dx_BF = 656.5;
        double Dy_BF = 138.2;
        double Dz_BF = 506.5;
        double Rx_BF = 5.187 * SEC_TO_RAD;
        double Ry_BF = -2.540 * SEC_TO_RAD;
        double Rz_BF = 5.256 * SEC_TO_RAD;
        double M_BF = (-12.61/1000000.0)+1;

        try {
            Ellipsoid bessel = Ellipsoid.BESSEL;
            Ellipsoid wgs = Ellipsoid.WGS_1984;

            // Cambio di datun sull'origine
            GeocentricInfo geo = new GeocentricInfo();
            geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);
            Punto3D orig_radians = GeodesicUtils.degreeToRadians(new Punto3D(lon_0, lat_0, 0));
            Punto3D orig_geocentric = geo.geodeticToGeocentric(orig_radians.y, orig_radians.x, 0);

            double x_tmp = (orig_geocentric.x - Dx_BF) / M_BF;
            double y_tmp = (orig_geocentric.y - Dy_BF) / M_BF;
            double z_tmp = (orig_geocentric.z - Dz_BF) / M_BF;

            orig_geocentric.x =        x_tmp + Rz_BF*y_tmp - Ry_BF*z_tmp;
            orig_geocentric.y = -Rz_BF*x_tmp +       y_tmp + Rx_BF*z_tmp;
            orig_geocentric.z =  Ry_BF*x_tmp - Rx_BF*y_tmp +       z_tmp;

            geo.setGeocentricParameters(bessel.equatorRadius, bessel.poleRadius);
            Punto3D orig_post_radians = geo.geocentricToGeodetic(orig_geocentric.x, orig_geocentric.y, orig_geocentric.z);
            Punto3D orig_post_degree = GeodesicUtils.radiansToDegree(orig_post_radians);

            // Retro-proiezione
            final String proiezioneCassini = "+proj=cass +ellps=bessel +lat_0=" + orig_post_degree.y + " +lon_0=" +
                    orig_post_degree.x + " +x_0=1.3";
            Projection cassini = ProjectionFactory.fromPROJ4Specification(proiezioneCassini.split(" "));

            Punto2D destinazione = new Punto2D();
            cassini.inverseTransform(
                    orig,
                    destinazione);

            // Cambio di datum sulla destinazione
            Punto2D destinazione_radians = GeodesicUtils.degreeToRadians(destinazione);
            geo.setGeocentricParameters(bessel.equatorRadius, bessel.poleRadius);
            Punto3D punto_geocentric = geo.geodeticToGeocentric(destinazione_radians.y, destinazione_radians.x, 0);

            double x_out = M_BF*(       punto_geocentric.x - Rz_BF*punto_geocentric.y + Ry_BF*punto_geocentric.z) + Dx_BF;
            double y_out = M_BF*( Rz_BF*punto_geocentric.x +       punto_geocentric.y - Rx_BF*punto_geocentric.z) + Dy_BF;
            double z_out = M_BF*(-Ry_BF*punto_geocentric.x + Rx_BF*punto_geocentric.y +       punto_geocentric.z) + Dz_BF;

            geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);

            Punto3D punto_radians = geo.geocentricToGeodetic(x_out, y_out, z_out);
            Punto3D punto_degree = GeodesicUtils.radiansToDegree(punto_radians);

            return new Punto3D(punto_degree.x, punto_degree.y, 0);
        } catch(Exception e) {
            throw new ConversionException(e.getLocalizedMessage());
        }
	}

}
