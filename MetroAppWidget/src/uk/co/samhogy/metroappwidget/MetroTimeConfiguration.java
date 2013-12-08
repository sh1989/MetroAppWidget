
package uk.co.samhogy.metroappwidget;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import uk.co.samhogy.metroappwidget.data.DataSource;
import uk.co.samhogy.metroappwidget.model.Station;

import java.util.ArrayList;
import java.util.Collections;

public class MetroTimeConfiguration extends ListActivity {

    private static final String PREFS_NAME = "uk.co.samhogy.metroappwidget.MetroTimeConfiguration";
    private static final String PREF_PREFIX_KEY = "prefix_";

    private int appWidgetId;

    private DataSource source;
    private ArrayList<Station> stations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        source = new DataSource(this);
        source.open();
        stations = source.getStations();
        Collections.sort(stations);

        setListAdapter(new StationListAdapter(this, stations));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final Station station = stations.get(position);
        saveStationId(this, station.id(), appWidgetId);

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        MetroTimeProvider.updateAppWidget(this, manager, appWidgetId, station);

        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, result);
        finish();
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
        return prefix;
    }

    static void deleteStationId(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}
