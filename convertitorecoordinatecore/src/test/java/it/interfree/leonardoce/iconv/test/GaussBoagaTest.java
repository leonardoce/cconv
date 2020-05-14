package it.interfree.leonardoce.iconv.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.core.convs.GaussRomaToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Created by leonardo on 1/1/15.
 */
public class GaussBoagaTest {
    private final static double PRECISIONE_GRADI = 0.00003;
    private final static double PRECISIONE_METRI = 3;

    // Punto D612-1660-01 da fiduciali.it
    @Test
    public void testTrigonometricoFirenzeDuomo() throws Exception {
        Punto3D orig = new Punto3D(1681668.7, 4849166.11, 0);
        GaussRomaToLatLong conv = new GaussRomaToLatLong(FusoGauss.FUSO_OVEST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(11.25694, 43.773115, 0);
        AssertUtils.assertEquals("firenze duomo", expected, dest, PRECISIONE_GRADI);
    }

    @Test
    public void testTrigonometricoFirenzeDuomo2() throws Exception {
        Punto3D orig = new Punto3D(11.25694, 43.773115, 0);
        LatLongToGaussRoma conv = new LatLongToGaussRoma(FusoGauss.FUSO_OVEST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(1681668.7, 4849166.11, 0);
        AssertUtils.assertEquals("firenze duomo", expected, dest, PRECISIONE_METRI);
    }

    // Punto L736A-015C-01 (Venezia) da fiduciali.it
    @Test
    public void testVeneziaSantoStefano() throws Exception {
        Punto3D orig = new Punto3D(2311313.44, 5034569.49, 0);
        GaussRomaToLatLong conv = new GaussRomaToLatLong(FusoGauss.FUSO_EST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(12.331955, 45.433298, 0);
        AssertUtils.assertEquals("venezia santo stefano", expected, dest, PRECISIONE_GRADI);
    }

    @Test
    public void testVeneziaSantoStefano2() throws Exception {
        Punto3D orig = new Punto3D(12.331955, 45.433298, 0);
        LatLongToGaussRoma conv = new LatLongToGaussRoma(FusoGauss.FUSO_EST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(2311313.44, 5034569.49, 0);
        AssertUtils.assertEquals("venezia santo stefano", expected, dest, PRECISIONE_METRI);
    }

    // Punto L219-12460-53
    @Test
    public void testTorinoGiardinoReale() throws Exception {
        Punto3D orig = new Punto3D(1396811.19, 4991835.38, 0);
        GaussRomaToLatLong conv = new GaussRomaToLatLong(FusoGauss.FUSO_OVEST);
        Punto3D dest = conv.convert(orig);

        AssertUtils.assertEquals("torino giardino reale", new Punto3D(7.688763, 45.072285, 0), dest, PRECISIONE_GRADI);
    }

    @Test
    public void testTorinoGiardinoReale2() throws Exception {
        Punto3D orig = new Punto3D(7.688763, 45.072285, 0);
        LatLongToGaussRoma conv = new LatLongToGaussRoma(FusoGauss.FUSO_OVEST);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(1396811.19, 4991835.38, 0);
        AssertUtils.assertEquals("torino giardino reale", expected, dest, PRECISIONE_METRI);

    }
}
