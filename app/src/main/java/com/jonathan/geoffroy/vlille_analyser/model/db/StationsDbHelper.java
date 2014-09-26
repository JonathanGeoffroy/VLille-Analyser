package com.jonathan.geoffroy.vlille_analyser.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.StationOrderBy;

import java.util.ArrayList;
import java.util.List;

import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.ID;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.LAT;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.LNG;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.NAME;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.NBBIKES;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.NBFREE;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.STAR;
import static com.jonathan.geoffroy.vlille_analyser.model.db.StationContract.StationEntry.TABLE_NAME;

/**
 * Created by jonathan on 20/09/14.
 */
public class StationsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " integer primary key autoincrement, "
            + NAME + " text not null, "
            + NBBIKES + " integer, "
            + NBFREE + " integer, "
            + STAR + " integer, "
            + LAT + " double, "
            + LNG + " double "
            + ");";
    private static final String DATABASE_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private StationOrderBy orderBy;
    private boolean onlyStar;

    public StationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        orderBy = StationOrderBy.NAME;
        onlyStar = false;
    }

    /**
     * Create a new ContentValues with station's values
     *
     * @param station the station to convert into a ContentValues
     * @return
     */
    public static ContentValues stationToContentValues(Station station) {
        ContentValues values = new ContentValues();
        values.put(ID, station.getId());
        values.put(NAME, station.getName());
        values.put(NBBIKES, station.getNbBikes());
        values.put(NBFREE, station.getNbFree());
        values.put(STAR, station.isStar());
        values.put(LAT, station.getPosition().latitude);
        values.put(LNG, station.getPosition().longitude);

        return values;
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
                        c.getInt(3),
                        new LatLng(c.getDouble(5), c.getDouble(6))
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
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToStationList(c);
    }

    public ArrayList<Station> getStarredStations() {
        SQLiteDatabase db = getReadableDatabase();
        String selection = STAR + " = ?";
        String[] selectionValues = {"1"};
        Cursor c = db.query(
                TABLE_NAME,
                null,
                selection,
                selectionValues,
                null,
                null,
                null);
        return cursorToStationList(c);
    }

    public ArrayList<Station> getStations() {
        SQLiteDatabase db = getReadableDatabase();
        String selection = null;
        String[] values = null;

        // Select only starred stations if needed
        if (onlyStar) {
            selection = STAR + " = ?";
            values = new String[1];
            values[0] = "1";
        }

        // Order the result
        String orderByDb = orderBy.getOrder();

        Cursor c = db.query(
                TABLE_NAME,
                null,
                selection,
                values,
                null,
                null,
                orderByDb);
        return cursorToStationList(c);
    }

    public List<Station> getSearchedStations(String search) {
        return null;
    }

    public void addStations(List<Station> stations) {
        SQLiteDatabase db = getWritableDatabase();
        for (Station s : stations) {
            addStation(db, s);
        }
    }

    private void addStation(SQLiteDatabase db, Station station) {
        db.insert(
                TABLE_NAME,
                null,
                stationToContentValues(station)
        );
    }

    public void saveStations(List<Station> stations) {
        SQLiteDatabase db = getWritableDatabase();
        for (Station s : stations) {
            try {
                addStation(db, s);
            } catch (SQLiteConstraintException e) {
                updateStation(db, s);
            }
        }

    }

    public void updateStation(Station station) {
        updateStation(getWritableDatabase(), station);
    }

    private void updateStation(SQLiteDatabase db, Station station) {
        ContentValues stationValues = stationToContentValues(station);
        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(station.getId())};

        db.update(
                TABLE_NAME,
                stationValues,
                selection,
                selectionArgs);
    }

    public void setOrderBy(StationOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public void setOnlyStar(boolean onlyStar) {
        this.onlyStar = onlyStar;
    }
}