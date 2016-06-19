package bo.hs.com.trackyourmovement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 08.06.2016.
 */
public class DatabaseConnection {
    // Database fields
    private SQLiteDatabase database;
    private CoordinatesDbHelper dbHelper;
    private String[] allColumns = { CoordinatesDbHelper.COLUMN_NAME_ENTRY_ID,
            CoordinatesDbHelper.COLUMN_NAME_USER, CoordinatesDbHelper.COLUMN_NAME_LAT,
            CoordinatesDbHelper.COLUMN_NAME_LON, CoordinatesDbHelper.COLUMN_NAME_SPEED,
            CoordinatesDbHelper.COLUMN_NAME_TIME};

    public DatabaseConnection(Context context) {
        Log.e("Create DC Object", "Datenbankobject wurde erstellt");
        dbHelper = new CoordinatesDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.e("Open DB", "Database is now opened");
    }

    public void close() {
        dbHelper.close();
        Log.e("Close DB", "Database is now closed");
    }


    public TrackPoint createTrackPoint(String user, double lat, double lon, double speed, String date) {
        ContentValues values = new ContentValues();
        values.put(CoordinatesDbHelper.COLUMN_NAME_USER, user);
        values.put(CoordinatesDbHelper.COLUMN_NAME_LAT, lat);
        values.put(CoordinatesDbHelper.COLUMN_NAME_LON, lon);
        values.put(CoordinatesDbHelper.COLUMN_NAME_SPEED, speed);
        values.put(CoordinatesDbHelper.COLUMN_NAME_TIME, date);
        long insertId = database.insert(CoordinatesDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(CoordinatesDbHelper.TABLE_NAME, allColumns, CoordinatesDbHelper.COLUMN_NAME_ENTRY_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        TrackPoint newTrackPoint = cursorToTrackPoint(cursor);
        cursor.close();
        return newTrackPoint;
    }

    /*
    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }*/

    public void deleteCompleTable() {
        database.delete(CoordinatesDbHelper.TABLE_NAME, null, null);
    }

    public List<TrackPoint> getAllTrackPoints() {
        List<TrackPoint> trackpoints = new ArrayList<TrackPoint>();

        Cursor cursor = database.query(CoordinatesDbHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TrackPoint tp = cursorToTrackPoint(cursor);
            trackpoints.add(tp);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return trackpoints;
    }

    private TrackPoint cursorToTrackPoint(Cursor cursor) {
        TrackPoint tp = new TrackPoint();
        tp.setId(cursor.getLong(0));
        tp.setUser(cursor.getString(1));
        tp.setLatitude(cursor.getDouble(2));
        tp.setLongitude(cursor.getDouble(3));
        tp.setSpeed(cursor.getDouble(4));
        tp.setDate(cursor.getString(5));
        return tp;
    }
}
