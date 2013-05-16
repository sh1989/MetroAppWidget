package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class MetroTimeProvider extends AppWidgetProvider {

	// Called every updatePeriodMillis
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {		
		final int N = appWidgetIds.length;
		
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];
			String stationName = MetroTimeConfiguration.loadStationName(context, appWidgetId);
			updateAppWidget(context, appWidgetManager, appWidgetId, stationName);
		}	
	}

	static void updateAppWidget(Context context,
			AppWidgetManager manager, int appWidgetId, String stationName) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		views.setTextViewText(R.id.widget_title, stationName);
		manager.updateAppWidget(appWidgetId, views);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		
		for (int i=0; i<N; i++) {
			MetroTimeConfiguration.deleteStationName(context, appWidgetIds[i]);
		}
	}
	
}
