package it.interfree.leonardoce.iconv.test;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.core.convs.GaussRomaToLatLong;
import it.interfree.leonardoce.iconv.core.convs.LatLongToCassini;
import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Created by leonardo on 1/1/15.
 */
public class AssertUtils {
    public static void assertEquals(Punto3D excected, Punto3D actual, double precision) {
        double deltax = Math.abs(excected.x - actual.x);
        double deltay = Math.abs(excected.y - actual.y);
        double delta = Math.sqrt(deltax*deltax + deltay*deltay);
        if (delta>precision) {
            throw new AssertionError("\n\nExpected: " + box(excected) + "\nActual: " + box(actual) + "\nDelta: " + box(delta) + " required " + box(precision) + "\n");
        } else {
            System.out.println("Scarto " + box(delta) + " atteso " + box(precision));
        }
    }

    public static void assertEquals(String desc, Punto3D excected, Punto3D actual, double precision) {
        double deltax = Math.abs(excected.x - actual.x);
        double deltay = Math.abs(excected.y - actual.y);
        double delta = Math.sqrt(deltax*deltax + deltay*deltay);
        if (delta>precision) {
            throw new AssertionError("\n\n" + box(desc) + "Expected: " + box(excected) + "\nActual: " + box(actual) + "\nDelta: " + box(delta) + " required " + box(precision) + "\n");
        } else {
            System.out.println(box(desc) + " Scarto " + box(delta) + " atteso " + box(precision));
        }
    }

    public static void assertOrigineCassiniGaussOvest(String desc, Punto3D origine, Punto3D cassini, Punto3D gauss, double precisione) throws ConversionException{
        // Gauss -> Cassini
        GaussRomaToLatLong conv1 = new GaussRomaToLatLong(FusoGauss.FUSO_OVEST);
        Punto3D latlong = conv1.convert(gauss);
        LatLongToCassini conv = new LatLongToCassini(origine.y,origine.x);
        Punto3D cassini_calcolato = conv.convert(latlong);
        AssertUtils.assertEquals(desc + " (gauss->cassini)", cassini, cassini_calcolato, precisione);

        // Cassini -> Gauss
        CassiniToLatLong conv2 = new CassiniToLatLong(origine.y,origine.x);
        Punto3D latlong_2 = conv2.convert(cassini);
        LatLongToGaussRoma conv3 = new LatLongToGaussRoma(FusoGauss.FUSO_OVEST);
        Punto3D gauss_calcolato = conv3.convert(latlong_2);
        AssertUtils.assertEquals(desc + " (cassini->gauss)", gauss, gauss_calcolato, precisione);
    }

    public static String box(Object value) {
        if (value==null) {
            return "[null]";
        } else {
            return "[" + value.toString() + "]";
        }
    }
}
