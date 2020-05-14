package it.interfree.leonardoce.iconv.math;

import java.text.DecimalFormat;
import java.util.Formatter;

public class PuntoUTM extends Punto3D {
    private int zona;

    public PuntoUTM(double px, double py, double pz, int zona) {
        super(px, py, pz);
        this.zona = zona;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String toString() {
        char c = this.y>0 ? 'N' : 'S';

        DecimalFormat dec = new DecimalFormat("###,##0.00");
        return ""+zona+c+" "+dec.format(y) + " " + dec.format(x);
    }
}
