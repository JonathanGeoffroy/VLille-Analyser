package com.jonathan.geoffroy.vlille_analyser.view.activity;

import android.app.Activity;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.db.StationsDbHelper;
import com.jonathan.geoffroy.vlille_analyser.view.fragment.GoogleMapFragment;
import com.jonathan.geoffroy.vlille_analyser.view.fragment.StationFragment;
import com.jonathan.geoffroy.vlille_analyser.view.fragment.StationsOptionsFragment;

import java.util.ArrayList;

/**
 * Created by geoffroy on 19/09/14.
 */
public abstract class StationsActivity extends Activity implements StationFragment.OnStationFragmentInteractionListener, StationsOptionsFragment.OnOptionsFragmentInteractionListener, GoogleMapFragment.OnFragmentInteractionListener {
    protected ArrayList<Station> stations;
    protected StationsDbHelper db;

    /**
     * Appelé lorsque toutes les stations ont été ajoutés
     */
    public abstract void notifyStationsAdded();

    /**
     * Appelé lorsqu'une station a été modifiée.
     *
     * @param station la station modifiée
     */
    public abstract void notifyStationUpdated(Station station);

    /**
     * Appelé lorsque toutes les stations ont été modifiés.
     */
    public abstract void notifyAllStationsUpdated();

    /**
     *
     * @return La liste des stations à afficher
     */
    public ArrayList<Station> getStations() {
        return stations;
    }
}
