package leo.cconv.core.convs.test;

import org.junit.Assert;
import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.LatLongToUTM;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;
import leo.test.AssertUtils;

/**
 * Created by lcecchi on 7/3/15.
 */
public class TestLatLongToUTM {
    @Test
    public void testEsempio() throws Exception {
        Punto3D origine = new Punto3D(152.9525607, -26.8964040, 0);
        LatLongToUTM conversione = new LatLongToUTM(IConvDatumNames.WGS84);
        PuntoUTM destinazione = (PuntoUTM)conversione.convert(origine);
        Assert.assertEquals(56, destinazione.getZona());
        AssertUtils.assertEquals3D(new PuntoUTM(495289, -7025038, 0, 56), destinazione, 0.1);
    }
}
