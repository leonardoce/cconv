package it.interfree.leonardoce.convertitorecoordinatelib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import it.interfree.leonardoce.convertitorecoordinatelib.config.CurrentConfiguration;
import it.interfree.leonardoce.iconv.appconvs.LatLongToCassiniOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToGaussRomaOrigine;
import it.interfree.leonardoce.iconv.appconvs.LatLongToUTMZona;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.core.convs.LatLongToED50;
import it.interfree.leonardoce.iconv.core.convs.LatLongToMGRS;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.OSMUtility;
import it.interfree.leonardoce.iconv.utils.Indirizzo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;


/**
 * @author leonardo
 */
public class InputOSMActivity extends AppCompatActivity {

    private boolean coordinataDaGps = false;
    private Button btCercaMaps;
    private Button btConvertiLatLong;
    private EditText editIndirizzo;
    private MapView webMapView;
    private MyItemizedOverlay mOverlays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_osm);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // -------------------
        // Prende i componenti
        // -------------------

        btCercaMaps = (Button) findViewById(R.id.btCercaMaps);
        editIndirizzo = (EditText) findViewById(R.id.editTextMapIndirizzo);
        btConvertiLatLong = (Button) findViewById(R.id.btConvertiLatLong);
        webMapView = (MapView) findViewById(R.id.mapview);

        // ------------------------
        // Ascoltatore degli eventi
        // ------------------------

        btConvertiLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConverti();
            }
        });

        btCercaMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVai();
            }
        });

        configuraOSM();
    }

    private IGeoPoint getPunto() {
        if ( webMapView.getOverlays().isEmpty() ) {
            return null;
        } else if ( mOverlays.size()==0 ) {
            return null;
        } else {
            return mOverlays.getItem(0).getPoint();
        }

    }

    private void setPunto(GeoPoint p) {
        OverlayItem overlayitem = new OverlayItem("Fix", "", p);
        mOverlays.clear();
        mOverlays.addOverlay(overlayitem);

        if ( p.getLatitudeE6()==0 && p.getLongitudeE6()==0 ) {
            webMapView.getController().setZoom(3);
        } else {
            webMapView.getController().setZoom(15);
        }

        webMapView.getController().setCenter(p);
    }

    private void setPunto(Indirizzo a) {
        GeoPoint p = new GeoPoint(a.lat, a.lon);
        setPunto(p);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        GeoPoint p = new GeoPoint(savedInstanceState.getInt("punto_lat"),
                savedInstanceState.getInt("punto_lon"));
        setPunto(p);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salvo il punto nel quale ero
        outState.putInt("punto_lat", getPunto().getLatitudeE6());
        outState.putInt("punto_lon", getPunto().getLongitudeE6());
    }

    private void configuraOSM() {
        webMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        webMapView.setBuiltInZoomControls(true);
        webMapView.getController().setZoom(13);
        webMapView.setMultiTouchControls(true);
        webMapView.getOverlays().clear();
        webMapView.setMaxZoomLevel(30.0);

        Drawable drawable = this.getResources().getDrawable(R.drawable.pin_blu);
        mOverlays = new MyItemizedOverlay(drawable, this);
        webMapView.getOverlays().add(mOverlays);

        // Giusto per puntare da qualche parte deve andare in centro
        // al mondo, analogamente a quello che accade con Google Maps
        GeoPoint p = new GeoPoint(0, 0);
        setPunto(p);

    }

    @Override
    protected void onStart() {
        super.onStart();
        editIndirizzo.requestFocus();
    }

    // ------------------------------------
    // Va ad un certo indirizzo nelle mappe
    // ------------------------------------
    private String addressToString(Address a) {
        String result = "";
        int i = 0;

        result += string2null(a.getAddressLine(0)) + " (" + string2null(a.getLocality()) + ")";
        return result;
    }

    private String string2null(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    private void onVai() {
        String daCercare = editIndirizzo.getText().toString();
        if (daCercare.length() == 0) {
            return;
        }

        AzioneRicercaIndirizzoOSM azione = new AzioneRicercaIndirizzoOSM();
        azione.execute(daCercare);
    }

    private void scegliFraIndirizzi(final List<Indirizzo> risultati) {
        int i;

        if ( risultati==null ) {
            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.msg_no_internet))
                .setTitle(getString(R.string.app_name))
                .setPositiveButton(getString(R.string.ok), null)
                .show();
        } else if (risultati.isEmpty()) {
            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.msg_no_address))
                .setTitle(getString(R.string.app_name))
                .setPositiveButton(getString(R.string.ok), null)
                .show();
        } else if (risultati.size() == 1) {
            setPunto( risultati.get(0) );

        } else if (risultati.size() > 0) {
            String scelte[] = new String[risultati.size()];

            for (i = 0; i < risultati.size(); i++) {
                scelte[i] = risultati.get(i).nome;
            }

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setSingleChoiceItems(scelte, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        setPunto( risultati.get(arg1) );

                        arg0.dismiss();
                    }
                });
            adb.setNegativeButton(R.string.chiudi, null);
            adb.setTitle(R.string.input_osm_choose);
            adb.show();

        }
    }

    // -------------------------------------------
    // Gestione della conversione delle coordinate
    // -------------------------------------------
    private void onConverti() {
        if (getPunto() == null) {
            return;
        }

        // Proietto a Gauss Roma
        RisultatoConversione risultato = new RisultatoConversione();
        // Tipo di conversione
        risultato.tipo_ingresso = "Da Open Street Map ";
        if (coordinataDaGps) {
            risultato.tipo_ingresso += "(GPS)";
        }
        risultato.initPropertiesFromContext(this);

        Punto3D destGauss, destED50;

        IGeoPoint gp = getPunto();

        Punto3D orig = new Punto3D();
        orig.y = gp.getLatitudeE6()/1000000.0;
        orig.x = gp.getLongitudeE6()/1000000.0;
        risultato.lat = orig.y;
        risultato.longi = orig.x;

        risultato.descrizione_punto = editIndirizzo.getText().toString();

	// Adesso converto in tutti gli altri sistemi
	try {
		ConversioniUtils utils = new ConversioniUtils(this, risultato);
		utils.conversioneInLatLongED50();
		utils.conversioneInGauss();
		utils.conversioneInCassini();
		utils.conversioneInUTM();
		utils.conversioneInMGRS();
		utils.conversioneInWebMercator();
	} catch(Exception e) {
		new AlertDialog.Builder(this)
			.setMessage(getString(R.string.msg_coordinata_non_congruente))
			.setTitle(getString(R.string.app_name))
			.setPositiveButton(getString(R.string.ok), null)
			.show();		
		return;
	}

        // Faccio vedere il risultato
        Intent passaggioRisultato = new Intent(this, RisultatoConversioneActivity.class);
        passaggioRisultato.putExtra(ActivityGlobals.RISULTATO_CONVERSIONE, risultato);
        startActivity(passaggioRisultato);
    }

    class AzioneRicercaIndirizzoOSM extends AsyncTask<String, Void, List<Indirizzo> > {
        protected List<Indirizzo> doInBackground(String... ricerca) {
            try {
                return OSMUtility.trascodificaNominatim(ricerca[0]);
            } catch(IOException ioe) {
                return null;
            }
        }

        protected void onPostExecute(List<Indirizzo> risultato) {
            scegliFraIndirizzi(risultato);
        }
    }
}

class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    OverlayItem inDrag;
    private int xDragTouchOffset, yDragTouchOffset;
    private int xDragImageOffset = 0;
    private int yDragImageOffset = 0;
    private String TAG = "it.interfree.leonardoce.convertitorecoordinatelib.MyItemizedOverlay";
    private ImageView dragImage = null;
    private Drawable marker;

    public MyItemizedOverlay(Drawable defaultMarker, Activity ctx) {
        super(defaultMarker);
        marker = defaultMarker;
        dragImage = (ImageView) ctx.findViewById(R.id.drag);
        xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
        yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    public boolean onSnapToItem(int i, int i1, Point point, IMapView imv) {
        return false;
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    public void clear() {
        mOverlays.clear();
        populate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        final int action = event.getAction();
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final Projection pj = mapView.getProjection();
        Point p = new Point(); // reuse

        boolean result = false;

        if (action == MotionEvent.ACTION_DOWN) {
            for (OverlayItem item : mOverlays) {

                pj.toPixels(item.getPoint(), p);

                if (hitTest(item, marker, x - p.x, y - p.y)) {
                    result = true;
                    inDrag = item;
                    mOverlays.remove(inDrag);
                    populate();

                    xDragTouchOffset = 0;
                    yDragTouchOffset = 0;

                    setDragImagePosition(x, y);
                    dragImage.setVisibility(View.VISIBLE);

                    xDragTouchOffset = x - p.x;
                    yDragTouchOffset = y - p.y;

                    break;
                }
            }
        } else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
            dragImage.setVisibility(View.VISIBLE);
            setDragImagePosition(x, y);
            result = true;
        } else if (action == MotionEvent.ACTION_UP && inDrag != null) {
            dragImage.setVisibility(View.GONE);

            IGeoPoint g = pj.fromPixels(x - xDragTouchOffset, y - yDragTouchOffset);
            GeoPoint pt = new GeoPoint(g.getLatitudeE6(), g.getLongitudeE6());
            OverlayItem toDrop = new OverlayItem(inDrag.getTitle(),
                    inDrag.getSnippet(), pt);

            mOverlays.add(toDrop);
            populate();
            inDrag = null;
            result = true;

            if ((x - p.x) == xDragTouchOffset && (y - p.y) == yDragTouchOffset) {
                Log.d(TAG, "Do something here if desired because we didn't move item " + toDrop.getTitle());
            }
        }

        return (result || super.onTouchEvent(event, mapView));
    }

    private void setDragImagePosition(int x, int y) {
        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) dragImage.getLayoutParams();

        lp.setMargins(x - xDragImageOffset - xDragTouchOffset,
                y - yDragImageOffset - yDragTouchOffset, 0, 0);
        dragImage.setLayoutParams(lp);
    }
}
