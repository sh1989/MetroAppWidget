package uk.co.samhogy.metroappwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

public class MetroTimeConfiguration extends Activity {
	
	private static final String PREFS_NAME = "uk.co.samhogy.metroappwidget.MetroTimeConfiguration";
	private static final String PREF_PREFIX_KEY = "prefix_";
	
	private EditText stationText;
	private int appWidgetId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_configure);
		
		stationText = (EditText) findViewById(R.id.configure_station);
		Button accept = (Button) findViewById(R.id.configure_accept);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Context context = MetroTimeConfiguration.this; 
				
				String stationName = stationText.getText().toString();
				saveStationName(context, stationName, appWidgetId);
				
				AppWidgetManager manager = AppWidgetManager.getInstance(context);
				MetroTimeProvider.updateAppWidget(context, manager, appWidgetId, stationName);
				
				Intent result = new Intent();
				result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
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
	
	static void saveStationName(Context context, String text, int appWidgetId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
		prefs.commit();
	}
	
	static String loadStationName(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
		if (prefix != null) {
			return prefix;
		} else {
			return context.getString(R.string.widget_prefix_default);
		}
	}
	
	static void deleteStationName(Context context, int appWidgetId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefs.remove(PREF_PREFIX_KEY + appWidgetId);
		prefs.commit();
	}
}
