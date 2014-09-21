package com.jonathan.geoffroy.vlille_analyser.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 20/09/14.
 */
public class StationsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String DATABASE_CREATE = "CREATE TABLE " + StationEntry.TABLE_NAME + "("
            + StationEntry.ID + " integer primary key autoincrement, "
            + StationEntry.NAME + " text not null, "
            + StationEntry.NBBIKES + " integer, "
            + StationEntry.NBFREE + " integer, "
            + StationEntry.STAR + " integer"
            + ");";
    private static final String DATABASE_DELETE =
            "DROP TABLE IF EXISTS " + StationEntry.TABLE_NAME;

    public StationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create a new ContentValues with station's values
     *
     * @param station the station to convert into a ContentValues
     * @return
     */
    public static ContentValues stationToContentValues(Station station) {
        ContentValues values = new ContentValues();
        values.put(StationEntry.ID, station.getId());
        values.put(StationEntry.NAME, station.getName());
        values.put(StationEntry.NBBIKES, station.getNbBikes());
        values.put(StationEntry.NBFREE, station.getNbFree());
        values.put(StationEntry.STAR, station.isStar());

        return values;
    }

    /**
     * Create a new Station with values' values
     *
     * @param values the ContentValues to convert into a Station
     * @return
     */
    public static Station contentValuesToStation(ContentValues values) {
        return new Station(
                values.getAsInteger(StationEntry.ID),
                values.getAsString(StationEntry.NAME),
                values.getAsBoolean(StationEntry.STAR),
                values.getAsInteger(StationEntry.NBBIKES),
                values.getAsInteger(StationEntry.NBFREE));
    }

    private static ArrayList<Station> cursorToStationList(Cursor c) {
        ArrayList<Station> stations = new ArrayList<Station>();
        Station station;

        if (c.moveToFirst()) {
            do {
                station = new Station(
                        c.getInt(0), // id
                        c.getString(1),
                        c.getInt(4) == 1,
                        c.getInt(2),
                        c.getInt(3)
                );
                stations.add(station);
            } while (c.moveToNext());
            c.close();
        }
        return stations;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DATABASE_DELETE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Station> getAllStations() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(StationEntry.TABLE_NAME, null, null, null, null, null, null);
        return cursorToStationList(c);
    }

    public List<Station> getStarredStations() {
        return null;
    }

    // TODO
    public List<Station> getSearchedStations(String search) {
        return null;
    }

    public void addStations(List<Station> stations) {
        SQLiteDatabase db = getWritableDatabase();
        long newRowId;
        for (Station s : stations) {
            newRowId = db.insert(
                    StationEntry.TABLE_NAME,
                    null,
                    stationToContentValues(s)
            );
        }
    }

    // TODO
    public void updateStation(Station station) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues stationValues = stationToContentValues(station);
        String selection = StationEntry.ID + " = ?";
        String[] selectionArgs = {String.valueOf(station.getId())};

        db.update(
                StationEntry.TABLE_NAME,
                stationValues,
                selection,
                selectionArgs);
    }
}