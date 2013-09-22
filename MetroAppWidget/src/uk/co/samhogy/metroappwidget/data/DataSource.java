
package uk.co.samhogy.metroappwidget.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import uk.co.samhogy.metroappwidget.model.RailwayLines;
import uk.co.samhogy.metroappwidget.model.Station;

import java.util.ArrayList;

public class DataSource {
    private SQLiteDatabase database;
    private final DatabaseHelper helper;
    private final String[] allStationColumns =
    {
            BaseColumns._ID,
            StationColumns.STATION_NAME,
            StationColumns.LINES,
            StationColumns.URL
    };

    public DataSource(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getReadableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ArrayList<Station> getStations() {
        ArrayList<Station> stations = new ArrayList<Station>();
        Cursor c = database.query(Tables.STATIONS, allStationColumns, null, null,
                null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            stations.add(parse(c));
            c.moveToNext();
        }
        return stations;
    }

    public Station getStation(int stationId) {
        Cursor c = database.query(Tables.STATIONS, allStationColumns,
                BaseColumns._ID + " = ?", new String[] {
                    Integer.toString(stationId)
                },
                null, null, null);
        c.moveToFirst();
        return parse(c);
    }

    private Station parse(Cursor c) {
        RailwayLines lines = RailwayLines.ALL;
        switch (c.getInt(2))
        {
            case 1:
                lines = RailwayLines.GREEN;
                break;
            case 2:
                lines = RailwayLines.YELLOW;
                break;
            case 3:
            default:
                lines = RailwayLines.ALL;
        }
        return new Station(c.getInt(0), c.getString(1), lines, c.getString(3));
    }
}
