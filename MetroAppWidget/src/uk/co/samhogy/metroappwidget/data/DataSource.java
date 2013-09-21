
package uk.co.samhogy.metroappwidget.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import uk.co.samhogy.metroappwidget.model.RailwayLines;
import uk.co.samhogy.metroappwidget.model.Station;

import java.util.ArrayList;

public class DataSource {
    private SQLiteDatabase database;
    private final DatabaseHelper helper;
    private final String[] allStationColumns =
    {
            DatabaseHelper.COLUMN_UID,
            DatabaseHelper.COLUMN_STATIONNAME,
            DatabaseHelper.COLUMN_LINES
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
        Cursor c = database.query(DatabaseHelper.TABLE_STATIONS, allStationColumns, null, null,
                null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            stations.add(parse(c));
            c.moveToNext();
        }
        return stations;
    }

    public Station getStation(int stationId) {
        Cursor c = database.query(DatabaseHelper.TABLE_STATIONS, allStationColumns,
                DatabaseHelper.COLUMN_UID + " = ?", new String[] {
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
        return new Station(c.getInt(0), c.getString(1), lines);
    }
}
