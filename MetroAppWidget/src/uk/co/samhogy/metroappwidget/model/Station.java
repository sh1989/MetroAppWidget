package uk.co.samhogy.metroappwidget.model;

public class Station implements Comparable<Station> {
    private final int id;
    private final String name;
    private final RailwayLines lines;
    private final String url;

    public Station(int id, String name, RailwayLines lines, String url) {
        this.id = id;
        this.name = name;
        this.lines = lines;
        this.url = url;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int railwayLinesResourceId() {
        return lines.drawableResourceId;
    }

    public String timetableUrl() {
        return url;
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
