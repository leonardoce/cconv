package it.interfree.leonardoce.iconv.db;

import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OriginiCassiniManager implements IOriginiStorage
{
    private Context context;
    private SQLiteDatabase db;
    public static final String ID_PREFERENZE = "PreferenzeOrigine";
    private static final String KEY_ORIGINE_CASSINI = "origineCassini";
    public static final String DEF_ORIGINE_CASSINI = "torreMangiaSiena";
    private static final String KEY_FUSO_GAUSS = "fusoGauss";
    private static final int DEF_FUSO_GAUSS = FusoGauss.FUSO_OVEST.ordinal();
    private static final String KEY_ZONA_UTM = "zonaUTM";
    private static final int DEF_ZONA_UTM=32;
    private static final String KEY_DATUM_UTM = "datumUTM";
    private static final String DEF_DATUM_UTM = IConvDatumNames.ED50.name();

    private static final String KEY_CALIBRAZIONE_PITCH = "calibrazionePitch";
    private static final String KEY_CALIBRAZIONE_ROLL = "calibrazioneRoll";
    private static float DEF_CALIBRAZIONE_PITCH = 0;
    private static float DEF_CALIBRAZIONE_ROLL = 0;

    public OriginiCassiniManager(Context pContext)
    {
        context = pContext;
        db = new OriginiOpenHelper(context, null).getWritableDatabase();
    }

    // = Gestione delle origini Cassini =

    @Override
    public List<OrigineCassini> getOrigini()
    {
        LinkedList<OrigineCassini> result = new LinkedList<OrigineCassini>();
        Cursor c = db.rawQuery("SELECT id, x, y FROM origini ORDER BY id", null);

        try
            {
                while(c.moveToNext())
                    {
                        OrigineCassini orig = new OrigineCassini(c.getString(0),
                                                                 new Punto3D(c.getDouble(1), c.getDouble(2), 0)
                                                                 );
                        result.add(orig);
                    }
            }
        finally
            {
                c.close();
            }

        return result;
    }

    @Override
    public void save(OrigineCassini value)
    {
        ContentValues content = new ContentValues();
        content.put("id", value.getDescrizioneString());
        content.put("x", value.getFix().x);
        content.put("y", value.getFix().y);

        if (getOrigine(value.getDescrizioneString())==null)
            {
                db.insert("origini", null, content);
            }
        else
            {
                db.update("origini", content, "WHERE id=?", new String [] {value.getDescrizioneString()});
            }
    }

    @Override
    public OrigineCassini getOrigine(String nome)
    {
        Cursor c = db.rawQuery("SELECT id, x, y FROM origini WHERE id=?", new String[] {nome});
        OrigineCassini result = null;

        try
            {
                if(c.moveToFirst())
                    {
                        result = new OrigineCassini(c.getString(0),
                                                    new Punto3D(c.getDouble(1), c.getDouble(2), 0)
                                                    );
                    }
            }
        finally
            {
                c.close();
            }

        return result;
    }

    @Override
    public void close()
    {
        db.close();
    }

    @Override
    public void deleteOrigineCassini(String nome)
    {
        db.delete("origini", "id=?", new String[] {nome});
    }

    @Override
    public boolean isPredefinita(String nome)
    {
        return nome.equals(DEF_ORIGINE_CASSINI);
    }

    // = Gestione dell'origine selezionata =

    @Override
    public String getIdOrigineSelezionata()
    {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getString(KEY_ORIGINE_CASSINI, DEF_ORIGINE_CASSINI);
    }

    @Override
    public void setIdOrigineSelezionata(String id)
    {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        Editor e = pref.edit();
        e.putString(KEY_ORIGINE_CASSINI, id);
        e.commit();
    }

    // = Gestione del log delle conversioni =

    @Override
    public void addToLogConversioni(RisultatoConversione risultato)
    {
        ContentValues content = new ContentValues();
        content.put("time_last", risultato.time_last);
        content.put("tipo_ingresso", risultato.tipo_ingresso);
        content.put("x", risultato.x);
        content.put("y", risultato.y);
        content.put("latitude", risultato.lat);
        content.put("longitude", risultato.longi);
        content.put("nord", risultato.nord);
        content.put("est", risultato.est);
        content.put("fuso_gauss", risultato.fuso.getId());
        content.put("origine_cassini", risultato.idOrigineCassini);
        content.put("descrizione_punto", risultato.descrizione_punto);
        content.put("zona_utm", risultato.zonaUtm);
        content.put("x_utm", risultato.utm_est);
        content.put("y_utm", risultato.utm_nord);
        content.put("datum_utm", risultato.utm_datum);
        content.put("mgrs", risultato.puntoMgrs);
        content.put("latitude_ed50", risultato.lat_ed50);
        content.put("longitude_ed50", risultato.longi_ed50);
        content.put("webmercator_x", risultato.webmercator_x);
        content.put("webmercator_y", risultato.webmercator_y);

        db.insert("registro", null, content);

        // Vediamo cosa ho messo
        Cursor c = db.rawQuery("SELECT max(id) FROM registro", new String[0]);
        try
            {
                if (c.moveToFirst())
                    {
                        risultato.id = c.getInt(0);
                    }
            } finally
            {
                c.close();
            }
    }

    @Override
    public void updateLogConversioni(RisultatoConversione risultato)
    {
        ContentValues content = new ContentValues();
        content.put("time_last", risultato.time_last);
        content.put("tipo_ingresso", risultato.tipo_ingresso);
        content.put("x", risultato.x);
        content.put("y", risultato.y);
        content.put("latitude", risultato.lat);
        content.put("longitude", risultato.longi);
        content.put("nord", risultato.nord);
        content.put("est", risultato.est);
        content.put("fuso_gauss", risultato.fuso.getId());
        content.put("origine_cassini", risultato.idOrigineCassini);
        content.put("descrizione_punto", risultato.descrizione_punto);
        content.put("zona_utm", risultato.zonaUtm);
        content.put("x_utm", risultato.utm_est);
        content.put("y_utm", risultato.utm_nord);
        content.put("datum_utm", risultato.utm_datum);
        content.put("mgrs", risultato.puntoMgrs);
        content.put("latitude_ed50", risultato.lat_ed50);
        content.put("longitude_ed50", risultato.longi_ed50);
        content.put("webmercator_x", risultato.webmercator_x);
        content.put("webmercator_y", risultato.webmercator_y);

        db.update("registro", content, "id=?", new String[]{""+risultato.id});
    }

    @Override
    public void clearLogConversioni()
    {
        db.delete("registro", "1=1", new String[] {});
    }

    @Override
    public List<RisultatoConversione> getLog()
    {
        LinkedList<RisultatoConversione> result = new LinkedList<RisultatoConversione>();

        Cursor c = db.rawQuery("SELECT time_last, tipo_ingresso, x, y, latitude, longitude, " +
                               "nord, est, fuso_gauss, origine_cassini, descrizione_punto, " +
                               "id, zona_utm, x_utm, y_utm, datum_utm, mgrs, latitude_ed50, longitude_ed50, webmercator_x, webmercator_y " +
                               "FROM registro ORDER BY id", new String[] {});
        try
            {
                while(c.moveToNext())
                    {
                        RisultatoConversione ris = new RisultatoConversione();
                        ris.time_last = c.getLong(0);
                        ris.tipo_ingresso = c.getString(1);
                        ris.x = c.getDouble(2);
                        ris.y = c.getDouble(3);
                        ris.lat = c.getDouble(4);
                        ris.longi = c.getDouble(5);
                        ris.nord = c.getDouble(6);
                        ris.est = c.getDouble(7);
                        ris.fuso = FusoGauss.get(c.getInt(8));
                        ris.idOrigineCassini = c.getString(9);
                        ris.descrizione_punto = c.getString(10);
                        ris.id = c.getInt(11);
                        ris.zonaUtm = c.getInt(12);
                        ris.utm_est = c.getInt(13);
                        ris.utm_nord = c.getInt(14);
                        ris.utm_datum = c.getString(15);
                        ris.puntoMgrs = c.getString(16);
                        ris.lat_ed50 = c.getDouble(17);
                        ris.longi_ed50 = c.getDouble(18);
                        ris.webmercator_x = c.getDouble(19);
                        ris.webmercator_y = c.getDouble(20);
                        ris.inserito = true;
                        result.add(ris);
                    }
            } finally
            {
                c.close();
            }

        return result;
    }

    @Override
    public void deleteRigaLog(int id)
    {
        db.delete("registro", "id=?", new String[] {""+id});
    }

    // = Gestione del fuso Gauss selezionato =

    public FusoGauss getFusoGaussSelezionato()
    {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return FusoGauss.get(pref.getInt(KEY_FUSO_GAUSS, DEF_FUSO_GAUSS));
    }

    public void setFusoGaussSelezionato(FusoGauss fuso)
    {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        Editor e = pref.edit();
        e.putInt(KEY_FUSO_GAUSS, fuso.getId());
        e.commit();
    }

    // = Gestione della zona UTM selezionata =


    @Override
    public String getDatumUTMSelezionato() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getString(KEY_DATUM_UTM, DEF_DATUM_UTM);
    }

    @Override
    public void setDatumUTMSelezionato(String datum) {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        Editor e = pref.edit();
        e.putString(KEY_DATUM_UTM, datum);
        e.commit();
    }

    // == Gestione della calibrazione ==

    public void impostaCalibrazionePitch(float p) {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        Editor e = pref.edit();
        e.putFloat(KEY_CALIBRAZIONE_PITCH, p);
        e.commit();
    }

    public void impostaCalibrazioneRoll(float p) {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        Editor e = pref.edit();
        e.putFloat(KEY_CALIBRAZIONE_ROLL, p);
        e.commit();
    }

    public float getCalibrazionePitch() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getFloat(KEY_CALIBRAZIONE_PITCH, DEF_CALIBRAZIONE_PITCH);
    }

    public float getCalibrazioneRoll() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getFloat(KEY_CALIBRAZIONE_ROLL, DEF_CALIBRAZIONE_ROLL);
    }

    public boolean getGpsMostraNord() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getBoolean("gps_nord", true);
    }

    public boolean getGpsMostraBolla() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getBoolean("gps_bolla", true);
    }

    public boolean getGpsMostraSatelliti() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getBoolean("gps_satelliti", true);
    }

    public String getGpsTipoCoordinate() {
        SharedPreferences pref = context.getSharedPreferences(ID_PREFERENZE, 0);
        return pref.getString("gps_coordinates", "latlong");
    }
}
