package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.core.math.UTMUtils;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.*;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;


public class LatLongToUTM extends CoordinateConversionBase
{
    public IConvDatumNames datum;

	public LatLongToUTM(IConvDatumNames datum)
	{
        this.datum = datum;
	}

	@Override
	public String getDescription() {
		return "Proiezione in coordinate UTM";
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
		String proiezioneUTM;
        Punto2D destinazione;
        UTMUtils utils = new UTMUtils();

        // Il codice per prendere una zona UTM e' stato preso
        // dalla conversione MGRS

        double longitudine = orig.x;
        //double latitude = orig.y;
        int zona = (int)(Math.floor((longitudine+180)/6)+1);

        destinazione = new Punto2D();

        if (datum.equals(IConvDatumNames.WGS84))
        {
            double xy[] = new double[2];
            utils.LatLonToUTMXY(utils.DegToRad(orig.y), utils.DegToRad(orig.x), zona, xy);
            destinazione.x = xy[0];
            destinazione.y = Math.signum(orig.y)*xy[1];
        }
        else if (datum.equals(IConvDatumNames.ED50))
        {
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

                double xy[] = new double[2];
                utils.sm_a = intl.equatorRadius;
                utils.sm_b = intl.poleRadius;
                utils.LatLonToUTMXY(b_radians.y, b_radians.x, zona, xy);
                destinazione.x = xy[0];
                destinazione.y = Math.signum(orig.y)*xy[1];
            }
            catch(GeocentricException ce)
            {
                throw new ConversionException("Dati non coerenti", ce);
            }
        }

		return new PuntoUTM(destinazione.x, destinazione.y, 0, zona);
	}

}
