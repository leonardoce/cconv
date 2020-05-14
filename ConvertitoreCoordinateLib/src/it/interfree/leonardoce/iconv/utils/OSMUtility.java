package it.interfree.leonardoce.iconv.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Interroga il servizio Web di Nominatim per trascodificare l'indirizzo
 * utilizzando il loro servizio piuttosto che quello di Google
 */
public class OSMUtility {
    public static List<Indirizzo> trascodificaNominatim(String ricerca) throws IOException {
        return getGeoPoint(getLocationInfo(ricerca));
    }

    private static JSONArray getLocationInfo(String address) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, "UTF-8").replace("+","%20") + "&format=json";

        // essendo l'indirizzo messo in una componente dell'URL non ci vogliono i "+" per codificare gli spazi ma
        // il carattere %20. Strano ma vero

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setInstanceFollowRedirects(true);

        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream stream = urlConnection.getInputStream();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (Exception e) {
        }

        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = new JSONArray(stringBuilder.toString());
        } catch (JSONException e) {
        }

        return jsonObject;
    }

    private static List<Indirizzo> getGeoPoint(JSONArray risultati) {

        Double lon = new Double(0);
        Double lat = new Double(0);
        String nome = "";

        List<Indirizzo> risultatiFinali = new ArrayList<Indirizzo>();

        try {
            for ( int i=0; i<risultati.length(); i++ ) {
                lon = risultati.getJSONObject(i)
                    .getDouble("lon");

                lat = risultati.getJSONObject(i)
                    .getDouble("lat");

                nome = risultati.getJSONObject(i).getString("display_name");

                Indirizzo ind = new Indirizzo();
                ind.lat = lat;
                ind.lon = lon;
                ind.nome = nome;
                risultatiFinali.add(ind);
            }

        } catch (JSONException e) {
        }

        return risultatiFinali;
    }
}
