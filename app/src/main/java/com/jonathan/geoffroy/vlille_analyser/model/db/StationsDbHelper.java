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
 * Base de données permettant de garder en mémoire puis récupérer les données sur les stations
 * Chaque méthode permet de récupèrer les données en fonction d'options de l'IHM
 * * n'afficher que les favoris?
 * * ordonner les données par nom? par favoris?
 * Created by jonathan on 20/09/14.
 */
public class StationsDbHelper extends SQLiteOpenHelper {
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

    /**
     * La façon dont on doit ordonner les stations
     */
    private StationOrderBy orderBy;

    /**
     * Vrai si on doit ne récupérer que les stations favorites
     */
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

    /**
     * Récupère toutes les stations d'un curseur
     *
     * @param c le curseur qui contient les stations
     * @return les stations
     */
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

    /**
     * Récupère toutes les stations
     *
     * @return l'ensemble des stations de la base de données
     */
    public ArrayList<Station> getAllStations() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToStationList(c);
    }

    /**
     * Récuère les stations en fonction de order et onlyStar
     *
     * @return les stations ordonnées par <code>order</code>, et qui sont des favorites si <code>onlyStar</code> == true
     */
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

    /**
     * Ajoute une station dans la base de données
     *
     * @param db
     * @param station
     */
    private void addStation(SQLiteDatabase db, Station station) {
        db.insert(
                TABLE_NAME,
                null,
                stationToContentValues(station)
        );
    }

    /**
     * Sauvegarde les stations<br/>
     * Ajoute la station si elle n'existe pas en base de données, update cette station sinon.
     *
     * @param stations
     */
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