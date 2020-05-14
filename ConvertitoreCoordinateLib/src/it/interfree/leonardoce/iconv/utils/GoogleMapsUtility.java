package it.interfree.leonardoce.iconv.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class GoogleMapsUtility {
    public static String getGoogleMapsUrl(double lat, double longi) {
        try {
            String locParam;
            locParam = "loc:" + URLEncoder.encode(lat+" " + longi, "UTF-8");
            String arguments="z=12&q="+locParam;
            return ("http://maps.google.com/maps?" + arguments);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String string2null(String s) {
        if(s==null) {
            return "";
        } else {
            return s;
        }
    }

    private static String addressToString(Address a) {
        String result = "";
        int i = 0;

        result += string2null(a.getAddressLine(0)) + " (" + string2null(a.getLocality()) + ")";
        return result;
    }

    /**
     * Trascodifica un indirizzo per Google Maps.
     * Occhio che per licenza questo non deve essere utilizzato con
     * OpenStreetMap e che deve essere utilizzato come un task asincrono
     */
    public static List<Indirizzo> trascodificaGoogle(Context ctx, String nome) throws IOException {
        Geocoder geocoder = new Geocoder(ctx);

        List<Indirizzo> risultatiFinali = new ArrayList<Indirizzo>();

        try {
            final List<Address> risultati = geocoder.getFromLocationName(nome, 10);

            for( int i=0; i<risultati.size(); i++ ) {
                Indirizzo ind = new Indirizzo();
                ind.nome = addressToString(risultati.get(i));
                ind.lat = risultati.get(i).getLatitude();
                ind.lon = risultati.get(i).getLongitude();
                risultatiFinali.add(ind);
            }

        } catch(Exception ex) {
            // Qualche volta la richiesta fallisce per motivi di rete, specialmente da
            // quando hanno deprecato la versione 1 delle API di Google Maps. Allora che cosa
            // facciamo? Proviamo ad utilizzare i servizi REST che hanno un limite di chiamata
            // giornaliero ma dovrebbero essere piu' affidabili.

            risultatiFinali = getGeoPoint(getLocationInfo(nome));
        }

        return risultatiFinali;
    }

    private static JSONObject getLocationInfo(String address) throws IOException {
        URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") +"&sensor=false");
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

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
        }

        return jsonObject;
    }

    private static List<Indirizzo> getGeoPoint(JSONObject jsonObject) {

        Double lon = new Double(0);
        Double lat = new Double(0);
        String nome = "";

        List<Indirizzo> risultatiFinali = new ArrayList<Indirizzo>();

        try {
            JSONArray risultati = (JSONArray)jsonObject.get("results");
            for ( int i=0; i<risultati.length(); i++ ) {
                lon = risultati.getJSONObject(i)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

                lat = risultati.getJSONObject(i)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

                nome = risultati.getJSONObject(i).getString("formatted_address");

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
