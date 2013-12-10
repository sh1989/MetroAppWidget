
package uk.co.samhogy.metroappwidget.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Arrival implements Comparable<Arrival> {

    final String platform;
    final String destination;
    final Date time;
    final SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);

    public Arrival(String platform, String destination, Date time) {
        this.platform = platform;
        this.destination = destination;
        this.time = time;
    }

    public String platform() {
        return platform;
    }

    public String destination() {
        return destination;
    }

    public String time() {
        return format.format(time);
    }

    @Override
    public int compareTo(Arrival another) {
        return time.compareTo(another.time);
    }
}
