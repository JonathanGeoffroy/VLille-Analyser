package com.jonathan.geoffroy.vlille_analyser.view.activity;

import android.app.Activity;

import com.jonathan.geoffroy.vlille_analyser.model.Station;

import java.util.ArrayList;

/**
 * Created by geoffroy on 19/09/14.
 */
public abstract class StationsActivity extends Activity {
    protected ArrayList<Station> stations;

    public abstract void notifyStationsChanged();
}
