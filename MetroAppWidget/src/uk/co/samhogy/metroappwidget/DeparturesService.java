
package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import uk.co.samhogy.metroappwidget.data.DataSource;
import uk.co.samhogy.metroappwidget.model.Arrival;
import uk.co.samhogy.metroappwidget.model.Station;
import uk.co.samhogy.metroappwidget.web.JSONParser;
import uk.co.samhogy.metroappwidget.web.StopBoardRequest;

import java.util.ArrayList;
import java.util.List;

public class DeparturesService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent i) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), i);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final Context context;
        private final int appWidgetId;
        private List<Arrival> data = new ArrayList<Arrival>();
        private static final String URL_PREFIX = "http://myjourney.nexus.org.uk/stopBoard/";
        private DataSource source;

        public ListRemoteViewsFactory(Context context, Intent i) {
            this.context = context;
            this.appWidgetId = i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            if (source == null) {
                source = new DataSource(context);
                source.open();
            }
        }

        @Override
        public void onDestroy() {
            if (source != null) {
                source.close();
            }
        }

        @Override
        public void onDataSetChanged() {
            if (haveInternetAccess()) {
                int stationId = ActiveWidgets.stationIdForWidget(context, appWidgetId);
                if (stationId != -1) {
                    final Station s = source.getStation(stationId);

                    final String response =
                            StopBoardRequest.getTimesForStation(URL_PREFIX + s.timetableUrl());
                    data = JSONParser.getArrivals(response);
                }
            }
            else {
                Log.debug("No interweb connection detected, bailing out.");
                data.clear();
            }
        }

        private boolean haveInternetAccess()
        {
            final ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public RemoteViews getLoadingView() {
            return layout(R.layout.list_loading);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Arrival arrival = data.get(position);

            RemoteViews rv = layout(R.layout.list_row);
            rv.setTextViewText(R.id.row_platform, arrival.platform());
            rv.setTextViewText(R.id.row_destination, arrival.destination());
            rv.setTextViewText(R.id.row_time, arrival.time());
            return rv;
        }

        private RemoteViews layout(int resourceId) {
            return new RemoteViews(context.getPackageName(), resourceId);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
