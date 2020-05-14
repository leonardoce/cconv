package leo.cconv.core.convs.test;

import org.junit.Test;

import it.interfree.leonardoce.iconv.core.convs.UTMToLatLong;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;
import leo.test.AssertUtils;

/**
 * Created by lcecchi on 7/3/15.
 */
public class TestUTMToLatLong {
    /**
     * Questo e' un caso dato da un utente australiano
     * che mostra come UTM non funzionava nell'emisfero SUD
     * @throws Exception
     */
    @Test
    public void testEsempio() throws Exception {
        PuntoUTM utm = new PuntoUTM(495289, -7025038, 0, 56);
        UTMToLatLong conversione = new UTMToLatLong(IConvDatumNames.WGS84);
        Punto3D punto = conversione.convert(utm);
        AssertUtils.assertEquals2D(new Punto2D(152.9525607, -26.8964040), punto, 0.001);
    }
}
