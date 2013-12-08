
package uk.co.samhogy.metroappwidget.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.samhogy.metroappwidget.Log;
import uk.co.samhogy.metroappwidget.model.Arrival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONParser {

    private static final String ARRIVALS = "arrivals";
    private static final String STOP_NAME = "stopName";
    private static final String DESTINATION = "destination";
    private static final String SCHEDULED_TIME = "scheduledTime";
    private static final String ESTIMATED_WAIT = "estimatedWait";

    public static List<Arrival> getArrivals(String response) {
        final List<Arrival> data = new ArrayList<Arrival>();

        try {
            JSONObject json = new JSONObject(response);
            JSONArray arrivals = json.getJSONArray(ARRIVALS);
            JSONObject arrival;
            for (int i = 0; i < arrivals.length(); i++) {
                arrival = arrivals.getJSONObject(i);
                data.add(new Arrival(
                        arrival.getString(STOP_NAME),
                        stationOnly(arrival.getString(DESTINATION)),
                        timeOnly(arrival.getString(SCHEDULED_TIME)),
                        waitOnly(arrival.getString(ESTIMATED_WAIT))));
            }
        } catch (JSONException e) {
            Log.error("Unable to parse JSON response", e);
        }

        Collections.sort(data);
        return data;
    }

    private static String stationOnly(String s) {
        // Extract station name from destination name.
        return s.replaceAll("Newcastle", "").
                replaceAll("Metro Station", "").
                replaceAll("\\(city centre\\)", "").
                replaceAll("\\(centre\\)", "").
                replaceAll("\\(Tyne and wear\\)", "").
                replaceFirst("Sunderland", "").
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
