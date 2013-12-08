
package uk.co.samhogy.metroappwidget.model;

public class Arrival implements Comparable<Arrival> {

    final String platform;
    final String destination;
    final String time;
    final int estimatedWait;

    public Arrival(String platform, String destination, String time, int estimatedWait) {
        this.platform = platform;
        this.destination = destination;
        this.time = time;
        this.estimatedWait = estimatedWait;
    }

    public String platform() {
        return platform;
    }

    public String destination() {
        return destination;
    }

    public String time() {
        return time;
    }

    @Override
    public int compareTo(Arrival another) {
        if (estimatedWait == another.estimatedWait) {
            return 0;
        } else if (estimatedWait < another.estimatedWait) {
            return -1;
        }

        return 1;
    }
}
