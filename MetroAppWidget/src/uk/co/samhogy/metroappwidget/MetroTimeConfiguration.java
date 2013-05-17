
package uk.co.samhogy.metroappwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import uk.co.samhogy.metroappwidget.data.DataSource;
import uk.co.samhogy.metroappwidget.data.Station;

import java.util.ArrayList;
import java.util.Collections;

public class MetroTimeConfiguration extends Activity {

    private static final String PREFS_NAME = "uk.co.samhogy.metroappwidget.MetroTimeConfiguration";
    private static final String PREF_PREFIX_KEY = "prefix_";

    private Spinner stationText;
    private int appWidgetId;

    private DataSource source;
    private ArrayList<Station> stations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_configure);

        source = new DataSource(this);
        source.open();
        stations = source.getStations();
        Collections.sort(stations);

        stationText = (Spinner) findViewById(R.id.configure_station);

        ArrayAdapter<Station> dataAdapter = new ArrayAdapter<Station>(this,
                android.R.layout.simple_spinner_item, stations);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationText.setAdapter(dataAdapter);

        Button accept = (Button) findViewById(R.id.configure_accept);
        accept.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Context context = MetroTimeConfiguration.this;
                final Station station = (Station) stationText.getSelectedItem();

                saveStationId(context, station.getId(), appWidgetId);

                AppWidgetManager manager = AppWidgetManager
                        .getInstance(context);
                MetroTimeProvider.updateAppWidget(context, manager,
                        appWidgetId, station);

                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId);
                setResult(RESULT_OK, result);
                finish();
            }

        });

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (source != null) {
            source.close();
        }
    }

    static void saveStationId(Context context, int stationId, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, stationId);
        prefs.commit();
    }

    static int loadStationId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int prefix = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
        if (prefix != -1) {
            return prefix;
        } else {
            return 0;
        }
    }

    static void deleteStationId(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}
