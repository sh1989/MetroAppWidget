
package uk.co.samhogy.metroappwidget.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.samhogy.metroappwidget.Log;
import uk.co.samhogy.metroappwidget.model.Arrival;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JSONParser {

    private static final String LAST_UPDATED = "lastUpdated";
    private static final String ARRIVALS = "arrivals";
    private static final String STOP_NAME = "stopName";
    private static final String DESTINATION = "destination";
    private static final String SCHEDULED_TIME = "scheduledTime";

    public static List<Arrival> getArrivals(String response) {
        final List<Arrival> data = new ArrayList<Arrival>();
        final Calendar calendar = Calendar.getInstance();

        try {
            JSONObject json = new JSONObject(response);
            Date lastUpdatedDate = time(json.getString(LAST_UPDATED), calendar, null);
            JSONArray arrivals = json.getJSONArray(ARRIVALS);
            JSONObject arrival;

            for (int i = 0; i < arrivals.length(); i++) {
                arrival = arrivals.getJSONObject(i);
                data.add(new Arrival(
                        arrival.getString(STOP_NAME),
                        station(arrival.getString(DESTINATION)),
                        time(arrival.getString(SCHEDULED_TIME), calendar, lastUpdatedDate)));
            }

        } catch (JSONException e) {
            Log.error("Unable to parse JSON response", e);
        }

        Collections.sort(data);
        return data;
    }

    private static String station(String s) {
        // Extract station name from destination name.
        return s.replaceAll("Newcastle", "").
                replaceAll("Metro Station", "").
                replaceAll("\\(city centre\\)", "").
                replaceAll("\\(centre\\)", "").
                replaceAll("\\(Tyne and wear\\)", "").
                replaceFirst("Sunderland", "").
                trim();
    }

    /**
     * Converts a HH:mm string into a Date, with respect to a given
     * referenceDate.
     */
    private static Date time(String s, Calendar calendar, Date referenceDate) {
        String time = s.replaceAll("[^:\\d]", "").trim();
        String[] timeUnits = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeUnits[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeUnits[1]));

        Date dateTime = calendar.getTime();

        // Check to see whether we have ticked over to the next day.
        if (referenceDate != null && dateTime.before(referenceDate)) {
            calendar.add(Calendar.DATE, 1);
            dateTime = calendar.getTime();
        }

        return dateTime;
    }
}
