package it.unive.milan.roberto.acquaalta.places;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.Place;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<Place> mPlaces;

    public PlacesAdapter(List<Place> places) {
        // copia l'array di input nell'array del nuovo adapter
        this.mPlaces = places;
    }
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvLevel;
        private final CardView crdCard;

        // ViewHolder mantiene anche l'informazione all'intero oggetto Place
        private Place place = new Place();

        public PlaceViewHolder(View v) {
            super(v);

            // Imposta azione al click
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });

            // imposta riferimenti ai campi
            this.tvName = (TextView) v.findViewById(R.id.tv_place_name);
            this.tvTime = (TextView) v.findViewById(R.id.tv_place_time);
            this.tvLevel = (TextView) v.findViewById(R.id.tv_place_level);
            this.crdCard = (CardView) v.findViewById(R.id.crd_place);
        }


        // GETTERS
        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvTime() {
            return tvTime;
        }

        public TextView getTvLevel() {
            return tvLevel;
        }

        public CardView getCrdCard() {
            return crdCard;
        }

        public Place getPlace() {
            return place;
        }

        public void setPlace(Place place) { this.place = place; }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int viewToInflate = R.layout.item_place;

        // Istanzia una nuova view per il singolo item
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(viewToInflate, viewGroup, false);

        // restituisce un viewholder contenente la singola view
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder viewHolder, final int position) {
        // recupera info per l'elemento dall'array
        Place place = mPlaces.get(position);
        int bg;

        // imposta la variabile e i campi del viewholder
        viewHolder.setPlace(place);
        viewHolder.getTvName().setText(place.getStazione());
        viewHolder.getTvTime().setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(place.getData()));

        String valore;
        if (place.getValore() == null) {
            valore = "-";
        } else {
            valore = String.format("%.2f", place.getValore());
        }
        viewHolder.getTvLevel().setText(valore);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

}
