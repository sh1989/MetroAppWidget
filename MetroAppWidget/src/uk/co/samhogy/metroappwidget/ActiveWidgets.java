package uk.co.samhogy.metroappwidget;

import android.content.Context;
import android.content.SharedPreferences;

public class ActiveWidgets {
	private static final String PREFS_NAME = "uk.co.samhogy.metroappwidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    static void saveWidget(Context context, int stationId, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, stationId);
        prefs.commit();
    }

    static int stationIdForWidget(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int prefix = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
        return prefix;
    }

    static void deleteWidget(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}
