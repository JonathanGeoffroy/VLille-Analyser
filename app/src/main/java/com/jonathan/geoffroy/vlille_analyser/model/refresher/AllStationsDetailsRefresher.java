package com.jonathan.geoffroy.vlille_analyser.model.refresher;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.List;

/**
 * Created by jonathan on 20/09/14.
 */
public class AllStationsDetailsRefresher extends StationDetailsRefresher {
    public AllStationsDetailsRefresher(StationsActivity activity) {
        super(activity);
    }

    @Override
    protected List<Station> getStationsToRefresh() {
        return activity.getStations();
    }
}
