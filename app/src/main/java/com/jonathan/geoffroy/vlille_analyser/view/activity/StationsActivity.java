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

    public abstract void notifyStationsAdded();

    public abstract void notifyStationUpdated(Station station);

    public abstract void notifyAllStationsUpdated();

    public ArrayList<Station> getStations() {
        return stations;
    }
}
