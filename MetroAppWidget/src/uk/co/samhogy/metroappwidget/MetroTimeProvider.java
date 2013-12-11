
package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import uk.co.samhogy.metroappwidget.data.DataSource;
import uk.co.samhogy.metroappwidget.model.Station;

public class MetroTimeProvider extends AppWidgetProvider {

    private DataSource source;

    @Override
    public void onEnabled(Context context) {
        Log.debug("onEnabled");
        super.onEnabled(context);
        if (source == null) {
            Log.debug("SOURCE CREATED in onEnabled");
            source = new DataSource(context);
            source.open();
        }
    }

    @Override
    public void onDisabled(Context context) {
        Log.debug("onDisabled");
        super.onDisabled(context);
        if (source != null) {
            Log.debug("SOURCE DELETED in onDisabled");
            source.close();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager,
            int[] appWidgetIds) {
        Log.debug("onUpdate");

        if (source == null) {
            Log.debug("SOURCE CREATED in onUpdate");
            source = new DataSource(context);
            source.open();
        }

        for (int appWidgetId : appWidgetIds) {
            int stationId = ActiveWidgets.stationIdForWidget(context, appWidgetId);
            if (stationId != -1) {
                updateAppWidget(context, manager, appWidgetId,
                        source.getStation(stationId));
            }
        }
    }

    static void updateAppWidget(Context context,
            AppWidgetManager manager, int appWidgetId, Station station) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setEmptyView(R.id.widget_list, R.id.empty_view);
        views.setViewVisibility(R.id.empty_view, View.GONE);
        views.setImageViewBitmap(R.id.widget_title, renderFont(context, station.name()));

        views.setImageViewResource(
                R.id.widget_lines, station.railwayLinesResourceId());

        Intent intent = new Intent(context, DeparturesService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list, intent);

        manager.updateAppWidget(appWidgetId, views);
        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    private static Bitmap renderFont(Context context, String text)
    {
        final float density =
                context.getResources().getDisplayMetrics().density;
        final int fontSize = (int) (28 * density);
        final int padding = (fontSize / 4);

        final Paint paint = new Paint();
        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Calvert.ttf");
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSize);

        final int textWidth = (int) (paint.measureText(text) + padding * 2);
        final int height = (int) (fontSize / 0.75);
        final Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, padding, fontSize, paint);

        return bitmap;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            ActiveWidgets.deleteWidget(context, appWidgetId);
        }
    }

}
