package uk.co.samhogy.metroappwidget.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource
{
	private SQLiteDatabase database;
	private DatabaseHelper helper;
	private String[] allStationColumns =
		{ DatabaseHelper.COLUMN_UID, 
		  DatabaseHelper.COLUMN_STATIONNAME,
		  DatabaseHelper.COLUMN_LINES};
	
	public DataSource(Context context)
	{
		helper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException
	{
		database = helper.getReadableDatabase();
	}
	
	public void close()
	{
		helper.close();
	}
	
	public ArrayList<Station> getStations()
	{
		ArrayList<Station> stations = new ArrayList<Station>();
		Cursor c = database.query(DatabaseHelper.TABLE_STATIONS, allStationColumns, null, null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast())
		{
			RailwayLines lines = RailwayLines.ALL;
			switch(c.getInt(2))
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
			
			stations.add(new Station(c.getString(1), lines));
			c.moveToNext();
		}
		return stations;			
	}
}
