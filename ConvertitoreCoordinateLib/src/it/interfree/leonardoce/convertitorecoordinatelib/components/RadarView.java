package it.interfree.leonardoce.convertitorecoordinatelib.components;

import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class RadarView extends View {
    private Dati datiDaDisegnare;
    Path stradaNord = new Path();
    Path perAngolo = new Path();
    Paint p = new Paint();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    DecimalFormat format = new DecimalFormat("##0.00");
    private final static double raggioBolla = 45;
    OriginiCassiniManager gestorePreferenze;

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        datiDaDisegnare = new Dati();
        gestorePreferenze = new OriginiCassiniManager(context);
    }

    public void setStatoGps(GpsStatus pStatus, Location loc) {
        datiDaDisegnare.updateFromGpsStatus(pStatus);
        invalidate();
    }

    public void setBussola(double stato, double campo) {
        //		if (datiDaDisegnare!=null && Math.abs(stato-datiDaDisegnare.bussola)>3)
        //	{
        datiDaDisegnare.bussola = stato;
        datiDaDisegnare.campoMagnetico = campo;
        invalidate();
        //	}
    }

    public void setBolla(double pitch, double roll) {

        if (pitch < (-raggioBolla)) {
            pitch = -raggioBolla;
        } else if (pitch > raggioBolla) {
            pitch = raggioBolla;
        }

        if (roll < (-raggioBolla)) {
            roll = -raggioBolla;
        } else if (roll > raggioBolla) {
            roll = raggioBolla;
        }

		/*		if (datiDaDisegnare!=null &&
            (Math.abs(pitch-datiDaDisegnare.pitch)>1
			|| Math.abs(roll-datiDaDisegnare.roll)>1) )
			{*/
        datiDaDisegnare.pitch = pitch;
        datiDaDisegnare.roll = roll;
        invalidate();
			/*		}*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Ambiente di lavoro
        int height = getHeight();
        int width = getWidth();

        float centroX = width / 2;
        float centroY = height / 2;

        canvas.translate(centroX, centroY);
        canvas.scale(1, 1);
        canvas.save();

        // Disegna i cerchi concentrici
        p.reset();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(3);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setPathEffect(null);

        float dimensioneTestoPiccolissimo = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        float dimensioneTestoPiccolo = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, metrics);
        float dimensioneTestoGrande = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, metrics);
        p.setTextSize(dimensioneTestoPiccolo);

        float raggioCerchi = Math.min(centroY, centroX) - dimensioneTestoGrande;

        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.rgb(240, 230, 140));
        canvas.drawCircle(0, 0, raggioCerchi, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        canvas.drawCircle(0, 0, raggioCerchi, p);
        p.setStrokeWidth(0);
        canvas.drawCircle(0, 0, raggioCerchi / 2, p);

        p.setTextAlign(Align.CENTER);
        p.setTextSize(dimensioneTestoGrande);

        if (datiDaDisegnare.campoMagnetico >= 70) {
            p.setColor(Color.RED);
        }

        p.setColor(Color.BLACK);

        // Designa lo stato del GPS
        if (datiDaDisegnare == null) {
            return;
        }

        // Ruota in base alla bussola
        canvas.rotate((float) datiDaDisegnare.bussola);

        // Questo disegna la lancia del nord
        float ampiezzaLancia = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);

        if ( gestorePreferenze.getGpsMostraNord() ) {
            stradaNord.reset();
            stradaNord.moveTo(0, -raggioCerchi);
            stradaNord.lineTo(-ampiezzaLancia, 0);
            stradaNord.lineTo(ampiezzaLancia, 0);
            stradaNord.close();
            stradaNord.moveTo(0, -raggioCerchi);
            stradaNord.lineTo(0, 0);

            p.setStrokeWidth(2);
            canvas.drawPath(stradaNord, p);
            p.setStrokeWidth(0);

            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.RED);
            stradaNord.reset();
            stradaNord.moveTo(0, -raggioCerchi);
            stradaNord.lineTo(ampiezzaLancia, 0);
            stradaNord.lineTo(0, 0);
            stradaNord.close();
            canvas.drawPath(stradaNord, p);

            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.rgb(165, 0, 0));
            stradaNord.reset();
            stradaNord.moveTo(0, -raggioCerchi);
            stradaNord.lineTo(-ampiezzaLancia, 0);
            stradaNord.lineTo(0, 0);
            stradaNord.close();
            canvas.drawPath(stradaNord, p);

            p.setStrokeWidth(2);
            p.setColor(Color.rgb(64, 64, 64));
            p.setStyle(Style.FILL_AND_STROKE);
            canvas.drawCircle(0, 0, ampiezzaLancia, p);
            p.setStrokeWidth(0);

            p.setStrokeWidth(2);
            p.setColor(Color.BLACK);
            p.setStyle(Style.STROKE);
            canvas.drawCircle(0, 0, ampiezzaLancia, p);
            p.setStrokeWidth(0);
        }

        // Aumentare dimensione scritta "N"
        p.setTextSize(dimensioneTestoGrande);
        p.setTextAlign(Align.CENTER);

        perAngolo.reset();
        perAngolo.moveTo(-10, -raggioCerchi - 4);
        perAngolo.lineTo(10, -raggioCerchi - 4);

        // Disegna i punti cardinali

        Typeface param = p.setTypeface(Typeface.create((String) null, Typeface.BOLD));
        canvas.drawTextOnPath("N", perAngolo, 0, 0, p);
        p.setTypeface(param);
        p.setTextSize(dimensioneTestoPiccolo);

        perAngolo.reset();
        perAngolo.moveTo(-10, raggioCerchi + 2 + dimensioneTestoPiccolo);
        perAngolo.lineTo(10, raggioCerchi + 2 + dimensioneTestoPiccolo);
        canvas.drawTextOnPath("S", perAngolo, 0, 0, p);

        Rect contenitoreTesto = new Rect();
        p.getTextBounds("E", 0, 1, contenitoreTesto);
        canvas.drawText("E", raggioCerchi + 8, Math.abs(contenitoreTesto.top - contenitoreTesto.bottom) / 2, p);

        p.getTextBounds("O", 0, 1, contenitoreTesto);
        canvas.drawText("O", -raggioCerchi - 4 - p.measureText("O"),
                Math.abs(contenitoreTesto.top - contenitoreTesto.bottom) / 2, p);

        p.setTextSize(dimensioneTestoPiccolissimo);

        // Disegno i satelliti
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        if ( gestorePreferenze.getGpsMostraSatelliti() ) {
            for (DatiSatellite sat : datiDaDisegnare.satelliti) {
                // Trasparenza in base al segnale
                // da 0 a 30 trasparenza, oltre 30 colore pieno
                int raggioSatellite = 0;
                if (sat.snr >= 30) {
                    raggioSatellite = 15;
                } else {
                    raggioSatellite = (int) ((15 * sat.snr) / 30);
                }

                // Verde se usato, rosso se non utilizzato
                if (sat.used) {
                    p.setColor(Color.GREEN);
                } else {
                    p.setColor(Color.RED);
                }


                float radianti = (float) GeodesicUtils.degreeToRadians(sat.azimuth);
                float raggio = (float) (((raggioBolla - sat.elevation) * raggioCerchi) / raggioBolla);
                canvas.drawCircle(
                        (float) Math.sin(radianti) * raggio,
                        -(float) Math.cos(radianti) * raggio,
                        raggioSatellite,
                        p);
            }
        }

        // Disegno i gradi
        p.setStrokeWidth(0);
        p.setColor(Color.BLACK);
        p.setTextAlign(Align.CENTER);
        int gradi = 0;
        while (gradi < 360) {
            float radianti = (float) GeodesicUtils.degreeToRadians(gradi);

            p.setStrokeWidth(0);
            if ((gradi % 30) == 0) {
                float delta = dimensioneTestoPiccolissimo * 1.6f;
                float xdm10 = (float) Math.sin(radianti - 0.1) * (raggioCerchi - delta);
                float ydm10 = -(float) Math.cos(radianti - 0.1) * (raggioCerchi - delta);
                float xdp10 = (float) Math.sin(radianti + 0.1) * (raggioCerchi - delta);
                float ydp10 = -(float) Math.cos(radianti + 0.1) * (raggioCerchi - delta);

                perAngolo.reset();
                perAngolo.moveTo(xdm10, ydm10);
                perAngolo.lineTo(xdp10, ydp10);

                canvas.drawTextOnPath("" + gradi, perAngolo, 0, 0, p);
            }

            float deltaLinea;
            if ((gradi % 10) == 0) {
                p.setStrokeWidth(2);
                deltaLinea = dimensioneTestoPiccolissimo;
            } else {
                p.setStrokeWidth(2);
                deltaLinea = dimensioneTestoPiccolissimo / 2;
            }

            canvas.drawLine(
                    (float) Math.sin(radianti) * raggioCerchi,
                    -(float) Math.cos(radianti) * raggioCerchi,
                    (float) Math.sin(radianti) * (raggioCerchi - deltaLinea),
                    -(float) Math.cos(radianti) * (raggioCerchi - deltaLinea),
                    p);

            gradi += 5;
        }

        // Disegno la prora
        float radiantiPlora = (float) GeodesicUtils.degreeToRadians(-datiDaDisegnare.bussola);
        perAngolo.reset();
        perAngolo.moveTo((float) Math.sin(radiantiPlora - 0.05) * (raggioCerchi + dimensioneTestoPiccolo),
                -(float) Math.cos(radiantiPlora - 0.05) * (raggioCerchi + dimensioneTestoPiccolo));
        perAngolo.lineTo((float) Math.sin(radiantiPlora + 0.05) * (raggioCerchi + dimensioneTestoPiccolo),
                -(float) Math.cos(radiantiPlora + 0.05) * (raggioCerchi + dimensioneTestoPiccolo));
        perAngolo.lineTo((float) Math.sin(radiantiPlora) * (raggioCerchi),
                -(float) Math.cos(radiantiPlora) * (raggioCerchi));
        perAngolo.close();
        p.setColor(Color.rgb(255, 138, 36));
        p.setStyle(Style.FILL_AND_STROKE);

        canvas.drawPath(perAngolo, p);
        p.setColor(Color.BLACK);
        p.setStyle(Style.STROKE);
        p.setStrokeWidth(1);
        canvas.drawPath(perAngolo, p);

        // Contringo la bolla nei margini
        double raggioDisegnoBolla = dimensioneTestoPiccolissimo * 2;
        double disegnoPitch;
        double disegnoRoll;
        double mod0;

        mod0 = Math.sqrt(Math.pow(datiDaDisegnare.roll, 2) + Math.pow(datiDaDisegnare.pitch, 2));
        if (mod0 == 0) {
            disegnoPitch = datiDaDisegnare.pitch;
            disegnoRoll = datiDaDisegnare.roll;
        } else {
            double mod1;
            double alfa1;

            mod1 = Math.min(mod0, raggioBolla * (raggioCerchi - raggioDisegnoBolla) / raggioCerchi);
            alfa1 = Math.asin(datiDaDisegnare.pitch / mod0);

            disegnoRoll = mod1 * Math.abs(Math.cos(alfa1)) * Math.signum(datiDaDisegnare.roll);
            disegnoPitch = mod1 * Math.abs(Math.sin(alfa1)) * Math.signum(datiDaDisegnare.pitch);
        }

        // Disegno la bolla
        canvas.restore();

        if ( gestorePreferenze.getGpsMostraBolla() ) {
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.WHITE);
            canvas.drawCircle(
                    (float) ((disegnoRoll / raggioBolla) * raggioCerchi),
                    (float) ((disegnoPitch / raggioBolla) * raggioCerchi),
                    (float) raggioDisegnoBolla,
                    p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(2);
            p.setColor(Color.rgb(189, 183, 107));
            canvas.drawCircle(
                    (float) ((disegnoRoll / raggioBolla) * raggioCerchi),
                    (float) ((disegnoPitch / raggioBolla) * raggioCerchi),
                    (float) raggioDisegnoBolla,
                    p);
        }

        // Linea nord-sud e e est-ovest
        canvas.rotate((float) datiDaDisegnare.bussola);
        p.setColor(Color.BLACK);
        canvas.drawLine(raggioCerchi, 0, dimensioneTestoPiccolissimo * 2.5f, 0, p);
        canvas.drawLine(-dimensioneTestoPiccolissimo * 2.5f, 0, -raggioCerchi, 0, p);
        canvas.drawLine(0, raggioCerchi, 0, dimensioneTestoPiccolissimo * 2.5f, p);
        canvas.drawLine(0, -dimensioneTestoPiccolissimo * 2.5f, 0, -raggioCerchi, p);
        canvas.drawCircle(0, 0, dimensioneTestoPiccolissimo * 2.5f, p); // bolla

        p.setStrokeWidth(0);

    }

    private static class Dati {
        // Tutti i gradi sono a base 360 est

        /**
         * Simile al getBearing(), la direzione di viaggio rispetto
         * al vero nord espressa in gradi est
         */
        double bussola;
        double campoMagnetico;

        /**
         * Per bolla planare
         */
        double pitch;
        double roll;

        /**
         * Satelliti
         */
        List<DatiSatellite> satelliti;

        public Dati() {
            satelliti = new LinkedList<DatiSatellite>();
        }

        public void updateFromGpsStatus(GpsStatus stat) {
            if (stat != null) {
                satelliti.clear();
                for (GpsSatellite sat : stat.getSatellites()) {
                    DatiSatellite dati = new DatiSatellite(sat);
                    satelliti.add(dati);
                }
            }
        }

        @SuppressWarnings("unused")
        private void initPerProve() {
            bussola = 20;

            satelliti.clear();
            satelliti.add(new DatiSatellite(1, 20, 80, 50, true));
            satelliti.add(new DatiSatellite(2, 50, 20, 30, true));
            satelliti.add(new DatiSatellite(3, 200, 40, 15, true));
            satelliti.add(new DatiSatellite(4, 260, 30, 65, false));
        }
    }

    private static class DatiSatellite {
        //int prn;
        float azimuth;
        float elevation;
        float snr;
        boolean used;

        public DatiSatellite(GpsSatellite sat) {
            //prn = sat.getPrn();
            azimuth = sat.getAzimuth();
            elevation = sat.getElevation();
            snr = sat.getSnr();
            used = sat.usedInFix();
        }

        public DatiSatellite(int prn, float azimuth, float elevation,
                             float snr, boolean used) {
            super();
            //this.prn = prn;
            this.azimuth = azimuth;
            this.elevation = elevation;
            this.snr = snr;
            this.used = used;
        }
    }
}

// Local Variables:
// indent-tabs-mode: nil
// c-indent-level: 4
// End:
