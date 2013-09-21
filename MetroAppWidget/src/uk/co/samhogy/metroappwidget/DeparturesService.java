
package uk.co.samhogy.metroappwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import uk.co.samhogy.metroappwidget.data.JSONParser;
import uk.co.samhogy.metroappwidget.model.Arrival;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
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
        private final String url = "http://myjourney.nexus.org.uk/stopBoard/410_Gateshead_(Tyne_and_Wear_Metro_Station)";

        public ListRemoteViewsFactory(Context context, Intent i) {
            this.context = context;
            this.appWidgetId = i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public void onDataSetChanged() {
            Log.d("metro", "making web request: " + url);

            BufferedReader in = null;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(URI.create(url));
                HttpResponse response = client.execute(request);
                in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();

                Log.d("metro", "parsing data");
                data = JSONParser.getArrivals(new JSONObject(sb.toString()));
                Log.d("metro", "data: " + data.size());
            } catch (Exception e) {
                Log.d("metro", "oh noes");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.d("metro", "oh noes AGAIN");
                    }
                }
            }
            Log.d("metro", "returning");
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
            // Get the default loading view.
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Arrival arrival = data.get(position);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_row);
            rv.setTextViewText(R.id.row_destination, arrival.getDestination());
            rv.setTextViewText(R.id.row_time, arrival.getTime());
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
