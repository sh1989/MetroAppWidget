
package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import uk.co.samhogy.metroappwidget.data.DataSource;
import uk.co.samhogy.metroappwidget.data.RailwayLines;
import uk.co.samhogy.metroappwidget.data.Station;

public class MetroTimeProvider extends AppWidgetProvider {

    private DataSource source;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        source = new DataSource(context);
        source.open();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        if (source != null) {
            source.close();
        }

    }

    // Called every updatePeriodMillis
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            int stationId = MetroTimeConfiguration.loadStationId(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, source.getStation(stationId));
        }
    }

    static void updateAppWidget(Context context,
            AppWidgetManager manager, int appWidgetId, Station station) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_title, station.getName());

        views.setViewVisibility(R.id.widget_line_all,
                station.getLines() == RailwayLines.ALL ? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.widget_line_green,
                station.getLines() == RailwayLines.GREEN ? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.widget_line_yellow,
                station.getLines() == RailwayLines.YELLOW ? View.VISIBLE : View.GONE);

        manager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            MetroTimeConfiguration.deleteStationId(context, appWidgetIds[i]);
        }
    }

}
