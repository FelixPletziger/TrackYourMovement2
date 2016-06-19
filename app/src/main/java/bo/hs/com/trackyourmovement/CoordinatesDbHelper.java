package bo.hs.com.trackyourmovement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Felix on 08.06.2016.
 */
public class CoordinatesDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final String TABLE_NAME = "trackpois";
    public static final String COLUMN_NAME_ENTRY_ID = "_ID";
    public static final String COLUMN_NAME_USER = "user";
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LON = "lon";
    public static final String COLUMN_NAME_SPEED = "speed";
    public static final String COLUMN_NAME_TIME = "timestamp";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LAT + DOUBLE_TYPE + COMMA_SEP +
                    COLUMN_NAME_LON + DOUBLE_TYPE + COMMA_SEP +
                    COLUMN_NAME_SPEED + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE + ")";


    public CoordinatesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("HelperDBConnection","Create DB Object");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.e("Create DB","SQLite DB created");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CoordinatesDbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
