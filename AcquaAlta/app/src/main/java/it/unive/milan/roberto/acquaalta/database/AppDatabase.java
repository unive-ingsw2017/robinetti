package it.unive.milan.roberto.acquaalta.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.unive.milan.roberto.acquaalta.utilities.NetworkUtils;

/**
 * Created by Roberto on 25 Jan 2018.
 */

@Database(entities = {Place.class, Forecast.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private final String PLACES_URL = "http://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    private final String FORECASTS_URL = "http://dati.venezia.it/sites/default/files/dataset/opendata/previsione.json";

    public abstract PlaceDao placeDao();
    public abstract ForecastDao forecastDao();

    private static AppDatabase INSTANCE = null;
    private Context context;

    public static AppDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        INSTANCE.context = context;
        return INSTANCE;
    }

    public void updateFromInternet(){
        new DataFetcher().execute();
    }

    public List<Date> findLastForecastDates() {
        List<Date> temp = new ArrayList<Date>();
        DateFormat formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
        try {
            for(Date d : this.forecastDao().findForecastDates()){
                Date formatted = null;

                    formatted = formatter.parse((formatter.format(d)));
                temp.add(formatted);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Uso una tabella hash per rimuovere tutti i duplicati
        Set<Date> hs = new HashSet<>();
        hs.addAll(temp);
        temp.clear();
        temp.addAll(hs);

        // Ordino in senso discentente
        Collections.sort(temp);
        Collections.reverse(temp);

        int l = temp.size()<3 ? temp.size() : 3;

        // Ritorno solo le ultime tre previsioni
        return temp.subList(0, l);
    }

    public List<Forecast> findForecasts (Date date) {
        List<Forecast> forecasts = this.forecastDao().getAll();
        Date maxDate = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        maxDate =  cal.getTime();

        Iterator<Forecast> iter = forecasts.iterator();

        while (iter.hasNext()) {
            Forecast f = iter.next();
            Date temp = f.getDataEstremale();
            if (!(temp.after(date) && temp.before(maxDate)))
                iter.remove();
        }

        return forecasts;
    }

    public class DataFetcher extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String response = null;
            Boolean placesError = false;
            Boolean forecastsError = false;
            URL places_url = null;
            URL forecasts_url = null;

            // PRENDI LUOGHI DA OPENDATA
            try {
                places_url = new URL(Uri.parse(PLACES_URL).toString());
            } catch (MalformedURLException e) {
                placesError = true;
            }

            try {
                response = NetworkUtils.getResponseFromHttpUrl(places_url);
            } catch (IOException e) {
                placesError = true;
            }

            if (!placesError && response!=null && !response.isEmpty()) {
                try {
                    List<Place> parsedPlaces = PlacesJsonParser.parseOpenData(response);
                    Place[] arrayPlaces = parsedPlaces.toArray(new Place[parsedPlaces.size()]);

                    AppDatabase db = AppDatabase.getDatabase(context);

                    for (Place place : arrayPlaces) {
                        if(db.placeDao().getPlace(place.getStazione())!=null) {
                            place.setScelto(db.placeDao().getPlace(place.getStazione()).isScelto());
                        }
                        else {
                            place.setScelto(false);
                        }
                    }

                    db.placeDao().deleteAll();
                    db.placeDao().insertAll(arrayPlaces);

                } catch (JSONException e) {
                    placesError = true;
                }
            }

            response = null;

            // PRENDI PREVISIONI DA OPENDATA
            try {
                forecasts_url = new URL(Uri.parse(FORECASTS_URL).toString());
            } catch (MalformedURLException e) {
                forecastsError = true;
            }

            try {
                response = NetworkUtils.getResponseFromHttpUrl(forecasts_url);
            } catch (IOException e) {
                forecastsError = true;
            }

            if (!forecastsError && response!=null && !response.isEmpty()) {
                try {
                    List<Forecast> parsedForecasts = ForecastsJsonParser.parseOpenData(response);
                    Forecast[] arrayForecasts = parsedForecasts.toArray(new Forecast[parsedForecasts.size()]);

                    AppDatabase db = AppDatabase.getDatabase(context);
                    db.forecastDao().deleteAll();
                    db.forecastDao().insertAll(arrayForecasts);

                } catch (JSONException e) {
                    placesError = true;
                }
            }

            return !(placesError || forecastsError);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean){
                Toast.makeText(context, "Errore di rete, riprova", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
