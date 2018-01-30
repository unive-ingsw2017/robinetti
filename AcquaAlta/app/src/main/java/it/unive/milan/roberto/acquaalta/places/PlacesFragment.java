package it.unive.milan.roberto.acquaalta.places;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.AppDatabase;
import it.unive.milan.roberto.acquaalta.database.Place;
import it.unive.milan.roberto.acquaalta.utilities.ItemClickSupport;

public class PlacesFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected PlacesAdapter mAdapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected List<Place> places = new ArrayList<Place>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_places, container, false);

        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.rv_places);
        mRecyclerView.setHasFixedSize(true);

        swipeRefreshLayout =
                (SwipeRefreshLayout) fragment.findViewById(R.id.swipe_refresh_places);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new refreshDB().execute();
            }
        });

        mAdapter = new PlacesAdapter(places);
        mRecyclerView.setAdapter(mAdapter);

        ItemClickSupport.addTo(mRecyclerView).setOnItemLongClickListener(
                new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        TextView tv = (TextView) v.findViewById(R.id.tv_place_name);
                        String place = tv.getText().toString();
                        showCancelDialog(place);
                        return false;
                    }
                }
        );

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        new refreshDB().execute();

        return fragment;
    }

    @Override
    public void onResume() {
        new refreshDB().execute();
        super.onResume();
    }

    private void showCancelDialog(final String place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Vuoi togliere questo luogo?");
        builder.setMessage("\"" + place + "\" verrà tolto dai luoghi monitorati.");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        new PlaceRemover().execute(place);
                        Toast.makeText(getContext(), "Luogo rimosso", Toast.LENGTH_SHORT).show();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    public class refreshDB extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase.getDatabase(getContext()).updateFromInternet();
            places.clear();
            if(AppDatabase.getDatabase(getContext()) == null){
                return null;
            }
            if(AppDatabase.getDatabase(getContext()).placeDao().getChosenPlaces() != null) {
                places.addAll(AppDatabase.getDatabase(getContext()).placeDao().getChosenPlaces());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
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
            new refreshDB().execute();
            mAdapter.notifyDataSetChanged();
        }
    }
}