
package uk.co.samhogy.metroappwidget.model;


public class Arrival implements Comparable<Arrival> {

    final int platform;
    final String destination;
    final String time;
    final int estimatedWait;

    public Arrival(String platform, String destination, String time, int estimatedWait) {
        this.platform = Integer.parseInt(platform);
        this.destination = destination;
        this.time = time;
        this.estimatedWait = estimatedWait;
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
        if (estimatedWait == another.estimatedWait) {
            return 0;
        } else if (estimatedWait < another.estimatedWait) {
            return -1;
        }

        return 1;
    }
}
