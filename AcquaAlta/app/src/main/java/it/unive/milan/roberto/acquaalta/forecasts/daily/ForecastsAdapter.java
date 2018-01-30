package it.unive.milan.roberto.acquaalta.forecasts.daily;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.Forecast;

public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ForecastViewholder> {
    private List<Forecast> mForecasts;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ForecastViewholder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvType;
        private final TextView tvLevel;

        private Forecast forecast = null;

        public ForecastViewholder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });

            this.tvTime = (TextView) v.findViewById(R.id.tv_time);
            this.tvType = (TextView) v.findViewById(R.id.tv_type);
            this.tvLevel = (TextView) v.findViewById(R.id.tv_level);
        }


        // GETTERS
        public TextView getTvTime() {
            return tvTime;
        }
        public TextView getTvType() {
            return tvType;
        }
        public TextView getTvLevel() {
            return tvLevel;
        }

        public Forecast getForecast() {
            return forecast;
        }

        public void setForecast(Forecast forecast) {
            this.forecast = forecast;
        }
    }

    public ForecastsAdapter(List<Forecast> forecasts) {
        this.mForecasts = forecasts;
    }

    @Override
    public ForecastViewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_forecast, viewGroup, false);
        return new ForecastViewholder(v);
    }

    @Override
    public void onBindViewHolder(ForecastViewholder viewHolder, final int position) {
        Forecast forecast = mForecasts.get(position);
        Log.d("ADAPTER", "onBindViewHolder: " + forecast);
        Date time = forecast.getDataEstremale();
        Log.d("VALORE", "onBindViewHolder() returned: " + forecast.getValore());

        // imposta la variabile e i campi del viewholder
        viewHolder.setForecast(forecast);
        viewHolder.getTvTime().setText(new SimpleDateFormat("HH:mm").format(time));
        viewHolder.getTvType().setText(forecast.getTipoEstremale());
        viewHolder.getTvLevel().setText(forecast.getValore().toString());
    }

    @Override
    public int getItemCount() {
        return mForecasts != null ? mForecasts.size() : 0;
    }
}
