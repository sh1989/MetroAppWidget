
package uk.co.samhogy.metroappwidget.data;

import android.content.Context;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.co.samhogy.metroappwidget.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mymetro.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STATIONS = "stations";
    public static final String COLUMN_UID = "_id";
    public static final String COLUMN_STATIONNAME = "stationname";
    public static final String COLUMN_LINES = "lines";

    private static final String STATIONS_CREATE =
            "CREATE TABLE " + TABLE_STATIONS +
                    "( " + COLUMN_UID + " integer primary key autoincrement, " +
                    COLUMN_STATIONNAME + " text not null, " +
                    COLUMN_LINES + " integer " +
                    ")";

    private final Context ctx;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.setLockingEnabled(false);
            createDatabase(database);

        } finally {
            database.setLockingEnabled(true);
        }
    }

    private void createDatabase(SQLiteDatabase database) {
        database.execSQL(STATIONS_CREATE);

        InsertHelper ih = new InsertHelper(database, TABLE_STATIONS);
        final int stationName = ih.getColumnIndex(COLUMN_STATIONNAME);
        final int lines = ih.getColumnIndex(COLUMN_LINES);

        InputStream is = ctx.getResources().openRawResource(R.raw.stations);
        try {
            BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rdr.readLine()) != null)
            {
                ih.prepareForInsert();

                String[] rowData = line.split(",");

                ih.bind(stationName, rowData[0]);
                ih.bind(lines, rowData[1]);

                ih.execute();
            }
        } catch (IOException ex) {
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            } finally {
                ih.close();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        onCreate(database);
    }
}
