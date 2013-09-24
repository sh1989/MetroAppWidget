
package uk.co.samhogy.metroappwidget.web;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.samhogy.metroappwidget.model.Arrival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONParser {

    public static List<Arrival> getArrivals(String response) {
        final List<Arrival> data = new ArrayList<Arrival>();

        try {
            JSONObject json = new JSONObject(response);
            JSONArray arrivals = json.getJSONArray("arrivals");
            JSONObject arrival;
            for (int i = 0; i < arrivals.length(); i++) {
                arrival = arrivals.getJSONObject(i);
                data.add(new Arrival(
                        arrival.getString("stopName"),
                        stationOnly(arrival.getString("destination")),
                        timeOnly(arrival.getString("scheduledTime")),
                        waitOnly(arrival.getString("estimatedWait"))));
            }
        } catch (JSONException e) {
            Log.e("MetroAppWidget", "Unable to parse JSON response", e);
        }

        Collections.sort(data);
        return data;
    }

    private static String stationOnly(String s) {
        return s.replaceAll("Newcastle", "").
                replaceAll("Metro Station", "").
                replaceAll("\\(city centre\\)", "").
                replaceAll("\\(centre\\)", "").
                replaceAll("\\(Tyne and wear\\)", "").
                trim();
    }

    private static String timeOnly(String s) {
        return s.replaceAll("[^:\\d]", "").trim();
    }

    private static int waitOnly(String s) {
        s = s.replace("due", "0")
                .replace(" min", "")
                .replace("s", "")
                .trim();
        return Integer.parseInt(s);
    }
}
