
package uk.co.samhogy.metroappwidget.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONParser {

    public static List<Arrival> getArrivals(JSONObject json) {
        List<Arrival> data = new ArrayList<Arrival>();

        try {
            JSONArray arrivals = json.getJSONArray("arrivals");
            JSONObject arrival;
            for (int i = 0; i < arrivals.length(); i++) {
                arrival = arrivals.getJSONObject(i);
                data.add(new Arrival(
                        arrival.getString("stopName"),
                        stationOnly(arrival.getString("destination")),
                        timeOnly(arrival.getString("scheduledTime"))));
            }
        } catch (JSONException e) {
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
}
