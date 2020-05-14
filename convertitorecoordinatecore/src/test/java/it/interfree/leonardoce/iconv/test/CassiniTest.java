package it.interfree.leonardoce.iconv.test;

import org.junit.Ignore;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.core.convs.GaussRomaToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToCassini;
import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Created by leonardo on 1/1/15.
 */
public class CassiniTest {
    private static double PRECISIONE_GRADI = 0.00004;
    private static double PRECISIONE_METRI = 3;

    // Punto L736A-015C-01 (Venezia) da fiduciali.it
    @Test
    public void testVeneziaSantoStefano() throws Exception {
        Punto3D orig = new Punto3D(-25708.79, -57882.27, 0);
        CassiniToLatLong conv = new CassiniToLatLong(45.954554, 12.660503);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(12.331955, 45.433298, 0);
        AssertUtils.assertEquals(expected, dest, PRECISIONE_GRADI);
    }

    @Test
    public void testVeneziaSantoStefano2() throws Exception {
        Punto3D orig = new Punto3D(12.331955, 45.433298, 0);
        LatLongToCassini conv = new LatLongToCassini(45.954554, 12.660503);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(-25708.79, -57882.27, 0);
        AssertUtils.assertEquals(expected, dest, PRECISIONE_METRI);
    }

    // Punto D612-1660-01 da fiduciali.it
    @Test
    public void testTrigonometricoFirenzeDuomo() throws Exception {
        Punto3D orig = new Punto3D(-6058.78, 50537.34, 0);
        CassiniToLatLong conv = new CassiniToLatLong(43.318293,11.332212);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(11.25694, 43.773115, 0);
        AssertUtils.assertEquals(expected, dest, PRECISIONE_GRADI);
    }

    @Test
    public void testTrigonometricoFirenzeDuomo2() throws Exception {
        Punto3D orig = new Punto3D(11.25694, 43.773115, 0);
        LatLongToCassini conv = new LatLongToCassini(43.318293,11.332212);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(-6058.78, 50537.34, 0);
        AssertUtils.assertEquals(expected, dest, PRECISIONE_METRI);
    }

    // Punto L219-12460-53
    @Test
    public void testGenova() throws Exception {
        Punto3D orig = new Punto3D(-90.506, -5628.539, 0);
        CassiniToLatLong conv = new CassiniToLatLong(44.461121,8.939468);
        Punto3D dest = conv.convert(orig);
        AssertUtils.assertEquals(new Punto3D(8.938333, 44.410469, 0), dest, PRECISIONE_GRADI);
    }

    @Test
    public void testGenova2() throws Exception {
        Punto3D orig = new Punto3D(8.938333, 44.410469, 0);
        LatLongToCassini conv = new LatLongToCassini(44.461121,8.939468);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(-90.506, -5628.539, 0);
        AssertUtils.assertEquals(expected, dest, PRECISIONE_METRI);

    }

    // Punto H647-0350-05
    @Test
    public void testRoma() throws Exception {
        Punto3D orig = new Punto3D(-205.506, -151.074, 0);
        CassiniToLatLong conv = new CassiniToLatLong(41.924403,12.452129);
        Punto3D dest = conv.convert(orig);
        AssertUtils.assertEquals("roma", new Punto3D(12.449655, 41.92304, 0), dest, PRECISIONE_GRADI);
    }

    @Test
    public void testRoma2() throws Exception {
        Punto3D orig = new Punto3D(12.449655, 41.92304, 0);
        LatLongToCassini conv = new LatLongToCassini(41.924403,12.452129);
        Punto3D dest = conv.convert(orig);
        Punto3D expected = new Punto3D(-205.506, -151.074, 0);
        AssertUtils.assertEquals("roma", expected, dest, PRECISIONE_METRI);
    }

}
