
package uk.co.samhogy.metroappwidget.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Arrival implements Comparable<Arrival> {

    final int platform;
    final String destination;
    final String time;
    Date date;

    public Arrival(String platform, String destination, String time) {
        this.platform = Integer.parseInt(platform);
        this.destination = destination;
        this.time = time;

        try {
            date = new SimpleDateFormat("hh:mm").parse(time);
        } catch (ParseException e) {
        }
    }

    public int getPlatform() {
        return platform;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int compareTo(Arrival another) {
        final int BEFORE = -1;
        final int AFTER = 1;

        if (platform == another.platform) {
            return date.compareTo(another.date);
        } else {
            return platform < another.platform ? BEFORE : AFTER;
        }
    }
}
