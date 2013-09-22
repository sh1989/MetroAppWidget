
package uk.co.samhogy.metroappwidget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import uk.co.samhogy.metroappwidget.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mymetro.db";
    private static final int DATABASE_VERSION = 1;

    private static final String STATIONS_CREATE =
            "CREATE TABLE " + Tables.STATIONS + "( "
                    + BaseColumns._ID + " integer primary key autoincrement, "
                    + StationColumns.STATION_NAME + " text not null, "
                    + StationColumns.LINES + " integer, "
                    + StationColumns.URL + " text not null"
                    + ")";

    private final Context ctx;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(STATIONS_CREATE);

        String sql = "INSERT INTO " + Tables.STATIONS
                + " ( " + StationColumns.STATION_NAME + ", " + StationColumns.LINES
                + ", " + StationColumns.URL + " ) "
                + "VALUES (?, ?, ?)";

        database.beginTransaction();
        SQLiteStatement s = database.compileStatement(sql);
        InputStream is = ctx.getResources().openRawResource(R.raw.stations);
        try {
            BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rdr.readLine()) != null)
            {
                s.clearBindings();
                String[] rowData = line.split(",");
                s.bindString(1, rowData[0]);
                s.bindLong(2, Integer.parseInt(rowData[1]));
                s.bindString(3, rowData[2]);

                s.execute();
            }

            database.setTransactionSuccessful();
        } catch (IOException ex) {
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            } finally {
                database.endTransaction();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Tables.STATIONS);
        onCreate(database);
    }
}
