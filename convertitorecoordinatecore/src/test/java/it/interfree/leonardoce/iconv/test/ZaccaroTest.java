package it.interfree.leonardoce.iconv.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.core.convs.GaussRomaToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToCassini;
import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.core.convs.LatLongToUTM;
import it.interfree.leonardoce.iconv.core.convs.UTMToLatLong;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;

/**
 * Created by leonardo on 1/1/15.
 */
public class ZaccaroTest {
    // Caso Zaccaro, 28/12/2014
    @Test
    public void testLatLongToUtm() throws Exception {
        LatLongToUTM conv = new LatLongToUTM(IConvDatumNames.WGS84);

        Punto3D orig = new Punto3D();
        orig.x = 16.472502;
        orig.y = 40.699001;

        PuntoUTM dest = (PuntoUTM)conv.convert(orig);

        Assert.assertEquals(dest.x, 624403.464, 0.1);
        Assert.assertEquals(dest.y, 4506386.804, 0.1);
        Assert.assertEquals(33, dest.getZona());
    }

    // Caso Zaccaro, 28/12/2014
    @Test
    public void testUtmToLatLong() throws Exception {
        UTMToLatLong conv = new UTMToLatLong(IConvDatumNames.WGS84);

        PuntoUTM orig = new PuntoUTM(624403.464, 4506386.804, 0, 33);
        Punto3D dest = conv.convert(orig);

        Assert.assertEquals(40.699001, dest.y, 0.000001);
        Assert.assertEquals(16.472502, dest.x, 0.000001);
    }

    // Caso Zaccaro, 28/12/2014
    @Test
    public void testLatLongToGauss() throws Exception {
        Punto3D orig = new Punto3D(16.472502, 40.699001, 0);
        LatLongToGaussRoma conv = new LatLongToGaussRoma(FusoGauss.FUSO_EST);
        Punto3D expected = new Punto3D(2644411.97, 4506391.21, 0);
        Punto3D dest = conv.convert(orig);

        AssertUtils.assertEquals(expected, dest, 3);
    }

    // Caso Zaccaro, 28/12/2014
    @Test
    public void testGaussToLatLong() throws Exception {
        Punto3D orig = new Punto3D(2644411.97, 4506391.21, 0);
        GaussRomaToLatLong conv = new GaussRomaToLatLong(FusoGauss.FUSO_EST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(16.472502, 40.699001, 0);

        AssertUtils.assertEquals(expected, dest, 0.00003);
    }

    // Caso Zaccaro - Cassini -> Gauss
    @Test
    public void testCassiniToGauss() throws Exception {
        Punto3D orig = new Punto3D(-3500, 5650.00, 0);
        CassiniToLatLong conv1 = new CassiniToLatLong(
                GeodesicUtils.degreeToDecimal(40,41,56.4064),
                GeodesicUtils.degreeToDecimal(16,28,21.0095)
        );
        Punto3D latlong = conv1.convert(orig);
        LatLongToGaussRoma conv2 = new LatLongToGaussRoma(FusoGauss.FUSO_EST);
        Punto3D gauss = conv2.convert(latlong);

        Punto3D expected = new Punto3D(2640818.49, 4511980.52, 0);
        AssertUtils.assertEquals(expected, gauss, 3);
    }

    @Test
    public void testGaussToCassini() throws Exception {
        Punto3D gauss = new Punto3D(2640818.49, 4511980.52, 0);
        GaussRomaToLatLong conv1 = new GaussRomaToLatLong(FusoGauss.FUSO_EST);
        Punto3D latlong = conv1.convert(gauss);
        LatLongToCassini conv2 = new LatLongToCassini(
                GeodesicUtils.degreeToDecimal(40,41,56.4064),
                GeodesicUtils.degreeToDecimal(16,28,21.0095)
        );
        Punto3D dest = conv2.convert(latlong);

        Punto3D expected = new Punto3D(-3500, 5650.00, 0);
        AssertUtils.assertEquals(expected, dest, 3);
    }

    @Test
    public void testOrigineZaccaroPicciano() throws Exception {
        Punto3D latlong = new Punto3D(GeodesicUtils.degreeToDecimal(16,28,21.0095),
                GeodesicUtils.degreeToDecimal(40,41, 56.4064), 0);
        PuntoUTM utm = new PuntoUTM(624403.464, 4506386.804, 0, 33);
        Punto3D gauss = new Punto3D(2644411.97, 4506391.21, 0);
        confrontoLatLongUtmGauss("origine picciano", latlong, utm, gauss);
    }

    @Test
    public void testOrigineSerraCorneta() throws Exception {
        Punto3D latlong = new Punto3D(GeodesicUtils.degreeToDecimal(16, 10, 47.7599),
                GeodesicUtils.degreeToDecimal(40, 12, 53.5759), 0);
        PuntoUTM utm = new PuntoUTM(600403.530, 4452275.027, 0, 33);
        Punto3D gauss = new Punto3D(2620411.02, 4452280.13, 0);
        confrontoLatLongUtmGauss("origine serra corneta", latlong, utm, gauss);
    }

    @Test
    public void testTaranto() throws Exception {
        Punto3D latlong = new Punto3D(GeodesicUtils.degreeToDecimal(17, 13, 42.6544),
                GeodesicUtils.degreeToDecimal(40, 28, 34.3597), 0);
        PuntoUTM utm = new PuntoUTM(688904.955, 4482999.533, 0, 33);
        Punto3D gauss = new Punto3D(2708912.74, 4483002.72, 0);
        confrontoLatLongUtmGauss("origine taranto", latlong, utm, gauss);
    }

    private void confrontoLatLongUtmGauss(String desc, Punto3D latlong, PuntoUTM utm, Punto3D gauss) throws Exception
    {
        ICoordinateConversion conv = new LatLongToUTM(IConvDatumNames.WGS84);
        Punto3D utm_calcolate = conv.convert(latlong);
        AssertUtils.assertEquals(desc + " (latlong->utm)", utm, utm_calcolate, 0.5);

        conv = new LatLongToGaussRoma(FusoGauss.FUSO_EST);
        Punto3D gauss_calcolate = conv.convert(latlong);
        AssertUtils.assertEquals(desc + " (latlong->gauss)", gauss, gauss_calcolate, 4.3);

        conv = new UTMToLatLong(IConvDatumNames.WGS84);
        Punto3D latlong_calcolata_da_utm = conv.convert(utm);
        AssertUtils.assertEquals(desc + " (utm->latlong)", latlong, latlong_calcolata_da_utm, 0.00003);

        conv = new GaussRomaToLatLong(FusoGauss.FUSO_EST);
        Punto3D latlong_calcolata_da_gauss = conv.convert(gauss);
        AssertUtils.assertEquals(desc + " (gauss->latlong)", latlong, latlong_calcolata_da_gauss, 0.0003);
    }
}
