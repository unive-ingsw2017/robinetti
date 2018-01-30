package it.unive.milan.roberto.acquaalta.database;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class PlacesJsonParser {

    public PlacesJsonParser(){}

    public static List parseOpenData(String data) throws JSONException {

        List<Place> places = new ArrayList<>();
        JSONArray jArray = new JSONArray(data);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:ss");

        for (int i=0; i < jArray.length(); i++) {
            Place p = new Place();
            JSONObject singlePlace = jArray.getJSONObject(i);
            Date formattedDate = null;

            // estrazione dei dati dal JSON all'oogetto place
            p.setOrdine(singlePlace.getInt("ordine"));
            p.setStazione(singlePlace.getString("stazione"));
            p.setNome_abbr(singlePlace.getString("nome_abbr"));
            p.setLatDMSN(singlePlace.getDouble("latDMSN"));
            p.setLonDMSE(singlePlace.getDouble("lonDMSE"));
            p.setLatDDN(singlePlace.getDouble("latDDN"));
            p.setLonDDE(singlePlace.getDouble("lonDDE"));

            // parsing della data
            try {
                formattedDate = format.parse(singlePlace.getString("data"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setData(formattedDate);

            String a = singlePlace.getString("valore");
            if (a.equals("-999 m")) {
                p.setValore(null);
            }
            else {
                Double level = new Double(a.replace(" m", ""));
                p.setValore(level);
            }

            // aggiungi l'elemento alla lista
            places.add(p);
        }

        return places;
    }
}
