package leo.cconv.core.convs.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoMGRS;

/**
 * Created by lcecchi on 6/27/15.
 */
public class TestLatLongToMGRS {
    @Test
    public void testDaNickSempliceEgitto() throws Exception{
        Punto3D punto = new Punto3D(31, 30, 0);
        LatLongToMGRS conversione = new LatLongToMGRS();
        PuntoMGRS mgrs = (PuntoMGRS)conversione.convert(punto);
        Assert.assertEquals("36 R UU 07084 20469", mgrs.toString());
    }

    // Attenzione qua siamo in emisfero SUD
    @Test
    public void testDaNickAustralia() throws Exception {
        Punto3D punto = new Punto3D(152.9579985, -26.8964057, 0);
        LatLongToMGRS conversione = new LatLongToMGRS();
        PuntoMGRS mgrs = (PuntoMGRS)conversione.convert(punto);
        Assert.assertEquals("56 J MR 95829 25038", mgrs.toString());
    }

    @Test
    public void testRoma() throws Exception {
        Punto3D punto = new Punto3D(12.4827780, 41.8930560, 0);
        LatLongToMGRS conversione = new LatLongToMGRS();
        PuntoMGRS mgrs = (PuntoMGRS)conversione.convert(punto);
        Assert.assertEquals("33 T TG 91173 40966", mgrs.toString());
    }

    @Test
    public void testNewYork() throws Exception {
        Punto3D punto = new Punto3D(-74, 40.716667, 0);
        LatLongToMGRS conversione = new LatLongToMGRS();
        PuntoMGRS mgrs = (PuntoMGRS)conversione.convert(punto);
        Assert.assertEquals("18 T WL 84461 07786", mgrs.toString());
    }

    @Test
    public void testRioDeJaneiro() throws Exception {
        Punto3D punto = new Punto3D(-43.188056, -22.906944, 0);
        LatLongToMGRS conversione = new LatLongToMGRS();
        PuntoMGRS mgrs = (PuntoMGRS)conversione.convert(punto);
        Assert.assertEquals("23 K PQ 85839 65637", mgrs.toString());
    }
}
