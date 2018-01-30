package it.unive.milan.roberto.acquaalta.places.add;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;

public class PlacesAddAdapter extends RecyclerView.Adapter<PlacesAddAdapter.PlaceAddViewHolder> {

    private List<String> mPlaces = new ArrayList<String>();

    public PlacesAddAdapter(List<String> places) {
        if(places != null){
            this.mPlaces = places;
        }
    }

    public static class PlaceAddViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvPlace;

        public PlaceAddViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            this.tvPlace = (TextView) v.findViewById(R.id.tv_place);
        }
        // GETTERS
        public TextView getPlace() {
            return tvPlace;
        }
    }

    @Override
    public PlaceAddViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_place_add, viewGroup, false);
        return new PlaceAddViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlaceAddViewHolder viewHolder, final int position) {
        String place = mPlaces.get(position);
        viewHolder.getPlace().setText(place);
    }

    @Override
    public int getItemCount() {
        return mPlaces != null ? mPlaces.size() : 0;
    }

}
