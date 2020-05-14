package it.interfree.leonardoce.iconv.test;

import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.core.convs.GaussRomaToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToCassini;
import it.interfree.leonardoce.iconv.core.convs.LatLongToUTM;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;

/**
 * Created by leonardo on 1/2/15.
 */
public class Foglio19K61Test {
    @Test
    public void punto70220() throws Exception {
        AssertUtils.assertOrigineCassiniGaussOvest("70220",
                new Punto3D(11.332212, 43.318293, 0),
                new Punto3D(-6791.46, 49921.59, 0),
                new Punto3D(1680953.78, 4848529.60, 0),
                3
        );
    }

    @Test
    public void punto70240() throws Exception {
        AssertUtils.assertOrigineCassiniGaussOvest("70240",
                new Punto3D(11.332212, 43.318293, 0),
                new Punto3D(-5546.33, 49960.02, 0),
                new Punto3D(1682197.34, 4848603.10, 0),
                3
        );
    }

    @Test
    public void punto80140() throws Exception {
        AssertUtils.assertOrigineCassiniGaussOvest("80140",
                new Punto3D(11.332212, 43.318293, 0),
                new Punto3D(-6058.44, 50537.68, 0),
                new Punto3D(1681669.15, 4849166.10, 0),
                3
        );
    }

    @Test
    public void testNO() throws Exception {
        Punto3D catascale = new Punto3D(-3727.48, 50505.87, 0);
        CassiniToLatLong conv1 = new CassiniToLatLong(43.318293, 11.332212);
        Punto3D latlong = conv1.convert(catascale);
        LatLongToUTM conv2 = new LatLongToUTM(IConvDatumNames.ED50);
        Punto3D utm = conv2.convert(latlong);
        Punto3D expected = new Punto3D(684052.20, 4849382.60, 0);
        AssertUtils.assertEquals("NO (cassini->utm-ed50)", expected, utm, 3);
    }
}
