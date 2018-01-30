package it.unive.milan.roberto.acquaalta.places;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.AppDatabase;
import it.unive.milan.roberto.acquaalta.database.Place;

public class MapPlacesActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Place> places = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private CardView info;
    private ImageButton cvCollapse;
    private Button cvAdd;
    private TextView cvTitle;
    private TextView cvDescription;
    private TextView cvValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        info = (CardView) findViewById(R.id.maps_info);
        cvCollapse = (ImageButton) findViewById(R.id.cv_collapse);
        cvAdd = (Button) findViewById(R.id.cv_add);
        cvTitle = (TextView) findViewById(R.id.cv_title);
        cvDescription = (TextView) findViewById(R.id.cv_description);
        cvValue = (TextView) findViewById(R.id.cv_value);

        cvCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardCollapse();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        new PlacesFetcher().execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                cardCollapse();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                cardCollapse();
            }
        });
        addPlaces();
    }

    public Context getContext(){
        return this;
    }

    public void addPlaces(){
        for (Place p : places){
            Double lat = p.getLatDDN();
            Double lon = p.getLonDDE();
            if(lat!=null && lat!=0 && lon!=null && lon!=0){
                LatLng coord = new LatLng(lat, lon);
                String place = p.getStazione();
                MarkerOptions markerOptions = new MarkerOptions().position(coord).title(place);
                if(p.isScelto()){
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                Marker m = mMap.addMarker(markerOptions);
                m.setTag(p);
                mMap.setOnMarkerClickListener(this);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10.0f));
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(info.getVisibility()==View.VISIBLE){
            info.setVisibility(View.INVISIBLE);
        }
        final Place place = (Place) marker.getTag();
        cvTitle.setText(place.getStazione());
        cvDescription.setText(
            new StringBuilder()
                .append("Nome abbreviato:\t").append(place.getNome_abbr())
                .append("\nCoord DMS:\t").append(place.getLatDMSN() + ", " + place.getLonDMSE())
                .append("\nCoord DD:\t").append(place.getLatDMSN() + ", " + place.getLonDMSE())
                .append("\nUltima rilevazione:\t").append(new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ITALY).format(place.getData()))
        );
        cvValue.setText("Ultima rilevazione: " + (place.getValore()==null ? "-" : place.getValore().toString() + " m"));

        if(place.isScelto()){
            cvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PlaceRemover().execute(place.getStazione());
                    Toast.makeText(getContext(), "Luogo rimosso", Toast.LENGTH_SHORT).show();
                }
            });
            cvAdd.setText("Rimuovi");
        }
        else {
            cvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PlaceChooser().execute(place.getStazione());
                    Toast.makeText(getContext(), "Luogo aggiunto", Toast.LENGTH_SHORT).show();
                }
            });
            cvAdd.setText("Aggiungi");
        }
        info.setVisibility(View.VISIBLE);
        return false;
    }

    public void cardCollapse() {
        if(info.getVisibility()==View.VISIBLE) {
            info.setVisibility(View.INVISIBLE);
            info.setVisibility(View.GONE);
        }
    }

    private class PlacesFetcher extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<Place> mPlaces = AppDatabase.getDatabase(getContext()).placeDao().getAll();

            places.clear();
            places.addAll(mPlaces);
            Log.d("POSTI MAPPA", "doInBackground: " + places);
            return null;
        }
    }

    private class PlaceChooser extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            AppDatabase.getDatabase(getContext()).placeDao().setChosenPlace(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            finish();
        }
    }


    public class PlaceRemover extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            AppDatabase.getDatabase(getContext()).placeDao().setNotChosenPlace(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }
}
