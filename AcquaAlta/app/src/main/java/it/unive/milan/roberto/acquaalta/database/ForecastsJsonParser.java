package it.unive.milan.roberto.acquaalta.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unive.milan.roberto.acquaalta.database.Forecast;

public final class ForecastsJsonParser {

    public ForecastsJsonParser(){}

    public static List parseOpenData(String data) throws JSONException {

        List<Forecast> forecasts = new ArrayList<>();
        JSONArray jArray = new JSONArray(data);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:ss");

        for (int i=0; i < jArray.length(); i++) {
            Forecast f = new Forecast();
            JSONObject singleForecast = jArray.getJSONObject(i);
            Date formattedDate = null;

            // parsing della data di estrazione
            try {
                formattedDate = format.parse(singleForecast.getString("DATA_PREVISIONE"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            f.setDataPrevisione(formattedDate);

            // parsing della data di estrazione
            try {
                formattedDate = format.parse(singleForecast.getString("DATA_ESTREMALE"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            f.setDataEstremale(formattedDate);

            f.setTipoEstremale(singleForecast.getString("TIPO_ESTREMALE"));

            f.setValore(singleForecast.getInt("VALORE"));

            // aggiungi l'elemento alla lista
            forecasts.add(f);
        }

        return forecasts;
    }
}
