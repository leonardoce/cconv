package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.components.RadarView;
import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.GoogleMapsUtility;
import it.interfree.leonardoce.iconv.utils.ResUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class StatoGpsActivity extends AppCompatActivity implements LocationListener,
        SensorEventListener {
    private Location lastLocation;
    private RadarView radar;
    private GnssStatus lastStatus;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor sensoreMagnetometro, sensoreAccelerometro;
    private TextView txtSatellitiTotali;
    private TextView txtSatellitiUsati;
    private TextView txtUltimoPunto;
    private TextView txtPrecisioneUltimoPunto;
    private TextView txtHeading;
    private long tempoUltimoPunto;
    private Button btConvertiAdesso;
    private Button btInvia;

    private float[] mGeomagnetic;
    private float[] mAccelerometer;
    private double utesla;

    private long lastTimeStamp;

    DecimalFormat format = new DecimalFormat("##0.0");
    double lastBussola = 0;

    // Occhio perche' queste non considerano
    // la taratura, che deve essere sottratta manualmente
    double lastPitch = 0;
    double lastRoll = 0;

    float lastDeltaPitch = 0;
    float lastDeltaRoll = 0;

    // In realta' serve per la calibrazione della
    // bolla
    OriginiCassiniManager gestorePreferenze;

    @SuppressWarnings("unused")
    private static String TAG = "StatoGpsActivity";

    private GnssStatus.Callback gpsCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stato_gps);

        lastLocation = null;
        lastStatus = null;
        tempoUltimoPunto = 0;

        // Componenti
        // ----------

        radar = (RadarView) this.findViewById(R.id.statusRadarView);
        txtSatellitiTotali = (TextView) this
                .findViewById(R.id.txtSatellitiTotali);
        txtSatellitiUsati = (TextView) this
                .findViewById(R.id.txtSatellitiUsati);
        txtUltimoPunto = (TextView) this
                .findViewById(R.id.txtUltimoPunto);
        txtPrecisioneUltimoPunto = (TextView) this
                .findViewById(R.id.txtPrecisioneUltimoPunto);
        txtHeading = (TextView) this
                .findViewById(R.id.txtHeading);
        btConvertiAdesso = (Button) this.findViewById(R.id.btConvertiAdesso);

        // Listener
        // --------
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensoreMagnetometro = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensoreAccelerometro = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btConvertiAdesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertiAdesso();
            }
        });
        btConvertiAdesso.setEnabled(false);

        // Per la calibrazione della bolla
        // -------------------------------

        gestorePreferenze = new OriginiCassiniManager(this);
        Button btBolla = (Button) this.findViewById(R.id.btBolla);
        btInvia = (Button) this.findViewById(R.id.btSend);

        if (btBolla != null) {
            btBolla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StatoGpsActivity.this);
                    builder.setTitle(R.string.stato_gps_bolla);
                    builder.setItems(R.array.scelte_imposta_bolla, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                calibrazioneBolla();
                            } else {
                                resetBolla();
                            }
                        }
                    });

                    builder.create().show();
                }
            });
        }

        if (btInvia != null) {
            btInvia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviaPosizione();
                }
            });
            btInvia.setEnabled(false);
        }

        lastDeltaRoll = gestorePreferenze.getCalibrazioneRoll();
        lastDeltaPitch = gestorePreferenze.getCalibrazionePitch();

        gpsCallback = new GnssStatus.Callback() {
            @Override
            public void onStarted() {
                Log.d("GNSS", "Started");
            }

            @Override
            public void onStopped() {
                Log.d("GNSS", "Stopped");
            }

            @Override
            public void onFirstFix(int ttffMillis) {
                Log.d("GNSS", String.format("First fix %d", ttffMillis));
            }

            @Override
            public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                StatoGpsActivity.this.onSatelliteStatusChanged(status);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gpsCallback);
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastTimeStamp = System.currentTimeMillis();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSIONS", "The user denied the permission request ACCESS_FINE_LOCATION");
            finish();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSIONS", "The user denied the permission request ACCESS_COARSE_LOCATION");
            finish();
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                100L, 0f, this);
        locationManager.registerGnssStatusCallback(gpsCallback);

        sensorManager.registerListener(this, sensoreMagnetometro,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensoreAccelerometro,
                SensorManager.SENSOR_DELAY_UI);
    }

    public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
        lastStatus = status;
        radar.setStatoGps(lastStatus, lastLocation);

        int satelliteCount = lastStatus.getSatelliteCount();
        int usedSatelliteCount = 0;
        for(int i=0; i<satelliteCount; i++) {
            if(lastStatus.usedInFix(i)) {
                usedSatelliteCount++;
            }
        }
        txtSatellitiTotali.setText(
                String.format(Locale.getDefault(), "%d", satelliteCount));
        txtSatellitiUsati.setText(
                String.format(Locale.getDefault(), "%d", usedSatelliteCount));
    }

    private void aggiornaPrecisione() {
        String precisione = "";

        if (lastLocation != null && lastLocation.hasAccuracy()) {
            DecimalFormat dec = new DecimalFormat("#0.00");
            precisione = dec.format(lastLocation.getAccuracy()) + " m";
        } else {
            precisione = "n.d.";
        }

        // Campo magnetico bussola
        precisione += " / " + format.format(utesla) + " uT";
        txtPrecisioneUltimoPunto.setText(precisione);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        radar.setStatoGps(lastStatus, lastLocation);

        // Qui bisogna fare una "pre-conversione" nei
        // vari sistemi di coordinate in base alla scelta dell'utente

        Punto3D orig = new Punto3D(lastLocation.getLongitude(),
                lastLocation.getLatitude(), 0);
        DecimalFormat dec = new DecimalFormat("###,##0.00");

        if ( gestorePreferenze.getGpsTipoCoordinate().equals("latlong") ) {
            txtUltimoPunto.setText(ResUtils.formatLatitude(this, location.getLatitude()) + "\n" + ResUtils.formatLongitude(this, location.getLongitude()));
        } else if (gestorePreferenze.getGpsTipoCoordinate().equals("utm")) {

            try {
                PuntoUTM destUTM;
                ICoordinateConversion proiezione = new LatLongToUTMZona(this);
                destUTM = (PuntoUTM) proiezione.convert(orig);

                txtUltimoPunto.setText(destUTM.getZona() + "T\n" + dec.format(destUTM.y) + "\n" + dec.format(destUTM.x));
            } catch (Exception e) {
            }

        } else if (gestorePreferenze.getGpsTipoCoordinate().equals("cassini")) {
            Punto3D destCassini;

            try {
                ICoordinateConversion proiezione = new LatLongToCassiniOrigine(this);
                destCassini = proiezione.convert(orig);
                // Occhio: non e' uno scambio x/y
                txtUltimoPunto.setText("X:" + dec.format(destCassini.y) + "\nY:" + dec.format(destCassini.x));
            } catch (Exception e) {
            }


        } else if (gestorePreferenze.getGpsTipoCoordinate().equals("gauss")) {
            RisultatoConversione risultato = new RisultatoConversione();

            Punto3D destGauss;
            risultato.lat = orig.y;
            risultato.longi = orig.x;

            try {
                ICoordinateConversion conv = new LatLongToGaussRomaOrigine(this);
                destGauss = conv.convert(orig);

                risultato.nord = destGauss.y;
                risultato.est = destGauss.x;

                txtUltimoPunto.setText("N:" + dec.format(destGauss.y) + "\nE:" + dec.format(destGauss.x));
            } catch (Exception e) {
            }


        } else if (gestorePreferenze.getGpsTipoCoordinate().equals("mgrs")) {
            try {
                ICoordinateConversion conversioneMGRS = new LatLongToMGRS();
                Punto3D risultatoMgrs = conversioneMGRS.convert(orig);
                txtUltimoPunto.setText(risultatoMgrs.toString());
            } catch (Exception exc) {
            }
        }


        btConvertiAdesso.setEnabled(true);
        btInvia.setEnabled(true);

        if (tempoUltimoPunto == 0) {
            tempoUltimoPunto = System.currentTimeMillis();
        } else {
            long tempo = System.currentTimeMillis();
            // float tempoPunto = (tempo-tempoUltimoPunto)/1000f;
            tempoUltimoPunto = tempo;
        }

        aggiornaPrecisione();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void convertiAdesso() {
        if (lastLocation == null) {
            return;
        }

        // Proietto a Gauss Roma
        RisultatoConversione risultato = new RisultatoConversione();

        Punto3D destGauss;
        Punto3D orig = new Punto3D(lastLocation.getLongitude(),
                lastLocation.getLatitude(), 0);
        risultato.lat = orig.y;
        risultato.longi = orig.x;

        try {
            ICoordinateConversion conv = new LatLongToGaussRomaOrigine(this);
            destGauss = conv.convert(orig);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.msg_coordinata_non_congruente))
                    .setTitle(getString(R.string.app_name)).show();
            return;
        }

        risultato.nord = destGauss.y;
        risultato.est = destGauss.x;

        // Cambio datum a ED50
        Punto3D destED50;
        try {
            ICoordinateConversion conv = new LatLongToED50();
            destED50 = conv.convert(orig);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.msg_coordinata_non_congruente))
                    .setTitle(getString(R.string.app_name))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
            return;
        }

        risultato.lat_ed50 = destED50.y;
        risultato.longi_ed50 = destED50.x;

        // Proietto a Cassini Soldner

        Punto3D destCassini;

        try {
            ICoordinateConversion proiezione = new LatLongToCassiniOrigine(this);
            destCassini = proiezione.convert(orig);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.msg_coordinata_non_congruente))
                    .setTitle(getString(R.string.app_name)).show();
            return;
        }

        risultato.x = destCassini.y;
        risultato.y = destCassini.x;

        // Proietto a UTM

        try {
            PuntoUTM destUTM;
            ICoordinateConversion proiezione = new LatLongToUTMZona(this);
            destUTM = (PuntoUTM) proiezione.convert(orig);
            risultato.utm_est = destUTM.x;
            risultato.utm_nord = destUTM.y;
            risultato.zonaUtm = destUTM.getZona();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.msg_coordinata_non_congruente))
                    .setTitle(getString(R.string.app_name)).show();
            return;
        }

        // Conversione verso MGRS
        try {
            ICoordinateConversion conversioneMGRS = new LatLongToMGRS();
            Punto3D risultatoMgrs = conversioneMGRS.convert(orig);
            risultato.puntoMgrs = risultatoMgrs.toString();
        } catch (Exception exc) {
            risultato.puntoMgrs = "<KO>";
        }

        // Tipo di conversione
        risultato.tipo_ingresso = "Da GPS ";
        risultato.descrizione_punto = " (" + this.getString(R.string.stato_gps_heading) + " " +
                (int) (360 - lastBussola) + "\u00b0, " +
                this.getString(R.string.stato_gps_precisione) + " " + txtPrecisioneUltimoPunto.getText() + ")";
        risultato.initPropertiesFromContext(this);

        // Faccio vedere il risultato
        Intent passaggioRisultato = new Intent(this,
                RisultatoConversioneActivity.class);
        passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE,
                risultato);
        startActivity(passaggioRisultato);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long currentTimeStamp = System.currentTimeMillis();

        // l'incastro succede sempre sull'accelerometro

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (mAccelerometer == null) {
                mAccelerometer = event.values.clone();
            } else {
                lowPassFilter(event.values, mAccelerometer, 0.2f);
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            if (mGeomagnetic == null) {
                mGeomagnetic = event.values.clone();
            } else {
                lowPassFilter(event.values, mGeomagnetic, 0.2f);
            }
        }

        if ((currentTimeStamp - lastTimeStamp) < 15) return;

        if (mGeomagnetic != null && mAccelerometer != null) {

            float R[] = new float[9];
            float outR[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometer,
                    mGeomagnetic);

            if (success) {
                float orientation[] = new float[3];

                // Qui deve essere ruotata
                int rotation = this.getWindowManager().getDefaultDisplay().getRotation();

                if (rotation == Surface.ROTATION_0) {
                    // nop()
                    outR = R; //AXIS_X, AXIS_Y
                } else if (rotation == Surface.ROTATION_90) {
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, outR);
                } else if (rotation == Surface.ROTATION_180) {
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, outR);
                } else if (rotation == Surface.ROTATION_270) {
                    SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, outR);
                }

                SensorManager.getOrientation(outR, orientation);

                double bussola = -GeodesicUtils.radiansToDegree(orientation[0]);

                if (bussola < 0) {
                    bussola += 360;
                }

                double pitch = GeodesicUtils.radiansToDegree(orientation[1]);
                double roll = GeodesicUtils.radiansToDegree(orientation[2]);

                utesla = (double) Math.sqrt(mGeomagnetic[0] * mGeomagnetic[0] +
                        mGeomagnetic[1] * mGeomagnetic[1] +
                        mGeomagnetic[2] * mGeomagnetic[2]);


                // Questo mette i dati nel logcat in modo da poter
                // debuggare i dati del sensore
                // Log.d("Orientamento", bussola + "\t" + pitch + "\t" + roll);

                        /*bussola = smussaDati(lastBussola, bussola, currentTimeStamp-lastTimeStamp,
                                             360.0/32000.0, 360.0/4000.0, 0.5, false);

                        pitch = smussaDati(lastPitch, pitch, currentTimeStamp-lastTimeStamp,
                                           (90.0/16000.0)/4, 90.0/10000.0, 0.1, false);

                        roll = smussaDati(lastRoll, roll, currentTimeStamp-lastTimeStamp,
                                          (90.0/16000.0)/4, 90.0/10000.0, 0.1, false);*/

                utesla = (double) Math.sqrt(mGeomagnetic[0] * mGeomagnetic[0] +
                        mGeomagnetic[1] * mGeomagnetic[1] +
                        mGeomagnetic[2] * mGeomagnetic[2]);
                aggiornaPrecisione();

                radar.setBussola(bussola, utesla);
                radar.setBolla(pitch - lastDeltaPitch, roll - lastDeltaRoll);

                txtHeading.setText("" + (int) (360 - bussola));

                lastBussola = bussola;
                lastTimeStamp = currentTimeStamp;
                lastPitch = pitch;
                lastRoll = roll;
            }
        }

    }

    public void calibrazioneBolla() {
        lastDeltaPitch = (float) lastPitch;
        lastDeltaRoll = (float) lastRoll;
        gestorePreferenze.impostaCalibrazioneRoll(lastDeltaRoll);
        gestorePreferenze.impostaCalibrazionePitch(lastDeltaPitch);
    }

    public void resetBolla() {
        lastDeltaPitch = 0;
        lastDeltaRoll = 0;
        gestorePreferenze.impostaCalibrazioneRoll(lastDeltaRoll);
        gestorePreferenze.impostaCalibrazionePitch(lastDeltaPitch);
    }

    /**
     * This low-pass filter, inplemented as a exponential mean, is applied to the
     * sensor inputs
     *
     * @param input
     * @param output
     * @param alfa
     */
    private static void lowPassFilter(float input[], float output[], float alfa) {
        if (output == null || input == null) return;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alfa * (input[i] - output[i]);
        }
    }

    public void inviaPosizione() {
        // In questo caso tutti gli utenti hanno diritto ad avere una
        // conversione in coordinate UTM

        Punto3D orig = new Punto3D(lastLocation.getLongitude(),
                lastLocation.getLatitude(), 0);

        PuntoUTM destUTM;
        try {
            ICoordinateConversion proiezione = new LatLongToUTMZona(this);
            destUTM = (PuntoUTM) proiezione.convert(orig);
        } catch (Exception e) {
            return;
        }

        // Vediamo di comporre un messaggio
        String messaggio = this.getString(R.string.stato_gps_sharing);
        messaggio = String.format(messaggio,
                ResUtils.formatLatLong(this, lastLocation.getLatitude(), lastLocation.getLongitude()),
            lastLocation.getAccuracy(),
            String.format("%sT N: %.2f E:%.2f", destUTM.getZona(), destUTM.y, destUTM.x),
                DateFormat.getDateTimeInstance().format(new Date()),
                GoogleMapsUtility.getGoogleMapsUrl(lastLocation.getLatitude(), lastLocation.getLongitude()));

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.format(this.getString(R.string.stato_gps_sharing_title), DateFormat.getDateTimeInstance().format(new Date())));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, messaggio);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.stato_gps_send)));
    }
}
