package it.unive.milan.roberto.acquaalta.places.add;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.AppDatabase;
import it.unive.milan.roberto.acquaalta.database.Place;
import it.unive.milan.roberto.acquaalta.utilities.ItemClickSupport;

public class PlaceAddActivity extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected PlacesAddAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected List<String> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_places_add);
        mRecyclerView.setHasFixedSize(true);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
            new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    TextView place = (TextView) v.findViewById(R.id.tv_place);
                    String stazione = place.getText().toString();
                    new PlaceChooser().execute(stazione);
                    Toast.makeText(getContext(), "Luogo aggiunto", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        );

        mAdapter = new PlacesAddAdapter(places);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new PlacesFetcher().execute();
    }

    public Context getContext(){
        return this;
    }

    private class PlacesFetcher extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<Place> mPlaces = AppDatabase.getDatabase(getContext()).placeDao().getNotChosenPlaces();

            places.clear();
            for(Place p : mPlaces){
                places.add(p.getStazione());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            mAdapter.notifyDataSetChanged();
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
            mAdapter.notifyDataSetChanged();
        }
    }
}
