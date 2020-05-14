package leo.cconv.core.convs.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.MGRSToLatLong;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoMGRS;
import leo.test.AssertUtils;

/**
 * Created by lcecchi on 7/3/15.
 */
public class TestMGRSToLatLong {
    @Test
    public void testDaNickAustralia() throws Exception {
        PuntoMGRS puntoMGRS = new PuntoMGRS();
        puntoMGRS.setZonaUtm(56);
        puntoMGRS.setBanda('J');
        puntoMGRS.setQuadrante("MR");
        puntoMGRS.x = 95289;
        puntoMGRS.y = 25038;

        MGRSToLatLong conversione = new MGRSToLatLong();
        Punto3D punto = conversione.convert(puntoMGRS);
    }

    @Test
    public void testDaNickSempliceEgitto() throws Exception {
        PuntoMGRS puntoMGRS = new PuntoMGRS();
        puntoMGRS.setZonaUtm(36);
        puntoMGRS.setBanda('R');
        puntoMGRS.setQuadrante("UU");
        puntoMGRS.x = 7084;
        puntoMGRS.y = 20469;

        MGRSToLatLong conversione = new MGRSToLatLong();
        Punto3D punto = conversione.convert(puntoMGRS);
        AssertUtils.assertEquals2D(punto, new Punto3D(31, 30, 0), 0.001);
    }

    @Test
    public void testRoma() throws Exception {
        PuntoMGRS puntoMGRS = new PuntoMGRS();
        puntoMGRS.setZonaUtm(33);
        puntoMGRS.setBanda('T');
        puntoMGRS.setQuadrante("TG");
        puntoMGRS.x = 91173;
        puntoMGRS.y = 40966;

        MGRSToLatLong conversione = new MGRSToLatLong();
        Punto3D punto = conversione.convert(puntoMGRS);
        AssertUtils.assertEquals2D(punto, new Punto3D(12.4827780, 41.8930560, 0), 0.001);
    }

    @Test
    public void testNewYork() throws Exception {
        PuntoMGRS puntoMGRS = new PuntoMGRS();
        puntoMGRS.setZonaUtm(18);
        puntoMGRS.setBanda('T');
        puntoMGRS.setQuadrante("WL");
        puntoMGRS.x = 84461;
        puntoMGRS.y = 7786;

        MGRSToLatLong conversione = new MGRSToLatLong();
        Punto3D punto = conversione.convert(puntoMGRS);
        AssertUtils.assertEquals2D(punto, new Punto3D(-74, 40.716667, 0), 0.001);
    }

    @Test
    public void testRioDeJaneiro() throws Exception {
        PuntoMGRS puntoMGRS = new PuntoMGRS();
        puntoMGRS.setZonaUtm(23);
        puntoMGRS.setBanda('K');
        puntoMGRS.setQuadrante("PQ");
        puntoMGRS.x = 85839;
        puntoMGRS.y = 65637;

        MGRSToLatLong conversione = new MGRSToLatLong();
        Punto3D punto = conversione.convert(puntoMGRS);
        AssertUtils.assertEquals2D(punto, new Punto3D(-43.188056, -22.906944, 0), 0.001);
    }
}
