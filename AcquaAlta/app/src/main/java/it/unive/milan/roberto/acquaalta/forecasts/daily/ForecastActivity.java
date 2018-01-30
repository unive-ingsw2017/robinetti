package it.unive.milan.roberto.acquaalta.forecasts.daily;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.unive.milan.roberto.acquaalta.R;
import it.unive.milan.roberto.acquaalta.database.AppDatabase;
import it.unive.milan.roberto.acquaalta.database.DateConverter;
import it.unive.milan.roberto.acquaalta.database.Forecast;

public class ForecastActivity extends AppCompatActivity {

    List<Forecast> forecasts = new ArrayList<>();
    Date mDate = null;
    TextView title;
    GraphView graph;
    List<DataPoint> points;
    RecyclerView mRecyclerView;
    protected ForecastsAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        title = (TextView) findViewById(R.id.tv_day);
        graph = (GraphView) findViewById(R.id.gv_forecasts);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_forecasts);
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new ForecastsAdapter(forecasts);
        mRecyclerView.setAdapter(mAdapter);


        // Utilizza un layout manager lineare
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
//        graph.getGridLabelRenderer().setHumanRounding(false);

        // Impostazione per evitare overflow del grafico e miglior rendering
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    long lValue = (long) value;

                    Date d = DateConverter.toDate(lValue);
                    Log.d("ETICHETTA", "formatLabel: double --> " + value + "; date --> " + d);

                    Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                    calendar.setTime(d);
                    Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                    String sHour = (hour < 10 ? "0" : "") + hour;
                    Integer minute = calendar.get(Calendar.MINUTE);
                    String sMinute = (minute < 10 ? "0" : "") + minute;

                    return sHour + ":" + sMinute;
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("date")){
            this.mDate = DateConverter.toDate(intent.getLongExtra("date", 0));
        }

        title.setText(new SimpleDateFormat("EEEE dd/MM/yyyy").format(mDate));

        new ForecastFetcher().execute();

        mAdapter.notifyDataSetChanged();
    }

    public Context getContext(){
        return this;
    }

    private class ForecastFetcher extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            forecasts.clear();
            forecasts.addAll(AppDatabase.getDatabase(getContext()).findForecasts(mDate));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            for (Forecast f : forecasts) {
//                StringBuilder sb = new StringBuilder()
//                        .append("\n")
//                        .append(f.getDataEstremale())
//                        .append(" --> ")
//                        .append(f.getValore())
//                        .append(" (")
//                        .append(f.getTipoEstremale())
//                        .append(")");
//
//                tv.append(sb.toString());
//            }
            new GraphUpdater().execute();
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GraphUpdater extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            points = new ArrayList<>();
            points.clear();
            List<Date> dates = new ArrayList<>();
            List<Integer> values = new ArrayList<>();

            for(Forecast f : forecasts) {
                dates.add(f.getDataEstremale());
                values.add(f.getValore());
            }

            for(int i=0; i<dates.size(); i++){
                points.add(new DataPoint(dates.get(i), values.get(i)));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DataPoint[] pointsArray = new DataPoint[points.size()];
            pointsArray = points.toArray(pointsArray);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointsArray);
            graph.addSeries(series);
        }
    }
}
