package com.jonathan.geoffroy.vlille_analyser.model.refresher;

import android.util.Log;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.request.StationDetailsTask;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jonathan on 20/09/14.
 */
public abstract class StationDetailsRefresher extends Timer {
    protected final StationsActivity activity;

    public StationDetailsRefresher(StationsActivity activity) {
        this.activity = activity;
    }

    public void enableRefreshing(int nbSeconds) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (Station station : getStationsToRefresh()) {
                    Log.i("VLille-Http", "" + station.getName() + " star? " + station.isStar());
                    new StationDetailsTask(activity, station).execute();
                }
            }
        };
        // Run as soon as possible
        timerTask.run();

        // Schedule every nbSeconds
        this.schedule(
                timerTask, nbSeconds * 1000);
    }

    public void disableRefreshing() {
        this.cancel();
        this.purge();
    }

    protected abstract List<Station> getStationsToRefresh();
}
