
package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class DeparturesService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent i) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), i);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private static final int count = 4;
        private final Context context;
        private final int appWidgetId;

        public ListRemoteViewsFactory(Context context, Intent i) {
            this.context = context;
            this.appWidgetId = i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDataSetChanged() {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public RemoteViews getLoadingView() {
            // Get the default loading view.
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_row);
            rv.setTextViewText(R.id.row_destination, "Monument");
            rv.setTextViewText(R.id.row_time, "10:48");
            return rv;
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
