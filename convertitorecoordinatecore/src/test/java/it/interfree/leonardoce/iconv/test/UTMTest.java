package it.interfree.leonardoce.iconv.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.LatLongToUTM;
import it.interfree.leonardoce.iconv.core.convs.UTMToLatLong;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;

/**
 * Created by leonardo on 1/1/15.
 */
public class UTMTest {
    // Punto D612-1660-01 da fiduciali.it
    @Test
    public void testTrigonometricoFirenzeDuomo() throws Exception {
        PuntoUTM orig = new PuntoUTM(681638.35, 4849149.34, 0, 32);
        UTMToLatLong conv = new UTMToLatLong(IConvDatumNames.WGS84);
        Punto3D dest = conv.convert(orig);

        Assert.assertEquals(43.773115, dest.y, 0.000001);
        Assert.assertEquals(11.25694, dest.x, 0.000001);
    }

    @Test
    public void testTrigonometricoFirenzeDuomo2() throws Exception {
        Punto3D orig = new Punto3D(11.25694, 43.773115, 0);
        LatLongToUTM conv = new LatLongToUTM(IConvDatumNames.WGS84);
        PuntoUTM dest = (PuntoUTM)conv.convert(orig);

        Assert.assertEquals(681638.35, dest.x, 0.1);
        Assert.assertEquals(4849149.34, dest.y, 0.1);
    }

    // Punto L736A-015C-01 (Venezia) da fiduciali.it
    @Test
    public void testVeneziaSantoStefano() throws Exception {
        PuntoUTM orig = new PuntoUTM(291309.26, 5034549.14, 0, 33);
        UTMToLatLong conv = new UTMToLatLong(IConvDatumNames.WGS84);
        Punto3D dest = conv.convert(orig);

        Assert.assertEquals(45.433298, dest.y, 0.000001);
        Assert.assertEquals(12.331955, dest.x, 0.000001);
    }

    @Test
    public void testVeneziaSantoStefano2() throws Exception {
        Punto3D orig = new Punto3D(12.331955, 45.433298, 0);
        LatLongToUTM conv = new LatLongToUTM(IConvDatumNames.WGS84);
        PuntoUTM dest = (PuntoUTM)conv.convert(orig);

        Assert.assertEquals(291309.26, dest.x, 0.1);
        Assert.assertEquals(5034549.14, dest.y, 0.1);
    }

    // Punto L219-12460-53
    @Test
    public void testTorinoGiardinoReale() throws Exception {
        PuntoUTM orig = new PuntoUTM(396784.49, 4991816.66, 0, 32);
        UTMToLatLong conv = new UTMToLatLong(IConvDatumNames.WGS84);
        Punto3D dest = conv.convert(orig);

        Assert.assertEquals(45.072285, dest.y, 0.000001);
        Assert.assertEquals(7.688763, dest.x, 0.000001);
    }

    @Test
    public void testTorinoGiardinoReale2() throws Exception {
        Punto3D orig = new Punto3D(7.688763, 45.072285, 0);
        LatLongToUTM conv = new LatLongToUTM(IConvDatumNames.WGS84);
        PuntoUTM dest = (PuntoUTM)conv.convert(orig);

        Assert.assertEquals(396784.49, dest.x, 0.1);
        Assert.assertEquals(4991816.66, dest.y, 0.1);
    }
}
