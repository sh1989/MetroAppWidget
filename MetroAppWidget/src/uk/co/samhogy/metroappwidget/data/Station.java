
package uk.co.samhogy.metroappwidget.data;

public class Station implements Comparable<Station> {
    private final String name;
    private final RailwayLines lines;

    public Station(String name, RailwayLines lines) {
        this.name = name;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public RailwayLines getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Station other) {
        return name.compareTo(other.name);
    }
}
