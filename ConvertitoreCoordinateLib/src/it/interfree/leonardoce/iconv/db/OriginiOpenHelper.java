package it.interfree.leonardoce.iconv.db;

import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OriginiOpenHelper extends SQLiteOpenHelper {
	private static final String ORIGINI_DATABASE_NAME = "origini.db";
	private static final int latestDatabaseVersion = 7;

	public OriginiOpenHelper(Context pContext, CursorFactory factory) {
		super(pContext, ORIGINI_DATABASE_NAME, factory, latestDatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// = Tabella delle origini =

		String createTabellaOrigini = "create table origini (id string not null primary key, "
				+ "x double precision, y double precision)";
		db.execSQL(createTabellaOrigini);

		// a questo punto mi inserisco anche
		// una origine cassini, intanto per far vedere come
		// si va

		double lat_0 = GeodesicUtils.degreeToDecimal(43, 19, 5.727);
		double lon_0 = GeodesicUtils.degreeToDecimal(11, 19, 55.9583);

		ContentValues content = new ContentValues();
		content.put("id", OriginiCassiniManager.DEF_ORIGINE_CASSINI);
		content.put("x", lon_0);
		content.put("y", lat_0);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "INNSBRUCK (NUOVO CATASTO)");
        content.put("y", 47.270495);
        content.put("x", 11.394035);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "PORDENONE");
        content.put("y", 45.954554);
        content.put("x", 12.660503);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "KRIMBERG (NUOVO CATASTO)");
        content.put("y", 45.929449);
        content.put("x", 14.471782);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE BRONZONE");
        content.put("y", 45.709048);
        content.put("x", 9.990771);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "VERCELLI (PUNTO IDEALE)");
        content.put("y", 45.450433);
        content.put("x", 8.205047);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "LODI");
        content.put("y", 45.31413);
        content.put("x", 9.502994);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "ALESSANDRIA");
        content.put("y", 44.91472);
        content.put("x", 8.61135);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "PORTONOVO");
        content.put("y", 44.532469);
        content.put("x", 11.753367);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "FORTE DIAMANTE");
        content.put("y", 44.461114);
        content.put("x", 8.939474);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE BIGNONE");
        content.put("y", 43.873513);
        content.put("x", 7.733709);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "URBINO");
        content.put("y", 43.725135);
        content.put("x", 12.636264);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "SIENA (TORRE DEL MANGIA)");
        content.put("y", 43.318285);
        content.put("x", 11.332211);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE PENNINO");
        content.put("y", 43.101383);
        content.put("x", 12.888754);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE OCRE");
        content.put("y", 42.256464);
        content.put("x", 13.443123);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "ROMA MTE MARIO");
        content.put("y", 41.924405);
        content.put("x", 12.452135);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "VALLE PALOMBO");
        content.put("y", 41.65103);
        content.put("x", 14.259638);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE CAIRO");
        content.put("y", 41.541532);
        content.put("x", 13.760545);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE PETRELLA");
        content.put("y", 41.322197);
        content.put("x", 13.665572);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "FRANCOLISE");
        content.put("y", 41.182662);
        content.put("x", 14.06383);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "CANCELLO");
        content.put("y", 40.993799);
        content.put("x", 14.430148);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MARIGLIANO");
        content.put("y", 40.925236);
        content.put("x", 14.456036);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MIRADOIS (NAPOLI)");
        content.put("y", 40.863634);
        content.put("x", 14.255497);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE TERMINIO");
        content.put("y", 40.841579);
        content.put("x", 14.937321);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "TARANTO");
        content.put("y", 40.476222);
        content.put("x", 17.228541);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "LECCE");
        content.put("y", 40.352009);
        content.put("x", 18.169302);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "SARDEGNA (PUNTO IDEALE)");
        content.put("y", 40.000409);
        content.put("x", 9.116896);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE BRUTTO");
        content.put("y", 39.140896);
        content.put("x", 16.421881);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "TORRE TITONE");
        content.put("y", 37.849035);
        content.put("x", 12.539843);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE ETNA");
        content.put("y", 37.764674);
        content.put("x", 14.98538);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MONTE CASTELLUCCIO");
        content.put("y", 37.416115);
        content.put("x", 13.779475);
        db.insert("origini", null, content);

        content.clear();
        content.put("id", "MINEO");
        content.put("y", 37.267086);
        content.put("x", 14.692592);
        db.insert("origini", null, content);


        // = Tabella del registro =

		String createTabellaRegistro = "create table registro (id integer primary key autoincrement, "
				+ "time_last integer, tipo_ingresso text, x double precision,"
				+ "y double precision, latitude double precision, longitude double precision,"
				+ "nord double precision, est double precision, fuso_gauss integer, origine_cassini text, descrizione_punto text,"
                + "zona_utm integer, x_utm double precision, y_utm double_precision, datum_utm text, mgrs text, " +
                "latitude_ed50 double precision, longitude_ed50 double precision, webmercator_x double precision, webmercator_y double precision)";
		db.execSQL(createTabellaRegistro);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion<=1 && newVersion>=2)
        {
            // Aggiungo i campi relativi alle coordinate UTM
            db.execSQL("alter table registro add column zona_utm integer");
            db.execSQL("alter table registro add column x_utm double precision");
            db.execSQL("alter table registro add column y_utm double_precision");
            db.execSQL("alter table registro add column datum_utm text");

            db.execSQL("update registro set zona_utm=0");
            db.execSQL("update registro set x_utm=0");
            db.execSQL("update registro set y_utm=0");
            db.execSQL("update registro set datum_utm=''");
        }
		
		if (oldVersion <= 2 && newVersion >=3)
		{
			db.execSQL("alter table registro add column mgrs text");
            db.execSQL("update registro set mgrs=''");
		}
		
		if (oldVersion <= 3 && newVersion >=4 ) {
			db.execSQL("alter table registro add column latitude_ed50 double precision");
			db.execSQL("alter table registro add column longitude_ed50 double precision");
            db.execSQL("update registro set latitude_ed50=0, longitude_ed50=0");
		}

        if (oldVersion <=4 && newVersion>=5 ) {
            ContentValues content = new ContentValues();

            content.clear();
            content.put("id", "INNSBRUCK (NUOVO CATASTO)");
            content.put("y", 47.270495);
            content.put("x", 11.394035);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "PORDENONE");
            content.put("y", 45.954554);
            content.put("x", 12.660503);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "KRIMBERG (NUOVO CATASTO)");
            content.put("y", 45.929449);
            content.put("x", 14.471782);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE BRONZONE");
            content.put("y", 45.709048);
            content.put("x", 9.990771);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "VERCELLI (PUNTO IDEALE)");
            content.put("y", 45.450433);
            content.put("x", 8.205047);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "LODI");
            content.put("y", 45.31413);
            content.put("x", 9.502994);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "ALESSANDRIA");
            content.put("y", 44.91472);
            content.put("x", 8.61135);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "PORTONOVO");
            content.put("y", 44.532469);
            content.put("x", 11.753367);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "FORTE DIAMANTE");
            content.put("y", 44.461114);
            content.put("x", 8.939474);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE BIGNONE");
            content.put("y", 43.873513);
            content.put("x", 7.733709);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "URBINO");
            content.put("y", 43.725135);
            content.put("x", 12.636264);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "SIENA (TORRE DEL MANGIA)");
            content.put("y", 43.318285);
            content.put("x", 11.332211);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE PENNINO");
            content.put("y", 43.101383);
            content.put("x", 12.888754);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE OCRE");
            content.put("y", 42.256464);
            content.put("x", 13.443123);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "ROMA MTE MARIO");
            content.put("y", 41.924405);
            content.put("x", 12.452135);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "VALLE PALOMBO");
            content.put("y", 41.65103);
            content.put("x", 14.259638);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE CAIRO");
            content.put("y", 41.541532);
            content.put("x", 13.760545);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE PETRELLA");
            content.put("y", 41.322197);
            content.put("x", 13.665572);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "FRANCOLISE");
            content.put("y", 41.182662);
            content.put("x", 14.06383);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "CANCELLO");
            content.put("y", 40.993799);
            content.put("x", 14.430148);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MARIGLIANO");
            content.put("y", 40.925236);
            content.put("x", 14.456036);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MIRADOIS (NAPOLI)");
            content.put("y", 40.863634);
            content.put("x", 14.255497);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE TERMINIO");
            content.put("y", 40.841579);
            content.put("x", 14.937321);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "TARANTO");
            content.put("y", 40.476222);
            content.put("x", 17.228541);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "LECCE");
            content.put("y", 40.352009);
            content.put("x", 18.169302);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "SARDEGNA (PUNTO IDEALE)");
            content.put("y", 40.000409);
            content.put("x", 9.116896);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE BRUTTO");
            content.put("y", 39.140896);
            content.put("x", 16.421881);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "TORRE TITONE");
            content.put("y", 37.849035);
            content.put("x", 12.539843);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE ETNA");
            content.put("y", 37.764674);
            content.put("x", 14.98538);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MONTE CASTELLUCCIO");
            content.put("y", 37.416115);
            content.put("x", 13.779475);
            db.insert("origini", null, content);

            content.clear();
            content.put("id", "MINEO");
            content.put("y", 37.267086);
            content.put("x", 14.692592);
            db.insert("origini", null, content);
        }

        if (oldVersion <=5 && newVersion>=6 )
        {
        	// Campi relativi a Web Mercator
            db.execSQL("alter table registro add column webmercator_x double precision");
            db.execSQL("alter table registro add column webmercator_y double_precision");

            db.execSQL("update registro set webmercator_x=0");
            db.execSQL("update registro set webmercator_y=0");
        }

        if (oldVersion <=6 && newVersion>=7 )
        {
            // Purtroppo ci sono stati dei problemi e le colonne nuove non venivano
            // create nei nuovi database alla versione 6
            Cursor c = db.rawQuery("select * from registro", new String[0]);
            int idx = c.getColumnIndex("webmercator_x");
            c.close();
            if ( idx==-1 ) {
                // Campi relativi a Web Mercator
                db.execSQL("alter table registro add column webmercator_x double precision");
                db.execSQL("alter table registro add column webmercator_y double_precision");

                db.execSQL("update registro set webmercator_x=0");
                db.execSQL("update registro set webmercator_y=0");
            }
        }
	}

}
