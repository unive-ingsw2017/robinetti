package it.unive.milan.roberto.acquaalta.forecasts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.AppDatabase;
import it.unive.milan.roberto.acquaalta.database.DateConverter;
import it.unive.milan.roberto.acquaalta.forecasts.daily.ForecastActivity;

public class ForecastsFragment extends Fragment {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected TextView tvDate1;
    protected TextView tvDate2;
    protected TextView tvDate3;
    protected CardView cw1;
    protected CardView cw2;
    protected CardView cw3;

    protected List<Date> dates = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_forecasts, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) fragment.findViewById(R.id.swipe_refresh_forecasts_new);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ForecastsFragment.refreshDB().execute();
            }
        });


        LayoutParams contentViewLayout = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
        swipeRefreshLayout.setLayoutParams( contentViewLayout );

        tvDate1 = (TextView) fragment.findViewById(R.id.tv_forecast_time_1);
        tvDate2 = (TextView) fragment.findViewById(R.id.tv_forecast_time_2);
        tvDate3 = (TextView) fragment.findViewById(R.id.tv_forecast_time_3);
        cw1 = (CardView) fragment.findViewById(R.id.cv_1) ;
        cw2 = (CardView) fragment.findViewById(R.id.cv_2) ;
        cw3 = (CardView) fragment.findViewById(R.id.cv_3) ;

        cw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = dates.get(0);
                Intent intent = new Intent(getContext(), ForecastActivity.class);
                intent.putExtra("date", DateConverter.fromDate(date));
                startActivity(intent);
            }
        });

        cw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = dates.get(1);
                Intent intent = new Intent(getContext(), ForecastActivity.class);
                intent.putExtra("date", DateConverter.fromDate(date));
                startActivity(intent);
            }
        });

        cw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = dates.get(2);
                Intent intent = new Intent(getContext(), ForecastActivity.class);
                intent.putExtra("date", DateConverter.fromDate(date));
                startActivity(intent);
            }
        });

        new refreshDB().execute();

        return fragment;
    }

    public class refreshDB extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase.getDatabase(getContext()).updateFromInternet();
            dates.clear();
            dates.addAll(AppDatabase.getDatabase(getContext()).findLastForecastDates());
            Log.d("DATES", "doInBackground: :" + dates);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Date date;

            tvDate1.setText("Nessun dato");
            tvDate2.setText("Nessun dato");
            tvDate3.setText("Nessun dato");


            if (dates.size()>0) {
                tvDate1.setText(new SimpleDateFormat("EEEE dd/MM/yyyy").format(dates.get(0)));
            }
            else cw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nessunDato();
                }
            });

            if (dates.size()>1) {
                tvDate2.setText(new SimpleDateFormat("EEEE dd/MM/yyyy").format(dates.get(1)));
            }
            else cw2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nessunDato();
                }
            });

            if (dates.size()>2) {
                tvDate3.setText(new SimpleDateFormat("EEEE dd/MM/yyyy").format(dates.get(2)));
            }
            else cw3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nessunDato();
                }
            });


            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void nessunDato() {
        Toast.makeText(getContext(), "Nessun dato disponibile", Toast.LENGTH_SHORT).show();
    }
}