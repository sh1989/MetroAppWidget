
package uk.co.samhogy.metroappwidget.data;

public class Station implements Comparable<Station> {
    private final int id;
    private final String name;
    private final RailwayLines lines;

    public Station(int id, String name, RailwayLines lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public int getId() {
        return id;
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
