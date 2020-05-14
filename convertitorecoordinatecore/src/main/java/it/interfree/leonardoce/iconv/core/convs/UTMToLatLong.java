package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.core.math.UTMUtils;
import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.*;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;


public class UTMToLatLong extends CoordinateConversionBase
{
    public IConvDatumNames datum;

	public UTMToLatLong(IConvDatumNames datum)
	{
        this.datum = datum;
	}

	@Override
	public String getDescription() {
		return "Retro-proiezione in coordinate UTM";
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

        Punto2D destinazione = new Punto2D();
        int zona;
        UTMUtils utils = new UTMUtils();

        if ( !(orig instanceof  PuntoUTM) ) {
            throw new ConversionException("tipo errato nella conversione UTM");
        } else {
            zona = ((PuntoUTM)orig).getZona();
        }

        if (IConvDatumNames.WGS84.equals(datum))
        {
            final String proiezioneUTM = "+proj=utm +zone=" + zona + " +datum=WGS84";
            Projection utm = ProjectionFactory.fromPROJ4Specification(proiezioneUTM.split(" "));
            utm.inverseTransform(
                    orig,
                    destinazione);

            double xy[] = new double[2];
            utils.UTMXYToLatLon(((PuntoUTM) orig).x, Math.abs(((PuntoUTM) orig).y), zona, ((PuntoUTM) orig).y<0, xy);
            destinazione.y = utils.RadToDeg(xy[0]);
            destinazione.x = utils.RadToDeg(xy[1]);
        }
        else if(IConvDatumNames.ED50.equals(datum))
        {
            try
            {
                Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
                double xy[] = new double[2];
                utils.sm_a = intl.equatorRadius;
                utils.sm_b = intl.poleRadius;
                utils.UTMXYToLatLon(((PuntoUTM) orig).x, Math.abs(((PuntoUTM) orig).y), zona, ((PuntoUTM) orig).y<0, xy);
                destinazione.y = utils.RadToDeg(xy[0]);
                destinazione.x = utils.RadToDeg(xy[1]);


                // Porto tutto in geocentriche
                GeocentricInfo geo = new GeocentricInfo();
                geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);

                Punto2D latlong_radians = GeodesicUtils.degreeToRadians(destinazione);
                Punto3D punto = geo.geodeticToGeocentric(latlong_radians.y, latlong_radians.x, 0);

                // cambio di Datum
                // ---------------

                //new Datum("European Datum 1950", Ellipsoid.INTERNATIONAL_1967, dx -87, dy -98, dz -121),

                punto.x += -87;
                punto.y += -98;
                punto.z += -121;

                // Riporto in geodesiche
                // ---------------------
                Ellipsoid wgs = Ellipsoid.WGS_1984;
                geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);
                Punto3D latlong_dest_radians = geo.geocentricToGeodetic(punto.x, punto.y, punto.z);

                Punto3D latlong_dest = GeodesicUtils.radiansToDegree(latlong_dest_radians);
                // La Z si misura comunque in radianti
                latlong_dest.z = latlong_dest_radians.z;

                destinazione.x = latlong_dest.x;
                destinazione.y = latlong_dest.y;
            } catch (GeocentricException ex)
            {
                throw new ConversionException("Dati non coerenti", ex);
            }
        }

		return new Punto3D(destinazione.x, destinazione.y, 0);
	}

}
