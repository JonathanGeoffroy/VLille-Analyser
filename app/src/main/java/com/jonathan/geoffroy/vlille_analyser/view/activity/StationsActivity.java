package com.jonathan.geoffroy.vlille_analyser.view.activity;

import android.app.Activity;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.db.StationsDbHelper;
import com.jonathan.geoffroy.vlille_analyser.view.fragment.StationFragment;

import java.util.ArrayList;

/**
 * Created by geoffroy on 19/09/14.
 */
public abstract class StationsActivity extends Activity implements StationFragment.OnStationFragmentInteractionListener {
    protected ArrayList<Station> stations;
    protected StationsDbHelper db;

    public abstract void notifyStationsAdded();

    public abstract void notifyStationsUpdated(Station station);

    public ArrayList<Station> getStations() {
        return stations;
    }
}
